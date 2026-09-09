package sdung.ongil.domain.helprequest.dto;

import sdung.ongil.domain.helprequest.entity.HelpRequestStatus;

import java.time.LocalDateTime;

public record HelpRequestCreateResponse(
        String requestId,
        HelpRequestStatus status,
        LocalDateTime createdAt
) {
}
