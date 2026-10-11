package sdung.ongil.domain.environment.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SafetyResponseDto {
    private String sidoName;
    private String stationName;
    private Integer pm10Value;
    private String pm10Grade;
    private Integer pm25Value;
    private String pm25Grade;
    private String safetyMessage;
}
