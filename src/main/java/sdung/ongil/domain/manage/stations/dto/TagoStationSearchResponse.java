package sdung.ongil.domain.manage.stations.dto;

import sdung.ongil.domain.stations.tago.TagoStation;


public record TagoStationSearchResponse(
        String tagoStationId,
        String name,
        double latitude,
        double longitude
) {
    public static TagoStationSearchResponse from(TagoStation station) {
        return new TagoStationSearchResponse(
                station.nodeId(),
                station.nodeNm(),
                station.gpsLati(),
                station.gpsLong()
        );
    }
}