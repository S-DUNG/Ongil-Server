package sdung.ongil.domain.helprequest.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sdung.ongil.domain.helprequest.dto.HelpRequestCreateRequest;
import sdung.ongil.domain.helprequest.dto.HelpRequestCreateResponse;
import sdung.ongil.domain.helprequest.dto.HelpRequestStatusResponse;
import sdung.ongil.domain.helprequest.entity.HelpRequest;
import sdung.ongil.domain.helprequest.service.HelpRequestService;

@RestController
@RequestMapping("/help-requests")
public class HelpRequestController {
    private final HelpRequestService helpRequestService;
    public HelpRequestController(HelpRequestService helpRequestService) {
        this.helpRequestService = helpRequestService;
    }

    @PostMapping
    public ResponseEntity<HelpRequestCreateResponse> createHelpRequest(
            @Valid @RequestBody HelpRequestCreateRequest request
            ) {
        HelpRequestCreateResponse response = helpRequestService.createHelpRequest(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<HelpRequestStatusResponse> getHelpRequestStatus(
            @PathVariable String requestId
    ) {
        HelpRequestStatusResponse response = helpRequestService.getHelpRequestStatus(requestId);
        return ResponseEntity.ok(response);
    }
}
