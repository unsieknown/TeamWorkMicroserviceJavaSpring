package com.mordiniaa.storageservice.messaging.rabbit.publish;

import com.mordiniaa.storageservice.config.rabbitmq.RabbitMQProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class RabbitMQPublisher {

    private final RabbitTemplate rabbitTemplate;
    private final RabbitMQProperties rabbitMQProperties;

    public void publishUserProfileChangedMessage(UserProfileImageChangedMessage message) {
        rabbitTemplate.convertAndSend(
                rabbitMQProperties.getUserExchange(),
                rabbitMQProperties.getUserPublishedRouting("imageChanged"),
                message
        );
    }
}
