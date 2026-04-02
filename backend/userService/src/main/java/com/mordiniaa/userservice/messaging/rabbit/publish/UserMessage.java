package com.mordiniaa.userservice.messaging.rabbit.publish;

import com.mordiniaa.userservice.models.AppRole;

import java.util.UUID;

public record UserMessage(UUID userId, AppRole appRole) {

    public UserMessage(UUID userId) {
        this(userId, AppRole.ROLE_USER);
    }
}
