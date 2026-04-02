package com.mordiniaa.teamservice.mappers;

import com.mordiniaa.teamservice.dto.TeamDetailedDto;
import com.mordiniaa.teamservice.dto.TeamShortDto;
import com.mordiniaa.teamservice.models.Team;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TeamMapper {

    public TeamShortDto toShortDto(Team team) {

        return new TeamDetailedDto(
                team.getTeamId(),
                team.getPresentationName()
        );
    }
}
