package sdung.ongil.domain.destination.voice;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import sdung.ongil.domain.destination.dto.DestinationSearchResult;
import sdung.ongil.domain.destination.service.DestinationService;
import sdung.ongil.domain.manage.smartpad.entity.SmartPadEntity;
import sdung.ongil.domain.manage.smartpad.repository.SmartPadRepository;
import sdung.ongil.domain.route.dto.RouteResponse;
import sdung.ongil.domain.route.dto.SimpleGuideResponse;
import sdung.ongil.domain.route.service.RouteService;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class VoiceGuideService {

    private final VoiceQueryParser voiceQueryParser;
    private final PlaceNameCorrector placeNameCorrector;
    private final DestinationService destinationService;
    private final SmartPadRepository smartPadRepository;
    private final RouteService routeService;

    public VoiceGuideResponse guide(Long padId, String spokenText) {

        // 1) 발화에서 목적지 키워드 추출
        String rawKeyword = voiceQueryParser.extractKeyword(spokenText);
        if (rawKeyword.isBlank()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "목적지를 알아듣지 못했어요. 다시 말씀해 주세요.");
        }
        String correctedKeyword = placeNameCorrector.correct(rawKeyword);
        log.info("[voice-guide] padId={}, spoken='{}', parsed='{}', corrected='{}'",
                padId, spokenText, rawKeyword, correctedKeyword);

        // 2) 스마트패드가 설치된 출발 정류장 확인
        SmartPadEntity smartPad = smartPadRepository.findById(padId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "해당 스마트패드를 찾을 수 없습니다. id=" + padId));
        Long originId = smartPad.getStationId();

        // 3) 목적지 검색: 여러 키워드로 후보를 모아 발화와 가장 가까운 것 선택
        DestinationSearchResult destination = findBestDestination(rawKeyword, correctedKeyword);
        if (destination == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "'" + rawKeyword + "'에 대한 목적지를 찾을 수 없어요. 다시 말씀해 주세요.");
        }
        String usedKeyword = destination.name();
        log.info("[voice-guide] 선택된 목적지: {}", destination);

        // 4) 경로 탐색 (ODsay)
        RouteResponse route = routeService.searchRoute(
                originId,
                destination.lat(),
                destination.lng(),
                destination.name()
        );

        // 5) 쉬운 안내문 생성
        SimpleGuideResponse simpleGuide = routeService.getSimpleGuide(route.getRouteId());

        return new VoiceGuideResponse(
                destination.name(),
                route.getRouteId(),
                simpleGuide.getGuideText(),
                usedKeyword      // 화면에 "'강남역'으로 안내할까요?" 표시용
        );
    }

    private DestinationSearchResult findBestDestination(String raw, String corrected) {
        String compact = raw.replaceAll("\\s+", "");

        Set<String> queries = new LinkedHashSet<>();
        queries.add(corrected);
        queries.add(raw);
        queries.add(compact);
        // 끝 글자를 1~2자 줄여서 오인식 대응 ("강남여" → "강남"), 최소 2글자
        for (int len = compact.length() - 1; len >= 2 && len >= compact.length() - 2; len--) {
            queries.add(compact.substring(0, len));
        }

        int limit = placeNameCorrector.allowedDistance(raw);
        DestinationSearchResult best = null;
        int bestDist = Integer.MAX_VALUE;
        DestinationSearchResult firstResult = null;   // 비슷한 게 없을 때 기존 동작(1순위) 대비용

        for (String query : queries) {
            List<DestinationSearchResult> results = destinationService.search(query);
            if (results.isEmpty()) {
                continue;
            }
            if (firstResult == null) {
                firstResult = results.get(0);
            }
            for (DestinationSearchResult r : results) {
                int d = placeNameCorrector.distance(raw, r.name());
                if (d < bestDist) {      // 동점이면 검색 순서가 앞선 것 유지
                    bestDist = d;
                    best = r;
                }
            }
            if (best != null && bestDist <= limit) {
                break;                   // 충분히 비슷한 걸 찾았으면 추가 호출 안 함
            }
        }

        if (best != null && bestDist <= limit) {
            return best;
        }
        return firstResult;              // 비슷한 게 없으면 기존처럼 1순위 사용
    }
}