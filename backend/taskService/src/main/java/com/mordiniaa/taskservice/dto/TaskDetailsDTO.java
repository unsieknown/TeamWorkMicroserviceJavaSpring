package com.mordiniaa.taskservice.dto;

import com.mordiniaa.backend.dto.task.activity.TaskActivityElementDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class TaskDetailsDTO extends TaskShortDto {

    private List<TaskActivityElementDto> taskActivityElements;
}
