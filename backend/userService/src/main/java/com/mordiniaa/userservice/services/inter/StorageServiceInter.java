package com.mordiniaa.userservice.services.inter;

import com.mordiniaa.userservice.clients.storage.StorageServiceClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StorageServiceInter {

    private final StorageServiceClient storageServiceClient;

    public String saveProfileImage(UUID userId, MultipartFile imageFile) {
        return storageServiceClient.addProfileImage(userId, imageFile);
    }
}
