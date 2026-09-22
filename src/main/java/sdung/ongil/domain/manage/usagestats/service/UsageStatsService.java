package sdung.ongil.domain.manage.usagestats.service;

import org.springframework.stereotype.Service;
import sdung.ongil.domain.helprequest.entity.HelpRequest;
import sdung.ongil.domain.helprequest.repository.HelpRequestRepository;
import sdung.ongil.domain.manage.stations.entity.ManageStations;
import sdung.ongil.domain.manage.stations.repository.ManageStationsRepository;
import sdung.ongil.domain.manage.usagestats.dto.StationUsage;
import sdung.ongil.domain.manage.usagestats.dto.TimeSlotUsage;
import sdung.ongil.domain.manage.usagestats.dto.UsagePeriod;
import sdung.ongil.domain.manage.usagestats.dto.UsageStatsResponse;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class UsageStatsService {

    private static final int SLOT_HOURS = 2;
    private static final String UNKNOWN_STATION_NAME = "알 수 없음";

    private final HelpRequestRepository helpRequestRepository;
    private final ManageStationsRepository manageStationsRepository;

    public UsageStatsService(HelpRequestRepository helpRequestRepository,
                             ManageStationsRepository manageStationsRepository) {
        this.helpRequestRepository = helpRequestRepository;
        this.manageStationsRepository = manageStationsRepository;
    }

    public UsageStatsResponse getUsageStats(UsagePeriod period) {
        LocalDateTime start = period.startDateTime();
        LocalDateTime end = period.endDateTime();

        List<HelpRequest> requests = helpRequestRepository.findByCreatedAtBetween(start, end);

        long totalCount = requests.size();
        double dailyAverage = period.days() == 0 ? 0 : (double) totalCount / period.days();

        List<TimeSlotUsage> hourlyUsage = calculateHourlyUsage(requests);
        List<StationUsage> topStations = calculateTopStations(requests);

        String mostUsedTimeSlot = hourlyUsage.stream()
                .max(Comparator.comparingLong(TimeSlotUsage::count))
                .filter(t -> t.count() > 0)
                .map(TimeSlotUsage::timeSlot)
                .orElse(null);

        String mostUsedStation = topStations.isEmpty() ? null : topStations.get(0).stationName();

        return new UsageStatsResponse(
                totalCount,
                dailyAverage,
                mostUsedStation,
                mostUsedTimeSlot,
                hourlyUsage,
                topStations
        );
    }

    private List<TimeSlotUsage> calculateHourlyUsage(List<HelpRequest> requests) {
        Map<Integer, Long> countBySlot = requests.stream()
                .collect(Collectors.groupingBy(
                        r -> r.getCreatedAt().getHour() / SLOT_HOURS,
                        Collectors.counting()
                ));

        return java.util.stream.IntStream.range(0, 24 / SLOT_HOURS)
                .mapToObj(slot -> new TimeSlotUsage(
                        formatSlotLabel(slot),
                        countBySlot.getOrDefault(slot, 0L)
                ))
                .toList();
    }

    private String formatSlotLabel(int slotIndex) {
        int startHour = slotIndex * SLOT_HOURS;
        int endHour = startHour + SLOT_HOURS;
        return String.format("%02d:00-%02d:00", startHour, endHour);
    }

    private List<StationUsage> calculateTopStations(List<HelpRequest> requests) {
        // 1. stationId 기준으로 카운트 집계 (location 대신 stationId 사용)
        Map<Long, Long> countByStationId = requests.stream()
                .filter(r -> r.getStationId() != null)
                .collect(Collectors.groupingBy(HelpRequest::getStationId, Collectors.counting()));

        // 2. 이용건수 기준 상위 5개 stationId 추출
        List<Map.Entry<Long, Long>> top5 = countByStationId.entrySet().stream()
                .sorted(Map.Entry.<Long, Long>comparingByValue().reversed())
                .limit(5)
                .toList();

        if (top5.isEmpty()) {
            return List.of();
        }

        // 3. 상위 5개에 해당하는 정류장 이름을 한 번에 조회 (N+1 방지)
        List<Long> topStationIds = top5.stream().map(Map.Entry::getKey).toList();
        Map<Long, String> stationNameById = manageStationsRepository.findAllById(topStationIds).stream()
                .collect(Collectors.toMap(ManageStations::getId, ManageStations::getName));

        // 4. StationUsage로 변환
        return top5.stream()
                .map(e -> new StationUsage(
                        stationNameById.getOrDefault(e.getKey(), UNKNOWN_STATION_NAME),
                        e.getValue()
                ))
                .toList();
    }
}