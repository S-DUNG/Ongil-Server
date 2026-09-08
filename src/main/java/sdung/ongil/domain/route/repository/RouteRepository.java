package sdung.ongil.domain.route.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sdung.ongil.domain.route.entity.Route;

public interface RouteRepository extends JpaRepository<Route, Long> {
}