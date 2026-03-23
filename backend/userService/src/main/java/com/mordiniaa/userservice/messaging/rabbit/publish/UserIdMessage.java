package com.mordiniaa.userservice.messaging.rabbit.publish;

import java.util.UUID;

public record UserIdMessage(UUID userId) {
}
