package com.mordiniaa.authservice.security.token;

public interface Token {

    String getTokenName();

    String getToken();

    long getTtl();
}
