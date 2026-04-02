package com.mordiniaa.bordservice.dto.task.activity;

import com.mordiniaa.bordservice.models.task.TaskStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class TaskStatusChangeDto extends TaskActivityElementDto {

    private TaskStatus prevStatus;
    private TaskStatus nextStatus;
}
