package com.mordiniaa.storageservice.controllers.inter;

import com.mordiniaa.storageservice.services.profileImagesStorage.ImagesStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/inter/images")
public class InterImageController {

    private final ImagesStorageService imagesStorageService;

    @PostMapping(value = "/profile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> addProfileImage(
            @RequestParam UUID userId,
            @RequestPart("file") MultipartFile file
    ) {

        String imageKey = imagesStorageService.addProfileImage(userId, file);
        return ResponseEntity.ok(imageKey);
    }
}
