package com.mordiniaa.notification.messages;

import com.mordiniaa.notification.services.EmailSenderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaConsumer {

    private final EmailSenderService emailSenderService;

    @KafkaListener(topics = "${spring.kafka.topics.passwordResetToken.name}", groupId="${spring.kafka.topics.passwordResetToken.groupId}")
    public void consume(PasswordTokenMessage message) {
        String email = message.getUserEmail();
        String token = message.getPasswordResetToken();

        if (email != null && token != null)
            emailSenderService.sendPasswordResetToken(email, token);
    }
}
