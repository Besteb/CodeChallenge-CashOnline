package com.cash_online.Code_Challenge.controllers;

import com.cash_online.Code_Challenge.exceptionhandling.exceptions.LoanNotFoundException;
import com.cash_online.Code_Challenge.exceptionhandling.exceptions.PageNotFoundException;
import com.cash_online.Code_Challenge.exceptionhandling.exceptions.PageSizeException;
import com.cash_online.Code_Challenge.models.Loan;
import com.cash_online.Code_Challenge.models.LoanPaginatedResponse;
import com.cash_online.Code_Challenge.models.LoanRequest;
import com.cash_online.Code_Challenge.repositories.LoanRepository;
import com.cash_online.Code_Challenge.services.LoanService;
import com.sun.istack.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/loans")
@RestController()
public class LoanController {
    @Autowired
    private LoanService loanService;

    @Autowired
    private LoanRepository loanRepository;

    @GetMapping
    public LoanPaginatedResponse getLoans(
            @RequestParam("page") Integer page,
            @RequestParam("size") Integer size,
            @Nullable @RequestParam(value = "user_id", required = false) Long userId
    ) throws PageNotFoundException, PageSizeException {

        return loanService.getPaginatedLoans(page, size, userId);
    }

    @PostMapping
    public Loan addNewLoan(@RequestBody LoanRequest loanRequest) {

        return loanRepository.save(
                Loan.builder()
                        .total(loanRequest.getTotal())
                        .userId(loanRequest.getUserId())
                        .build()
        );

    }

    @DeleteMapping("/{id}")
    public void deleteLoan(@PathVariable Long id) throws LoanNotFoundException {
        if (!loanRepository.existsById(id))
            throw new LoanNotFoundException(
                    String.format("CCA-Delete-Loan-Exception: No loan with id %s exists.", id));
        loanRepository.deleteById(id);
    }

}
