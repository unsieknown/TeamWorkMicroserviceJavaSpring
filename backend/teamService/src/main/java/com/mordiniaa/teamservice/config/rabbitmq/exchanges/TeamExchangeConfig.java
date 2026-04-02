package com.mordiniaa.teamservice.config.rabbitmq.exchanges;

import com.mordiniaa.teamservice.config.rabbitmq.RabbitMQProperties;
import org.springframework.amqp.core.ExchangeBuilder;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class TeamExchangeConfig {

    @Bean
    public TopicExchange teamExchange(RabbitMQProperties rabbitMQProperties) {
        return ExchangeBuilder.topicExchange(rabbitMQProperties.getTeamExchange())
                .durable(true)
                .build();
    }
}
