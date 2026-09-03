package sdung.ongil.domain.stations.controller;

import sdung.ongil.domain.stations.dto.BusArrivalResponse;
import sdung.ongil.domain.stations.dto.CongestionForecastResponse;
import sdung.ongil.domain.stations.dto.NearbyStationResponse;
import sdung.ongil.domain.stations.service.StationsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/stations")
public class StationsController {

    private final StationsService stationsService;

    public StationsController(StationsService stationsService) {
        this.stationsService = stationsService;
    }

    @GetMapping("/nearby")
    public List<NearbyStationResponse> getNearbyStations(
            @RequestParam double lat,
            @RequestParam double lng,
            @RequestParam(required = false, defaultValue = "1000") double radiusMeters
    ) {
        return stationsService.findNearbyStations(lat, lng, radiusMeters);
    }

    @GetMapping("/{stationId}/arrivals")
    public List<BusArrivalResponse> getArrivals(
            @PathVariable String stationId,
            @RequestParam String cityCode
    ) {
        return stationsService.getArrivals(stationId, cityCode);
    }

    @GetMapping("/{stationId}/congestion-forecast")
    public List<CongestionForecastResponse> getCongestionForecast(@PathVariable String stationId) {
        return stationsService.getCongestionForecast(stationId);
    }
}