package com.mordiniaa.authservice.clients;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mordiniaa.authservice.security.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Base64;

@Service
@RequiredArgsConstructor
public class ServiceTokenProvider {

    private final JwtService jwtService;
    private final ObjectMapper objectMapper;
    private String token;
    private Instant expiresAt;

    public String getToken() {

        if (token == null || isExpired()) {
            refreshToken();
        }
        return token;
    }

    private boolean isExpired() {
        return expiresAt == null || Instant.now().isAfter(expiresAt.minusSeconds(30));
    }

    private void refreshToken() {

        String newToken = jwtService.buildServiceToken("auth-service");

        this.token = newToken;
        this.expiresAt = extractExpiration(newToken);
    }

    private Instant extractExpiration(String jwt) {
        try {

            String payload = jwt.split("\\.")[1];

            String json = new String(
                    Base64.getUrlDecoder().decode(payload),
                    StandardCharsets.UTF_8
            );

            JsonNode node = objectMapper.readTree(json);

            return Instant.ofEpochSecond(node.get("exp").asLong());
        } catch (Exception e) {
            throw new IllegalStateException("Invalid JWT");
        }
    }
}
