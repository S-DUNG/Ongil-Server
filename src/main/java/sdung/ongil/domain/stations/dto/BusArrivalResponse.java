package sdung.ongil.domain.stations.dto;

import sdung.ongil.domain.stations.entity.SmartPadStatus;

public record BusArrivalResponse(
        String busNumber,
        int etaMinutes,
        int etaSeconds,
        int remainingStop
) {
}
