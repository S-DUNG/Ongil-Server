package sdung.ongil.domain.route.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "route")
@Getter
@NoArgsConstructor
public class Route {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long originId;          // 출발 정류장 ID (등록된 키오스크)

    @Column(nullable = false)
    private Double destinationLat;  // 목적지 위도 (임의 좌표, 등록 여부 무관)

    @Column(nullable = false)
    private Double destinationLng;  // 목적지 경도

    @Column(length = 100)
    private String destinationName; // 목적지 이름 (프론트에서 전달)

    @Column(nullable = false)
    private int totalTime;      // 총 소요시간(분)

    @Column(nullable = false)
    private int payment;        // 요금

    @Column(nullable = false)
    private int transferCount;  // 환승 횟수

    @Lob
    @Column(nullable = false, columnDefinition = "TEXT")
    private String pathDataJson;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public Route(Long originId, Double destinationLat, Double destinationLng, String destinationName,
                 int totalTime, int payment, int transferCount, String pathDataJson) {
        this.originId = originId;
        this.destinationLat = destinationLat;
        this.destinationLng = destinationLng;
        this.destinationName = destinationName;
        this.totalTime = totalTime;
        this.payment = payment;
        this.transferCount = transferCount;
        this.pathDataJson = pathDataJson;
        this.createdAt = LocalDateTime.now();
    }
}