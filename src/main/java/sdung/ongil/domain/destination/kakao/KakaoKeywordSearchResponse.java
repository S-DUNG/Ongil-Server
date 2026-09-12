package sdung.ongil.domain.destination.kakao;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record KakaoKeywordSearchResponse(
        List<KakaoPlaceDocument> documents,
        KakaoMeta meta
) {
}