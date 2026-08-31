package sdung.ongil.domain.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sdung.ongil.domain.auth.entity.AdminRefreshTokenEntity;

import java.util.Optional;

public interface AdminRefreshTokenRepository extends JpaRepository<AdminRefreshTokenEntity, Long> {

    // 해시값으로 토큰 찾기 (아직 폐기 안 된 것만)
    Optional<AdminRefreshTokenEntity> findByTokenHashAndRevokedFalse(String tokenHash);
}