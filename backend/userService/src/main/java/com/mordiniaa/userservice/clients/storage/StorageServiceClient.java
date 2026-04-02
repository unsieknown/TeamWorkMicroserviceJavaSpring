package com.mordiniaa.userservice.clients.storage;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

import java.util.UUID;

@HttpExchange
public interface StorageServiceClient {

    @PostExchange(value = "/inter/images/profile", contentType= MediaType.MULTIPART_FORM_DATA_VALUE)
    String addProfileImage(
            @RequestParam UUID userId,
            @RequestPart("file") MultipartFile imageFile
    );
}
