package com.mordiniaa.notesservice.requests.deadline;

import com.mordiniaa.notesservice.models.deadline.Priority;
import com.mordiniaa.notesservice.models.deadline.Status;
import com.mordiniaa.notesservice.requests.CreateNoteRequest;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
public class CreateDeadlineNoteRequest extends CreateNoteRequest implements DeadlineNoteRequest {

    @NotNull(message = "Status is required")
    private Status status;

    @NotNull(message = "Priority is required")
    private Priority priority;

    @NotNull(message = "Deadline is required")
    @Future(message = "Deadline must be in the future")
    private Instant deadline;
}
