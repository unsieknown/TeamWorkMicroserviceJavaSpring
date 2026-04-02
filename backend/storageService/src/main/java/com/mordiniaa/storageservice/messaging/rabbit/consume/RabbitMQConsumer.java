package com.mordiniaa.storageservice.messaging.rabbit.consume;

import com.mordiniaa.storageservice.utils.CloudStorageServiceUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RabbitMQConsumer {

    private final CloudStorageServiceUtils cloudStorageServiceUtils;

    @RabbitListener(queues = "${rabbitmq.queue.userCreate}")
    public void createUserStorage(UserMessage message) {
        UUID userId = message.userId();
        cloudStorageServiceUtils.createNewStorageSafely(userId);
    }
}
