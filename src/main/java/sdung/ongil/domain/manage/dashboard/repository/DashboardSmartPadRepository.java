package sdung.ongil.domain.manage.dashboard.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import sdung.ongil.domain.manage.smartpad.entity.SmartPadEntity;

import java.util.List;

public interface DashboardSmartPadRepository extends JpaRepository<SmartPadEntity, Long> {
    List<SmartPadEntity> findTop5ByOrderByCreatedAtDesc();
    @Query("SELECT s.status AS status, COUNT(s) AS count " +
            "FROM SmartPadEntity s " +
            "GROUP BY s.status")
    List<SmartPadStatusCount> countGroupByStatus();
}
