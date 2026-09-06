package sdung.ongil.domain.statistics.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@Table(
        name = "service_usage_log",
        indexes = {
                @Index(name = "idx_usage_service_name", columnList = "serviceName"),
                @Index(name = "idx_usage_called_at", columnList = "calledAt")
        }
)
public class ServiceUsageLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String serviceName;

    @Column(nullable = false)
    private LocalDateTime calledAt;

    private Long memberId;

    public ServiceUsageLog(String serviceName, LocalDateTime calledAt, Long memberId) {
        this.serviceName = serviceName;
        this.calledAt = calledAt;
        this.memberId = memberId;
    }
}
