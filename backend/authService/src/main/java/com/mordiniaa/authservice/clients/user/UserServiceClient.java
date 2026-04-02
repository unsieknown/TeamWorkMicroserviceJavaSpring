package com.mordiniaa.authservice.clients.user;

import com.mordiniaa.authservice.response.UserInfoResponse;
import com.mordiniaa.authservice.security.service.user.SecurityUserProjection;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

import java.util.Optional;
import java.util.UUID;

@HttpExchange
public interface UserServiceClient {

    @GetExchange("/inter/users")
    Optional<UserInfoResponse> getUserInfoByUsername(@RequestParam String username);

    @GetExchange("/inter/users/security-user")
    Optional<SecurityUserProjection> findSecurityUserByUsername(@RequestParam String username);

    @GetExchange("/inter/users/security-user")
    Optional<SecurityUserProjection> findSecurityUserById(@RequestParam UUID userId);

    @PostExchange("/inter/users/password")
    void updateUserPassword(@RequestParam UUID userId, @RequestBody String newPassword);
}
