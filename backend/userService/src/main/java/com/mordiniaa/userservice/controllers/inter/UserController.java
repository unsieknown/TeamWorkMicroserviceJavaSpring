package com.mordiniaa.userservice.controllers.inter;

import com.mordiniaa.userservice.dto.UserDto;
import com.mordiniaa.userservice.exceptions.BadRequestException;
import com.mordiniaa.userservice.projections.SecurityUserProjection;
import com.mordiniaa.userservice.responses.interservice.UserDtoCollectionResponse;
import com.mordiniaa.userservice.responses.interservice.UserInfoResponse;
import com.mordiniaa.userservice.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/inter/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<UserInfoResponse> getUserInfo(
            @RequestParam String username
    ) {

        UserInfoResponse infoResponse = userService.getUserInfo(username);
        return ResponseEntity.ok(infoResponse);
    }

    @GetMapping("/security-user")
    public ResponseEntity<SecurityUserProjection> getSecurityUser(
            @RequestParam(required = false) String username,
            @RequestParam(required = false) UUID userId
    ) {

        if (userId != null && username != null)
            throw new BadRequestException("Invalid Parameters");

        SecurityUserProjection user;
        if (userId != null)
            user = userService.getSecurityUserById(userId);
        else
            user = userService.getSecurityUserByUsername(username);

        return ResponseEntity.ok(user);
    }

    @GetMapping("/exist/{userId}")
    public ResponseEntity<Boolean> checkUserExistence(@PathVariable UUID userId) {

        boolean available = false;
        try {
            userService.checkUserAvailability(userId);
            available = true;
        } catch (Exception e) {
            // Ignore
        }
        return ResponseEntity.ok(available);
    }

    @PostMapping("/exist")
    public ResponseEntity<Boolean> checkUserExistence(@RequestBody Set<UUID> ids) {
        return ResponseEntity.ok(userService.checkUsersAvailability(ids));
    }

    @PostMapping("/batch")
    public ResponseEntity<UserDtoCollectionResponse> getUsersByIds(
            @RequestBody Set<UUID> ids
            ) {

        Set<UserDto> users = userService.batchUsers(ids);
        UserDtoCollectionResponse response = new UserDtoCollectionResponse(users);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/password")
    public ResponseEntity<Void> updateUserPassword(@RequestParam UUID userId, @RequestBody String password) {
        userService.updatePasswordByUserId(userId, password);

        return ResponseEntity.ok().build();
    }
}
