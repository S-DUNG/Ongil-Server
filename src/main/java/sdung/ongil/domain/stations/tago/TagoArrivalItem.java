package sdung.ongil.domain.stations.tago;

public record TagoArrivalItem(
        String routeNo,
        int arrivalSeconds,
        int remainingStops
) {
}
