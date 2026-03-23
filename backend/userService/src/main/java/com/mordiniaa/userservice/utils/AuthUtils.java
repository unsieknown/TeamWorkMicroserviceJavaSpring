package com.mordiniaa.userservice.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class AuthUtils {

    public UUID authenticatedUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        JwtPrincipal principal = (JwtPrincipal) authentication.getPrincipal();
        return principal.userId();
    }
}
