package com.mordiniaa.teamservice.services;

import com.mordiniaa.teamservice.repositories.TeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class InterTeamService {

    private final TeamRepository teamRepository;

    public boolean existsTeamWithManager(UUID teamId, UUID managerId) {
        return teamRepository.existsTeamByTeamIdAndManagerId(teamId, managerId);
    }

    public boolean existsTeamWithUser(UUID teamId, UUID userId) {
        return teamRepository.existsUserInTeam(teamId, userId);
    }
}
