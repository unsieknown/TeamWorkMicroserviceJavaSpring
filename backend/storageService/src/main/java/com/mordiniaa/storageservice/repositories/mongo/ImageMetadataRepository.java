package com.mordiniaa.storageservice.repositories.mongo;

import com.mordiniaa.storageservice.models.imageStorage.ImageMetadata;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;
import java.util.UUID;

public interface ImageMetadataRepository extends MongoRepository<ImageMetadata, ObjectId> {
    Optional<ImageMetadata> findImageMetadataByOwnerId(UUID ownerId);
}
