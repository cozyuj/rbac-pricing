package com.example.demo.config;

import com.example.demo.domain.CustomUserDetails;
import com.example.demo.domain.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.CustomUserDetailsService;
import com.example.demo.utils.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;
    private final CustomUserDetailsService customUserDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");
        String token = null;
        String username = null;
        String uri = request.getRequestURI();
        String method = request.getMethod();

        log.info("===== JwtFilter 시작 - {} {} =====", token, authHeader);
        log.info("Authorization 헤더: {}", authHeader != null ? "존재 (Bearer...)" : "없음");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
            log.info("JWT 토큰 추출 성공, 길이: {}", token.length());
            try {
                username = jwtUtil.extractUsername(token);
                log.info("JWT에서 username 추출 성공: {}", username);
            } catch (Exception e) {
                logger.warn("JWT 파싱 실패: {}");
            }
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            CustomUserDetails userDetails = (CustomUserDetails) customUserDetailsService.loadUserByUsername(username);
            log.info("사용자 인증 시작: {}", username);
            log.info("UserDetails 로드 성공 - ID: {}, Role: {}", userDetails.getId(), userDetails.getRole());
            if (jwtUtil.validateToken(token, userDetails)) {
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authToken);
                log.info("JWT 인증 성공! SecurityContext에 인증 정보 설정 완료");
            }else{
                log.error("JWT 검증 실패!");
            }
        }
        log.info("===== JwtFilter 종료 - 다음 필터로 진행 =====");
        filterChain.doFilter(request, response);
    }
}
