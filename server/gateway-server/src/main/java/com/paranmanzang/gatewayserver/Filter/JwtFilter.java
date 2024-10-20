package com.paranmanzang.gatewayserver.Filter;

import com.paranmanzang.gatewayserver.jwt.JWTUtil;
import com.paranmanzang.gatewayserver.jwt.JwtTokenServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseCookie;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class JwtFilter implements WebFilter {

    private final JWTUtil jwtUtil;
    private final JwtTokenServiceImpl jwtTokenService;

    public JwtFilter(JWTUtil jwtUtil, JwtTokenServiceImpl jwtTokenService) {
        this.jwtUtil = jwtUtil;
        this.jwtTokenService = jwtTokenService;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        String jwtToken = resolveToken(exchange.getRequest());
        log.info("제일 앞 JWT token: {}", jwtToken);
        String Auth = null;
        String refreshToken = null;

        // JWT 토큰이 없거나 만료된 경우
        if (jwtToken == null || jwtUtil.isExpired(jwtToken)) {
            // Authorization 토큰이 쿠키에 있는지 확인
            if (exchange.getRequest().getCookies().containsKey("Authorization")) {
                Auth = exchange.getRequest().getCookies().getFirst("Authorization").getValue();
                exchange.getResponse().getHeaders().set("Authorization", "Bearer " + Auth);
                //exchange.getResponse().addCookie(deleteCookie("Authorization", null));
                log.info("두번째 JWT token: {}", Auth);
                String nickname = jwtUtil.getNickNameFromToken(Auth);
                List<String> roles = jwtUtil.getRoleFromToken(Auth);
                List<GrantedAuthority> authorities = roles.stream()
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(nickname, null, authorities);

                // ReactiveSecurityContextHolder에 인증 정보 설정
                return chain.filter(exchange)
                        .contextWrite(ReactiveSecurityContextHolder.withAuthentication(authentication))
                        .doOnSuccess(unused -> log.info("User '{}' with roles '{}' has been authenticated", nickname, roles))
                        .doOnError(error -> log.error("Failed to authenticate user '{}': {}", nickname, error.getMessage()));
            }

            // Refresh 토큰 처리
            else if (exchange.getRequest().getCookies().containsKey("refresh")) {
                refreshToken = exchange.getRequest().getCookies().getFirst("refresh").getValue();
                log.info("Refresh token value: {}", refreshToken);
                jwtTokenService.deleteToken(refreshToken);
                exchange.getResponse().addCookie(deleteCookie("refresh", null));
                log.info("Refresh cookie cleared");
                return chain.filter(exchange); // Refresh cookie가 없으면 필터 체인 계속 진행
            } else {
                log.warn("Refresh cookie not found");
                return chain.filter(exchange); // Refresh cookie가 없으면 필터 체인 계속 진행
            }
        }

        // 유효한 JWT 토큰이 있는 경우 인증 정보 설정
        if (jwtToken != null && !jwtUtil.isExpired(jwtToken)) {
            String nickname = jwtUtil.getNickNameFromToken(jwtToken);
            List<String> roles = jwtUtil.getRoleFromToken(jwtToken);
            List<GrantedAuthority> authorities = roles.stream()
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());

            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(nickname, null, authorities);

            // ReactiveSecurityContextHolder에 인증 정보 설정
            return chain.filter(exchange)
                    .contextWrite(ReactiveSecurityContextHolder.withAuthentication(authentication))
                    .doOnSuccess(unused -> log.info("User '{}' with roles '{}' has been authenticated", nickname, roles))
                    .doOnError(error -> log.error("Failed to authenticate user '{}': {}", nickname, error.getMessage()));
        }

        // JWT 토큰이 없거나 유효하지 않은 경우 필터 체인 계속 진행
        return chain.filter(exchange);
    }

    // Authorization 헤더에서 JWT 토큰을 추출하는 메서드
    private String resolveToken(ServerHttpRequest request) {
        String authHeader = request.getHeaders().getFirst("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7).trim(); // Bearer 이후의 토큰만 반환
        }
        return null; // JWT 토큰이 없을 경우 null 반환
    }

    private ResponseCookie deleteCookie(String key, String value) {
        log.info("Creating cookie: {} = {}", key, value);
        return ResponseCookie.fromClientResponse(key, value)
                .maxAge(0)  // 쿠키의 만료 시간을 초 단위로 설정
                .path("/")
                .httpOnly(false)
                .build();
    }
}