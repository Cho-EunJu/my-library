package com.cho.library.backend.auth.config;

import com.cho.library.backend.auth.filter.JwtAuthenticationFilter;
import com.cho.library.backend.auth.service.OAuth2Service;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    // CORS 허용 오리진
    @Value("${cors.allowed-origins}")
    private String allowedOrigins;

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)  // CSRF: REST API(JWT) 사용이므로 비활성화
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // 세션을 STATELESS로 (JWT 기반)
                .authorizeHttpRequests(auth -> auth
                    // 1) 로그인/리다이렉트 준비/콜백 등 인증 없이 허용
                    .requestMatchers("/api/auth/**").permitAll()
                    // 2) 정적 리소스, 루트 등 (필요 시 추가)
                    .requestMatchers("/", "/error").permitAll()
                    // 3) 그 외는 인증 필요
                    .anyRequest().authenticated()
                )

                // 폼로그인/기본로그인 사용 안 함 (전부 JWT로)
                .httpBasic(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable);

        http.addFilterBefore(jwtAuthenticationFilter,
                org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    // 시큐리티 레벨 CORS 설정
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();

        List<String> origins = Arrays.asList(allowedOrigins.split(","));

        config.setAllowedOrigins(origins);
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);
        // JWT를 헤더로 주고 받으므로 Authorization 헤더 노출 허용
        config.setExposedHeaders(List.of("Authorization"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/api/**", config); // API 경로에만 CORS 적용 (필요 시 /** 로 변경)
        return source;
    }
}
