package com.mordiniaa.userservice.mappers;

import com.mordiniaa.backend.dto.user.UserDto;
import com.mordiniaa.backend.models.user.DbUser;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserDto toDto(DbUser dbUser) {
        return UserDto.builder()
                .userId(dbUser.getUserId())
                .username(dbUser.getUsername())
                .imageUrl(dbUser.getImageKey())
                .build();
    }
}
