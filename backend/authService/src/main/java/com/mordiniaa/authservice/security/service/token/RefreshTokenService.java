package com.mordiniaa.authservice.security.service.token;

import com.mordiniaa.authservice.exceptions.RefreshTokenException;
import com.mordiniaa.authservice.repositories.RefreshTokenRepository;
import com.mordiniaa.authservice.security.model.token.RefreshTokenEntity;
import com.mordiniaa.authservice.security.model.token.RefreshTokenFamily;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Duration;
import java.time.Instant;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    @Getter
    @Value("${security.app.refresh-token.validity-days}")
    private int validityDays;

    private final RefreshTokenFamilyService refreshTokenFamilyService;
    private final RefreshTokenRepository refreshTokenRepository;

    @Transactional
    public RefreshTokenEntity generateRefreshTokenEntity(RefreshTokenFamily family, String rawToken, List<String> roles) {
        Instant now = Instant.now();
        RefreshTokenEntity token = buildRefreshToken(family, now, null, rawToken, roles);
        return refreshTokenRepository.save(token);
    }

    @Transactional
    public RefreshTokenEntity rotate(UUID userId, Long tokenId, String oldRawToken, String newRawToken, List<String> roles, HttpServletRequest request) {

        RefreshTokenEntity storedTokenEntity = getRefreshToken(tokenId);
        return rotate(userId, storedTokenEntity, oldRawToken, newRawToken, roles);
    }

    @Transactional
    public RefreshTokenEntity rotate(UUID userId, RefreshTokenEntity entity, String oldRawToken, String newRawToken, List<String> roles) {

        RefreshTokenFamily family = entity.getRefreshTokenFamily();

        Instant now = Instant.now();
        boolean tokenExpired = entity.getExpiresAt().isBefore(now);

        if (entity.isRevoked() || tokenExpired) {
            throw new RefreshTokenException("Refresh Token Used Or Revoked");
        }

        if (!Objects.equals(family.getUserId(), userId))
            throw new RefreshTokenException("Invalid Refresh Token");

        boolean familyExpired = family.getExpiresAt().isBefore(now);
        if (family.isRevoked() || familyExpired) {
            throw new RefreshTokenException("Session Is Expired");
        }

        if (!validateTokensMatch(oldRawToken, entity.getHashedToken())) {
            log.info("Invalid refresh token");
            throw new RefreshTokenException("Invalid Refresh Token");
        }

        RefreshTokenEntity newTokenEntity = buildRefreshToken(family, now, entity.getId(), newRawToken, roles);
        RefreshTokenEntity savedEntity = refreshTokenRepository.save(newTokenEntity);

        rotateToken(savedEntity.getId(), entity.getId(), now);
        return savedEntity;
    }

    public RefreshTokenEntity getRefreshToken(Long tokenId) {
        return refreshTokenRepository.findById(tokenId)
                .orElseThrow(() -> new RefreshTokenException("Invalid Refresh Token"));
    }

    @Transactional
    public void rotateToken(Long newTokenId, Long oldTokenId, Instant revokedAt) {
        refreshTokenRepository.rotateToken(newTokenId, oldTokenId, revokedAt);
    }

    private byte[] sha256Bytes(String token) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            return digest.digest(token.getBytes(StandardCharsets.UTF_8));
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 not available", e);
        }
    }

    private RefreshTokenEntity buildRefreshToken(RefreshTokenFamily family, Instant time, Long parentId, String rawToken, List<String> roles) {

        String hashed = Base64.getUrlEncoder().withoutPadding().encodeToString(sha256Bytes(rawToken));
        return RefreshTokenEntity.builder()
                .hashedToken(hashed)
                .refreshTokenFamily(family)
                .parentId(parentId)
                .roles(roles)
                .createdAt(time)
                .expiresAt(time.plus(Duration.ofDays(validityDays)))
                .build();
    }

    @Transactional
    public void deactivateRefreshToken(Long idPart, String tokenPart) {

        RefreshTokenEntity entity = refreshTokenRepository.findRefreshTokenEntityById(idPart)
                .orElseThrow(() -> new RefreshTokenException("Session Expired"));

        RefreshTokenFamily family = entity.getRefreshTokenFamily();
        if (!validateTokensMatch(tokenPart, entity.getHashedToken())) {
            throw new RefreshTokenException("Invalid Refresh Token");
        }

        refreshTokenFamilyService.deactivateUserAuthenticationFamily(family.getId(), Instant.now());
    }

    public boolean validateTokensMatch(String rawToken, String storedToken) {
        return MessageDigest.isEqual(
                sha256Bytes(rawToken),
                Base64.getUrlDecoder().decode(storedToken)
        );
    }
}
