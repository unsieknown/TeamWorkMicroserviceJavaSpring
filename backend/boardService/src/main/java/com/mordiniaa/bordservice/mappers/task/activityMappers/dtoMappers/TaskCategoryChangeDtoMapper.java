package com.mordiniaa.bordservice.mappers.task.activityMappers.dtoMappers;

import com.mordiniaa.bordservice.dto.task.activity.TaskCategoryChangeDto;
import com.mordiniaa.bordservice.dto.user.UserDto;
import com.mordiniaa.bordservice.models.task.activity.TaskCategoryChange;
import org.springframework.stereotype.Component;

@Component
public class TaskCategoryChangeDtoMapper extends AbstractActivityDtoMapper<TaskCategoryChange, TaskCategoryChangeDto> {


    @Override
    protected TaskCategoryChangeDto toTypedDto(TaskCategoryChange element, UserDto user) {

        TaskCategoryChangeDto.TaskCategoryChangeDtoBuilder<?, ?> builder =
                TaskCategoryChangeDto.builder();
        mapBase(element, builder, user);
        return builder
                .prevTaskCategoryName(element.getPrevCategory())
                .nextTaskCategoryName(element.getNextCategory())
                .build();
    }

    @Override
    public Class<TaskCategoryChange> getSupportedType() {
        return TaskCategoryChange.class;
    }
}
