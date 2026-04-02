package com.mordiniaa.bordservice.mappers.task.activityMappers;

import com.mordiniaa.bordservice.dto.task.activity.TaskActivityElementDto;
import com.mordiniaa.bordservice.dto.user.UserDto;
import com.mordiniaa.bordservice.exceptions.UnexpectedException;
import com.mordiniaa.bordservice.mappers.task.activityMappers.dtoMappers.AbstractActivityDtoMapper;
import com.mordiniaa.bordservice.models.task.activity.TaskActivityElement;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class TaskActivityMapper {

    private final Map<Class<? extends TaskActivityElement>, AbstractActivityDtoMapper<?, ?>> taskActivityDtoMappers;

    public TaskActivityMapper(List<AbstractActivityDtoMapper<?, ?>> dtoMappers) {
        this.taskActivityDtoMappers = dtoMappers.stream()
                .collect(Collectors.toMap(
                        AbstractActivityDtoMapper::getSupportedType,
                        Function.identity()
                ));
    }

    public TaskActivityElementDto toDto(TaskActivityElement element, UserDto user) {

        AbstractActivityDtoMapper<?, ?> mapper = taskActivityDtoMappers.get(element.getClass());
        if (mapper == null) {
            throw new UnexpectedException("Unknow Error Occurred");
        }
        return mapper.toDto(element, user);
    }
}
