package sdung.ongil.domain.environment.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.coyote.Response;

import java.util.List;

@Getter
@NoArgsConstructor
public class KmaWeatherApiResponse {
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
        private Items items;
    }

    @Getter
    @NoArgsConstructor
    public static class Items {
        private List<Item> item;
    }

    @Getter
    @NoArgsConstructor
    public static class Item {
        private String baseDate;
        private String baseTime;
        private String category;
        private String obsrValue;
    }
}
