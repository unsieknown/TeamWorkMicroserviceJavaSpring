package com.mordiniaa.storageservice.security;

import org.springframework.security.oauth2.jwt.Jwt;

public class UserContext {

    private static final ThreadLocal<Jwt> CURRENT = new ThreadLocal<>();

    public static void set(Jwt jwt) {
        CURRENT.set(jwt);
    }

    public static Jwt get() {
        return CURRENT.get();
    }

    public static void clear() {
        CURRENT.remove();
    }
}
