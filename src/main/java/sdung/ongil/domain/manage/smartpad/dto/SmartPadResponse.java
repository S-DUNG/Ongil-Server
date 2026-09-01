package sdung.ongil.domain.manage.smartpad.dto;

import lombok.Getter;
import sdung.ongil.domain.manage.smartpad.entity.SmartPadEntity;
import sdung.ongil.domain.manage.smartpad.entity.SmartPadStatus;

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

    public SmartPadResponse(SmartPadEntity entity) {
        this.id = entity.getId();
        this.stationId = entity.getStationId();
        this.serialNumber = entity.getSerialNumber();
        this.status = entity.getStatus();
        this.installedAt = entity.getInstalledAt();
        this.createdAt = entity.getCreatedAt();
        this.updatedAt = entity.getUpdatedAt();
    }
}