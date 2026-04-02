package com.mordiniaa.notesservice.requests.regular;

import com.mordiniaa.notesservice.models.regular.Category;
import com.mordiniaa.notesservice.requests.CreateNoteRequest;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CreateRegularNoteRequest extends CreateNoteRequest implements RegularNoteRequest {

    @NotNull(message = "Category is required")
    private Category category;
}
