package sdung.ongil.domain.manage.smartpad.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import sdung.ongil.domain.manage.smartpad.dto.*;
import sdung.ongil.domain.manage.smartpad.entity.SmartPadEntity;
import sdung.ongil.domain.manage.smartpad.repository.SmartPadRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SmartPadService {

    private final SmartPadRepository smartPadRepository;

    // 목록 조회
    @Transactional(readOnly = true)
    public List<SmartPadResponse> getList() {
        return smartPadRepository.findAll()
                .stream()
                .map(SmartPadResponse::new)
                .toList();
    }

    // 상세 조회
    @Transactional(readOnly = true)
    public SmartPadResponse getDetail(Long padId) {
        SmartPadEntity entity = findEntityOrThrow(padId);
        return new SmartPadResponse(entity);
    }

    // 등록
    @Transactional
    public SmartPadResponse create(SmartPadCreateRequest request) {
        validateSerialNumberNotDuplicated(request.getSerialNumber(), null);

        SmartPadEntity entity = new SmartPadEntity(
                request.getStationId(),
                request.getSerialNumber(),
                request.getInstalledAt()
        );
        SmartPadEntity saved = smartPadRepository.save(entity);
        return new SmartPadResponse(saved);
    }

    // 정보 수정
    @Transactional
    public SmartPadResponse update(Long padId, SmartPadUpdateRequest request) {
        SmartPadEntity entity = findEntityOrThrow(padId);
        validateSerialNumberNotDuplicated(request.getSerialNumber(), padId);

        entity.updateInfo(
                request.getStationId(),
                request.getSerialNumber(),
                request.getInstalledAt()
        );
        return new SmartPadResponse(entity);
    }

    // 상태 변경
    @Transactional
    public SmartPadResponse updateStatus(Long padId, SmartPadStatusRequest request) {
        SmartPadEntity entity = findEntityOrThrow(padId);
        entity.changeStatus(request.getStatus());
        return new SmartPadResponse(entity);
    }

    // 삭제
    @Transactional
    public void delete(Long padId) {
        SmartPadEntity entity = findEntityOrThrow(padId);
        smartPadRepository.delete(entity);
    }

    // ===== 공통: ID로 찾기 =====
    private SmartPadEntity findEntityOrThrow(Long padId) {
        return smartPadRepository.findById(padId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "해당 스마트패드를 찾을 수 없습니다. id=" + padId
                ));
    }

    // ===== 공통: 시리얼번호 중복 체크 =====
    private void validateSerialNumberNotDuplicated(String serialNumber, Long excludePadId) {
        smartPadRepository.findBySerialNumber(serialNumber)
                .filter(found -> !found.getId().equals(excludePadId)) // 자기 자신은 제외
                .ifPresent(found -> {
                    throw new ResponseStatusException(
                            HttpStatus.CONFLICT, "이미 등록된 시리얼번호입니다: " + serialNumber
                    );
                });
    }
}