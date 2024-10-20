package com.paranmanzang.gatewayserver.jwt;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class CustomAuthenticationFailureHandler {
    public Mono<Void> handleFailure(ServerWebExchange webFilterExchange, Throwable exception) {
        return ServerResponse.status(HttpStatus.UNAUTHORIZED).build().then();
    }
}
