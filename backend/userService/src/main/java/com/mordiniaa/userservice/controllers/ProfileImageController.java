package com.mordiniaa.userservice.controllers;

import com.mordiniaa.userservice.responses.APIResponse;
import com.mordiniaa.userservice.services.UserProfileImageService;
import com.mordiniaa.userservice.utils.AuthUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user/image")
public class ProfileImageController {

    private final AuthUtils authUtils;
    private final UserProfileImageService userProfileImageService;

    @PostMapping("/add")
    public ResponseEntity<APIResponse<Void>> addProfileImage(@RequestBody MultipartFile profileImage) {

        UUID userId = authUtils.authenticatedUserId();
        userProfileImageService.addProfileImage(userId, profileImage);
        return new ResponseEntity<>(
                new APIResponse<>(
                        "Added Image Successfully",
                        null
                ),
                HttpStatus.CREATED
        );
    }

    @PutMapping("/default")
    public ResponseEntity<APIResponse<Void>> setDefaultImage() {

        UUID userId = authUtils.authenticatedUserId();
        userProfileImageService.setDefaultProfileImage(userId);
        return new ResponseEntity<>(
                new APIResponse<>(
                        "Image Set Successfully",
                        null
                ),
                HttpStatus.CREATED
        );
    }
}
