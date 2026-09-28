package sdung.ongil.domain.manage.stations.dto;

import sdung.ongil.domain.manage.stations.entity.ManageStations;

import java.time.LocalDateTime;

public record ManageStationsResponse(
        Long id,
        String tagoStationId,
        String name,
        String address,
        boolean active,
        LocalDateTime createdAt,
        LocalDateTime updateAt
) {
    public static ManageStationsResponse from(ManageStations stations) {
        return new ManageStationsResponse(
                stations.getId(),
                stations.getTagoStationId(),
                stations.getName(),
                stations.getAddress(),
                stations.isActive(),
                stations.getCreatedAt(),
                stations.getUpdatedAt()
        );
    }
}
