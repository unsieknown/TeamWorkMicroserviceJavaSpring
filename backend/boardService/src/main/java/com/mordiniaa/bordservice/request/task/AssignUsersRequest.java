package com.mordiniaa.bordservice.request.task;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class AssignUsersRequest {

    @NotEmpty
    private Set<UUID> users;
}
