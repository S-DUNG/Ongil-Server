package sdung.ongil.domain.stations.dto;

import sdung.ongil.domain.manage.smartpad.entity.SmartPadStatus;

public record CongestionForecastResponse(
        int hour,
        SmartPadStatus status
) {
}
