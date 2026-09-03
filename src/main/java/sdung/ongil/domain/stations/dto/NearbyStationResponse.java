package sdung.ongil.domain.stations.dto;

public record NearbyStationResponse(
        String stationId,
        String name,
        double iat,
        double lng,
        double distanceMeters,
        String tagoNodeId,
        String tagoCityCode
) {
}
