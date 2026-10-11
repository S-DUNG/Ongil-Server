package sdung.ongil.domain.helprequest.dto;

import java.time.LocalDateTime;
import java.util.List;

public record ErrorResponse(
        String message,
        LocalDateTime timestamp,
        List<String> details
) {
    public static ErrorResponse of (String message) {
        return new ErrorResponse(message, LocalDateTime.now(), List.of());
    }

    public static ErrorResponse of(String message, List<String> details) {
        return new ErrorResponse(message, LocalDateTime.now(), details);
    }
}
