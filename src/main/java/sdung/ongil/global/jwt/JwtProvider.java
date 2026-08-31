package sdung.ongil.domain.auth.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtProvider {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.access-token-expire-ms}")
    private long accessTokenExpireMs;

    // application.properties의 jwt.secret 값으로 서명용 키 만들기
    private SecretKey key() {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    // AccessToken 생성
    public String createAccessToken() {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + accessTokenExpireMs);

        return Jwts.builder()
                .setSubject("admin")
                .claim("type", "access")
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(key(), SignatureAlgorithm.HS256)
                .compact();
    }

    // AccessToken 검증 (유효하면 true, 만료/변조면 false)
    public boolean validateAccessToken(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(key())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            return "access".equals(claims.get("type"));
        } catch (JwtException | IllegalArgumentException e) {
            // 토큰이 만료됐거나, 위조됐거나, 형식이 이상하면 여기로 옴
            return false;
        }
    }
}