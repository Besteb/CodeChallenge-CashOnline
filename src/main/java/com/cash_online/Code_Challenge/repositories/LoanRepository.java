package com.cash_online.Code_Challenge.repositories;

import com.cash_online.Code_Challenge.models.Loan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface LoanRepository extends PagingAndSortingRepository<Loan, Long> {
    Page<Loan> findAllByUserId(Long userId, Pageable pageable);
}
