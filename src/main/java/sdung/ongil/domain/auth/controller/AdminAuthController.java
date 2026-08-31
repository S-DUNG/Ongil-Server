package sdung.ongil.domain.auth.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sdung.ongil.domain.auth.dto.LoginRequest;
import sdung.ongil.domain.auth.dto.RefreshRequest;
import sdung.ongil.domain.auth.entity.AdminRefreshTokenEntity;
import sdung.ongil.global.jwt.JwtProvider;
import sdung.ongil.domain.auth.repository.AdminRefreshTokenRepository;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AdminAuthController {

    private final JwtProvider jwtProvider;
    private final AdminRefreshTokenRepository refreshTokenRepository;

    @Value("${admin.password}")
    private String adminPassword;

    @Value("${jwt.refresh-token-expire-ms}")
    private long refreshTokenExpireMs;

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        if (!adminPassword.equals(request.getPassword())) {
            return ResponseEntity.status(401).body(Map.of("message", "비밀번호가 틀렸습니다."));
        }

        String accessToken = jwtProvider.createAccessToken();
        String refreshToken = issueRefreshToken();

        return ResponseEntity.ok(Map.of(
                "accessToken", accessToken,
                "refreshToken", refreshToken
        ));
    }

    // 토큰 재발급
    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@RequestBody RefreshRequest request) {
        String hash = sha256(request.getRefreshToken());

        Optional<AdminRefreshTokenEntity> found =
                refreshTokenRepository.findByTokenHashAndRevokedFalse(hash);

        if (found.isEmpty() || found.get().getExpiresAt().isBefore(LocalDateTime.now())) {
            return ResponseEntity.status(401).body(Map.of("message", "리프레시 토큰이 유효하지 않습니다. 다시 로그인해주세요."));
        }

        // 기존 토큰은 폐기 (rotation: 재사용 방지)
        AdminRefreshTokenEntity old = found.get();
        old.setRevoked(true);
        refreshTokenRepository.save(old);

        String newAccessToken = jwtProvider.createAccessToken();
        String newRefreshToken = issueRefreshToken();

        return ResponseEntity.ok(Map.of(
                "accessToken", newAccessToken,
                "refreshToken", newRefreshToken
        ));
    }

    // 리프레시 토큰 새로 만들어서 DB에 저장하고, 원문은 클라이언트에게 반환
    private String issueRefreshToken() {
        String rawToken = UUID.randomUUID().toString() + UUID.randomUUID();
        String hash = sha256(rawToken);

        LocalDateTime expiresAt = LocalDateTime.now().plusNanos(refreshTokenExpireMs * 1_000_000);

        AdminRefreshTokenEntity entity = new AdminRefreshTokenEntity(hash, expiresAt);
        refreshTokenRepository.save(entity);

        return rawToken;
    }

    // 원문 토큰을 해시로 변환 (DB에는 이 해시값만 저장)
    private String sha256(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(input.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hashBytes);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}