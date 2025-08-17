package com.cho.library.backend.auth.filter;

import com.cho.library.backend.auth.util.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    // 인증 없이 통과시킬 화이트리스트 경로 (AuthController 경로 등)
    private static final List<String> WHITELIST = List.of(
            "/api/auth/",      // prefix 체크를 위해 startsWith 사용
            "/api/auth/oauth2/",
            "/api/auth/oauth2/cb/",
            "/", "/error"
    );

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        return WHITELIST.stream().anyMatch(path::startsWith);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        String header = request.getHeader("Authorization");

        if(header != null && header.startsWith("Bearer ")) {
            String token = header.replace("Bearer ", "");

            try {
                Claims claims = jwtUtil.parseClaims(token);
                String userId = claims.getSubject();
                String role = claims.get("role", String.class);

                // 권한 세팅
                Collection<? extends GrantedAuthority> authorities =
                        role != null ? List.of(new SimpleGrantedAuthority(role)) : List.of();

                // Authentication 객체 생성 후 SecurityContext에 저장
                Authentication auth = new UsernamePasswordAuthenticationToken(userId, null, authorities);
                SecurityContextHolder.getContext().setAuthentication(auth);

            } catch (Exception e) {
                log.warn("JWT 검증 실패: {}", e.getMessage());

            }
        }
        chain.doFilter(request, response);
    }
}
