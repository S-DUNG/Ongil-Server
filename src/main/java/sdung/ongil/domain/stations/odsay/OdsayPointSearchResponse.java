package sdung.ongil.domain.stations.odsay;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record OdsayPointSearchResponse(
        Result result
) {
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Result(List<OdsayStation> station) {}
}
