package sdung.ongil.domain.stations.odsay;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@Slf4j
@Component
public class OdsayClient {
    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final String apiKey;

    public OdsayClient(@Value("${odsay.api-key}") String apiKey) {
        this.apiKey = apiKey;
    }

    public List<OdsayStation> searchNearby(double lat, double lng, double radiusMeters) {
        String url = UriComponentsBuilder
                .fromUriString("https://api.odsay.com/v1/api/pointSearch")
                .queryParam("apiKey", apiKey)
                .queryParam("x", lng)
                .queryParam("y", lat)
                .queryParam("radius", (int) radiusMeters)
                .queryParam("stationClass", 1)
                .toUriString();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Referer", "http://localhost:8080");
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        String rawResponse = restTemplate.exchange(url, HttpMethod.GET, entity, String.class).getBody();

        try {
            OdsayPointSearchResponse response = objectMapper.readValue(rawResponse, OdsayPointSearchResponse.class);
            if (response.result() == null || response.result().station() == null) {
                log.warn("Odsay pointSearch 빈 결과. lat={}, lng={}, rawResponse={}", lat, lng, rawResponse);
                return List.of();
            }
            return response.result().station();
        } catch (Exception e) {
            log.error("Odsay pointSearch 파싱 실패(에러 응답 의심). lat={}, lng={}, rawResponse={}", lat, lng, rawResponse, e);
            return List.of();
        }
    }

    // 경로 탐색
    public OdsayPathSearchResponse searchPath(double startLat, double startLng, double endLat, double endLng) {
        String url = UriComponentsBuilder
                .fromUriString("https://api.odsay.com/v1/api/searchPubTransPathT")
                .queryParam("apiKey", apiKey)
                .queryParam("lang", 0)
                .queryParam("SearchPathType", 2)
                .queryParam("SX", startLng)
                .queryParam("SY", startLat)
                .queryParam("EX", endLng)
                .queryParam("EY", endLat)
                .toUriString();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Referer", "http://localhost:8080");

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        String rawResponse = restTemplate.exchange(url, HttpMethod.GET, entity, String.class).getBody();
        log.info("Odsay searchPath 원본 응답: {}", rawResponse);

        return restTemplate.exchange(url, HttpMethod.GET, entity, OdsayPathSearchResponse.class).getBody();
    }
}