package com.mordiniaa.gatewayserver.security.exceptions;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.server.ServerAuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j
@Component
public class EntryPoint implements ServerAuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    public EntryPoint(@Qualifier("customOM") ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public Mono<Void> commence(ServerWebExchange exchange, AuthenticationException ex) {

        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> responseMap = new LinkedHashMap<>();
        responseMap.put("status", HttpStatus.UNAUTHORIZED.value());
        responseMap.put("message", "Authentication required");
        responseMap.put("error", "Unauthorized");
        responseMap.put("path", exchange.getRequest().getPath().value());
        responseMap.put("timestamp", Instant.now());

        byte[] bytes;
        try {
            bytes = objectMapper.writeValueAsBytes(responseMap);
        } catch (Exception e) {
            return Mono.error(e);
        }

        DataBuffer buffer = response.bufferFactory().wrap(bytes);
        return response.writeWith(Mono.just(buffer));
    }
}
