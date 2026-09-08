package sdung.ongil.domain.route.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import sdung.ongil.domain.route.dto.RouteResponse;
import sdung.ongil.domain.route.service.RouteService;

@RestController
@RequestMapping("/routes")
@RequiredArgsConstructor
public class RouteController {

    private final RouteService routeService;

    // 대중교통 경로 탐색
    @GetMapping
    public ResponseEntity<RouteResponse> searchRoute(
            @RequestParam Long originId,
            @RequestParam Long destinationId
    ) {
        return ResponseEntity.ok(routeService.searchRoute(originId, destinationId));
    }
}