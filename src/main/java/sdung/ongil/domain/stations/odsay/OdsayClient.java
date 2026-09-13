package sdung.ongil.domain.stations.odsay;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@Component
public class OdsayClient {
    private final RestTemplate restTemplate = new RestTemplate();
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

        OdsayPointSearchResponse response = restTemplate.getForObject(url, OdsayPointSearchResponse.class);

        if (response == null || response.result() == null || response.result().station() == null) {
            return List.of();
        }
        return response.result().station();
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
        headers.set("Referer", "http://localhost:8080");  // ODsay 콘솔에 등록한 URI와 정확히 동일하게

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        // 임시: 원본 문자열로 먼저 확인
        String rawResponse = restTemplate.exchange(url, HttpMethod.GET, entity, String.class).getBody();
        System.out.println("ODsay 원본 문자열 응답: " + rawResponse);

        return restTemplate.exchange(url, HttpMethod.GET, entity, OdsayPathSearchResponse.class).getBody();
    }
}