package com.cash_online.Code_Challenge.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoanPaginatedResponse {
    private List<Loan> items;
    private Pagination pagination;
}
