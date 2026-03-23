package com.mordiniaa.userservice.events.events;

import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

//Handled For RabbitMQ
public record UserProfileImageEvent(UUID userId, MultipartFile imageFile) {
}
