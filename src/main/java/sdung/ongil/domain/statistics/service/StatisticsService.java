package sdung.ongil.domain.statistics.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sdung.ongil.domain.statistics.dto.ServiceUsageResponse;
import sdung.ongil.domain.statistics.repository.ServiceUsageLogRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StatisticsService {
    private final ServiceUsageLogRepository serviceUsageLogRepository;

    public List<ServiceUsageResponse> getAllServiceUsage() {
        return serviceUsageLogRepository.countAll().stream()
                .map(ServiceUsageResponse::new)
                .collect(Collectors.toList());
    }

    public List<ServiceUsageResponse> getServiceUsageByPeriod(LocalDate startDate, LocalDate endDate) {
        LocalDateTime start = startDate.atStartOfDay();
        LocalDateTime end = endDate.plusDays(1).atStartOfDay();
        return serviceUsageLogRepository.countByPeriod(start, end).stream()
                .map(ServiceUsageResponse::new)
                .collect(Collectors.toList());
    }
}
