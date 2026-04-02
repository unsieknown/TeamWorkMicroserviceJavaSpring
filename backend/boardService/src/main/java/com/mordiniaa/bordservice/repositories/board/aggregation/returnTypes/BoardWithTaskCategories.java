package com.mordiniaa.bordservice.repositories.board.aggregation.returnTypes;

import com.mordiniaa.bordservice.models.board.TaskCategory;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class BoardWithTaskCategories extends BoardMembersOnly {

    private List<TaskCategory> taskCategories;
}
