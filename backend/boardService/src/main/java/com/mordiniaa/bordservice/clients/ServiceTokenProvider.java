package com.mordiniaa.bordservice.clients;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mordiniaa.bordservice.clients.auth.AuthServiceClient;
import com.mordiniaa.bordservice.config.InterserviceProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Base64;

@Service
@RequiredArgsConstructor
public class ServiceTokenProvider {

    private final AuthServiceClient authServiceClient;
    private final InterserviceProperties interserviceProperties;
    private final ObjectMapper objectMapper;
    private String token;
    private Instant expiresAt;

    public synchronized String getToken() {

        if (token == null || isExpired()) {
            refreshToken();
        }
        return token;
    }

    private boolean isExpired() {
        return expiresAt == null || Instant.now().isAfter(expiresAt.minusSeconds(30));
    }

    private void refreshToken() {
        String newToken = authServiceClient.tokenResponse(
                interserviceProperties.getCurrentService()
        ).token();

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
            throw new IllegalStateException("Invalid JWT", e);
        }
    }
}
