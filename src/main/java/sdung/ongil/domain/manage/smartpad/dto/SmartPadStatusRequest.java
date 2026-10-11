package sdung.ongil.domain.manage.smartpad.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import sdung.ongil.domain.manage.smartpad.entity.SmartPadStatus;

@Getter
@NoArgsConstructor
public class SmartPadStatusRequest {
    private SmartPadStatus status;
}