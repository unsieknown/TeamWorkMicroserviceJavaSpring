package com.mordiniaa.authservice.messaging.kafka.publish;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PasswordTokenMessage {

    private String userEmail;
    private String passwordResetToken;
}
