package sdung.ongil.domain.destination.voice;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/destinations")
@RequiredArgsConstructor
public class VoiceGuideController {

    private final VoiceGuideService voiceGuideService;

    // 음성 목적지 안내: GET /destinations/voice-guide?padId=1&text=강남역에 가고 싶어
    @GetMapping("/voice-guide")
    public ResponseEntity<VoiceGuideResponse> voiceGuide(
            @RequestParam Long padId,
            @RequestParam String text
    ) {
        return ResponseEntity.ok(voiceGuideService.guide(padId, text));
    }
}