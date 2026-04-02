package com.mordiniaa.bordservice.repositories.board.aggregation.returnTypes;

import com.mordiniaa.bordservice.models.board.BoardMember;
import com.mordiniaa.bordservice.models.board.BoardMembers;
import com.mordiniaa.bordservice.models.board.BoardTemplate;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.types.ObjectId;

import java.time.Instant;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class BoardMembersOnly implements BoardMembers, BoardTemplate {

    private ObjectId id;
    private String boardName;
    private BoardMember owner;
    private List<BoardMember> members;
    private Instant createdAt;
    private Instant updatedAt;
}
