package com.mordiniaa.teamservice.controllers;

import com.mordiniaa.teamservice.dto.TeamDetailedDto;
import com.mordiniaa.teamservice.dto.TeamShortDto;
import com.mordiniaa.teamservice.responses.APIResponse;
import com.mordiniaa.teamservice.responses.CollectionResponse;
import com.mordiniaa.teamservice.responses.PageMeta;
import com.mordiniaa.teamservice.services.TeamService;
import com.mordiniaa.teamservice.utils.AuthUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/teams/manager")
public class TeamManagerController {

    private final AuthUtils authUtils;
    private final TeamService teamService;


    @GetMapping
    public ResponseEntity<CollectionResponse<TeamShortDto>> getTeams() {

        UUID managerId = authUtils.authenticatedUserId();
        List<TeamShortDto> dtos = teamService.getTeamsForManager(managerId);
        PageMeta pageMeta = new PageMeta(
                0,
                dtos.size(),
                dtos.size(),
                1,
                true
        );
        return ResponseEntity.ok(
                new CollectionResponse<>(
                        dtos,
                        pageMeta
                )
        );
    }

    @GetMapping("/{teamId}")
    public ResponseEntity<APIResponse<TeamDetailedDto>> getTeamDetails(@PathVariable UUID teamId) {

        UUID managerId = authUtils.authenticatedUserId();
        TeamDetailedDto detailedDto = teamService.getTeamDetails(teamId, managerId);

        return ResponseEntity.ok(
                new APIResponse<>(
                        "Success",
                        detailedDto
                )
        );
    }
}
