package com.mordiniaa.authservice.services;

import com.mordiniaa.authservice.exceptions.BadRequestException;
import com.mordiniaa.authservice.exceptions.RefreshTokenException;
import com.mordiniaa.authservice.models.PasswordResetToken;
import com.mordiniaa.authservice.repositories.PasswordResetTokenRepository;
import com.mordiniaa.authservice.request.RefreshTokenRequest;
import com.mordiniaa.authservice.request.ResetPasswordTokenRequest;
import com.mordiniaa.authservice.security.service.token.RefreshTokenService;
import com.mordiniaa.authservice.security.service.token.TokenService;
import com.mordiniaa.authservice.security.service.user.SecurityUser;
import com.mordiniaa.authservice.security.token.TokenSet;
import com.mordiniaa.authservice.services.inter.UserServiceInter;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final TokenService tokenService;
    private final RefreshTokenService refreshTokenService;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final UserServiceInter userServiceInter;
    private final PasswordEncoder passwordEncoder;

    public TokenSet authenticate(Authentication authentication) {

        SecurityUser user = (SecurityUser) authentication.getPrincipal();
        log.info("Security user: {}", user);

        List<String> roles = user.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();
        return tokenService.issue(user.getUserId(), roles);
    }

    public TokenSet refresh(RefreshTokenRequest refreshTokenRequest) {

        String refreshToken = refreshTokenRequest.getRefreshToken();

        if (refreshToken == null) {
            throw new RefreshTokenException("Invalid Or Missing Refresh Token");
        }

        return tokenService.refreshToken(refreshToken);
    }

    @Transactional
    public void logout(String refreshToken) {

        int dotIdx = refreshToken.indexOf(".");
        if (dotIdx < 1) throw new RefreshTokenException("Invalid Refresh Token");

        String idPart = refreshToken.substring(0, dotIdx);
        String tokenPart = refreshToken.substring(dotIdx + 1);

        long tokenId = parseTokenId(idPart);

        refreshTokenService.deactivateRefreshToken(tokenId, tokenPart);
    }

    private long parseTokenId(String idPart) {
        try {
            return Long.parseLong(idPart);
        } catch (NumberFormatException e) {
            throw new RefreshTokenException("Invalid Refresh Token");
        }
    }

    public void resetPassword(ResetPasswordTokenRequest request) {

        String token = request.getToken();
        String newPassword = request.getNewPassword();

        UUID storedToken;
        try {
            storedToken = UUID.fromString(token);
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid Token");
        }

        PasswordResetToken resetToken = passwordResetTokenRepository.findPasswordResetTokenByToken(storedToken)
                .orElseThrow(() -> new BadRequestException("Invalid Refresh Token"));

        if (resetToken.isUsed()) {
            throw new BadRequestException("Token Already Used");
        }

        if (resetToken.getExpiryDate().isBefore(Instant.now())) {
            throw new BadRequestException("Token Expired");
        }

        userServiceInter.updatePasswordByUserId(
                resetToken.getUserId(),
                passwordEncoder.encode(newPassword)
        );

        resetToken.setUsed(true);
        passwordResetTokenRepository.save(resetToken);
    }
}
