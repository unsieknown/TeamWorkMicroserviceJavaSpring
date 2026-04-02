package com.mordiniaa.gatewayserver.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;

@Configuration
public class RoutingConfig {

    @Bean
    public RouteLocator routeLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("auth-service", r -> r
                        .path("/api/v1/auth/**")
                        .filters(f -> f
                                .rewritePath("/api/v1/auth(?<segment>/?.*)", "/auth${segment}")
                        )
                        .uri("lb://AUTH-SERVICE")
                )
                .route("board-service", r-> r
                        .path("/api/v1/boards/**", "/api/v1/tasks/**")
                        .filters(f -> f
                                .rewritePath("/api/v1/boards(?<segment>/?.*)", "/boards${segment}")
                                .rewritePath("/api/v1/tasks(?<segment>/?.*)", "/tasks${segment}")
                        )
                        .uri("lb://BOARD-SERVICE")
                )
                .route("notes-service", r -> r
                        .path("/api/v1/notes/**")
                        .filters(f -> f
                                .rewritePath("/api/v1/notes(?<segment>/?.*)", "/notes${segment}")
                        )
                        .uri("lb://NOTES-SERVICE")
                )
                .route("storage-service", r -> r
                        .path("/api/v1/storage/**")
                        .filters(f -> f
                                .rewritePath("/api/v1/storage/resource(?<segment>/?.*)", "/storage/resource${segment}")
                                .rewritePath("/api/v1/storage/images(?<segment>/?.*)", "/images${segment}")
                        )
                        .uri("lb://STORAGE-SERVICE")
                )
                .route("team-service", r -> r
                        .path("/api/v1/teams/**")
                        .filters(f -> f
                                .rewritePath("/api/v1/teams(?<segment>/?.*)", "/teams${segment}")
                        )
                        .uri("lb://TEAM-SERVICE")
                )
                .route("user-service", r -> r
                        .path("/api/v1/users/**")
                        .filters(f -> f
                                .rewritePath("/api/v1/users(?<segment>/?.*)", "/users${segment}")
                        )
                        .uri("lb://USER-SERVICE")
                )
                .build();
    }
}
