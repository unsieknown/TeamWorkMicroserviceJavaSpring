package com.mordiniaa.userservice.services;

import com.mordiniaa.userservice.events.events.UserDefaultImageEvent;
import com.mordiniaa.userservice.messaging.rabbit.consume.UserProfileImageChangedMessage;
import com.mordiniaa.userservice.exceptions.BadRequestException;
import com.mordiniaa.userservice.repositories.UserRepository;
import com.mordiniaa.userservice.services.inter.StorageServiceInter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    private final StorageServiceInter storageServiceInter;

    @Transactional
    public void addProfileImage(UUID userId, MultipartFile file) {

        if (!userRepository.existsUserByUserIdAndDeletedFalse(userId))
            throw new BadRequestException("User Not Found");

        String imageKey = storageServiceInter.saveProfileImage(userId, file);
        setProfileImageKey(userId, imageKey);
    }

    public void setDefaultProfileImage(UUID userId) {

        if (!userRepository.existsUserByUserIdAndDeletedFalse(userId))
            throw new BadRequestException("User Not Found");

        applicationEventPublisher.publishEvent(
                new UserDefaultImageEvent(userId)
        );
    }

    @Transactional
    public void setProfileImageKey(UUID userId, String imageKey) {
        userRepository.setProfileImageKey(userId, imageKey);
        log.info("Image changed for user: {}", userId);
    }

    @Transactional
    @RabbitListener(queues = "${rabbitmq.queue.imageKey}")
    public void setProfileImageKey(UserProfileImageChangedMessage message) {
        userRepository.setProfileImageKey(message.userId(), message.imageKey());
        log.info("Image changed for user by event: {}", message.userId());
    }
}
