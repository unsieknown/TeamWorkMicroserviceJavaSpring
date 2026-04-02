package com.mordiniaa.authservice.security.token;

public record TokenSet(
        String accessToken,
        String refreshToken
) {}
