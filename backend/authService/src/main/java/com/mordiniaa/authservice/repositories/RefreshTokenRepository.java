package com.mordiniaa.authservice.repositories;

import com.mordiniaa.authservice.security.model.token.RefreshTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshTokenEntity, Long> {

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query(value = """
            update refresh_tokens old
            join refresh_tokens new on new.id = :newTokenId
            join refresh_token_families rtf on old.refresh_token_family = rtf.id
            set old.replaced_by_id = :newTokenId,
                old.revoked = true,
                old.revoked_at = :revokedAt
            where old.id = :oldTokenId
            """, nativeQuery = true)
    void rotateToken(Long newTokenId, Long oldTokenId, Instant revokedAt);

    Optional<RefreshTokenEntity> findRefreshTokenEntityById(Long idPart);
}
