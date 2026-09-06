package sdung.ongil.domain.manage.dashboard.dto;

import lombok.Getter;

@Getter
public class SmartPadStatusCountDto {
    private final String status;
    private final String label;
    private final long count;

    public SmartPadStatusCountDto(String status, String label, long count) {
        this.status = status;
        this.label = label;
        this.count = count;
    }
}
