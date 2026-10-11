package sdung.ongil.domain.manage.smartpad.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class SmartPadUpdateRequest {
    private Long stationId;
    private String serialNumber;
    private LocalDateTime installedAt;
}