package sdung.ongil.domain.stations.odsay;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record OdsayStation(
        String stationName,
        long stationID,
        double x,
        double y,
        String arsID
) {
}
