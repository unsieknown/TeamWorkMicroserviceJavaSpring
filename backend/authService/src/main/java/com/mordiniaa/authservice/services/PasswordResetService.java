package com.mordiniaa.authservice.services;

import com.mordiniaa.authservice.messaging.kafka.publish.KafkaPRTPublisher;
import com.mordiniaa.authservice.messaging.kafka.publish.PasswordTokenMessage;
import com.mordiniaa.authservice.models.PasswordResetToken;
import com.mordiniaa.authservice.repositories.PasswordResetTokenRepository;
import com.mordiniaa.authservice.response.UserInfoResponse;
import com.mordiniaa.authservice.services.inter.UserServiceInter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PasswordResetService {

    private final UserServiceInter userServiceInter;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final KafkaPRTPublisher kafkaPRTPublisher;

    public void generatePasswordResetTokenForUser(String username) {

        Optional<UserInfoResponse> userOpt = userServiceInter.getUserInfoByUsername(username);
        if (userOpt.isEmpty())
            return;

        UserInfoResponse user = userOpt.get();

        if (user.isDeleted() || !user.isAccountNonExpired() || !user.isAccountNonLocked()) {
            return;
        }

        String email = user.getEmail();

        UUID token = UUID.randomUUID();
        Instant expiryDate = Instant.now().plus(24, ChronoUnit.HOURS);
        PasswordResetToken resetToken = new PasswordResetToken(token, expiryDate, user.getUserId());
        passwordResetTokenRepository.save(resetToken);

        kafkaPRTPublisher.publish(
                new PasswordTokenMessage(
                        email,
                        token.toString()
                )
        );
    }
}
