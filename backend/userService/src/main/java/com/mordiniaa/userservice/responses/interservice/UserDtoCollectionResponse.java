package com.mordiniaa.userservice.responses.interservice;

import com.mordiniaa.userservice.dto.UserDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDtoCollectionResponse {
    Set<UserDto> users;
}
