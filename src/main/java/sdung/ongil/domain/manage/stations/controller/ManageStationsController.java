package sdung.ongil.domain.manage.stations.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import sdung.ongil.domain.manage.stations.dto.ManageStationsCreateRequest;
import sdung.ongil.domain.manage.stations.dto.ManageStationsResponse;
import sdung.ongil.domain.manage.stations.dto.ManageStationsUpdateRequest;
import sdung.ongil.domain.manage.stations.service.ManageStationsService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@RequiredArgsConstructor
@RestController
@RequestMapping("/manage/stations")
public class ManageStationsController {
    private final ManageStationsService manageStationsService;

    @GetMapping
    public ResponseEntity<Page<ManageStationsResponse>> getStations(
            @RequestParam(required = false) String keyword,
            Pageable pageable
    ) {
        return ResponseEntity.ok(manageStationsService.getStations(keyword, pageable));
    }

    @GetMapping("/{stationId}")
    public ResponseEntity<ManageStationsResponse> getStation(@PathVariable Long stationId) {
        return ResponseEntity.ok(manageStationsService.getStation(stationId));
    }

    @PostMapping
    public ResponseEntity<ManageStationsResponse> createStation(
            @Valid @RequestBody ManageStationsCreateRequest request
            ) {
        ManageStationsResponse response = manageStationsService.createStation(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PatchMapping("/{stationId}")
    public ResponseEntity<ManageStationsResponse> updateStation(
            @PathVariable Long stationId,
            @Valid @RequestBody ManageStationsUpdateRequest request
            ) {
        return ResponseEntity.ok(manageStationsService.updateStation(stationId, request));
    }

    @DeleteMapping("/{stationId}")
    public ResponseEntity<Void> deleteStation(@PathVariable Long stationId) {
        manageStationsService.deactivateStation(stationId);
        return ResponseEntity.noContent().build();
    }
}
