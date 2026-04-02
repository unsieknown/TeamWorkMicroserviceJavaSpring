package com.mordiniaa.storageservice.clients.auth;

import com.mordiniaa.storageservice.respnses.received.TokenResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.service.annotation.HttpExchange;

@HttpExchange
public interface AuthServiceClient {

    @PostMapping("/internal/token")
    TokenResponse tokenResponse(@RequestHeader("X-Service-Name") String serviceName);
}
