package com.mordiniaa.taskservice.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CollectionResponse<T> {

    private List<T> items;
    private PageMeta pageMeta;
}
