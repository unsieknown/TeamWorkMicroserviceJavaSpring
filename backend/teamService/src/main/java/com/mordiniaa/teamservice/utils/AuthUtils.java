package com.mordiniaa.teamservice.utils;

import com.mordiniaa.teamservice.security.UserContext;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class AuthUtils {

    public UUID authenticatedUserId() {

        Jwt jwt = UserContext.get();

        if (jwt != null) {
            return UUID.fromString(jwt.getSubject());
        }

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth instanceof JwtAuthenticationToken jwtAuth) {
            Jwt userJwt = jwtAuth.getToken();

            if ("user".equals(userJwt.getClaim("type"))) {
                return UUID.fromString(userJwt.getSubject());
            }
        }
        throw new RuntimeException("No User context available");
    }
}
