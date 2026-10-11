package sdung.ongil.domain.stations.tago;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import tools.jackson.core.type.TypeReference;

@Component
public class TagoArrivalClient {
    private static final String BASE_URL =
            "http://apis.data.go.kr/1613000/ArvlInfoInqireService/getSttnAcctoArvlPrearngeInfoList";
    private final RestTemplate restTemplate = new RestTemplate();
    private final String serviceKey;

    public TagoArrivalClient(@Value("${tago.service-key}") String serviceKey) {
        this.serviceKey = serviceKey;
    }
    public List<TagoArrivalItem> getArrivals(String cityCode, String nodeId) {
        String url = BASE_URL
                + "?serviceKey=" + serviceKey
                + "&_type=json"
                + "&numOfRows=20"
                + "&cityCode=" + cityCode
                + "&nodeId=" + nodeId;
        String json = restTemplate.getForObject(URI.create(url), String.class);
        return parseArrivals(json);
    }
    private List<TagoArrivalItem> parseArrivals(String json) {
        List<TagoArrivalItem> arrivals = new ArrayList<>();
        if (json == null) {
            return arrivals;
        }
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(json);
            JsonNode itemNode = root.path("response").path("body").path("items").path("item");
            if (itemNode.isMissingNode() || itemNode.isNull()) {
                return arrivals;
            }
            List<JsonNode> items = itemNode.isArray()
                    ? mapper.convertValue(itemNode, new TypeReference<List<JsonNode>>() {})
                    : List.of(itemNode);
            for (JsonNode item : items) {
                arrivals.add(new TagoArrivalItem(
                        item.path("routeno").asText(),
                        item.path("arrtime").asInt(),
                        item.path("arrprevstationcnt").asInt()
                ));
            }
        } catch (Exception e) {
        }
        return arrivals;
    }
}
