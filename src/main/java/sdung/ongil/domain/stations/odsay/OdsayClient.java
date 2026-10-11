package sdung.ongil.domain.stations.odsay;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import sdung.ongil.global.exception.CustomException;
import sdung.ongil.global.exception.ErrorCode;
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
            JsonNode root = objectMapper.readTree(rawResponse);

            // ODsay는 에러가 나도 HTTP 200 + body에 "error" 필드로 응답하는 경우가 있어서 먼저 체크
            if (root.has("error")) {
                throwOdsayError(root, lat, lng);
            }

            OdsayPointSearchResponse response = objectMapper.treeToValue(root, OdsayPointSearchResponse.class);
            if (response.result() == null || response.result().station() == null) {
                log.warn("Odsay pointSearch 빈 결과(정상, 주변에 정류장 없음). lat={}, lng={}", lat, lng);
                return List.of();
            }
            return response.result().station();
        } catch (CustomException e) {
            throw e; // 위에서 던진 한도초과/에러는 그대로 위로 전파
        } catch (Exception e) {
            log.error("Odsay pointSearch 파싱 실패. lat={}, lng={}, rawResponse={}", lat, lng, rawResponse, e);
            return List.of();
        }
    }

    private void throwOdsayError(JsonNode root, double lat, double lng) {
        JsonNode errorNode = root.get("error").get(0);
        String code = errorNode.path("code").asText("UNKNOWN");
        String message = errorNode.path("message").asText("알 수 없는 오류");

        log.error("Odsay API 에러 응답. code={}, message={}, lat={}, lng={}", code, message, lat, lng);

        if ("429".equals(code)) {
            throw new CustomException(ErrorCode.ODSAY_QUOTA_EXCEEDED);
        }
        throw new CustomException(ErrorCode.ODSAY_API_ERROR);
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

        try {
            JsonNode root = objectMapper.readTree(rawResponse);
            if (root.has("error")) {
                JsonNode errorNode = root.get("error").get(0);
                String code = errorNode.path("code").asText("UNKNOWN");
                log.error("Odsay searchPath 에러 응답. code={}, rawResponse={}", code, rawResponse);
                if ("429".equals(code)) {
                    throw new CustomException(ErrorCode.ODSAY_QUOTA_EXCEEDED);
                }
                throw new CustomException(ErrorCode.ODSAY_API_ERROR);
            }
            return objectMapper.treeToValue(root, OdsayPathSearchResponse.class);
        } catch (CustomException e) {
            throw e;
        } catch (Exception e) {
            log.error("Odsay searchPath 파싱 실패. rawResponse={}", rawResponse, e);
            throw new CustomException(ErrorCode.ODSAY_API_ERROR);
        }
    }
}