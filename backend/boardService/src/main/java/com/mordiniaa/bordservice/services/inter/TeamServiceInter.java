package com.mordiniaa.bordservice.services.inter;

import com.mordiniaa.bordservice.clients.team.TeamServiceClient;
import com.mordiniaa.bordservice.exceptions.TeamNotFoundException;
import com.mordiniaa.bordservice.exceptions.UserNotInTeamException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TeamServiceInter {

    private final TeamServiceClient teamServiceClient;

    public void checkTeamAndManagerExistence(UUID teamId, UUID managerId) {
        if (!teamServiceClient.existsTeamByIdAndManagerId(teamId, managerId))
            throw new TeamNotFoundException("Team Not Found");
    }

    public void checkUserInTeamExistence(UUID teamId, UUID userId) {
        if (!teamServiceClient.existsUserInTeam(teamId, userId))
            throw new UserNotInTeamException("User Not Found In Team");
    }
}
