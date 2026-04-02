package com.mordiniaa.storageservice.services.profileImagesStorage;

import com.mordiniaa.storageservice.config.StorageProperties;
import com.mordiniaa.storageservice.exceptions.BadRequestException;
import com.mordiniaa.storageservice.exceptions.ImageNotFoundException;
import com.mordiniaa.storageservice.exceptions.UnexpectedException;
import com.mordiniaa.storageservice.messaging.rabbit.consume.UserMessage;
import com.mordiniaa.storageservice.messaging.rabbit.publish.RabbitMQPublisher;
import com.mordiniaa.storageservice.messaging.rabbit.publish.UserProfileImageChangedMessage;
import com.mordiniaa.storageservice.models.imageStorage.ImageMetadata;
import com.mordiniaa.storageservice.repositories.mongo.ImageMetadataRepository;
import com.mordiniaa.storageservice.services.StorageProvider;
import com.mordiniaa.storageservice.utils.CloudStorageServiceUtils;
import com.mordiniaa.storageservice.utils.MongoIdUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.InputStream;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ImagesStorageService {

    private final MongoIdUtils mongoIdUtils;
    private final ImageMetadataRepository imageMetadataRepository;
    private final StorageProvider storageProvider;
    private final StorageProperties storageProperties;
    private final CloudStorageServiceUtils cloudStorageServiceUtils;
    private final RabbitMQPublisher rabbitMQPublisher;

    public ResponseEntity<StreamingResponseBody> getProfileImage(String key) {

        if (storageProperties.getProfileImages().getDefaultImageKey().equals(key))
            return defaultImage();

        ObjectId objectId = mongoIdUtils.getObjectId(key);

        ImageMetadata meta = imageMetadataRepository.findById(objectId)
                .orElse(null);

        if (meta == null)
            return defaultImage();

        StreamingResponseBody body = outputStream -> {
            try (InputStream in = storageProvider.downloadFile(
                    storageProperties.getProfileImages().getPath(),
                    meta.getStoredName()
            )) {
                in.transferTo(outputStream);
            }
        };

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("image/" + meta.getExtension()))
                .body(body);
    }

    @Transactional
    public String addProfileImage(UUID userId, MultipartFile file) {

        StorageProperties.ProfileImages profileImages = storageProperties.getProfileImages();
        String mimetype = baseImageValidation(file, profileImages.getMimeTypes());

        ImageMetadata metadata = imageMetadataRepository.findImageMetadataByOwnerId(userId)
                .orElse(null);
        if (metadata != null)
            imageMetadataRepository.deleteById(metadata.getId());

        String originalName = file.getOriginalFilename();
        String ext = switch (getFileExtension(mimetype)) {
            case "jpeg", "jpg" -> "jpg";
            default -> "png";
        };

        String newImageName = cloudStorageServiceUtils.buildStorageKey().concat(".".concat(ext));
        String profileImagesPath = profileImages.getPath();

        String imageKey;
        try {
            addImage(
                    profileImagesPath,
                    newImageName,
                    ext,
                    profileImages.getProfileWidth(),
                    profileImages.getProfileHeight(),
                    file
            );

            ImageMetadata savedMeta = imageMetadataRepository.save(ImageMetadata.builder()
                    .originalName(originalName)
                    .storedName(newImageName)
                    .extension(ext)
                    .ownerId(userId)
                    .size(file.getSize())
                    .build()
            );

            imageKey = savedMeta.getId().toHexString();
            updateUserImageKey(userId, imageKey);
        } catch (Exception e) {
            if (metadata != null) {
                imageMetadataRepository.save(metadata);
                updateUserImageKey(userId, metadata.getId().toHexString());
            } else {
                updateUserImageKey(userId, profileImages.getDefaultImageKey());
            }
            throw new UnexpectedException("Unknown Error While Saving Image");
        }

        if (metadata != null) {
            storageProvider.delete(
                    profileImagesPath,
                    metadata.getStoredName()
            );
        }

        return imageKey;
    }

    public void addImage(String profileImagesPath, String storedName, String ext, int width, int height, MultipartFile file) {

        boolean uploaded = false;
        try (InputStream in = file.getInputStream()) {
            storageProvider.uploadImage(
                    profileImagesPath,
                    storedName,
                    ext,
                    width,
                    height,
                    in
            );
            uploaded = true;
        } catch (Exception e) {
            if (uploaded) {
                removeImage(profileImagesPath, storedName);
            }
            throw new UnexpectedException("Unknow Error Occurred");
        }
    }

    private void removeImage(String profileImagesPath, String storedName) {
        storageProvider.delete(
                profileImagesPath,
                storedName
        );
    }

    @Transactional
    @RabbitListener(queues = "${rabbitmq.queue.defaultImageKey}")
    public void setDefaultImage(UserMessage message) {

        UUID userId = message.userId();

        ImageMetadata metadata = imageMetadataRepository.findImageMetadataByOwnerId(userId)
                .orElse(null);

        if (metadata != null) {
            String storageName = metadata.getStoredName();
            storageProvider.delete(storageProperties.getProfileImages().getPath(), storageName);
        }

        updateUserImageKey(userId, storageProperties.getProfileImages().getDefaultImageKey());

        if (metadata != null)
            imageMetadataRepository.deleteById(metadata.getId());
    }

    @Transactional
    public void updateUserImageKey(UUID userId, String imageKey) {
        rabbitMQPublisher.publishUserProfileChangedMessage(
                new UserProfileImageChangedMessage(userId, imageKey)
        );
    }

    private ResponseEntity<StreamingResponseBody> defaultImage() {

        ClassPathResource resource = new ClassPathResource(storageProperties.getProfileImages().getDefaultImagePath());

        if (!resource.exists())
            throw new ImageNotFoundException("Default avatar not found in resources");

        StreamingResponseBody body = os -> {
            try (InputStream in = resource.getInputStream()) {
                in.transferTo(os);
            }
        };

        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_PNG)
                .body(body);
    }

    private String getFileExtension(String mimetype) {
        return mimetype.split("/")[1];
    }

    private String baseImageValidation(MultipartFile file, List<String> mimeTypes) {
        if (file.isEmpty())
            throw new BadRequestException("Invalid File Sent");

        String mimetype = file.getContentType();
        if (mimetype == null || !mimeTypes.contains(mimetype))
            throw new BadRequestException("Unsupported Mimetype");

        String originalName = file.getOriginalFilename();
        if (originalName == null || cloudStorageServiceUtils.containsPathSeparator(originalName))
            throw new BadRequestException("Illegal Characters Detected");

        return mimetype;
    }
}
