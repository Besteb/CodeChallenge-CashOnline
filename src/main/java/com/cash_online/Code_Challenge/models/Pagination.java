package com.cash_online.Code_Challenge.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class Pagination {

    private Integer page;
    private Integer size;
    private Long total;
}
