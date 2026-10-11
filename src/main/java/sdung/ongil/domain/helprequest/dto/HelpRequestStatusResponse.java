package sdung.ongil.domain.helprequest.dto;

import sdung.ongil.domain.helprequest.entity.HelpRequestStatus;

import java.time.LocalDateTime;

public record HelpRequestStatusResponse(
        String requestId,
        String title,
        HelpRequestStatus status,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
