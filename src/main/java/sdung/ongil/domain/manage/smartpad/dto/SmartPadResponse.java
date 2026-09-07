package sdung.ongil.domain.manage.smartpad.dto;

import lombok.Getter;
import sdung.ongil.domain.manage.smartpad.entity.SmartPadEntity;
import sdung.ongil.domain.manage.smartpad.entity.SmartPadStatus;
import sdung.ongil.domain.manage.stations.entity.ManageStations;

import java.time.LocalDateTime;

@Getter
public class SmartPadResponse {
    private final Long id;
    private final Long stationId;
    private final String serialNumber;
    private final SmartPadStatus status;
    private final LocalDateTime installedAt;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;
    private final Double latitude;
    private final Double longitude;

    public SmartPadResponse(SmartPadEntity entity, ManageStations station) {
        this.id = entity.getId();
        this.stationId = entity.getStationId();
        this.serialNumber = entity.getSerialNumber();
        this.status = entity.getStatus();
        this.installedAt = entity.getInstalledAt();
        this.createdAt = entity.getCreatedAt();
        this.updatedAt = entity.getUpdatedAt();
        this.latitude = (station != null) ? station.getLatitude():null;
        this.longitude = (station != null) ? station.getLongitude():null;
    }
}