package com.mordiniaa.userservice.messaging.rabbit.consume;

import java.util.UUID;

public record UserProfileImageChangedMessage(UUID userId, String imageKey) {
}
