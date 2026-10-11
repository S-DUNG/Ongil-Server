package sdung.ongil.domain.manage.congestion_data.dto;

import sdung.ongil.domain.manage.congestion_data.entity.CongestionData;
import sdung.ongil.domain.manage.congestion_data.entity.CongestionLevel;

import java.time.LocalDateTime;

public record CongestionDataResponse(
        Long id,
        Long stationiId,
        String stationName,
        LocalDateTime timeSlot,
        CongestionLevel level,
        Double averageIntervalMinutes,
        LocalDateTime createdAt
) {
    public static CongestionDataResponse from(CongestionData data) {
        return new CongestionDataResponse(
                data.getId(),
                data.getStation().getId(),
                data.getStation().getName(),
                data.getTimeSlot(),
                data.getLevel(),
                data.getAverageIntervalMinutes(),
                data.getCreatedAt()
        );
    }
}
