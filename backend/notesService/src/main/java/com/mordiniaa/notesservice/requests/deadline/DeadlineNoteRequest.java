package com.mordiniaa.notesservice.requests.deadline;


import com.mordiniaa.notesservice.models.deadline.Priority;
import com.mordiniaa.notesservice.models.deadline.Status;
import com.mordiniaa.notesservice.requests.NoteRequest;

import java.time.Instant;

public interface DeadlineNoteRequest extends NoteRequest {

    Status getStatus();

    Priority getPriority();

    Instant getDeadline();
}
