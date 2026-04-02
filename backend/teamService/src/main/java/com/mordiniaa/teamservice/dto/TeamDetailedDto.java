package com.mordiniaa.teamservice.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class TeamDetailedDto extends TeamShortDto {

    private Set<UserDto> teamMembers;

    public TeamDetailedDto(UUID teamId, String presentationName) {
        super(teamId, presentationName);
    }
}
