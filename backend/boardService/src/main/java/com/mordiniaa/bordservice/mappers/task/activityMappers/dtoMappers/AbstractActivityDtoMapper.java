package com.mordiniaa.bordservice.mappers.task.activityMappers.dtoMappers;

import com.mordiniaa.bordservice.dto.task.activity.TaskActivityElementDto;
import com.mordiniaa.bordservice.dto.user.UserDto;
import com.mordiniaa.bordservice.models.task.activity.TaskActivityElement;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class AbstractActivityDtoMapper<T extends TaskActivityElement, D extends TaskActivityElementDto> {

    public final TaskActivityElementDto toDto(TaskActivityElement element, UserDto user) {
        return toTypedDto(cast(element), user);
    }

    protected void mapBase(TaskActivityElement element,
                           TaskActivityElementDto.TaskActivityElementDtoBuilder<?, ?> b,
                           UserDto user) {
        b
                .user(user)
                .createdAt(element.getCreatedAt());
    }

    @SuppressWarnings("unchecked")
    public T cast(TaskActivityElement element) {
        return (T) element;
    }

    protected abstract D toTypedDto(T element, UserDto user);

    public abstract Class<T> getSupportedType();
}
