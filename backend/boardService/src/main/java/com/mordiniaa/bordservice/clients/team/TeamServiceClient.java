package com.mordiniaa.bordservice.clients.team;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

import java.util.UUID;

@HttpExchange
public interface TeamServiceClient {

    @GetExchange("/inter/teams/{teamId}/manager/{managerId}/exist")
    boolean existsTeamByIdAndManagerId(@PathVariable UUID teamId, @PathVariable UUID managerId);

    @GetExchange("/inter/teams/{teamId}/user/{userId}/exist")
    boolean existsUserInTeam(@PathVariable UUID teamId, @PathVariable UUID userId);
}
