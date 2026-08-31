package sdung.ongil.domain.auth.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "admin_refresh_token")
@Getter
@Setter
@NoArgsConstructor
public class AdminRefreshTokenEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 리프레시 토큰 원문이 아니라 해시값을 저장 (탈취 시 원문 유출 방지)
    @Column(nullable = false, unique = true, length = 255)
    private String tokenHash;

    // 토큰 만료 시간
    @Column(nullable = false)
    private LocalDateTime expiresAt;

    // 로그아웃하거나 재발급되면 true로 바꿔서 더 이상 못 쓰게 함
    @Column(nullable = false)
    private boolean revoked = false;

    public AdminRefreshTokenEntity(String tokenHash, LocalDateTime expiresAt) {
        this.tokenHash = tokenHash;
        this.expiresAt = expiresAt;
    }
}