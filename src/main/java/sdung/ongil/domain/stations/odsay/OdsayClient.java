package sdung.ongil.domain.stations.odsay;

import org.springframework.beans.factory.annotation.Value;
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

    //경로 탐색
    public OdsayPathSearchResponse searchPath(double startLat, double startLng, double endLat, double endLng) {
        String url = UriComponentsBuilder
                .fromUriString("https://api.odsay.com/v1/api/searchPubTransPathT")
                .queryParam("apiKey", apiKey)
                .queryParam("lang", 0)  // ⭐ 추가: 0 = 국문
                .queryParam("SX", startLng)
                .queryParam("SY", startLat)
                .queryParam("EX", endLng)
                .queryParam("EY", endLat)
                .toUriString();


        String rawResponse = restTemplate.getForObject(url, String.class);
        System.out.println("===== ODsay 원본 응답 =====");
        System.out.println(rawResponse);
        System.out.println("==========================");

        System.out.println("===== 요청 URL =====");
        System.out.println(url);
        System.out.println("=====================");

        return restTemplate.getForObject(url, OdsayPathSearchResponse.class);

    }


}
