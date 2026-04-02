package com.mordiniaa.storageservice.utils;

import com.mordiniaa.storageservice.exceptions.ResourceNotFoundException;
import com.mordiniaa.storageservice.exceptions.UnexpectedException;
import com.mordiniaa.storageservice.models.cloudStorage.FileNode;
import com.mordiniaa.storageservice.models.cloudStorage.FileNodeBaseMeta;
import com.mordiniaa.storageservice.models.cloudStorage.UserStorage;
import com.mordiniaa.storageservice.repositories.mysql.FileNodeRepository;
import com.mordiniaa.storageservice.repositories.mysql.UserStorageRepository;
import com.mordiniaa.storageservice.services.FileNodeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class CloudStorageServiceUtils {

    private final UserStorageRepository userStorageRepository;
    private final FileNodeRepository fileNodeRepository;
    private final FileNodeService fileNodeService;

    public boolean containsPathSeparator(String filename) {
        return filename.contains("/") || filename.contains("\\");
    }

    @Transactional
    public UserStorage getOrCreateUserStorage(UUID userId) {
        return userStorageRepository.findUserStorageByUserId(userId)
                .orElseGet(() -> createNewStorageSafely(userId));
    }

    @Transactional
    public UserStorage createNewStorageSafely(UUID userId) {
        try {
            return userStorageRepository.save(new UserStorage(userId));
        } catch (DataIntegrityViolationException exception) {
            return userStorageRepository.findById(userId)
                    .orElseThrow(() -> new UnexpectedException("Unknown Error Occurred"));
        }
    }

    @Transactional
    public FileNode getParentNode(UUID userId, UUID parentId, UserStorage userStorage) {
        FileNode parent;
        if (parentId == null) {
            parent = userStorage.getRootNode();
        } else {
            parent = fileNodeService.getDirectory(parentId, userId);
            if (parent == null) {
                log.error("Parent Node Not Found");
                throw new ResourceNotFoundException("Parent Path Element Not Found");
            }
        }
        return parent;
    }

    public Set<UUID> collectParentChain(FileNode parent, UUID userId) {
        Set<UUID> ids = new HashSet<>();

        FileNode current = parent;
        while (current != null) {
            ids.add(current.getId());
            current = current.getParentId() == null
                    ? null
                    : fileNodeRepository.findDirByIdAndOwnerId(current.getParentId(), userId).orElse(null);
        }
        return ids;
    }

    public FileNodeBaseMeta getBaseFileProjection(UUID parentId, UUID userId) {
        return fileNodeRepository.findParentMetaProjection(parentId, userId)
                .orElse(null);
    }

    public String buildStorageKey() {
        return UUID.randomUUID().toString();
    }
}
