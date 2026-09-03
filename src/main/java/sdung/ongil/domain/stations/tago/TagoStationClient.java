package sdung.ongil.domain.stations.tago;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Component
public class TagoStationClient {
    private static final String BASE_URL =
            "http://apis.data.go.kr/1613000/BusSttnInfoInqireService/getCrdntPrxmtSttnList";
    private final RestTemplate restTemplate = new RestTemplate();
    private final String serviceKey;

    public TagoStationClient(@Value("${tago.service-key}") String serviceKey) {
        this.serviceKey = serviceKey;
    }

    public List<TagoStation> searchNearby(double lat, double lng) {
        String encodedKey = URLEncoder.encode(serviceKey, StandardCharsets.UTF_8);
        String url = BASE_URL
                + "?serviceKey=" + encodedKey
                + "&_type=json"
                + "&numOfRows=50"
                + "&gpsLati=" + lat
                + "&gpsLong=" + lng;
        String json = restTemplate.getForObject(URI.create(url), String.class);
        return parseStations(json);
    }

    private List<TagoStation> parseStations(String json) {
        List<TagoStation> stations = new ArrayList<>();
        if (json == null) {
            return stations;
        }
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(json);
            JsonNode itemNode = root.path("response").path("body").path("items").path("item");
            if (itemNode.isMissingNode() || itemNode.isNull()) {
                return stations;
            }
            List<JsonNode> items = itemNode.isArray()
                    ? mapper.convertValue(itemNode, new TypeReference<List<JsonNode>>() {})
                    : List.of(itemNode);
            for (JsonNode item : items) {
                stations.add(new TagoStation(
                        item.path("nodeid").asText(),
                        item.path("nodenm").asText(),
                        item.path("gpslati").asDouble(),
                        item.path("gpslong").asDouble(),
                        item.path("citycode").asText()
                ));
            }
        } catch (Exception e) {
        }
        return stations;
    }
}
