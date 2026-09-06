package sdung.ongil.domain.statistics.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import sdung.ongil.domain.statistics.dto.ServiceUsageResponse;
import sdung.ongil.domain.statistics.service.StatisticsService;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/statistics")
public class StatisticsController {
    public final StatisticsService statisticsService;

    public List<ServiceUsageResponse> getServiceUsage(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDate endDate) {
        if (startDate != null && endDate != null) {
            return statisticsService.getServiceUsageByPeriod(startDate, endDate);
        }
        return statisticsService.getAllServiceUsage();
    }
}
