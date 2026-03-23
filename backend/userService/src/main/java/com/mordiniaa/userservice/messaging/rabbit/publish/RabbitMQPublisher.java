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

    public void publishUserCreatedMessage(UserIdMessage message) {
        rabbitTemplate.convertAndSend(
                rabbitMQProperties.getExchange(),
                rabbitMQProperties.getUserPublishedRouting("create"),
                message
        );
    }

    public void publishUserDeletedMessage(UserIdMessage message) {
        rabbitTemplate.convertAndSend(
                rabbitMQProperties.getExchange(),
                rabbitMQProperties.getUserPublishedRouting("delete"),
                message
        );
    }
}
