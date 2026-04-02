package com.mordiniaa.notesservice.requests.regular;

import com.mordiniaa.notesservice.models.regular.Category;
import com.mordiniaa.notesservice.requests.PatchNoteRequest;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PatchRegularNoteRequest extends PatchNoteRequest implements RegularNoteRequest {

    private Category category;
}
