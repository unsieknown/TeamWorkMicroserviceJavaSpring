package com.mordiniaa.userservice.events.events;

import java.util.UUID;

//Handled For RabbitMQ
public record UserDefaultImageEvent(UUID userid) {
}
