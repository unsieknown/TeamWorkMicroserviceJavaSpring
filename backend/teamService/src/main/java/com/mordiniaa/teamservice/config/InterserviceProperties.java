package com.mordiniaa.teamservice.config;

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

    private Map<String, String> services = new HashMap<>();

    public String getUserService() {
        return getService("user_service");
    }

    public String getAuthService() {
        return getService("auth_service");
    }

    public String getCurrentService() {
        return getService("current_service");
    }

    public String getService(String serviceName) {
        return services.get(serviceName);
    }
}
