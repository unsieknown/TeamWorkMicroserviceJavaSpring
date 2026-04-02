package com.mordiniaa.notification.services;

import org.springframework.stereotype.Service;

@Service
public class PasswordResetTokenService {

    public String createResetPasswordLink(String resetToken) {
        return "http://localhost:3000/reset-password?token=" + resetToken;
    }
}
