package sdung.ongil.domain.manage.stations.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ManageStationsCreateRequest(
        @NotBlank(message = "TAGO 정류장 ID는 필수입니다.")
        String tagoStationId,

        @NotBlank(message = "정류장 이름은 필수입니다.")
        String name,

        String address
) {
}
