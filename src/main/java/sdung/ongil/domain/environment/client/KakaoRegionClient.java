package sdung.ongil.domain.environment.client;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import sdung.ongil.domain.environment.dto.KakaoRegionApiResponse;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class KakaoRegionClient {

    private final RestTemplate restTemplate;

    @Value("${kakao.local.rest-api-key}")
    private String restApiKey;

    private static final String BASE_URL = "https://dapi.kakao.com/v2/local/geo/coord2regioncode.json";

    private static final Map<String, String> SIDO_NAME_MAP = Map.ofEntries(
            Map.entry("서울특별시", "서울"),
            Map.entry("부산광역시", "부산"),
            Map.entry("대구광역시", "대구"),
            Map.entry("인천광역시", "인천"),
            Map.entry("광주광역시", "광주"),
            Map.entry("대전광역시", "대전"),
            Map.entry("울산광역시", "울산"),
            Map.entry("세종특별자치시", "세종"),
            Map.entry("경기도", "경기"),
            Map.entry("강원특별자치도", "강원"),
            Map.entry("강원도", "강원"),
            Map.entry("충청북도", "충북"),
            Map.entry("충청남도", "충남"),
            Map.entry("전북특별자치도", "전북"),
            Map.entry("전라북도", "전북"),
            Map.entry("전라남도", "전남"),
            Map.entry("경상북도", "경북"),
            Map.entry("경상남도", "경남"),
            Map.entry("제주특별자치도", "제주")
    );

    public String getSidoName(double lat, double lng) {
        String url = UriComponentsBuilder.fromUriString(BASE_URL)
                .queryParam("x", lng) // 경도
                .queryParam("y", lat) // 위도
                .build(true)
                .toUriString();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "KakaoAK " + restApiKey);

        KakaoRegionApiResponse response = restTemplate.exchange(
                url, HttpMethod.GET, new HttpEntity<>(headers), KakaoRegionApiResponse.class
        ).getBody();

        if (response == null || response.getDocuments().isEmpty()) {
            throw new IllegalStateException("좌표에 대한 행정구역 정보를 찾을 수 없습니다. lat=" + lat + ", lng=" + lng);
        }

        String rawSido = response.getDocuments().get(0).getRegion1depthName();
        return SIDO_NAME_MAP.getOrDefault(rawSido, rawSido);
    }
}