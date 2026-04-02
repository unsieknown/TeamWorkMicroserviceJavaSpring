package com.mordiniaa.userservice.messaging.rabbit.publish;

import com.mordiniaa.userservice.config.rabbitmq.RabbitMQProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RabbitMQPublisher {

    private final RabbitTemplate rabbitTemplate;
    private final RabbitMQProperties rabbitMQProperties;

    public void publishUserCreatedMessage(UserMessage message) {
        rabbitTemplate.convertAndSend(
                rabbitMQProperties.getUserExchange(),
                rabbitMQProperties.getUserPublishedRouting("create"),
                message
        );
    }

    public void publishUserDeletedMessage(UserMessage message) {
        rabbitTemplate.convertAndSend(
                rabbitMQProperties.getUserExchange(),
                rabbitMQProperties.getUserPublishedRouting("delete"),
                message
        );
    }

    public void publishUserDefaultImageMessage(UserMessage message) {
        rabbitTemplate.convertAndSend(
                rabbitMQProperties.getUserExchange(),
                rabbitMQProperties.getUserPublishedRouting("defaultImage"),
                message
        );
    }
}
