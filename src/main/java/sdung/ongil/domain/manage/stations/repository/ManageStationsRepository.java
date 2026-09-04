package sdung.ongil.domain.manage.stations.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import sdung.ongil.domain.manage.stations.entity.ManageStations;

import java.util.Optional;

public interface ManageStationsRepository extends JpaRepository<ManageStations, Long> {
    Page<ManageStations> findByActiveTrue(Pageable pageable);
    Page<ManageStations> findByNameContainingAndActiveTrue(String name, Pageable pageable);
    Optional<ManageStations> findByTagoStationId(String tagoStaionId);
    boolean existsByTagoStationId(String tagoStationId);
}
