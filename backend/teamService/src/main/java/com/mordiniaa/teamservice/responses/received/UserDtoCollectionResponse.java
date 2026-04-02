package com.mordiniaa.teamservice.responses.received;

import com.mordiniaa.teamservice.dto.UserDto;
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
