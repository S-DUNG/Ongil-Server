package sdung.ongil.domain.helprequest.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sdung.ongil.domain.helprequest.entity.HelpRequest;

import java.util.Optional;

public interface HelpRequestRepository extends JpaRepository<HelpRequest, Long> {
    Optional<HelpRequest> findByRequestId(String requestId);
}
