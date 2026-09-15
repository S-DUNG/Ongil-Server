package sdung.ongil.domain.environment.client;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import sdung.ongil.domain.environment.dto.KmaWeatherApiResponse;

import java.net.URI;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
@RequiredArgsConstructor
public class KmaWeatherClient {
    private final RestTemplate restTemplate;

    @Value("${Kma.api-key}")
    private String serviceKey;

    private static final String BASE_URL =
            "https://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getUltraSrtNcst";

    public KmaWeatherApiResponse getUltraSrtNcst(int nx, int ny) {
        LocalDateTime baseDateTime = resolveBaseDateTime();
        String baseDate = baseDateTime.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String baseTime = baseDateTime.format(DateTimeFormatter.ofPattern("HH00"));

        String url = UriComponentsBuilder.fromUriString(BASE_URL)
                .queryParam("serviceKey", serviceKey)
                .queryParam("pageNo", 1)
                .queryParam("numOfRows", 10)
                .queryParam("dataType", "JSON")
                .queryParam("base_date", baseDate)
                .queryParam("base_time", baseTime)
                .queryParam("nx", nx)
                .queryParam("ny", ny)
                .build(true)
                .toUriString();
        return restTemplate.getForObject(URI.create(url), KmaWeatherApiResponse.class);
    }

    private LocalDateTime resolveBaseDateTime() {
        LocalDateTime now = LocalDateTime.now();
        if (now.getMinute() < 45) {
            now = now.minusHours(1);
        }
        return now.withMinute(0).withSecond(0).withNano(0);
    }
}
