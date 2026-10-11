package sdung.ongil.domain.manage.stations.Kakao;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record KakaoAddressSearchResponse(
        List<Document> documents
) {
   @JsonIgnoreProperties(ignoreUnknown = true)
   public record Document(
           @JsonProperty("address_name") String addressName,
           String x,
           String y
   ) {}
}
