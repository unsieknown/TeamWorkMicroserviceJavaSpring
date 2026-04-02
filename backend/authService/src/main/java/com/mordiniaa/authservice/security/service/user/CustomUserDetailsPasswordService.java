package com.mordiniaa.authservice.security.service.user;

import com.mordiniaa.authservice.exceptions.BadRequestException;
import com.mordiniaa.authservice.services.inter.UserServiceInter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsPasswordService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsPasswordService implements UserDetailsPasswordService {

    private final UserServiceInter userServiceInter;

    @Override
    @Transactional
    public UserDetails updatePassword(UserDetails user, String newPassword) {

        SecurityUser securityUser = (SecurityUser) user;
        UUID userId = securityUser.getUserId();

        userServiceInter.updatePasswordByUserId(userId, newPassword);

        SecurityUserProjection updatedUser = userServiceInter.findSecurityUserByUsername(securityUser.getUsername())
                .orElseThrow(() -> new BadRequestException("User Not Found"));

        return SecurityUser.build(updatedUser);
    }
}
