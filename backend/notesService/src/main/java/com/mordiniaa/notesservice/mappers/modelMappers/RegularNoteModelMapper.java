package com.mordiniaa.notesservice.mappers.modelMappers;

import com.mordiniaa.notesservice.models.regular.RegularNote;
import com.mordiniaa.notesservice.requests.NoteRequest;
import com.mordiniaa.notesservice.requests.regular.CreateRegularNoteRequest;
import com.mordiniaa.notesservice.requests.regular.PatchRegularNoteRequest;
import com.mordiniaa.notesservice.requests.regular.RegularNoteRequest;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class RegularNoteModelMapper extends AbstractNoteModelMapper<RegularNoteRequest, RegularNote> {

    @Override
    protected RegularNote toModelTyped(RegularNoteRequest regularNoteRequest) {
        RegularNote.RegularNoteBuilder<?, ?> builder = RegularNote.builder();
        mapBase(regularNoteRequest, builder);
        return builder.category(regularNoteRequest.getCategory())
                .build();
    }

    @Override
    public Set<Class<? extends NoteRequest>> getSupportedRequestClasses() {
        return Set.of(
                CreateRegularNoteRequest.class,
                PatchRegularNoteRequest.class
        );
    }

    @Override
    public Class<RegularNote> getSupportedClass() {
        return RegularNote.class;
    }

    @Override
    protected void updateModelTyped(RegularNote note, RegularNoteRequest noteRequest) {

        super.updateModelTyped(note, noteRequest);

        if (noteRequest.getCategory() != null)
            note.setCategory(noteRequest.getCategory());
    }
}
