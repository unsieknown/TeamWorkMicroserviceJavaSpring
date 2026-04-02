package com.mordiniaa.authservice.security.service;

import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.security.KeyPair;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class JwtService {

    private final KeyPair keyPair;

    public String buildJwt(UUID userId, List<String> roles) {

        Instant now = Instant.now();

        return Jwts.builder()
                .issuer("auth-service")
                .subject(userId.toString())
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plusSeconds(3600)))

                .claim("roles", roles)
                .claim("type", "user")
                .signWith(keyPair.getPrivate())
                .compact();
    }

    public String buildServiceToken(String serviceName, HttpServletRequest request) {
        // TODO: Validate Request
        return buildServiceToken(serviceName);
    }

    public String buildServiceToken(String serviceName) {
        Instant now = Instant.now();

        return Jwts.builder()
                .issuer("auth-service")
                .subject(serviceName)
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plusSeconds(600)))
                .claim("roles", List.of("SERVICE"))
                .claim("type", "service")
                .signWith(keyPair.getPrivate())
                .compact();
    }
}
