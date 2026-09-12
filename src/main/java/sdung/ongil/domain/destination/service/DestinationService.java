package sdung.ongil.domain.destination.service;

import org.springframework.stereotype.Service;
import sdung.ongil.domain.destination.cache.DestinationCache;
import sdung.ongil.domain.destination.dto.DestinationSearchResult;
import sdung.ongil.domain.destination.kakao.KakaoLocalClient;
import sdung.ongil.domain.destination.kakao.KakaoPlaceDocument;

import java.util.List;
import java.util.UUID;

@Service
public class DestinationService {

    private final KakaoLocalClient kakaoLocalClient;
    private final DestinationCache destinationCache;

    public DestinationService(KakaoLocalClient kakaoLocalClient, DestinationCache destinationCache) {
        this.kakaoLocalClient = kakaoLocalClient;
        this.destinationCache = destinationCache;
    }

    public List<DestinationSearchResult> search(String query) {
        var response = kakaoLocalClient.searchByKeyword(query);
        System.out.println("카카오 원본 응답: " + response);

        if (response == null || response.documents() == null) {
            return List.of();
        }

        return response.documents().stream()
                .map(this::toResultAndCache)
                .toList();
    }

    // destinationId로 캐시된 목적지 정보 조회 (없거나 만료됐으면 null)
    public DestinationSearchResult getById(String destinationId) {
        return destinationCache.get(destinationId);
    }

    private DestinationSearchResult toResultAndCache(KakaoPlaceDocument doc) {
        String destinationId = UUID.randomUUID().toString();

        DestinationSearchResult result = new DestinationSearchResult(
                destinationId,
                doc.placeName(),
                doc.addressName(),
                Double.parseDouble(doc.y()),  // 위도
                Double.parseDouble(doc.x())   // 경도
        );

        destinationCache.save(result);  // 검색 결과를 캐시에 저장
        return result;
    }
}