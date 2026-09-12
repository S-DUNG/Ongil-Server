package sdung.ongil.domain.destination.dto;

public record DestinationSearchResult(
        String destinationId,
        String name,
        String addressName,
        double lat,
        double lng
) {
}