package com.mordiniaa.bordservice.models.task.activity;

import com.mordiniaa.bordservice.models.task.TaskStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.TypeAlias;

@Getter
@Setter
@TypeAlias("status_change")
@NoArgsConstructor
public class TaskStatusChange extends TaskActivityElement {

    private TaskStatus prevStatus;
    private TaskStatus nextStatus;
}
