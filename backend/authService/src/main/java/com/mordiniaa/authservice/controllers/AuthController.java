package com.mordiniaa.authservice.controllers;

import com.mordiniaa.authservice.request.LoginRequest;
import com.mordiniaa.authservice.request.RefreshTokenRequest;
import com.mordiniaa.authservice.request.ResetPasswordTokenRequest;
import com.mordiniaa.authservice.response.APIResponse;
import com.mordiniaa.authservice.security.token.TokenSet;
import com.mordiniaa.authservice.services.AuthService;
import com.mordiniaa.authservice.services.PasswordResetService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final AuthenticationManager authenticationManager;
    private final PasswordResetService passwordResetService;

    @PostMapping("/signin")
    public ResponseEntity<TokenSet> login(@RequestBody LoginRequest loginRequest) {

        Authentication authentication;
        try {
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(),
                            loginRequest.getPassword()
                    )
            );
        } catch (Exception e) {
            throw new BadCredentialsException("Invalid Credentials");
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);

        return ResponseEntity.ok(authService.authenticate(authentication));
    }

    @PostMapping("/signout")
    public ResponseEntity<Void> signOut(@Valid @RequestBody RefreshTokenRequest refreshToken) {

        authService.logout(refreshToken.getRefreshToken());

        return ResponseEntity.ok().build();
    }

    @PostMapping("/refresh")
    public ResponseEntity<TokenSet> refreshTokens(@RequestBody RefreshTokenRequest refreshTokenRequest) {

        return ResponseEntity.ok(authService.refresh(refreshTokenRequest));
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<APIResponse<Void>> forgotPassword(
            @RequestBody String username
    ) {

        passwordResetService.generatePasswordResetTokenForUser(username);

        return ResponseEntity.ok(
                new APIResponse<>(
                        "If the email address exists in our system, a password reset token will be sent to it shortly.",
                        null
                )
        );
    }

    @PostMapping("/reset-password")
    public ResponseEntity<APIResponse<Void>> resetPassword(
            @Valid @RequestBody ResetPasswordTokenRequest request
    ) {
        authService.resetPassword(request);
        return ResponseEntity.ok(
                new APIResponse<>(
                        "Password Changed Successfully",
                        null
                )
        );
    }
}
