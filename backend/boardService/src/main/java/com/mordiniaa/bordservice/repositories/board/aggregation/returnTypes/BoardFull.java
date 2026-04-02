package com.mordiniaa.bordservice.repositories.board.aggregation.returnTypes;

import com.mordiniaa.bordservice.dto.user.UserDto;
import com.mordiniaa.bordservice.models.board.BoardTemplate;
import com.mordiniaa.bordservice.models.task.Task;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.types.ObjectId;

import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class BoardFull implements BoardTemplate {

    private ObjectId id;
    private UserDto owner;
    private List<UserDto> members;
    private UUID teamId;
    private String boardName;
    private List<TaskCategoryFull> taskCategories;
    private Instant createdAt;
    private Instant updatedAt;

    @Getter
    @Setter
    @NoArgsConstructor
    public static class TaskCategoryFull{

        private int position = 0;

        private String categoryName;

        private Set<Task> tasks = new HashSet<>();

        private Instant createdAt;
    }
}
