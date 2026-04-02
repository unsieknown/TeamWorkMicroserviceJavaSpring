package com.mordiniaa.storageservice.config.rabbitmq.queueBindings;

import com.mordiniaa.storageservice.config.rabbitmq.RabbitMQProperties;
import com.mordiniaa.storageservice.messaging.rabbit.publish.RabbitMQPublisher;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UserCreateBindingConfig {

    @Bean
    public Queue userCreateQueue(RabbitMQProperties rabbitMQProperties) {
        return new Queue(
                rabbitMQProperties.getQueue().get("userCreate"),
                true
        );
    }

    @Bean
    public Binding userCreateBinding(Queue userCreateQueue, TopicExchange userExchange, RabbitMQPublisher rabbitMQPublisher, RabbitMQProperties rabbitMQProperties) {
        return BindingBuilder.bind(userCreateQueue)
                .to(userExchange)
                .with(rabbitMQProperties.getUserReceivedRouting("create"));
    }
}
