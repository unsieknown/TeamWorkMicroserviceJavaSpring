package com.mordiniaa.bordservice.services.inter;

import com.mordiniaa.bordservice.clients.user.UserServiceClient;
import com.mordiniaa.bordservice.dto.user.UserDto;
import com.mordiniaa.bordservice.exceptions.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceInter {

    private final UserServiceClient userServiceClient;

    public void checkUserExistence(UUID userId) {
        boolean available = userServiceClient.existsUser(userId);
        if (!available)
            throw new BadRequestException("User Not Found Or Not Available");
    }

    public void checkUsersExistence(Set<UUID> ids) {
        boolean available = userServiceClient.existUsers(ids);
        if (!available)
            throw new BadRequestException("Users Not Found Or Not Available");
    }

    public Set<UserDto> batchUsers(Set<UUID> userIds) {
        return userServiceClient.batchUsers(userIds).getUsers();
    }
}
