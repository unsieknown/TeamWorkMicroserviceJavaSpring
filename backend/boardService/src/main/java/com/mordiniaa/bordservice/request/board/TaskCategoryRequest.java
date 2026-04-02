package com.mordiniaa.bordservice.request.board;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TaskCategoryRequest {
    private String newCategoryName;
    private String existingCategoryName;
}
