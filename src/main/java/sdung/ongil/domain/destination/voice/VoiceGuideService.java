package sdung.ongil.domain.destination.voice;

import lombok.RequiredArgsConstructor;
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

import java.util.List;

@Service
@RequiredArgsConstructor
public class VoiceGuideService {

    private final VoiceQueryParser voiceQueryParser;
    private final DestinationService destinationService;
    private final SmartPadRepository smartPadRepository;
    private final RouteService routeService;

    public VoiceGuideResponse guide(Long padId, String spokenText) {

        // 1) 발화 문장에서 목적지 키워드만 추출
        String keyword = voiceQueryParser.extractKeyword(spokenText);

        // 2) 이 키오스크(스마트패드)가 설치된 정류장(originId) 확인
        SmartPadEntity smartPad = smartPadRepository.findById(padId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "해당 스마트패드를 찾을 수 없습니다. id=" + padId
                ));
        Long originId = smartPad.getStationId();

        // 3) 목적지 검색 (카카오) → 1순위 결과 채택
        List<DestinationSearchResult> searchResults = destinationService.search(keyword);
        if (searchResults.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "'" + keyword + "'에 대한 목적지를 찾을 수 없습니다."
            );
        }
        DestinationSearchResult destination = searchResults.get(0);
        System.out.println("선택된 목적지: " + destination);  // ← 임시 추가

        // 4) 경로 탐색 (ODsay)
        RouteResponse route = routeService.searchRoute(
                originId,
                destination.lat(),
                destination.lng(),
                destination.name()
        );

        // 5) 쉬운 안내문 생성
        SimpleGuideResponse simpleGuide = routeService.getSimpleGuide(route.getRouteId());

// 6) 이미 만들어진 guideText 그대로 사용 (String.join 직접 안 해도 됨)
        String guideText = simpleGuide.getGuideText();

        return new VoiceGuideResponse(
                destination.name(),
                route.getRouteId(),
                guideText
        );
    }
}