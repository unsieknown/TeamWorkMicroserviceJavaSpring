package com.mordiniaa.storageservice.models.cloudStorage;

import java.util.UUID;

public interface FileNodeStorageKey {

    UUID getId();

    NodeType getNodeType();

    String getName();

    String getStorageKey();
}
