package sdung.ongil.domain.stations.dto;

public record BusArrivalResponse(
        String busNumber,
        int etaMinutes,
        int etaSeconds,
        int remainingStop
) {
}
