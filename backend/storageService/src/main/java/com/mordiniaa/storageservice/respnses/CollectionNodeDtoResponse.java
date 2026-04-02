package com.mordiniaa.storageservice.respnses;


import com.mordiniaa.storageservice.dto.FileNodeDto;

import java.util.List;

public class CollectionNodeDtoResponse extends CollectionResponse<FileNodeDto> {

    public CollectionNodeDtoResponse(List<FileNodeDto> items, PageMeta pageMeta) {
        super(items, pageMeta);
    }
}
