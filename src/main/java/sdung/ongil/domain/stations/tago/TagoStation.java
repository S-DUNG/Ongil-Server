package sdung.ongil.domain.stations.tago;

public record TagoStation(
        String nodeId,
        String nodeNm,
        double gpsLati,
        double gpsLong,
        String cityCode
) {
}
