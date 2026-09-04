package sdung.ongil.domain.manage.stations.service;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sdung.ongil.domain.manage.stations.dto.ManageStationsCreateRequest;
import sdung.ongil.domain.manage.stations.dto.ManageStationsResponse;
import sdung.ongil.domain.manage.stations.dto.ManageStationsUpdateRequest;
import sdung.ongil.domain.manage.stations.entity.ManageStations;
import sdung.ongil.domain.manage.stations.repository.ManageStationsRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Builder
public class ManageStationsService {
    private final ManageStationsRepository manageStationsRepository;
    public Page<ManageStationsResponse> getStations(String keyword, Pageable pageable) {
        Page<ManageStations> stations = (keyword == null || keyword.isBlank())
                ? manageStationsRepository.findByActiveTrue(pageable)
                : manageStationsRepository.findByNameContainingAndActiveTrue(keyword, pageable);
        return stations.map(ManageStationsResponse::from);
    }

    public ManageStationsResponse getStation(Long stationId) {
        return ManageStationsResponse.from(findActiveStation(stationId));
    }

    @Transactional
    public ManageStationsResponse createStation(ManageStationsCreateRequest request) {
        if (manageStationsRepository.existsByTagoStationId(request.tagoStationId())) {
            throw new IllegalStateException(
                    "이미 등록된 TAGO 정류장입니다. tagoStationId=" + request.tagoStationId());
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
}
