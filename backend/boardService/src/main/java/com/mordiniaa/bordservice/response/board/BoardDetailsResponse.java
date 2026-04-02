package com.mordiniaa.bordservice.response.board;


import com.mordiniaa.bordservice.dto.board.BoardShortDto;
import com.mordiniaa.bordservice.response.APIResponse;

public class BoardDetailsResponse extends APIResponse<BoardShortDto> {

    public BoardDetailsResponse(String message, BoardShortDto data) {
        super(message, data);
    }
}
