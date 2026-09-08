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
    private Long originId;      // 출발 정류장 ID

    @Column(nullable = false)
    private Long destinationId; // 도착 정류장 ID

    @Column(nullable = false)
    private int totalTime;      // 총 소요시간(분)

    @Column(nullable = false)
    private int payment;        // 요금

    @Column(nullable = false)
    private int transferCount;  // 환승 횟수

    // ODsay가 준 구간별 상세 정보를 JSON 문자열 저장
    @Lob
    @Column(nullable = false, columnDefinition = "TEXT")
    private String pathDataJson;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public Route(Long originId, Long destinationId, int totalTime, int payment,
                 int transferCount, String pathDataJson) {
        this.originId = originId;
        this.destinationId = destinationId;
        this.totalTime = totalTime;
        this.payment = payment;
        this.transferCount = transferCount;
        this.pathDataJson = pathDataJson;
        this.createdAt = LocalDateTime.now();
    }
}