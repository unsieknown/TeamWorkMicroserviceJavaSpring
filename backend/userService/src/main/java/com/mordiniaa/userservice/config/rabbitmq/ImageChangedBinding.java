package com.mordiniaa.userservice.config.rabbitmq;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class ImageChangedBinding {

    private final RabbitMQProperties rabbitMQProperties;
    private final TopicExchange topicExchange;

    @Bean
    public Queue imageKeyQueue() {
        return new Queue(
                rabbitMQProperties.getQueue().get("imageKey"),
                true
        );
    }

    @Bean
    public Binding imageKeyBinding(Queue imageKeyQueue) {
        return BindingBuilder.bind(imageKeyQueue)
                .to(topicExchange)
                .with(rabbitMQProperties.getUserReceivedRouting("imageChanged"));
    }
}
