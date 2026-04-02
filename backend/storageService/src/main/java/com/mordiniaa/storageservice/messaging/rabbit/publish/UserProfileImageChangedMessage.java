package com.mordiniaa.storageservice.messaging.rabbit.publish;

import java.util.UUID;

public record UserProfileImageChangedMessage(UUID userId, String imageKey) {
}
