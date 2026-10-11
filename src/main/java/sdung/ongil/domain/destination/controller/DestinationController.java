package sdung.ongil.domain.destination.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sdung.ongil.domain.destination.dto.DestinationSearchResult;
import sdung.ongil.domain.destination.service.DestinationService;

import java.util.List;

@RestController
@RequestMapping("/destinations")
public class DestinationController {

    private final DestinationService destinationService;

    public DestinationController(DestinationService destinationService) {
        this.destinationService = destinationService;
    }

    // 목적지 검색: GET /destinations/search?query=스타벅스
    @GetMapping("/search")
    public ResponseEntity<List<DestinationSearchResult>> search(@RequestParam String query) {
        return ResponseEntity.ok(destinationService.search(query));
    }

    // 목적지 상세 조회: GET /destinations/{destinationId}
    @GetMapping("/{destinationId}")
    public ResponseEntity<DestinationSearchResult> getById(@PathVariable String destinationId) {
        DestinationSearchResult result = destinationService.getById(destinationId);

        if (result == null) {
            return ResponseEntity.notFound().build();  // 만료됐거나 없는 경우 404
        }

        return ResponseEntity.ok(result);
    }
}