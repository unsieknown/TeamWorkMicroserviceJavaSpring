package com.mordiniaa.userservice.config.rabbitmq.exchanges;

import com.mordiniaa.userservice.config.rabbitmq.RabbitMQProperties;
import org.springframework.amqp.core.ExchangeBuilder;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UserExchangeConfig {

    @Bean
    public TopicExchange userExchange(RabbitMQProperties rabbitMQProperties) {
        return ExchangeBuilder.topicExchange(rabbitMQProperties.getUserExchange())
                .durable(true)
                .build();
    }
}
