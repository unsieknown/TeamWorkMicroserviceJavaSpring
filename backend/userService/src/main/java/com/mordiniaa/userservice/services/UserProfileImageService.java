package com.mordiniaa.userservice.services;

import com.mordiniaa.userservice.config.rabbitmq.RabbitMQProperties;
import com.mordiniaa.userservice.events.events.UserDefaultImageEvent;
import com.mordiniaa.userservice.messaging.rabbit.consume.UserProfileImageChangedMessage;
import com.mordiniaa.userservice.events.events.UserProfileImageEvent;
import com.mordiniaa.userservice.exceptions.BadRequestException;
import com.mordiniaa.userservice.repositories.mysql.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserProfileImageService {

    private final UserRepository userRepository;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final RabbitMQProperties rabbitMQProperties;
    private final MongoUserService mongoUserService;

    public void addProfileImage(UUID userId, MultipartFile file) {

        if (!userRepository.existsUserByUserIdAndDeletedFalse(userId))
            throw new BadRequestException("User Not Found");

        applicationEventPublisher.publishEvent(
                new UserProfileImageEvent(userId, file)
        );
    }

    public void setDefaultProfileImage(UUID userId) {

        if (!userRepository.existsUserByUserIdAndDeletedFalse(userId))
            throw new BadRequestException("User Not Found");

        applicationEventPublisher.publishEvent(
                new UserDefaultImageEvent(userId)
        );
    }

    @Transactional
    @RabbitListener(queues = "${rabbitmq.queue.imageKey}") // TODO: Implement To Receive From Storage Service
    public void setProfileImageKey(UserProfileImageChangedMessage message) {
        userRepository.setProfileImageKey(message.userId(), message.imageKey());
        mongoUserService.setProfileImageKey(message.userId(), message.imageKey());
        log.info("Image changed for user: {}", message.userId());
    }
}
