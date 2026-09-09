package sdung.ongil.domain.helprequest.service;

import sdung.ongil.domain.helprequest.entity.HelpRequest;
import sdung.ongil.domain.helprequest.repository.HelpRequestRepository;
import sdung.ongil.domain.helprequest.dto.HelpRequestCreateRequest;
import sdung.ongil.domain.helprequest.dto.HelpRequestCreateResponse;
import sdung.ongil.domain.helprequest.dto.HelpRequestStatusResponse;
import sdung.ongil.domain.helprequest.exception.HelpRequestNotFoundException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class HelpRequestService {

    private final HelpRequestRepository helpRequestRepository;

    public HelpRequestService(HelpRequestRepository helpRequestRepository) {
        this.helpRequestRepository = helpRequestRepository;
    }

    public HelpRequestCreateResponse createHelpRequest(HelpRequestCreateRequest request) {
        String requestId = UUID.randomUUID().toString();

        HelpRequest helpRequest = new HelpRequest(
                requestId,
                request.requesterId(),
                request.title(),
                request.content(),
                request.location()
        );

        HelpRequest saved = helpRequestRepository.save(helpRequest);

        return new HelpRequestCreateResponse(
                saved.getRequestId(),
                saved.getStatus(),
                saved.getCreatedAt()
        );
    }

    public HelpRequestStatusResponse getHelpRequestStatus(String requestId) {
        HelpRequest helpRequest = helpRequestRepository.findByRequestId(requestId)
                .orElseThrow(() -> new HelpRequestNotFoundException(requestId));

        return new HelpRequestStatusResponse(
                helpRequest.getRequestId(),
                helpRequest.getTitle(),
                helpRequest.getStatus(),
                helpRequest.getCreatedAt(),
                helpRequest.getUpdatedAt()
        );
    }
}