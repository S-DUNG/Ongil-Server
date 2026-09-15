package sdung.ongil.domain.environment.client;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import sdung.ongil.domain.environment.dto.AirKoreaApiResponse;

@Component
@RequiredArgsConstructor
public class AirKoreaClient {
    private final RestTemplate restTemplate;

    @Value("${AirKorea.api-key}")
    private String serviceKey;

    private static final String BASE_URL =
            "https://apis.data.go.kr/B552584/ArpltnInforInqireSvc/getCtprvnRltmMesureDnsty";

    public AirKoreaApiResponse getRealtimeDensityBysido(String sidoName) {
        String url = UriComponentsBuilder.fromUriString(BASE_URL)
                .queryParam("serviceKey", serviceKey)
                .queryParam("returnType", "json")
                .queryParam("numOfRows", 10)
                .queryParam("pageNo", 1)
                .queryParam("sidoName", sidoName)
                .queryParam("ver", "1.3")
                .build()
                .toUriString();

        return restTemplate.getForObject(url, AirKoreaApiResponse.class);
    }
}
