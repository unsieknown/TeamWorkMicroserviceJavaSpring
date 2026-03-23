package com.mordiniaa.userservice.events.events;

import java.util.UUID;

public record UserUsernameChangedEvent(UUID userId, String username) {
}
