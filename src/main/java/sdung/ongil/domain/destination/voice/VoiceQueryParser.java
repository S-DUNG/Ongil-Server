package sdung.ongil.domain.destination.voice;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class VoiceQueryParser {

    // 문장 끝에 자주 붙는 표현들 (긴 표현부터 먼저 검사해야 함)
    private static final List<String> SUFFIXES = List.of(
            "에 가고 싶어요", "에 가고 싶어", "에 가고싶어요", "에 가고싶어",
            "으로 가고 싶어요", "으로 가고 싶어", "로 가고 싶어요", "로 가고 싶어",
            "로 가줘", "으로 가줘", "에 가줘",
            "로 안내해줘", "으로 안내해줘", "에 안내해줘",
            "에 갈래", "으로 갈래", "로 갈래"
    );

    public String extractKeyword(String spokenText) {
        if (spokenText == null) {
            return "";
        }

        String trimmed = spokenText.trim();

        for (String suffix : SUFFIXES) {
            if (trimmed.endsWith(suffix)) {
                return trimmed.substring(0, trimmed.length() - suffix.length()).trim();
            }
        }

        return trimmed;
    }
}