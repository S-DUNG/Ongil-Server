package sdung.ongil.domain.manage.usagestats.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import sdung.ongil.domain.manage.usagestats.dto.UsagePeriod;
import sdung.ongil.domain.manage.usagestats.dto.UsageStatsResponse;
import sdung.ongil.domain.manage.usagestats.service.UsageStatsService;

@RestController
@RequestMapping("/manage/usage-stats")
public class UsageStatsController {

    private final UsageStatsService usageStatsService;

    public UsageStatsController(UsageStatsService usageStatsService) {
        this.usageStatsService = usageStatsService;
    }

    @GetMapping
    public UsageStatsResponse getUsageStats(@RequestParam UsagePeriod period) {
        return usageStatsService.getUsageStats(period);
    }
}