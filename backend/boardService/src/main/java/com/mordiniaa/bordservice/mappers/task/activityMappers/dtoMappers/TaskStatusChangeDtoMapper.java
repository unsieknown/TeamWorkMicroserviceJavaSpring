package com.mordiniaa.bordservice.mappers.task.activityMappers.dtoMappers;

import com.mordiniaa.bordservice.dto.task.activity.TaskStatusChangeDto;
import com.mordiniaa.bordservice.dto.user.UserDto;
import com.mordiniaa.bordservice.models.task.activity.TaskStatusChange;
import org.springframework.stereotype.Component;

@Component
public class TaskStatusChangeDtoMapper extends AbstractActivityDtoMapper<TaskStatusChange, TaskStatusChangeDto> {

    @Override
    protected TaskStatusChangeDto toTypedDto(TaskStatusChange element, UserDto user) {

        TaskStatusChangeDto.TaskStatusChangeDtoBuilder<?, ?> builder =
                TaskStatusChangeDto.builder();
        mapBase(element, builder, user);
        return builder
                .prevStatus(element.getPrevStatus())
                .nextStatus(element.getNextStatus())
                .build();
    }

    @Override
    public Class<TaskStatusChange> getSupportedType() {
        return TaskStatusChange.class;
    }
}
