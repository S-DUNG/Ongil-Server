package sdung.ongil.domain.manage.stations.dto;

public record ManageStationsUpdateRequest(
        String name,
        Double latitude,
        Double longitude,
        String address
) {
}
