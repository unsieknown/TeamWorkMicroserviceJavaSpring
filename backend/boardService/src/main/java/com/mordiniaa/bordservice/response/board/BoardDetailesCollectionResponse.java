package com.mordiniaa.bordservice.response.board;


import com.mordiniaa.bordservice.dto.board.BoardShortDto;
import com.mordiniaa.bordservice.response.CollectionResponse;
import com.mordiniaa.bordservice.response.PageMeta;

import java.util.List;

public class BoardDetailesCollectionResponse extends CollectionResponse<BoardShortDto> {
    public BoardDetailesCollectionResponse(List<BoardShortDto> items, PageMeta pageMeta) {
        super(items, pageMeta);
    }
}
