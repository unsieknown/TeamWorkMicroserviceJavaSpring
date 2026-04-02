package com.mordiniaa.authservice.repositories;

import com.mordiniaa.authservice.security.model.token.RefreshTokenFamily;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.Instant;

@Repository
public interface RefreshTokenFamilyRepository extends JpaRepository<RefreshTokenFamily, Long> {

    @Modifying
    @Query(value = """
            update refresh_token_families family
            left join refresh_tokens token on family.id = token.refresh_token_family
            set family.revoked = true,
                family.revoked_at = :revokedAt,
                token.revoked = true,
                token.revoked_at = :revokedAt
                where family.id = :familyId
            """, nativeQuery = true)
    void deactivateAuthenticationsFamily(Long familyId, Instant revokedAt);
}
