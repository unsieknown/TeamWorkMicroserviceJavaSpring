package com.mordiniaa.bordservice.clients.auth;

import com.mordiniaa.bordservice.response.received.TokenResponse;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

@HttpExchange("/auth")
public interface AuthServiceClient {

    @PostExchange("/internal/token")
    TokenResponse tokenResponse(@RequestHeader("X-Service-Name") String serviceName);
}
