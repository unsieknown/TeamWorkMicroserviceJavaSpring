package com.mordiniaa.notesservice.mappers.dtoMappers;

import com.mordiniaa.notesservice.dto.DeadlineNoteDto;
import com.mordiniaa.notesservice.models.deadline.DeadlineNote;
import org.springframework.stereotype.Component;

@Component
public class DeadlineNoteDtoMapper extends AbstractNoteDtoMapper<DeadlineNote, DeadlineNoteDto> {

    @Override
    public DeadlineNoteDto toDtoTyped(DeadlineNote note) {
        DeadlineNoteDto.DeadlineNoteDtoBuilder<?, ?> builder =
                DeadlineNoteDto.builder();

        mapBase(note, builder);

        return builder
                .priority(note.getPriority())
                .status(note.getStatus())
                .deadline(note.getDeadline())
                .build();
    }

    @Override
    public Class<DeadlineNote> getSupportedClass() {
        return DeadlineNote.class;
    }
}
