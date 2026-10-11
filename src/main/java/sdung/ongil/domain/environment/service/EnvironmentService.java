package sdung.ongil.domain.environment.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import sdung.ongil.domain.environment.client.AirKoreaClient;
import sdung.ongil.domain.environment.client.KakaoRegionClient;
import sdung.ongil.domain.environment.client.KmaWeatherClient;
import sdung.ongil.domain.environment.dto.AirKoreaApiResponse;
import sdung.ongil.domain.environment.dto.KmaWeatherApiResponse;
import sdung.ongil.domain.environment.dto.SafetyResponseDto;
import sdung.ongil.domain.environment.dto.WeatherResponseDto;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EnvironmentService {

    private final KmaWeatherClient kmaWeatherClient;
    private final AirKoreaClient airKoreaClient;
    private final KakaoRegionClient kakaoRegionClient;

    public WeatherResponseDto getWeather(double lat, double lng) {
        GridConverter.Grid grid = GridConverter.toGrid(lat, lng);
        KmaWeatherApiResponse response = kmaWeatherClient.getUltraSrtNcst(grid.nx(), grid.ny());

        KmaWeatherApiResponse.Items itemsWrapper = response.getResponse().getBody().getItems();
        List<KmaWeatherApiResponse.Item> items =
                (itemsWrapper == null || itemsWrapper.getItem() == null) ? List.of() : itemsWrapper.getItem();
        Map<String, String> values = items.stream()
                .collect(Collectors.toMap(
                        KmaWeatherApiResponse.Item::getCategory,
                        KmaWeatherApiResponse.Item::getObsrValue,
                        (a, b) -> b));

        return WeatherResponseDto.builder()
                .baseDate(items.isEmpty() ? null : items.get(0).getBaseDate())
                .baseTime(items.isEmpty() ? null : items.get(0).getBaseTime())
                .temperature(parseDouble(values.get("T1H")))
                .humidity(parseDouble(values.get("REH")))
                .windSpeed(parseDouble(values.get("WSD")))
                .precipitationType(toPrecipitationType(values.get("PTY")))
                .build();
    }

    public SafetyResponseDto getSafety(double lat, double lng) {
        String sidoName = kakaoRegionClient.getSidoName(lat, lng);
        AirKoreaApiResponse response = airKoreaClient.getRealtimeDensityBySido(sidoName);

        List<AirKoreaApiResponse.Item> rawItems = response.getResponse().getBody().getItems();
        List<AirKoreaApiResponse.Item> items = rawItems == null ? List.of() : rawItems;

        AirKoreaApiResponse.Item nearest = items.isEmpty() ? null : items.get(0);

        Integer pm10 = nearest == null ? null : parseInt(nearest.getPm10Value());
        Integer pm25 = nearest == null ? null : parseInt(nearest.getPm25Value());

        return SafetyResponseDto.builder()
                .sidoName(sidoName)
                .stationName(nearest == null ? null : nearest.getStationName())
                .pm10Value(pm10)
                .pm10Grade(gradeOfPm10(pm10))
                .pm25Value(pm25)
                .pm25Grade(gradeOfPm25(pm25))
                .safetyMessage(buildSafetyMessage(pm10, pm25))
                .build();
    }

    private String toPrecipitationType(String code) {
        if (code == null) return "정보없음";
        return switch (code) {
            case "0" -> "없음";
            case "1" -> "비";
            case "2" -> "비/눈";
            case "3" -> "눈";
            case "5" -> "빗방울";
            case "6" -> "빗방울눈날림";
            case "7" -> "눈날림";
            default -> "정보없음";
        };
    }

    private String gradeOfPm10(Integer value) {
        if (value == null) return "정보없음";
        if (value <= 30) return "좋음";
        if (value <= 80) return "보통";
        if (value <= 150) return "나쁨";
        return "매우나쁨";
    }

    private String gradeOfPm25(Integer value) {
        if (value == null) return "정보없음";
        if (value <= 15) return "좋음";
        if (value <= 35) return "보통";
        if (value <= 75) return "나쁨";
        return "매우나쁨";
    }

    private String buildSafetyMessage(Integer pm10, Integer pm25) {
        if ((pm10 != null && pm10 > 150) || (pm25 != null && pm25 > 75)) {
            return "미세먼지 농도가 매우 나쁨 수준입니다. 외출 시 마스크 착용을 권장합니다.";
        }
        if ((pm10 != null && pm10 > 80) || (pm25 != null && pm25 > 35)) {
            return "미세먼지 농도가 나쁨 수준입니다. 장시간 외출 시 주의가 필요합니다.";
        }
        return "현재 대기질은 양호한 수준입니다.";
    }

    private Double parseDouble(String s) {
        try {
            return s == null ? null : Double.parseDouble(s);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private Integer parseInt(String s) {
        try {
            return s == null ? null : Integer.parseInt(s);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}