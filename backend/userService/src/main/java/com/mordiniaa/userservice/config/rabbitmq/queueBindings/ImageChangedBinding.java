package com.mordiniaa.userservice.config.rabbitmq.queueBindings;

import com.mordiniaa.userservice.config.rabbitmq.RabbitMQProperties;
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


    @Bean
    public Queue imageKeyQueue(RabbitMQProperties rabbitMQProperties) {
        return new Queue(
                rabbitMQProperties.getQueue().get("imageKey"),
                true
        );
    }

    @Bean
    public Binding imageKeyBinding(Queue imageKeyQueue, TopicExchange userExchange, RabbitMQProperties rabbitMQProperties) {
        return BindingBuilder.bind(imageKeyQueue)
                .to(userExchange)
                .with(rabbitMQProperties.getUserReceivedRouting("imageChanged"));
    }
}
