package com.mordiniaa.storageservice.config.rabbitmq.exchanges;

import com.mordiniaa.storageservice.config.rabbitmq.RabbitMQProperties;
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
