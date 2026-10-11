package sdung.ongil.domain.manage.dashboard.dto;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class RecentItemDto {
    private final long id;
    private final String name;
    private final LocalDateTime createdAt;

    public RecentItemDto(Long id, String name, LocalDateTime createdAt) {
        this.id = id;
        this.name = name;
        this.createdAt = createdAt;
    }
}
