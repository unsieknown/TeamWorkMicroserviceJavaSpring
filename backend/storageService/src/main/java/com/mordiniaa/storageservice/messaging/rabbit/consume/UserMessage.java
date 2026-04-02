package com.mordiniaa.storageservice.messaging.rabbit.consume;


import com.mordiniaa.storageservice.models.AppRole;

import java.util.UUID;

public record UserMessage(UUID userId, AppRole appRole) {

    public UserMessage(UUID userId) {
        this(userId, AppRole.ROLE_USER);
    }
}
