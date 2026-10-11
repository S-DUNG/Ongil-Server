package sdung.ongil.domain.manage.stations.dto;

import sdung.ongil.domain.stations.tago.TagoStation;

public record TagoStationSearchResponse(
        Long stationId,
        String tagoStationId,
        String name,
        String nodeNo,
        double latitude,
        double longitude
) {
    public static TagoStationSearchResponse from(TagoStation station, Long stationId) {
        return new TagoStationSearchResponse(
                stationId,
                station.nodeId(),
                station.nodeNm(),
                station.nodeNo(),
                station.gpsLati(),
                station.gpsLong()
        );
    }
}