package com.mordiniaa.notesservice.requests.regular;


import com.mordiniaa.notesservice.models.regular.Category;
import com.mordiniaa.notesservice.requests.NoteRequest;

public interface RegularNoteRequest extends NoteRequest {

    Category getCategory();
}
