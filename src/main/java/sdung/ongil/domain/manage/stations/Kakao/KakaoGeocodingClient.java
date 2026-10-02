package sdung.ongil.domain.manage.stations.Kakao;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Optional;

@Slf4j
@Component
public class KakaoGeocodingClient {
    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final String restApiKey;

    public KakaoGeocodingClient(@Value("${kakao.local.rest-api-key}") String restApiKey) {
        this.restApiKey = restApiKey;
    }

    public Optional<GeocodeResult> geocode(String address) {
        String url = UriComponentsBuilder
                .fromUriString("https://dapi.kakao.com/v2/local/search/address.json")
                .queryParam("query", address)
                .encode()
                .toUriString();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "KakaoAK" + restApiKey);
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        try {
            String rawResponse = restTemplate.exchange(url, HttpMethod.GET, entity, String.class).getBody();
            KakaoAddressSearchResponse response =
                    objectMapper.readValue(rawResponse, KakaoAddressSearchResponse.class);

            if (response.documents() == null || response.documents().isEmpty()) {
                log.warn("카카오 주소 검색 결과 없음. address={}, rawResponse={}", address, rawResponse);
                return Optional.empty();
            }

            KakaoAddressSearchResponse.Document doc = response.documents().get(0);
            double lng = Double.parseDouble(doc.x());
            double lat = Double.parseDouble(doc.y());
            return Optional.of(new GeocodeResult(lat, lng));
        } catch (org.springframework.web.client.RestClientResponseException e) {
            log.error("카카오 주소 검색 API 호출 실패. address={}, status={}, body={}",
                    address, e.getStatusCode(), e.getResponseBodyAsString(), e);
            return Optional.empty();
        } catch (Exception e) {
            log.error("카카오 주소 검색 파싱 실패(에러 응답 의심). address={}, rawResponse={}", address, e);
            return Optional.empty();
        }
    }
}
