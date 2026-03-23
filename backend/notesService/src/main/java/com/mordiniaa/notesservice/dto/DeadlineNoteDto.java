package com.mordiniaa.notesservice.dto;

import com.mordiniaa.backend.models.note.deadline.Priority;
import com.mordiniaa.backend.models.note.deadline.Status;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.Instant;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class DeadlineNoteDto extends NoteDto {

    private Priority priority;
    private Status status;
    private Instant deadline;
}
