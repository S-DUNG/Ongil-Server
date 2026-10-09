package sdung.ongil.domain.destination.voice;

import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Component
public class VoiceQueryParser {

    private static final Pattern PUNCT = Pattern.compile("[\\p{Punct}\\p{IsPunctuation}]+");
    private static final Pattern SPACES = Pattern.compile("\\s+");

    // 앞쪽 군말 (연속 가능): "저기 음 강남역..."
    private static final Pattern PREFIX =
            Pattern.compile("^((저기요?|저|음+|어+|아+|그|혹시|좀)\\s+)+");

    // 동사구: 여기부터 문장 끝까지 전부 제거 ("가고 싶어서", "가고 십어" 등 변형 포함)
    private static final Pattern VERB_PHRASE = Pattern.compile(
            "\\s*(가\\s*고\\s*(싶|십|시프|시퍼)|가\\s*줘|가\\s*주세요|가\\s*자|갈\\s*래|갈\\s*게"
                    + "|가는\\s*(길|법|방법)|어떻게\\s*가|안내\\s*(해|좀|부탁)|데려\\s*다|알려\\s*(줘|주)|찾아\\s*(줘|주)).*$");

    // 확실한 조사
    private static final Pattern PARTICLE = Pattern.compile("(에서|에|으로)$");

    // "로"는 대학로, 종로처럼 이름의 일부일 수 있어서 장소 접미어 뒤일 때만 조사로 취급
    private static final Pattern PLACE_SUFFIX = Pattern.compile(
            ".*(역|터미널|공항|병원|학교|대학교|시장|공원|시청|구청|센터|마트|약국|은행|도서관|박물관|호텔|교회|성당)$");

    public String extractKeyword(String spokenText) {
        if (spokenText == null) {
            return "";
        }

        String result = PUNCT.matcher(spokenText).replaceAll(" ");
        result = SPACES.matcher(result).replaceAll(" ").trim();

        result = PREFIX.matcher(result).replaceFirst("");
        result = VERB_PHRASE.matcher(result).replaceFirst("").trim();

        result = PARTICLE.matcher(result).replaceFirst("");
        if (result.endsWith("로")) {
            String withoutRo = result.substring(0, result.length() - 1);
            if (PLACE_SUFFIX.matcher(withoutRo).matches()) {
                result = withoutRo;
            }
        }
        return result.trim();
    }
}