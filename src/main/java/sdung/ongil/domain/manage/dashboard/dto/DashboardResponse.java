package sdung.ongil.domain.manage.dashboard.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class DashboardResponse {
    private final long totalStationCount;
    private final long totalSmartPadCount;
    private final List<SmartPadStatusCountDto> smartPadStatusCounts;
    private List<RecentItemDto> recentStations;
    private final List<RecentItemDto> recentSmartPads;

    public DashboardResponse(long totalStationCount,
                             long totalSmartPadCount,
                             List<SmartPadStatusCountDto> smartPadStatusCounts,
                             List<RecentItemDto> recetStations,
                             List<RecentItemDto> recentSmartPads) {
        this.totalStationCount = totalStationCount;
        this.totalSmartPadCount = totalSmartPadCount;
        this.smartPadStatusCounts = smartPadStatusCounts;
        this.recentStations = recentStations;
        this.recentSmartPads = recentSmartPads;
    }
}
