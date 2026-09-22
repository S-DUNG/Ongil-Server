package sdung.ongil.domain.helprequest.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record HelpRequestCreateRequest(
        @NotBlank(message = "requesterId는 필수입니다.") String requesterId,
        @NotBlank(message = "title은 필수입니다.") String title,
        @NotBlank(message = "content는 필수입니다.") String content,
        @NotNull(message = "stationId는 필수입니다.") Long stationId
) {
}