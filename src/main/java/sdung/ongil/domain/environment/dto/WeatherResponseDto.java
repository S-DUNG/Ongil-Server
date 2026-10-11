package sdung.ongil.domain.environment.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class WeatherResponseDto {
    private String baseDate;
    private String baseTime;
    private Double temperature;
    private Double humidity;
    private Double windSpeed;
    private String precipitationType;
}
