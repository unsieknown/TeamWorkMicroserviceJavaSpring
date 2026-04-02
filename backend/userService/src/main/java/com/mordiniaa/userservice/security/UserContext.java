package com.mordiniaa.userservice.security;

import org.springframework.security.oauth2.jwt.Jwt;

public class UserContext {

    private static final ThreadLocal<Jwt> CONTEXT = new ThreadLocal<>();

    public static void set(Jwt jwt) {
        CONTEXT.set(jwt);
    }

    public static Jwt get() {
        return CONTEXT.get();
    }

    public static void clear() {
        CONTEXT.remove();
    }
}
