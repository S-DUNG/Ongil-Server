package sdung.ongil.domain.stations.service;

import sdung.ongil.domain.stations.dto.BusArrivalResponse;
import sdung.ongil.domain.stations.dto.CongestionForecastResponse;
import sdung.ongil.domain.stations.dto.NearbyStationResponse;
import sdung.ongil.domain.stations.entity.SmartPadStatus;
import sdung.ongil.domain.stations.odsay.OdsayClient;
import sdung.ongil.domain.stations.odsay.OdsayStation;
import sdung.ongil.domain.stations.tago.TagoArrivalClient;
import sdung.ongil.domain.stations.tago.TagoArrivalItem;
import sdung.ongil.domain.stations.tago.TagoStation;
import sdung.ongil.domain.stations.tago.TagoStationClient;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

@Service
public class StationsService {
    private static final double TAGO_MATCH_MAX_DISTANCE_METERS = 100;

    private final OdsayClient odsayClient;
    private final TagoStationClient tagoStationClient;
    private final TagoArrivalClient tagoArrivalClient;

    public StationsService(
            OdsayClient odsayClient,
            TagoStationClient tagoStationClient,
            TagoArrivalClient tagoArrivalClient
    ) {
        this.odsayClient = odsayClient;
        this.tagoStationClient = tagoStationClient;
        this.tagoArrivalClient = tagoArrivalClient;
    }

    public List<NearbyStationResponse> findNearbyStations(double lat, double lng, double radiusMeters) {
        List<OdsayStation> odsayStations = odsayClient.searchNearby(lat, lng, radiusMeters);
        List<TagoStation> tagoStations = tagoStationClient.searchNearby(lat, lng);

        List<NearbyStationResponse> result = new ArrayList<>();
        for (OdsayStation s : odsayStations) {
            double distance = haversineDistance(lat, lng, s.y(), s.x());
            TagoStation matched = findNearestTago(s.y(), s.x(), tagoStations);

            result.add(new NearbyStationResponse(
                    String.valueOf(s.stationID()),
                    s.stationName(),
                    s.y(),
                    s.x(),
                    Math.round(distance * 10) / 10.0,
                    matched != null ? matched.nodeId() : null,
                    matched != null ? matched.cityCode() : null
            ));
        }
        result.sort(Comparator.comparingDouble(NearbyStationResponse::distanceMeters));
        return result;
    }

    public List<BusArrivalResponse> getArrivals(String nodeId, String cityCode) {
        List<TagoArrivalItem> items = tagoArrivalClient.getArrivals(cityCode, nodeId);

        List<BusArrivalResponse> arrivals = new ArrayList<>();
        for (TagoArrivalItem item : items) {
            arrivals.add(new BusArrivalResponse(
                    item.routeNo(),
                    item.arrivalSeconds() / 60,
                    item.arrivalSeconds() % 60,
                    item.remainingStops()
            ));
        }
        arrivals.sort(Comparator.comparingInt(a -> a.etaMinutes() * 60 + a.etaSeconds()));
        return arrivals;
    }

    public List<CongestionForecastResponse> getCongestionForecast(String stationId) {
        Random random = new Random(stationId.hashCode());
        List<CongestionForecastResponse> forecast = new ArrayList<>();
        for (int hour = 0; hour < 24; hour++) {
            forecast.add(new CongestionForecastResponse(hour, randomStatus(random)));
        }
        return forecast;
    }

    private SmartPadStatus randomStatus(Random random) {
        return SmartPadStatus.values()[random.nextInt(SmartPadStatus.values().length)];
    }

    private TagoStation findNearestTago(double lat, double lng, List<TagoStation> candidates) {
        TagoStation nearest = null;
        double nearestDistance = Double.MAX_VALUE;
        for (TagoStation candidate : candidates) {
            double distance = haversineDistance(lat, lng, candidate.gpsLati(), candidate.gpsLong());
            if (distance < nearestDistance) {
                nearestDistance = distance;
                nearest = candidate;
            }
        }
        return (nearest != null && nearestDistance <= TAGO_MATCH_MAX_DISTANCE_METERS) ? nearest : null;
    }

    private double haversineDistance(double lat1, double lng1, double lat2, double lng2) {
        double earthRadius = 6371000;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLng = Math.toRadians(lng2 - lng1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(dLng / 2) * Math.sin(dLng / 2);
        return earthRadius * 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
    }
}