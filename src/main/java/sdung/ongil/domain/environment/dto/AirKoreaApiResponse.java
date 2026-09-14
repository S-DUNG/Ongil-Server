package sdung.ongil.domain.environment.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class AirKoreaApiResponse {
    private Response response;

    @Getter
    @NoArgsConstructor
    public static class Response {
        private Header header;
        private Body body;
    }

    @Getter
    @NoArgsConstructor
    public static class Header {
        private String resultCode;
        private String resultMsg;
    }

    @Getter
    @NoArgsConstructor
    public static class Body {
        private List<Item> items;
    }

    @Getter
    @NoArgsConstructor
    public static class Item {
        private String stationName;
        private String pm10Value;
        private String pm25Value;
        private String dataTime;
    }
}
