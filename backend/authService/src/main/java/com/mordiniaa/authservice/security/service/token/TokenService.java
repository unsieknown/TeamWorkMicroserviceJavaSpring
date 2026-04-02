package com.mordiniaa.authservice.security.service.token;

import com.mordiniaa.authservice.exceptions.BadRequestException;
import com.mordiniaa.authservice.exceptions.RefreshTokenException;
import com.mordiniaa.authservice.security.model.token.RefreshTokenEntity;
import com.mordiniaa.authservice.security.model.token.RefreshTokenFamily;
import com.mordiniaa.authservice.security.service.JwtService;
import com.mordiniaa.authservice.security.service.user.SecurityUserProjection;
import com.mordiniaa.authservice.security.token.TokenSet;
import com.mordiniaa.authservice.services.inter.UserServiceInter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class TokenService {

    private final RawTokenService rawTokenService;
    private final RefreshTokenService refreshTokenService;
    private final JwtService jwtService;
    private final RefreshTokenFamilyService refreshTokenFamilyService;
    private final UserServiceInter userServiceInter;

    @Transactional
    public TokenSet issue(UUID userId, List<String> roles) {

        String rawToken = rawTokenService.generateOpaqueToken();

        RefreshTokenFamily family = refreshTokenFamilyService.createNewFamily(userId);

        RefreshTokenEntity entity = refreshTokenService.generateRefreshTokenEntity(
                family,
                rawToken,
                roles
        );

        String refreshToken = entity.getId() + "." + rawToken;

        String jwtToken = jwtService.buildJwt(
                userId,
                roles
        );

        return new TokenSet(jwtToken, refreshToken);
    }

    @Transactional
    public TokenSet refreshToken(String rawToken) {

        int dotIdx = rawToken.indexOf(".");
        if (dotIdx < 1) throw new RefreshTokenException("Invalid Refresh Token");

        String idPart = rawToken.substring(0, dotIdx);
        String tokenPart = rawToken.substring(dotIdx + 1);

        long tokenId = getTokenId(idPart);

        RefreshTokenEntity entity = refreshTokenService.getRefreshToken(tokenId);
        RefreshTokenFamily family = entity.getRefreshTokenFamily();

        SecurityUserProjection user = userServiceInter.findSecurityUserById(family.getUserId())
                .orElseThrow(() -> new BadRequestException("Bad Credentials"));

        String newRawToken = rawTokenService.generateOpaqueToken();
        List<String> roles = List.of(user.getAppRole().name());

        RefreshTokenEntity storedEntity;
        try {
            storedEntity = refreshTokenService.rotate(user.getUserId(), entity, tokenPart, newRawToken, roles);
        } catch (Exception e) {
            throw new RefreshTokenException("Invalid Refresh Token");
        }

        String refreshToken = storedEntity.getId() + "." + newRawToken;

        String jwtToken = jwtService.buildJwt(
                user.getUserId(),
                roles
        );

        return new TokenSet(jwtToken, refreshToken);
    }

    private Long getTokenId(String tokenId) {
        try {
            return Long.parseLong(tokenId);
        } catch (NumberFormatException e) {
            throw new RefreshTokenException("Invalid Refresh Token");
        }
    }
}
