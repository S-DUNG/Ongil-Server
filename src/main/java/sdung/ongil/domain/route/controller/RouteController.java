package sdung.ongil.domain.route.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import sdung.ongil.domain.route.dto.RouteResponse;
import sdung.ongil.domain.route.dto.SimpleGuideResponse;
import sdung.ongil.domain.route.service.RouteService;

@RestController
@RequestMapping("/routes")
@RequiredArgsConstructor
public class RouteController {

    private final RouteService routeService;

    // 대중교통 경로 탐색 (출발지: 등록된 정류장 / 목적지: 임의 좌표)
    @GetMapping
    public ResponseEntity<RouteResponse> searchRoute(
            @RequestParam Long originId,
            @RequestParam double destinationLat,
            @RequestParam double destinationLng,
            @RequestParam(required = false) String destinationName
    ) {
        return ResponseEntity.ok(
                routeService.searchRoute(originId, destinationLat, destinationLng, destinationName)
        );
    }

    // AI(알고리즘 기반) 쉬운 경로 안내
    @GetMapping("/{routeId}/simple-guide")
    public ResponseEntity<SimpleGuideResponse> getSimpleGuide(
            @PathVariable Long routeId
    ) {
        return ResponseEntity.ok(routeService.getSimpleGuide(routeId));
    }
}