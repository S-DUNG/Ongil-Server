package sdung.ongil.domain.stations.dto;

import sdung.ongil.domain.stations.entity.SmartPadStatus;

public record CongestionForecastResponse(
        int hour,
        SmartPadStatus status
) {
}
