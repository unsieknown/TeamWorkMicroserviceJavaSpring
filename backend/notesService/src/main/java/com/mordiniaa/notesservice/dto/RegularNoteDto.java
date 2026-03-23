package com.mordiniaa.notesservice.dto;

import com.mordiniaa.backend.models.note.regular.Category;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class RegularNoteDto extends NoteDto {

    private Category category;
}
