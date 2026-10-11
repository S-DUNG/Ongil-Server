package sdung.ongil.domain.manage.smartpad.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import sdung.ongil.domain.manage.smartpad.dto.*;
import sdung.ongil.domain.manage.smartpad.entity.SmartPadEntity;
import sdung.ongil.domain.manage.smartpad.repository.SmartPadRepository;
import sdung.ongil.domain.manage.stations.entity.ManageStations;
import sdung.ongil.domain.manage.stations.repository.ManageStationsRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SmartPadService {

    private final SmartPadRepository smartPadRepository;
    private final ManageStationsRepository manageStationsRepository;

    @Transactional(readOnly = true)
    public List<SmartPadResponse> getList() {
        return smartPadRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public SmartPadResponse getDetail(Long padId) {
        SmartPadEntity entity = findEntityOrThrow(padId);
        return toResponse(entity);
    }

    @Transactional
    public SmartPadResponse create(SmartPadCreateRequest request) {
        validateSerialNumberNotDuplicated(request.getSerialNumber(), null);
        validateStationExists(request.getStationId());

        SmartPadEntity entity = new SmartPadEntity(
                request.getStationId(),
                request.getSerialNumber(),
                request.getInstalledAt()
        );
        SmartPadEntity saved = smartPadRepository.save(entity);
        return toResponse(saved);
    }

    @Transactional
    public SmartPadResponse update(Long padId, SmartPadUpdateRequest request) {
        SmartPadEntity entity = findEntityOrThrow(padId);
        validateSerialNumberNotDuplicated(request.getSerialNumber(), padId);
        validateStationExists(request.getStationId());

        entity.updateInfo(
                request.getStationId(),
                request.getSerialNumber(),
                request.getInstalledAt()
        );
        return toResponse(entity);
    }

    @Transactional
    public SmartPadResponse updateStatus(Long padId, SmartPadStatusRequest request) {
        SmartPadEntity entity = findEntityOrThrow(padId);
        entity.changeStatus(request.getStatus());
        return toResponse(entity);
    }

    @Transactional
    public void delete(Long padId) {
        SmartPadEntity entity = findEntityOrThrow(padId);
        smartPadRepository.delete(entity);
    }

    private SmartPadResponse toResponse(SmartPadEntity entity) {
        ManageStations station = manageStationsRepository.findById(entity.getStationId())
                .orElse(null);
        return new SmartPadResponse(entity, station);
    }

    private SmartPadEntity findEntityOrThrow(Long padId) {
        return smartPadRepository.findById(padId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "해당 스마트패드를 찾을 수 없습니다. id=" + padId
                ));
    }

    private void validateSerialNumberNotDuplicated(String serialNumber, Long excludePadId) {
        smartPadRepository.findBySerialNumber(serialNumber)
                .filter(found -> !found.getId().equals(excludePadId))
                .ifPresent(found -> {
                    throw new ResponseStatusException(
                            HttpStatus.CONFLICT, "이미 등록된 시리얼번호입니다: " + serialNumber
                    );
                });
    }

    private void validateStationExists(Long stationId) {
        if (!manageStationsRepository.existsById(stationId)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "존재하지 않는 정류장입니다. stationId=" + stationId
            );
        }
    }
}