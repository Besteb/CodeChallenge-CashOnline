package com.cash_online.Code_Challenge.services;

import com.cash_online.Code_Challenge.exceptionhandling.exceptions.PageNotFoundException;
import com.cash_online.Code_Challenge.exceptionhandling.exceptions.PageSizeException;
import com.cash_online.Code_Challenge.models.Loan;
import com.cash_online.Code_Challenge.models.LoanPaginatedResponse;
import com.cash_online.Code_Challenge.models.Pagination;
import com.cash_online.Code_Challenge.repositories.LoanRepository;
import com.sun.istack.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class LoanService {

    @Autowired
    private LoanRepository loanRepository;

    public LoanPaginatedResponse getPaginatedLoans(
            Integer page, Integer size, @Nullable Long userId) throws PageNotFoundException, PageSizeException {

        if (page < 1) throw new PageNotFoundException(
                String.format("Page number must not be less than one! Given: %d", page));
        if (size < 1) throw new PageSizeException(
                String.format("Page size must not be less than one! Given: %d", size));

        LoanPaginatedResponse loanPaginatedResponse = new LoanPaginatedResponse();
        Page<Loan> loans;

        if (userId != null)
            loans = this.loanRepository.findAllByUserId(userId, PageRequest.of(page - 1, size));
        else
            loans = this.loanRepository.findAll(PageRequest.of(page - 1, size));

        loanPaginatedResponse.setItems(loans.getContent());
        loanPaginatedResponse.setPagination(new Pagination(page, loans.getSize(), loans.getTotalElements()));

        return loanPaginatedResponse;
    }
}
