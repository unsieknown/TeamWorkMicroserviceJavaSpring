package com.mordiniaa.teamservice.services;

import com.mordiniaa.teamservice.clients.user.UserServiceClient;
import com.mordiniaa.teamservice.dto.TeamDetailedDto;
import com.mordiniaa.teamservice.dto.TeamShortDto;
import com.mordiniaa.teamservice.dto.UserDto;
import com.mordiniaa.teamservice.exceptions.TeamNotFoundException;
import com.mordiniaa.teamservice.mappers.TeamMapper;
import com.mordiniaa.teamservice.models.Team;
import com.mordiniaa.teamservice.repositories.TeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TeamService {

    private final TeamRepository teamRepository;
    private final TeamMapper teamMapper;
    private final UserServiceClient userServiceClient;

    Team getTeam(UUID teamId) {
        return teamRepository.findById(teamId)
                .orElseThrow(TeamNotFoundException::new);
    }

    public List<TeamShortDto> getTeamsForManager(UUID managerId) {
        return teamRepository.findAllByManagerId(managerId).stream()
                .map(teamMapper::toShortDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public TeamDetailedDto getTeamDetails(UUID teamId, UUID managerId) {

        Team team = teamRepository.findTeamByTeamIdAndManagerId(teamId, managerId)
                .orElseThrow(TeamNotFoundException::new);

        Set<UserDto> dtoMembers = userServiceClient.getUsersByIds(team.getTeamMembers()).getUsers();

        TeamDetailedDto teamDetailedDto = (TeamDetailedDto) teamMapper.toShortDto(team);
        teamDetailedDto.setTeamMembers(dtoMembers);
        return teamDetailedDto;
    }
}
