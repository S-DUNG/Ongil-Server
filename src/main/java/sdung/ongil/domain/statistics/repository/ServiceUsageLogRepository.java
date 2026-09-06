package sdung.ongil.domain.statistics.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import sdung.ongil.domain.statistics.entity.ServiceUsageLog;

import java.time.LocalDateTime;
import java.util.List;

public interface ServiceUsageLogRepository extends JpaRepository<ServiceUsageLog, Long> {
    // 전체 기간
    @Query("SELECT s.serviceName AS serviceName, COUNT(s) AS count " +
            "FROM ServiceUsageLog s " +
            "GROUP BY s.serviceName " +
            "ORDER BY COUNT(s) DESC")
    List<ServiceUsageCount> countAll();

    // 기간별
    @Query("SELECT s.serviceName AS serviceName, COUNT(s) AS count " +
            "FROM ServiceUsageLog s " +
            "WHERE s.calledAt >= :start AND s.calledAt < :end " +
            "GROUP BY s.serviceName " +
            "ORDER BY COUNT(s) DESC")
    List<ServiceUsageCount> countByPeriod(@Param("start")LocalDateTime start, @Param("end") LocalDateTime end);
}
