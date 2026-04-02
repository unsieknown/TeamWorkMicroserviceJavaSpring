package com.mordiniaa.bordservice.messaging.rabbit.consume;


import java.util.UUID;

public record UserMessage(UUID userId, AppRole appRole) {

    public UserMessage(UUID userId) {
        this(userId, AppRole.ROLE_USER);
    }

    public enum AppRole {
        ROLE_USER,
        ROLE_MANAGER
    }
}
