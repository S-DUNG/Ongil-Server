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
import com.fasterxml.jackson.core.type.TypeReference;
import sdung.ongil.domain.route.dto.SimpleGuideResponse;

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

    @Transactional(readOnly = true)
    public SimpleGuideResponse getSimpleGuide(Long routeId) {
        Route route = routeRepository.findById(routeId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "해당 경로를 찾을 수 없습니다. id=" + routeId
                ));

        ManageStations origin = findStationOrThrow(route.getOriginId());
        ManageStations destination = findStationOrThrow(route.getDestinationId());

        List<OdsayPathSearchResponse.SubPath> subPaths = fromJson(route.getPathDataJson());

        List<String> steps = new java.util.ArrayList<>();
        for (int i = 0; i < subPaths.size(); i++) {
            OdsayPathSearchResponse.SubPath subPath = subPaths.get(i);
            boolean isFirst = (i == 0);
            boolean isLast = (i == subPaths.size() - 1);

            String startName = isFirst ? origin.getName() : subPath.startName();
            String endName = isLast ? destination.getName() : subPath.endName();

            steps.add(buildStepSentence(subPath, startName, endName, isLast));
        }

        return new SimpleGuideResponse(
                route.getId(),
                route.getTotalTime(),
                route.getPayment(),
                route.getTransferCount(),
                steps
        );
    }

    private String buildStepSentence(OdsayPathSearchResponse.SubPath subPath,
                                     String startName, String endName, boolean isLast) {
        int time = subPath.sectionTime();
        String ending = isLast ? "이동하면 도착입니다." : "이동하세요.";
        String from = (startName != null) ? startName + "에서 " : "";
        String busNo = getBusNo(subPath);

        return switch (subPath.trafficType()) {
            case 1 -> from + "지하철을 타고 " + endName + "까지 " + time + "분 " + ending;
            case 2 -> from + busNo + "번 버스를 타고 " + endName + "까지 " + time + "분 " + ending;
            case 3 -> isLast
                    ? from + "도보로 " + time + "분 이동하면 " + endName + "에 도착합니다."
                    : from + "도보로 " + time + "분 " + ending;
            default -> from + time + "분 " + ending;
        };
    }

    private String getBusNo(OdsayPathSearchResponse.SubPath subPath) {
        if (subPath.lane() == null || subPath.lane().isEmpty()) {
            return "";
        }
        String busNo = subPath.lane().get(0).busNo();
        return (busNo != null) ? busNo : "";
    }

    private String buildStepSentence(OdsayPathSearchResponse.SubPath subPath, boolean isLast) {
        int time = subPath.sectionTime();
        String ending = isLast ? "이동하면 도착입니다." : "이동하세요.";

        return switch (subPath.trafficType()) {
            case 1 -> "지하철을 타고 " + subPath.endName() + "까지 " + time + "분 " + ending;
            case 2 -> "버스를 타고 " + subPath.endName() + "까지 " + time + "분 " + ending;
            case 3 -> "도보로 " + time + "분 " + ending;
            default -> time + "분 " + ending;
        };
    }

    private List<OdsayPathSearchResponse.SubPath> fromJson(String json) {
        try {
            return objectMapper.readValue(json, new TypeReference<>() {});
        } catch (JsonProcessingException e) {
            throw new RuntimeException("경로 데이터 파싱 중 오류가 발생했습니다.", e);
        }
    }

    private String toJson(Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("경로 데이터 변환 중 오류가 발생했습니다.", e);
        }
    }
}