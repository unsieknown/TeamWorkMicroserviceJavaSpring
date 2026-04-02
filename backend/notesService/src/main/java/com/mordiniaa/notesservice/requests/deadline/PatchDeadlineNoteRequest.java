package com.mordiniaa.notesservice.requests.deadline;

import com.mordiniaa.notesservice.models.deadline.Priority;
import com.mordiniaa.notesservice.models.deadline.Status;
import com.mordiniaa.notesservice.requests.PatchNoteRequest;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
public class PatchDeadlineNoteRequest extends PatchNoteRequest implements DeadlineNoteRequest {

    private Status status;
    private Priority priority;
    private Instant deadline;
}
