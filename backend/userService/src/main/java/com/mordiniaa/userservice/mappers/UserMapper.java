package com.mordiniaa.userservice.mappers;

import com.mordiniaa.userservice.dto.UserDto;
import com.mordiniaa.userservice.models.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserDto toDto(User dbUser) {
        return UserDto.builder()
                .userId(dbUser.getUserId())
                .username(dbUser.getUsername())
                .imageKey(dbUser.getImageKey())
                .build();
    }
}
