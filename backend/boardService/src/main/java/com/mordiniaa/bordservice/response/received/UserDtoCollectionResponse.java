package com.mordiniaa.bordservice.response.received;

import com.mordiniaa.bordservice.dto.user.UserDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class UserDtoCollectionResponse {
    Set<UserDto> users;
}
