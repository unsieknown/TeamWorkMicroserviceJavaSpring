package com.mordiniaa.userservice.config;

import com.mordiniaa.userservice.models.AppRole;
import com.mordiniaa.userservice.models.User;
import com.mordiniaa.userservice.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.Files;
import java.nio.file.Path;


@Slf4j
@Component
@RequiredArgsConstructor
public class Setup {

    @Bean
    @Transactional
    CommandLineRunner setAdmin(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            String password = Files.readString(Path.of("/run/secrets/admin_password.txt")).trim();
            long adminCount = userRepository.countByRole_AppRole(AppRole.ROLE_ADMIN);
            if (adminCount != 1) {
                if (adminCount > 1) {
                    throw new RuntimeException("There Are More Admins");
                } else {
                    throw new RuntimeException("No Admin Set");
                }
            }

            User user = userRepository.findUsersByRole_AppRole(AppRole.ROLE_ADMIN)
                    .orElseThrow(() -> new IllegalStateException("Admin user missing"));

            if (user.getPassword() != null) {
                log.info("Admin Password Already Set");
                return;
            }

            user.setPassword(passwordEncoder.encode(password));
            userRepository.save(user);

            log.info("Admin password initialized from environment");
        };
    }
}