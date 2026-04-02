package com.mordiniaa.notesservice.responses;


import com.mordiniaa.notesservice.dto.NoteDto;

public class NoteDtoApiResponse extends APIResponse<NoteDto> {

    public NoteDtoApiResponse(String message, NoteDto data) {
        super(message, data);
    }
}
