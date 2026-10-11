package sdung.ongil.domain.helprequest.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sdung.ongil.domain.helprequest.entity.HelpRequest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface HelpRequestRepository extends JpaRepository<HelpRequest, Long> {
    Optional<HelpRequest> findByRequestId(String requestId);
    List<HelpRequest> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end);
}