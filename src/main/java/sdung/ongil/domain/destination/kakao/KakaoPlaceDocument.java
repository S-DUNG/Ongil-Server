package sdung.ongil.domain.destination.kakao;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record KakaoPlaceDocument(
        @JsonProperty("id") String id,
        @JsonProperty("place_name") String placeName,
        @JsonProperty("address_name") String addressName,
        @JsonProperty("road_address_name") String roadAddressName,
        @JsonProperty("x") String x,  // 경도(longitude)
        @JsonProperty("y") String y,  // 위도(latitude)
        @JsonProperty("place_url") String placeUrl
) {
}