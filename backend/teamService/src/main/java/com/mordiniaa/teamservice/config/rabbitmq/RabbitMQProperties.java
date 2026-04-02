package com.mordiniaa.teamservice.config.rabbitmq;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "rabbitmq")
public class RabbitMQProperties {

    private Map<String, String> exchange = new HashMap<>();
    private Map<String, String> queue = new HashMap<>();
    private Routing routing;

    @Getter
    @Setter
    public static class Routing {

        private Map<String, Map<String, String>> received = new HashMap<>();
        private Map<String, Map<String, String>> published = new HashMap<>();
    }

    public String getTeamExchange() {
        return exchange.get("team");
    }

    public String getUserExchange() {
        return exchange.get("user");
    }

    public String getUserPublishedRouting(String action) {
        return getPublishedRouting("user", action);
    }

    public String getUserReceivedRouting(String action) {
        return getReceivedRouting("user", action);
    }

    public String getPublishedRouting(String domain, String action) {
        return routing.getPublished()
                .getOrDefault(domain, Map.of())
                .get(action);
    }

    public String getReceivedRouting(String domain, String action) {
        return routing.getReceived()
                .getOrDefault(domain, Map.of())
                .get(action);
    }
}
