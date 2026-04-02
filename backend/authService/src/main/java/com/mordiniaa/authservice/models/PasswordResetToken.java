package com.mordiniaa.authservice.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@Table(name = "password_reset_tokens")
@Entity(name = "PasswordResetToken")
@NoArgsConstructor
public class PasswordResetToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, name = "token")
    private UUID token;

    @Column(nullable = false, name = "expiry_date")
    private Instant expiryDate;

    @Column(name = "used", nullable = false)
    private boolean used = false;

    private UUID userId;

    public PasswordResetToken(UUID token, Instant expiryDate, UUID userId) {
        this.token = token;
        this.expiryDate = expiryDate;
        this.userId = userId;
    }
}
