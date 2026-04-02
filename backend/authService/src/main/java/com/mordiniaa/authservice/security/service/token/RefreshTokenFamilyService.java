package com.mordiniaa.authservice.security.service.token;

import com.mordiniaa.authservice.repositories.RefreshTokenFamilyRepository;
import com.mordiniaa.authservice.security.model.token.RefreshTokenFamily;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenFamilyService {

    @Value("${security.app.refresh-token-family.max-session-days}")
    private int maxSessionDays;

    private final RefreshTokenFamilyRepository refreshTokenFamilyRepository;

    @Transactional
    public RefreshTokenFamily createNewFamily(UUID userId) {
        Instant now = Instant.now();
        RefreshTokenFamily newFamily = new RefreshTokenFamily(userId);
        newFamily.setCreatedAt(now);
        newFamily.setExpiresAt(now.plus(Duration.ofDays(maxSessionDays)));

        return refreshTokenFamilyRepository.save(newFamily);
    }

    @Transactional
    public void deactivateUserAuthenticationFamily(Long familyId, Instant revokedAt) {
        refreshTokenFamilyRepository.deactivateAuthenticationsFamily(familyId, revokedAt);
    }
}
