package com.cash_online.Code_Challenge.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class LoanRequest {

    private final Double total;
    private final Long userId;
}
