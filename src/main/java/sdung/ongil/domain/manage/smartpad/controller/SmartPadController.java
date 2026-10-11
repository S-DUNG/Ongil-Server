package sdung.ongil.domain.manage.smartpad.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sdung.ongil.domain.manage.smartpad.dto.*;
import sdung.ongil.domain.manage.smartpad.service.SmartPadService;

import java.util.List;

@RestController
@RequestMapping("/manage/smart-pads")
@RequiredArgsConstructor
public class SmartPadController {

    private final SmartPadService smartPadService;

    // 목록 조회
    @GetMapping
    public ResponseEntity<List<SmartPadResponse>> getList() {
        return ResponseEntity.ok(smartPadService.getList());
    }

    // 상세 조회
    @GetMapping("/{padId}")
    public ResponseEntity<SmartPadResponse> getDetail(@PathVariable Long padId) {
        return ResponseEntity.ok(smartPadService.getDetail(padId));
    }

    // 등록
    @PostMapping
    public ResponseEntity<SmartPadResponse> create(@RequestBody SmartPadCreateRequest request) {
        SmartPadResponse response = smartPadService.create(request);
        return ResponseEntity.status(201).body(response);
    }

    // 정보 수정
    @PatchMapping("/{padId}")
    public ResponseEntity<SmartPadResponse> update(
            @PathVariable Long padId,
            @RequestBody SmartPadUpdateRequest request
    ) {
        return ResponseEntity.ok(smartPadService.update(padId, request));
    }

    // 상태 변경
    @PatchMapping("/{padId}/status")
    public ResponseEntity<SmartPadResponse> updateStatus(
            @PathVariable Long padId,
            @RequestBody SmartPadStatusRequest request
    ) {
        return ResponseEntity.ok(smartPadService.updateStatus(padId, request));
    }

    // 삭제
    @DeleteMapping("/{padId}")
    public ResponseEntity<Void> delete(@PathVariable Long padId) {
        smartPadService.delete(padId);
        return ResponseEntity.noContent().build();
    }
}