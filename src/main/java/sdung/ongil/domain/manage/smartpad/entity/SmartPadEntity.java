package sdung.ongil.domain.manage.smartpad.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "smart_pad")
@Getter
@NoArgsConstructor
public class SmartPadEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 이 패드가 설치된 정류장 ID
    @Column(nullable = false)
    private Long stationId;

    // 패드 고유 시리얼 번호
    @Column(nullable = false, unique = true, length = 100)
    private String serialNumber;

    // 상태: 정상 / 점검중 / 고장
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SmartPadStatus status;

    // 설치 일시
    @Column(nullable = false)
    private LocalDateTime installedAt;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    // 등록할 때 쓰는 생성자
    public SmartPadEntity(Long stationId, String serialNumber, LocalDateTime installedAt) {
        this.stationId = stationId;
        this.serialNumber = serialNumber;
        this.installedAt = installedAt;
        this.status = SmartPadStatus.NORMAL; // 처음 등록하면 기본적으로 "정상" 상태
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    // ===== 정보 수정용 메서드 =====
    public void updateInfo(Long stationId, String serialNumber, LocalDateTime installedAt) {
        this.stationId = stationId;
        this.serialNumber = serialNumber;
        this.installedAt = installedAt;
        this.updatedAt = LocalDateTime.now();
    }

    // ===== 상태 변경용 메서드 =====
    public void changeStatus(SmartPadStatus status) {
        this.status = status;
        this.updatedAt = LocalDateTime.now();
    }
}