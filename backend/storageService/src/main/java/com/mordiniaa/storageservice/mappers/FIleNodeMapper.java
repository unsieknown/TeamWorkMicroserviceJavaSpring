package com.mordiniaa.storageservice.mappers;

import com.mordiniaa.storageservice.dto.FileNodeDto;
import com.mordiniaa.storageservice.models.cloudStorage.FileNode;
import com.mordiniaa.storageservice.models.cloudStorage.NodeType;
import org.springframework.stereotype.Component;

@Component
public class FIleNodeMapper {

    public FileNodeDto toDto(FileNode fileNode, String parentPath) {
        FileNodeDto dto = new FileNodeDto();
        dto.setNodeType(fileNode.getNodeType());
        dto.setId(fileNode.getId());
        dto.setParentPath(parentPath);
        dto.setSize(fileNode.getNodeType().equals(NodeType.DIRECTORY) ? fileNode.getSubTreeSize() : fileNode.getSize());
        dto.setName(fileNode.getName());
        return dto;
    }
}
