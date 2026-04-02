package com.mordiniaa.teamservice.config.rabbitmq.queueBindings;

import com.mordiniaa.teamservice.config.rabbitmq.RabbitMQProperties;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class UserDeleteBinding {

    @Bean
    public Queue userDeleteKeyQueue(RabbitMQProperties rabbitMQProperties) {
        return new Queue(
                rabbitMQProperties.getQueue().get("userDelete"),
                true
        );
    }

    @Bean
    public Binding userDeleteKeyBinding(Queue userDeleteQueue, TopicExchange userExchange, RabbitMQProperties rabbitMQProperties) {
        return BindingBuilder.bind(userDeleteQueue)
                .to(userExchange)
                .with(rabbitMQProperties.getUserReceivedRouting("delete"));
    }
}
