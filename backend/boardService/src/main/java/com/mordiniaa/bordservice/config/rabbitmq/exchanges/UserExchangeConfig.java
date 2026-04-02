package com.mordiniaa.bordservice.config.rabbitmq.exchanges;

import com.mordiniaa.bordservice.config.rabbitmq.RabbitMQProperties;
import org.springframework.amqp.core.ExchangeBuilder;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class UserExchangeConfig {

    private final RabbitMQProperties rabbitMQProperties;

    public UserExchangeConfig(RabbitMQProperties rabbitMQProperties) {
        this.rabbitMQProperties = rabbitMQProperties;
    }

    @Bean
    public TopicExchange userExchange() {
        return ExchangeBuilder.topicExchange(
                        rabbitMQProperties.getUserExchange()
                )
                .durable(true)
                .build();
    }
}
