package sdung.ongil.domain.manage.stations.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sdung.ongil.domain.manage.stations.Kakao.GeocodeResult;
import sdung.ongil.domain.destination.kakao.KakaoLocalClient;
import sdung.ongil.domain.manage.stations.Kakao.KakaoGeocodingClient;
import sdung.ongil.domain.manage.stations.dto.ManageStationsCreateRequest;
import sdung.ongil.domain.manage.stations.dto.ManageStationsResponse;
import sdung.ongil.domain.manage.stations.dto.ManageStationsUpdateRequest;
import sdung.ongil.domain.manage.stations.dto.TagoStationSearchResponse;
import sdung.ongil.domain.manage.stations.entity.ManageStations;
import sdung.ongil.domain.manage.stations.repository.ManageStationsRepository;
import sdung.ongil.domain.stations.tago.TagoStationClient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ManageStationsService {
    private final ManageStationsRepository manageStationsRepository;
    private final TagoStationClient tagoStationClient;
    private final KakaoGeocodingClient kakaoGeocodingClient;

    public Page<ManageStationsResponse> getStations(String keyword, Pageable pageable) {
        Page<ManageStations> stations = (keyword == null || keyword.isBlank())
                ? manageStationsRepository.findByActiveTrue(pageable)
                : manageStationsRepository.findByNameContainingAndActiveTrue(keyword, pageable);
        return stations.map(ManageStationsResponse::from);
    }

    public ManageStationsResponse getStation(Long stationId) {
        return ManageStationsResponse.from(findActiveStation(stationId));
    }

    public List<TagoStationSearchResponse> searchTagoStations(double lat, double lng) {
        return tagoStationClient.searchNearby(lat, lng).stream()
                .map(tagoStation -> {
                    Long stationId = manageStationsRepository
                            .findByTagoStationIdAndActiveTrue(tagoStation.nodeId())
                            .map(ManageStations::getId)
                            .orElse(null);
                    return TagoStationSearchResponse.from(tagoStation, stationId);
                })
                .toList();
    }

    public List<TagoStationSearchResponse> searchTagoStationsByAddress(String address) {
        GeocodeResult geo = kakaoGeocodingClient.geocode(address)
                .orElseThrow(() -> new IllegalArgumentException("주소를 찾을 수 없습니다. address=" + address));
        return searchTagoStations(geo.lat(), geo.lng());
    }

    @Transactional
    public ManageStationsResponse createStation(ManageStationsCreateRequest request) {
        Optional<ManageStations> existing =
                manageStationsRepository.findByTagoStationId(request.tagoStationId());

        if (existing.isPresent()) {
            ManageStations station = existing.get();
            if (station.isActive()) {
                throw new IllegalStateException(
                        "이미 등록된 TAGO 정류장입니다. tagoStationId=" + request.tagoStationId());
            }

            station.activate();
            station.updateInfo(request.name(), request.latitude(), request.longitude(), request.address());
            return ManageStationsResponse.from(station);
        }

        ManageStations stations = ManageStations.builder()
                .tagoStationId(request.tagoStationId())
                .name(request.name())
                .latitude(request.latitude())
                .longitude(request.longitude())
                .address(request.address())
                .build();
        return ManageStationsResponse.from(manageStationsRepository.save(stations));
    }

    @Transactional
    public ManageStationsResponse updateStation(Long stationId, ManageStationsUpdateRequest request) {
        ManageStations stations = findActiveStation(stationId);
        stations.updateInfo(request.name(),  request.latitude(), request.longitude(), request.address());
        return ManageStationsResponse.from(stations);
    }

    @Transactional
    public void deactivateStation(Long stationId) {
        ManageStations stations = findActiveStation(stationId);
        stations.deactivate();
    }

    private ManageStations findActiveStation(Long stationId) {
        ManageStations stations = manageStationsRepository.findById(stationId)
                .orElseThrow(() -> new IllegalArgumentException("정류장을 찾을 수 없습니다. id=" + stationId));
        if (!stations.isActive()) {
            throw new IllegalArgumentException("정류장을 찾을 수 없습니다. id=" + stationId);
        }
        return stations;
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