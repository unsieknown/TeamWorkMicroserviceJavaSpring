package com.mordiniaa.storageservice.config.rabbitmq.queueBindings;

import com.mordiniaa.storageservice.config.rabbitmq.RabbitMQProperties;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DefaultImageBinding {

    @Bean
    public Queue defaultImageKeyQueue(RabbitMQProperties rabbitMQProperties) {
        return new Queue(rabbitMQProperties.getQueue().get("defaultImageKey"));
    }

    @Bean
    public Binding defaultImageKeyBinding(Queue defaultImageKeyQueue, TopicExchange userExchange, RabbitMQProperties rabbitMQProperties) {
        return BindingBuilder.bind(defaultImageKeyQueue)
                .to(userExchange)
                .with(rabbitMQProperties.getUserReceivedRouting("defaultImage"));
    }
}
