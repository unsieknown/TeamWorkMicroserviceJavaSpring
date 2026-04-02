package com.mordiniaa.authservice.messaging.kafka.publish;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaPRTPublisher {

    @Value("${spring.kafka.topics.user.passwordResetToken.name}")
    private String passwordResetTokenTopic;

    private final KafkaTemplate<String, PasswordTokenMessage> kafkaTemplate;

    public void publish(PasswordTokenMessage message) {
        try {
            kafkaTemplate.send(passwordResetTokenTopic, message);
        } catch (Exception e) {
            // Ignore
        }
    }
}
