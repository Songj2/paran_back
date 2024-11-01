package com.paranmanzang.gatewayserver.Filter;

import com.paranmanzang.gatewayserver.jwt.JwtTokenServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class LogoutFilter implements WebFilter {

    private final JwtTokenServiceImpl jwtTokenService; // JWT 토큰 서비스를 주입받습니다.

    // 생성자 주입
    public LogoutFilter(JwtTokenServiceImpl jwtTokenService) {
        this.jwtTokenService = jwtTokenService;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        // 로그아웃 요청인지 확인
        log.info("Incoming request: {} {}", exchange.getRequest().getMethod(), exchange.getRequest().getURI());

        if (exchange.getRequest().getURI().getPath().equals("/logout")) {
            log.info("Logout request received");

            // Authorization 헤더 확인
            String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
            log.info("Authorization header: {}", authHeader);

            String refreshToken = null; // refreshToken 변수를 초기화합니다.

            // "refresh" 쿠키가 존재하는지 확인합니다.
            if (exchange.getRequest().getCookies().containsKey("refresh")) {
                refreshToken = exchange.getRequest().getCookies().getFirst("refresh").getValue();
                log.info("Refresh token value: {}", refreshToken);

                // refreshToken 삭제
                jwtTokenService.deleteToken(refreshToken);
                exchange.getResponse().getHeaders().add(HttpHeaders.SET_COOKIE,createCookie("refresh", null).toString());

                log.info("Refresh cookie cleared");
            } else {
                log.warn("Refresh cookie not found");
            }

            log.info("Refresh token after processing: {}", refreshToken);

            // JWT 토큰이 존재하는 경우 처리
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                String token = authHeader.substring(7); // "Bearer " 이후의 토큰 추출
                log.info("Extracted token: {}", token);

                // 토큰 블랙리스트에 추가
                jwtTokenService.blacklistToken(token, 600000);
                log.info("Token has been blacklisted");

                // 응답에서 Authorization 헤더를 삭제합니다.
                exchange.getResponse().getHeaders().remove(HttpHeaders.AUTHORIZATION);
                log.info("Authorization header removed from response");

                // 추가적인 로그아웃 처리를 할 수 있습니다.
                exchange.getResponse().setStatusCode(HttpStatus.OK);
                log.info("Response status set to OK");

                return exchange.getResponse().setComplete(); // 응답 완료
            } else {
                log.warn("No Bearer token found in Authorization header");
            }
        } else {
            log.info("Request path is not for logout: {}", exchange.getRequest().getURI().getPath());
        }

        return chain.filter(exchange); // 필터 체인을 계속 진행
    }

    private ResponseCookie createCookie(String key, String value) {
        log.info("Creating cookie: {} = {}", key, value);
        return ResponseCookie.fromClientResponse(key, value)
                .maxAge(0)  // 쿠키의 만료 시간을 초 단위로 설정
                .path("/")
                .httpOnly(false)
                .build();
    }
}