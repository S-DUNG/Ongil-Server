package sdung.ongil.domain.helprequest.dto;

import jakarta.validation.constraints.NotBlank;

public record HelpRequestCreateRequest(
        @NotBlank(message = "requesterId는 필수입니다.") String requesterId,
        @NotBlank(message = "title은 필수입니다.") String title,
        @NotBlank(message = "content는 필수입니다.") String content,
        String location
) {
}
