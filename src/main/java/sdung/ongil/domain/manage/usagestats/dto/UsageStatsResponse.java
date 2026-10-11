package sdung.ongil.domain.manage.usagestats.dto;

import java.util.List;

public record UsageStatsResponse(
        long totalCount,
        double dailyAverage,
        String mostUsedStation,
        String mostUsedTimeSlot,
        List<TimeSlotUsage> hourlyUsage,
        List<StationUsage> topStations
) {
}