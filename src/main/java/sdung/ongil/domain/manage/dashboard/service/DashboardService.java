package sdung.ongil.domain.manage.dashboard.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sdung.ongil.domain.manage.dashboard.dto.DashboardResponse;
import sdung.ongil.domain.manage.dashboard.dto.RecentItemDto;
import sdung.ongil.domain.manage.dashboard.dto.SmartPadStatusCountDto;
import sdung.ongil.domain.manage.dashboard.repository.DashboardSmartPadRepository;
import sdung.ongil.domain.manage.dashboard.repository.DashboardStationRepository;
import sdung.ongil.domain.manage.smartpad.entity.SmartPadStatus;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DashboardService {
    private final DashboardStationRepository stationRepository;
    private final DashboardSmartPadRepository smartPadRepository;

    public DashboardResponse getDashboard() {
        long totalStationCount = stationRepository.count();
        long totalSmartPadCount = smartPadRepository.count();

        List<SmartPadStatusCountDto> statusCountDtos = smartPadRepository.countGroupByStatus().stream()
                .map(projection -> new SmartPadStatusCountDto(
                        projection.getStatus().name(),
                        toLabel(projection.getStatus()),
                        projection.getCount()
                ))
                .collect(Collectors.toList());

        List<RecentItemDto> recentStations = stationRepository.findTop5ByOrderByCreatedAtDesc().stream()
                .map(station -> new RecentItemDto(station.getId(), station.getName(), station.getCreatedAt()))
                .collect(Collectors.toList());

        List<RecentItemDto> recentSmartPads = smartPadRepository.findTop5ByOrderByCreatedAtDesc().stream()
                .map(pad -> new RecentItemDto(pad.getId(), pad.getSerialNumber(), pad.getCreatedAt()))
                .collect(Collectors.toList());

        return new DashboardResponse(
                totalStationCount,
                totalSmartPadCount,
                statusCountDtos,
                recentStations,
                recentSmartPads
        );
    }

    private String toLabel(SmartPadStatus status) {
        return switch (status) {
            case NORMAL -> "정상";
            case INSPECTING -> "점검중";
            case BROKEN -> "고장";
        };
    }
}
