package sdung.ongil.domain.destination.kakao;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Component
public class KakaoLocalClient {
    private final RestTemplate restTemplate = new RestTemplate();
    private final String apiKey;

    public KakaoLocalClient(@Value("${kakao.local.rest-api-key}") String apiKey) {
        this.apiKey = apiKey;
    }

    public KakaoKeywordSearchResponse searchByKeyword(String query) {
        URI uri = UriComponentsBuilder
                .fromUriString("https://dapi.kakao.com/v2/local/search/keyword.json")
                .queryParam("query", query)
                .build()
                .encode()
                .toUri();

        System.out.println("카카오 요청 URI: " + uri);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "KakaoAK " + apiKey);

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        return restTemplate.exchange(
                uri,
                HttpMethod.GET,
                entity,
                KakaoKeywordSearchResponse.class
        ).getBody();
    }
}