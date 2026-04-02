package com.mordiniaa.authservice.controllers;

import com.mordiniaa.authservice.config.InterserviceProperties;
import com.mordiniaa.authservice.security.service.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth/internal")
public class InternalTokenController {

    private final JwtService jwtService;
    private final InterserviceProperties interserviceProperties;

    @PostMapping("/token")
    public Map<String, String> getServiceToken(
            @RequestHeader("X-Service-Name") String serviceName,
            HttpServletRequest request
    ) {
        System.out.println(request.getHeader("User-Agent"));
        System.out.println(request.getRemoteAddr());
        String token = jwtService.buildServiceToken(serviceName, request);

        System.out.println(interserviceProperties.getServices());
        System.out.println(interserviceProperties.getServices().get(serviceName));
        return Map.of("token", token);
    }
}
