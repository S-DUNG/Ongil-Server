package sdung.ongil.domain.manage.dashboard.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sdung.ongil.domain.manage.stations.entity.ManageStations;

import java.util.List;

public interface DashboardStationRepository extends JpaRepository<ManageStations, Long> {
    long countByActiveTrue();
    List<ManageStations> findTop5ByActiveTrueOrderByCreatedAtDesc();
}
