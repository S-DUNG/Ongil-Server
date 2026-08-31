package sdung.ongil.global.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import sdung.ongil.global.jwt.JwtAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // JWT 방식이라 세션/쿠키 기반 CSRF 방어는 필요 없어서 꺼둠
                .csrf(AbstractHttpConfigurer::disable)

                // 세션을 서버에 저장 안 함 (토큰 기반이니까)
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                .authorizeHttpRequests(auth -> auth
                        // 로그인/재발급은 토큰 없이도 호출 가능해야 함
                        .requestMatchers("/auth/**").permitAll()
                        // /manage로 시작하는 건 반드시 인증 필요
                        .requestMatchers("/manage/**").authenticated()
                        // 나머지는 일단 다 허용 (필요하면 나중에 조정)
                        .anyRequest().permitAll()
                )

                // 우리가 만든 필터를 스프링 시큐리티 필터 체인에 끼워넣기
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}