package com.mordiniaa.bordservice.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TaskCategoryRequest {
    private String newCategoryName;
    private String existingCategoryName;
}
