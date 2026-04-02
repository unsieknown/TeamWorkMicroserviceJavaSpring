package com.mordiniaa.bordservice.repositories.board.aggregation.returnTypes;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class BoardMembersTasksOnly extends BoardMembersOnly {

    private List<TaskCreatorProjectionWithOptPosition> tasks;
}
