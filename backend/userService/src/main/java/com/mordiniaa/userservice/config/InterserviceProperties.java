package com.mordiniaa.userservice.config;

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

    public String getStorageService() {
        return getService("storage_service");
    }

    public String getAuthService() {
        return getService("auth_service");
    }

    public String getCurrentService() {
        return getService("current_service");
    }

    private String getService(String serviceName) {
        return services.get(serviceName);
    }
}
