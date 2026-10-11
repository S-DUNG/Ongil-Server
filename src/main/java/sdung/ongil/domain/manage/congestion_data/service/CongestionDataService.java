package sdung.ongil.domain.manage.congestion_data.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sdung.ongil.domain.manage.congestion_data.dto.CongestionDataResponse;
import sdung.ongil.domain.manage.congestion_data.entity.CongestionData;
import sdung.ongil.domain.manage.congestion_data.repository.CongestionDataRepository;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CongestionDataService {
    private final CongestionDataRepository congestionDataRepository;
    public Page<CongestionDataResponse> getCongestionDataList(Long stationId, LocalDateTime from, LocalDateTime to, Pageable pageable) {
        Page<CongestionData> result;
        if (stationId != null && from != null && to != null) {
            result = congestionDataRepository.findByStationIdAndTimeSlotBetween(stationId, from,to,pageable);
        } else if (stationId != null) {
            result = congestionDataRepository.findByStationId(stationId, pageable);
        } else if (from != null && to != null) {
            result = congestionDataRepository.findByTimeSlotBetween(from, to, pageable);
        } else {
            result = congestionDataRepository.findAll(pageable);
        }
        return result.map(CongestionDataResponse::from);
    }

    public CongestionDataResponse getCongestionData(Long congestionDataId) {
        CongestionData data = congestionDataRepository.findById(congestionDataId).orElseThrow(() -> new IllegalArgumentException(
                "혼잡도 데이터를 찾을 수 없습니다. id=" + congestionDataId
        ));
        return CongestionDataResponse.from(data);
    }
}
