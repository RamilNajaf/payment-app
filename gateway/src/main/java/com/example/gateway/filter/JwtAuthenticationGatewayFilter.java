package com.example.gateway.filter;


import com.example.gateway.util.JwtUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;

import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import static com.example.gateway.constants.Constants.*;


@Component
public class JwtAuthenticationGatewayFilter implements GlobalFilter, Ordered {

    private final JwtUtil jwtUtil;


    public JwtAuthenticationGatewayFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }


    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String requestPath = request.getURI().getPath();

        boolean isPublicEndpoint = PUBLIC_ENDPOINTS.stream()
                .anyMatch(requestPath::startsWith);

        if (isPublicEndpoint) {
            return chain.filter(exchange);
        }

        final String token = request.getHeaders().getOrEmpty(AUTHORIZATION).get(0);

        try {
            jwtUtil.validateToken(token.substring(7));
            Claims claims = jwtUtil.parseJwtClaims(token.substring(7));
            String userId = String.valueOf(claims.get(USER_ID));

            ServerHttpRequest mutatedRequest = exchange.getRequest()
                    .mutate()
                    .header(X_USER_ID, userId)
                    .build();

            ServerWebExchange mutatedExchange = exchange.mutate()
                    .request(mutatedRequest)
                    .build();

            return chain.filter(mutatedExchange);
        } catch (ExpiredJwtException e) {
            return Mono.error(new RuntimeException("Access Token Expired"));
        } catch (Exception exception) {
            return Mono.error(new RuntimeException("Invalid Jwt Token"));
        }
    }

    @Override
    public int getOrder() {
        return -1; // Ensures the filter runs early
    }
}