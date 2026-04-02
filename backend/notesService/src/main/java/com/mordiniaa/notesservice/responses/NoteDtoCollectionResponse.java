package com.mordiniaa.notesservice.responses;


import com.mordiniaa.notesservice.dto.NoteDto;

import java.util.List;

public class NoteDtoCollectionResponse extends CollectionResponse<NoteDto> {
    public NoteDtoCollectionResponse(List<NoteDto> items, PageMeta pageMeta) {
        super(items, pageMeta);
    }
}
