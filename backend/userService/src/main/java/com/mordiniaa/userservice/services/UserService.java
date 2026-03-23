package com.mordiniaa.userservice.services;

import com.mordiniaa.userservice.dto.UserDto;
import com.mordiniaa.userservice.exceptions.BadRequestException;
import com.mordiniaa.userservice.mappers.UserMapper;
import com.mordiniaa.userservice.models.mysql.AppRole;
import com.mordiniaa.userservice.models.mysql.User;
import com.mordiniaa.userservice.repositories.mysql.UserRepository;
import com.mordiniaa.userservice.requests.ResetPasswordTokenRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final UserMapper userMapper;

    public User getUser(UUID userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new BadRequestException("User Not Found"));
    }

    @Transactional
    public UserDto getUserDto(String username) {
        return userRepository.findUserByUsernameAndDeletedFalse(username)
                .map(user -> UserDto.builder()
                        .userId(user.getUserId())
                        .username(username)
                        .imageKey(user.getImageKey())
                        .email(user.getContact().getEmail())
                        .build()
                )
                .orElseThrow(() -> new BadRequestException("User Not Found"));
    }

    // TODO: Move To Auth Service, Auth Service Requests User Service To Get User DTO, generates token, forwards to notification service to send email with token
    /*@Transactional
    public void generatePasswordResetToken(String username) {

        User user = userRepository.findUserByUsernameAndDeletedFalse(username)
                .orElseThrow(() -> new BadRequestException("User Not Found"));

        Contact userContactData = user.getContact();

        String email = userContactData.getEmail();

        // TODO: Move To Auth Service And Get Token
        *//*UUID token = UUID.randomUUID();
        Instant expiryDate = Instant.now().plus(24, ChronoUnit.HOURS);
        PasswordResetToken resetToken = new PasswordResetToken(token, expiryDate, user);
        passwordResetTokenRepository.save(resetToken);*//*

        //Frontend Does Not Exist Yet

        // TODO: Send to Notification By Kafka
        *//*String resetUrl = "http://localhost:3000" + "/reset-password?token=" + token;
        emailService.sendPasswordResetEmail(email, resetUrl);*//*
    }*/

    @Transactional
    public void resetPassword(UUID userId, ResetPasswordTokenRequest request) {

        //TODO: Move To Auth Service And Call By Request
        /*String token = request.getToken();
        String newPassword = request.getNewPassword();

        UUID storedToken;
        try {
            storedToken = UUID.fromString(token);
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid Token");
        }

        PasswordResetToken resetToken = passwordResetTokenRepository.findPasswordResetTokenByToken(storedToken)
                .orElseThrow(() -> new BadRequestException("Invalid Refresh Token"));

        if (resetToken.isUsed()) {
            throw new BadRequestException("Token Already Used");
        }

        if (resetToken.getExpiryDate().isBefore(Instant.now())) {
            throw new BadRequestException("Token Expired");
        }*/

        User user = userRepository.findUserByUserIdAndDeletedFalse(userId)
                .orElseThrow(() -> new BadRequestException("User Not Found"));
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);

        // TODO: Move To Auth Service
        /*resetToken.setUsed(true);
        passwordResetTokenRepository.save(resetToken);*/

        // TODO: Return Response To Auth Service
    }

    public User findNonDeletedUserAndAppRole(UUID userId, AppRole appRole) {
        return userRepository.findUserByUserIdAndDeletedFalseAndRole_AppRole(userId, appRole)
                .orElseThrow(() -> new BadRequestException("User Not Found"));
    }
}
