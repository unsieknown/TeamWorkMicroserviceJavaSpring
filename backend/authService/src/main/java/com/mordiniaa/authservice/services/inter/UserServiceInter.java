package com.mordiniaa.authservice.services.inter;

import com.mordiniaa.authservice.clients.user.UserServiceClient;
import com.mordiniaa.authservice.response.UserInfoResponse;
import com.mordiniaa.authservice.security.service.user.SecurityUserProjection;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceInter {

    private final UserServiceClient userServiceClient;

    public Optional<UserInfoResponse> getUserInfoByUsername(String username) {
        try {
            return userServiceClient.getUserInfoByUsername(username);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public Optional<SecurityUserProjection> findSecurityUserByUsername(String username) {
        try {
            return userServiceClient.findSecurityUserByUsername(username);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public void updatePasswordByUserId(UUID userId, String newPassword) {
        userServiceClient.updateUserPassword(userId, newPassword);
    }

    public Optional<SecurityUserProjection> findSecurityUserById(UUID userId) {
        try {
            return userServiceClient.findSecurityUserById(userId);
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}
