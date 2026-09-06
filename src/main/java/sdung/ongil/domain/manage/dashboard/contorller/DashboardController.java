package sdung.ongil.domain.manage.dashboard.contorller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sdung.ongil.domain.manage.dashboard.dto.DashboardResponse;
import sdung.ongil.domain.manage.dashboard.service.DashboardService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/manage")
public class DashboardController {
    private final DashboardService dashboardService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/dashboard")
    public DashboardResponse getDashboard() {
        return dashboardService.getDashboard();
    }
}
