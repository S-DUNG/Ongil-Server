package sdung.ongil.domain.manage.congestion_data.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import sdung.ongil.domain.manage.congestion_data.dto.CongestionDataResponse;
import sdung.ongil.domain.manage.congestion_data.service.CongestionDataService;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/manage/congestion-data")
@RequiredArgsConstructor
public class CongestionDataController {
    private final CongestionDataService congestionDataService;

    @GetMapping
    public ResponseEntity<Page<CongestionDataResponse>> getCongestionDataList(
            @RequestParam(required = false) Long stationId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)LocalDateTime from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)LocalDateTime to, Pageable pageable
            ) {
        return ResponseEntity.ok(congestionDataService.getCongestionDataList(stationId, from, to, pageable));
    }

    @GetMapping("/{congestionDataId}")
    public ResponseEntity<CongestionDataResponse> getCongestionData(@PathVariable Long congestionDataId) {
        return ResponseEntity.ok(congestionDataService.getCongestionData(congestionDataId));
    }
}
