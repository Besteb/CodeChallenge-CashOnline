package com.cash_online.Code_Challenge.services;

import com.cash_online.Code_Challenge.exceptionhandling.exceptions.PageNotFoundException;
import com.cash_online.Code_Challenge.exceptionhandling.exceptions.PageSizeException;
import com.cash_online.Code_Challenge.models.Loan;
import com.cash_online.Code_Challenge.models.LoanPaginatedResponse;
import com.cash_online.Code_Challenge.models.Pagination;
import com.cash_online.Code_Challenge.models.User;
import com.cash_online.Code_Challenge.repositories.LoanRepository;
import com.cash_online.Code_Challenge.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@Profile("test")
@SpringBootTest
@ExtendWith(SpringExtension.class)
public class LoanServiceTest {
    @Autowired
    private LoanService loanService;

    @Autowired
    private LoanRepository loanRepository;
    @Autowired
    private UserRepository userRepository;

    private List<Loan> LOAN_LIST;

    private User user;

    @BeforeEach
    public void init() {
        loanRepository.deleteAll();
        userRepository.deleteAll();

        LOAN_LIST = new ArrayList<>();

        user = new User("email", "name1", "lastname1");
        User tempUser2 = new User("email2", "name2", "lastname2");

        userRepository.save(user);
        userRepository.save(tempUser2);

        LOAN_LIST.add(new Loan(1230.00, user.getId()));
        LOAN_LIST.add(new Loan(1000.00, user.getId()));
        LOAN_LIST.add(new Loan(3000.00, tempUser2.getId()));

        loanRepository.saveAll(LOAN_LIST);
    }

    @Test
    public void getPaginatedLoans_calledWithPageSizeAndUserId_success() throws PageNotFoundException, PageSizeException {

        Integer pageNumber = 1;
        Integer pageSize = 5;
        Long expectedTotal = 2L;

        LoanPaginatedResponse loanExpectedResponse = LoanPaginatedResponse
                .builder()
                .items(LOAN_LIST.subList(0, 2))
                .pagination(
                        Pagination
                                .builder()
                                .page(pageNumber)
                                .size(pageSize)
                                .total(expectedTotal)
                                .build()
                )
                .build();

        LoanPaginatedResponse result = loanService.getPaginatedLoans(pageNumber, pageSize, user.getId());

        assertEquals(loanExpectedResponse, result);
    }

    @Test
    public void getPaginatedLoans_calledWithOnlyPageAndSize_success() throws PageNotFoundException, PageSizeException {

        Integer pageNumber = 1;
        Integer pageSize = 2;
        Long expectedTotal = (long) LOAN_LIST.size();

        LoanPaginatedResponse loanExpectedResponse = LoanPaginatedResponse
                .builder()
                .items(LOAN_LIST.subList(0, pageSize))
                .pagination(
                        Pagination
                                .builder()
                                .page(pageNumber)
                                .size(pageSize)
                                .total(expectedTotal)
                                .build()
                )
                .build();

        LoanPaginatedResponse result = loanService.getPaginatedLoans(pageNumber, pageSize, null);

        assertEquals(result, loanExpectedResponse);
    }

    @Test
    public void getPaginateLoans_calledWithPageNumberAs0_ThrowPageNotFoundException() {

        Integer pageNumber = 0;
        Integer pageSize = 1;

        Exception exception = assertThrows(
                PageNotFoundException.class,
                () -> loanService.getPaginatedLoans(pageNumber, pageSize, null));
        assertEquals("Page number must not be less than one! Given: 0", exception.getMessage());
    }

    @Test
    public void getPaginateLoans_calledWithPageNumberAndSizeAs0_ThrowPageSizeException() {

        Integer pageNumber = 1;
        Integer pageSize = 0;

        Exception exception = assertThrows(
                PageSizeException.class,
                () -> loanService.getPaginatedLoans(pageNumber, pageSize, null));
        assertEquals("Page size must not be less than one! Given: 0", exception.getMessage());
    }
}