package com.mordiniaa.teamservice.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TeamCreationRequest {

    @NotBlank
    @Size(min = 5, max = 40)
    @Pattern(regexp = "^\\p{L}+([ -]\\p{L}+)*$")
    private String teamName;
}
