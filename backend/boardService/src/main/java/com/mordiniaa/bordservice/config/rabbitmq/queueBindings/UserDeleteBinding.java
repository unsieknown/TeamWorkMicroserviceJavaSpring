package com.mordiniaa.bordservice.config.rabbitmq.queueBindings;

import com.mordiniaa.bordservice.config.rabbitmq.RabbitMQProperties;
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
    public Binding userDeleteKeyBinding(Queue userDeleteKeyQueue, TopicExchange userExchange, RabbitMQProperties rabbitMQProperties) {
        return BindingBuilder.bind(userDeleteKeyQueue)
                .to(userExchange)
                .with(rabbitMQProperties.getUserReceivedRouting("delete"));
    }
}
