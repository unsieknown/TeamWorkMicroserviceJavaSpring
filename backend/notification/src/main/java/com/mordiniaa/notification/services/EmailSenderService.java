package com.mordiniaa.notification.services;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailSenderService {

    @Value("${spring.mail.username}")
    private String from;

    private final JavaMailSender mailSender;
    private final PasswordResetTokenService passwordResetTokenService;

    public void sendPasswordResetToken(String to, String resetToken) {

        String link = passwordResetTokenService.createResetPasswordLink(resetToken);
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setFrom(from);
        message.setSubject("PASSWORD Reset Token");
        message.setText("Click The Link To Reset Your Password: " + link);
        mailSender.send(message);
    }
}
