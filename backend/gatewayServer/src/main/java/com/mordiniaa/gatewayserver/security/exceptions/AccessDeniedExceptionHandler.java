package com.mordiniaa.gatewayserver.security.exceptions;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;

import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.server.authorization.ServerAccessDeniedHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j
@Component
public class AccessDeniedExceptionHandler implements ServerAccessDeniedHandler {
    private final ObjectMapper objectMapper;

    public AccessDeniedExceptionHandler(@Qualifier("customOM") ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, AccessDeniedException ex) {

        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.FORBIDDEN);
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> responseMap = new LinkedHashMap<>();
        responseMap.put("status", 403);
        responseMap.put("error", "Forbidden");
        responseMap.put("message", "You do not have permission to access this resource.");
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
