package sdung.ongil.domain.destination.voice;

import jakarta.validation.ConstraintViolationException;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@Validated
@RestController
@RequestMapping("/destinations")
@RequiredArgsConstructor
public class VoiceGuideController {

    private final VoiceGuideService voiceGuideService;

    // 음성 목적지 안내: GET /destinations/voice-guide?padId=1&text=강남역에 가고 싶어
    @GetMapping("/voice-guide")
    public ResponseEntity<VoiceGuideResponse> voiceGuide(
            @RequestParam Long padId,
            @RequestParam @NotBlank(message = "목적지를 알아듣지 못했어요. 다시 말씀해 주세요.")
            @Size(max = 100, message = "문장이 너무 길어요. 짧게 다시 말씀해 주세요.") String text
    ) {
        return ResponseEntity.ok(voiceGuideService.guide(padId, text));
    }

    // 검증 실패를 500이 아니라 400으로 내려주기 위한 처리
    @ExceptionHandler(ConstraintViolationException.class)
    public void handleValidation(ConstraintViolationException e) {
        String message = e.getConstraintViolations().stream()
                .map(v -> v.getMessage())
                .findFirst()
                .orElse("잘못된 요청입니다.");
        log.warn("[voice-guide] 입력 검증 실패: {}", message);
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, message);
    }
}