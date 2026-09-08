package sdung.ongil.domain.route.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import sdung.ongil.domain.manage.stations.entity.ManageStations;
import sdung.ongil.domain.manage.stations.repository.ManageStationsRepository;
import sdung.ongil.domain.route.dto.RouteResponse;
import sdung.ongil.domain.route.entity.Route;
import sdung.ongil.domain.route.repository.RouteRepository;
import sdung.ongil.domain.stations.odsay.OdsayClient;
import sdung.ongil.domain.stations.odsay.OdsayPathSearchResponse;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RouteService {

    private final RouteRepository routeRepository;
    private final ManageStationsRepository stationsRepository;
    private final OdsayClient odsayClient;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Transactional
    public RouteResponse searchRoute(Long originId, Long destinationId) {

        ManageStations origin = findStationOrThrow(originId);
        ManageStations destination = findStationOrThrow(destinationId);

        OdsayPathSearchResponse response = odsayClient.searchPath(
                origin.getLatitude(), origin.getLongitude(),
                destination.getLatitude(), destination.getLongitude()
        );

        if (response == null || response.result() == null
                || response.result().path() == null || response.result().path().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "경로를 찾을 수 없습니다.");
        }

        OdsayPathSearchResponse.Path bestPath = response.result().path().get(0);
        List<OdsayPathSearchResponse.SubPath> subPaths = bestPath.subPath();

        String pathDataJson = toJson(subPaths);

        Route route = new Route(
                originId,
                destinationId,
                bestPath.info().totalTime(),
                bestPath.info().payment(),
                bestPath.info().busTransitCount(),
                pathDataJson
        );
        Route saved = routeRepository.save(route);

        return new RouteResponse(saved, subPaths);
    }

    private ManageStations findStationOrThrow(Long stationId) {
        return stationsRepository.findById(stationId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "해당 정류장을 찾을 수 없습니다. id=" + stationId
                ));
    }

    private String toJson(Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("경로 데이터 변환 중 오류가 발생했습니다.", e);
        }
    }
}