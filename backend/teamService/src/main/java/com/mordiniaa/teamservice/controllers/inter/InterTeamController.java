package com.mordiniaa.teamservice.controllers.inter;

import com.mordiniaa.teamservice.services.InterTeamService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/inter/teams/{teamId}")
public class InterTeamController {

    private final InterTeamService interTeamService;

    public InterTeamController(InterTeamService interTeamService) {
        this.interTeamService = interTeamService;
    }

    @GetMapping("/manager/{managerId}/exist")
    public ResponseEntity<Boolean> existsTeamWithManager(
            @PathVariable UUID teamId,
            @PathVariable UUID managerId
    ) {

        boolean exists = interTeamService.existsTeamWithManager(teamId, managerId);
        return ResponseEntity.ok(exists);
    }

    @GetMapping("/user/{userId}/exist")
    public ResponseEntity<Boolean> existsTeamWithUser(
            @PathVariable UUID teamId,
            @PathVariable UUID userId
    ) {

        boolean exists = interTeamService.existsTeamWithUser(teamId, userId);
        return ResponseEntity.ok(exists);
    }
}
