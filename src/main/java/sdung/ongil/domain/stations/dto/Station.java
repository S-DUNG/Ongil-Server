package sdung.ongil.domain.stations.dto;

public record Station(
        String id,
        String stationsId,
        String name,
        double lat,
        double log
) {
}
