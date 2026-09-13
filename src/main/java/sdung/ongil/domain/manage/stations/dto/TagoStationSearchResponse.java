package sdung.ongil.domain.manage.stations.dto;

import sdung.ongil.domain.stations.tago.TagoStation;

public record TagoStationSearchResponse(
        Long stationId,
        String tagoStationId,
        String name,
        double latitude,
        double longitude
) {
    public static TagoStationSearchResponse from(TagoStation station, Long stationId) {
        return new TagoStationSearchResponse(
                stationId,
                station.nodeId(),
                station.nodeNm(),
                station.gpsLati(),
                station.gpsLong()
        );
    }
}