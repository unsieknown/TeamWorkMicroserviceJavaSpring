package com.mordiniaa.bordservice.mappers.task.activityMappers.dtoMappers;

import com.mordiniaa.bordservice.dto.task.activity.TaskCommentDto;
import com.mordiniaa.bordservice.dto.user.UserDto;
import com.mordiniaa.bordservice.models.task.activity.TaskComment;
import org.springframework.stereotype.Component;

@Component
public class TaskCommentDtoMapper extends AbstractActivityDtoMapper<TaskComment, TaskCommentDto> {

    @Override
    protected TaskCommentDto toTypedDto(TaskComment element, UserDto user) {
        TaskCommentDto.TaskCommentDtoBuilder<?, ?> builder =
                TaskCommentDto.builder();

        mapBase(element, builder, user);
        return builder
                .commentId(element.getCommentId())
                .comment(element.getComment().trim())
                .updated(element.isUpdated())
                .build();
    }

    @Override
    public Class<TaskComment> getSupportedType() {
        return TaskComment.class;
    }
}
