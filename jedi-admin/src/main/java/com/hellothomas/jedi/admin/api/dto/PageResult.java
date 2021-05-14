package com.hellothomas.jedi.admin.api.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PageResult<T> {
    private List<T> content;

    private long total;

    private int pageNum;

    private int pageSize;
}
