package com.mordiniaa.authservice.config.kafka;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class KafkaTopicsConfig {

    @Value("${spring.kafka.topics.user.passwordResetToken.name}")
    private String passwordResetTokenTopic;

    @Bean
    public NewTopic createPasswordResetTopic() {
        return new NewTopic(passwordResetTokenTopic, 3, (short) 1);
    }
}
