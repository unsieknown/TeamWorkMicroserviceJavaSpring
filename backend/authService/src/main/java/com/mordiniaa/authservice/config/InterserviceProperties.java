package com.mordiniaa.authservice.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "interservice")
public class InterserviceProperties {
    Map<String, String> services = new HashMap<>();

    public String getUserService() {
        return services.get("user_service");
    }
}
