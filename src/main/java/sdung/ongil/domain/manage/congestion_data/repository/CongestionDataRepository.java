package sdung.ongil.domain.manage.congestion_data.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import sdung.ongil.domain.manage.congestion_data.entity.CongestionData;

import java.time.LocalDateTime;

public interface CongestionDataRepository extends JpaRepository<CongestionData, Long> {
    Page<CongestionData> findByStationId(Long stationId, Pageable pageable);
    Page<CongestionData> findByTimeSlotBetween(LocalDateTime from, LocalDateTime to, Pageable pageable);
    Page<CongestionData> findByStationIdAndTimeSlotBetween(Long stationId, LocalDateTime from, LocalDateTime to, Pageable pageable);
}
