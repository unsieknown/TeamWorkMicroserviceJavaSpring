package com.mordiniaa.bordservice.models.board;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Document("boards")
@TypeAlias("board")
public class Board implements BoardMembers, BoardTemplate {

    @Id
    private ObjectId id;

    @Indexed
    @Field("owner")
    private BoardMember owner;

    @Indexed
    @Field("teamId")
    private UUID teamId;

    @Indexed
    @Field("boardName")
    private String boardName;

    @Field("highestTaskCategoryPosition")
    private int nextPosition = 0;

    @Field("taskCategories")
    private List<TaskCategory> taskCategories = new ArrayList<>();

    @Field("members")
    private List<BoardMember> members = new ArrayList<>();

    @Field("createdAt")
    private Instant createdAt = Instant.now();

    @Field("updatedAt")
    private Instant updatedAt = Instant.now();

    @Field("archived")
    private boolean archived = false;

    @Field("deleted")
    private boolean deleted = false;

    public void addMember(BoardMember member) {
        if (!members.contains(member))
            members.add(member);
    }

    public void removeMember(UUID userId) {
        members.stream()
                .filter(bm -> bm.getUserId().equals(userId))
                .findFirst()
                .ifPresent(member -> members.remove(member));
    }

    public void removeTaskCategory(TaskCategory category) {
        if (taskCategories.remove(category))
            nextPosition--;
    }
}

