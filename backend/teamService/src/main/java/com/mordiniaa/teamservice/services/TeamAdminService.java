package com.mordiniaa.teamservice.services;

import com.mordiniaa.teamservice.clients.user.UserServiceClient;
import com.mordiniaa.teamservice.dto.TeamShortDto;
import com.mordiniaa.teamservice.exceptions.BadRequestException;
import com.mordiniaa.teamservice.exceptions.TeamNotFoundException;
import com.mordiniaa.teamservice.exceptions.UsersNotAvailableException;
import com.mordiniaa.teamservice.mappers.TeamMapper;
import com.mordiniaa.teamservice.messaging.rabbit.consume.UserMessage;
import com.mordiniaa.teamservice.models.Team;
import com.mordiniaa.teamservice.repositories.TeamRepository;
import com.mordiniaa.teamservice.requests.TeamCreationRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Random;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TeamAdminService {

    private final TeamRepository teamRepository;
    private final TeamService teamService;
    private final TeamMapper teamMapper;
    private final UserServiceClient userClient;

    @Transactional
    public TeamShortDto createTeam(TeamCreationRequest teamCreationRequest) {

        String teamName = teamCreationRequest.getTeamName().trim();
        String lowerTeamName = teamName.toLowerCase();
        if (teamRepository.existsByTeamNameIgnoreCaseAndActiveTrue(lowerTeamName))
            throw new BadRequestException("Team Already Exists");

        Team team = new Team(lowerTeamName);
        team.setPresentationName(teamName);

        return teamMapper.toShortDto(teamRepository.save(team));
    }

    @Transactional
    public TeamShortDto assignManagerToTeam(UUID userId, UUID teamId) {

        boolean isValidManager = userClient.existsUser(userId);
        if (!isValidManager)
            throw new BadRequestException("User is not a manager or does not exist");

        Team team = teamRepository.findTeamByTeamIdAndActiveTrue(teamId)
                .orElseThrow(TeamNotFoundException::new);

        if (team.getManagerId() != null) {
            if (team.getManagerId().equals(userId))
                // This manager Already Assigned
                throw new BadRequestException("Manager Already Assigned");
            // Other Manager Already Assigned
            throw new BadRequestException("Other Manager Is Already Assigned");
        }

        team.setManagerId(userId);
        return teamMapper.toShortDto(teamRepository.save(team));
    }

    @Transactional
    public void removeManagerFromTeam(UUID teamId) {

        Team team = teamService.getTeam(teamId);
        if (team.getManagerId() == null)
            throw new UsersNotAvailableException("Manager Not Found For Team");

        team.removeManager();
        teamRepository.save(team);
    }

    @Transactional
    public void archiveTeam(UUID teamId) {

        Team team = teamRepository.findTeamByTeamIdAndActiveTrue(teamId)
                .orElseThrow(TeamNotFoundException::new);

        team.setTeamName(team.getTeamName() + "_" + new Random().nextInt(100, 999));
        team.deactivate();
        teamRepository.save(team);
    }

    @Transactional
    public void addToTeam(UUID userId, UUID teamId) {

        Team team = teamRepository.findTeamByTeamIdAndActiveTrue(teamId)
                .orElseThrow(TeamNotFoundException::new);

        UUID managerId = team.getManagerId();
        if (managerId != null && managerId.equals(userId))
            throw new BadRequestException("You cannot add Manager as a team member");

        boolean isMember = team.getTeamMembers().stream()
                .anyMatch(uId -> uId.equals(userId));
        if (isMember)
            throw new BadRequestException("User Already Team Member");

        boolean isValidManager = userClient.existsUser(userId);
        if (!isValidManager)
            throw new BadRequestException("User is not a manager or does not exist");
        team.addMember(userId);

        teamRepository.save(team);
    }

    @Transactional
    public void removeFromTeam(UUID userId, UUID teamId) {

        Team team = teamService.getTeam(teamId);
        UUID managerId = team.getManagerId();
        if (managerId != null && managerId.equals(userId))
            throw new UnsupportedOperationException("Cannot Remove Manager From Members List");

        UUID uId = team.getTeamMembers()
                .stream()
                .filter(memberId -> memberId.equals(userId))
                .findFirst()
                .orElseThrow(UsersNotAvailableException::new);

        team.removeMember(uId);
        teamRepository.save(team);
    }

    @Transactional
    @RabbitListener(queues = "${rabbitmq.queue.userDelete}")
    public void removeFromTeamByEvent(UserMessage message) {

        if (message.appRole().equals(UserMessage.AppRole.ROLE_MANAGER))
            teamRepository.removeManagerFromAllTeams(message.userId());
        else
            teamRepository.removeMemerFromAllTeams(message.userId());
    }
}
