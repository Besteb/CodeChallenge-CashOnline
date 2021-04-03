package com.cash_online.Code_Challenge.controllers;

import com.cash_online.Code_Challenge.models.Loan;
import com.cash_online.Code_Challenge.models.LoanPaginatedResponse;
import com.cash_online.Code_Challenge.models.LoanRequest;
import com.cash_online.Code_Challenge.models.Pagination;
import com.cash_online.Code_Challenge.repositories.LoanRepository;
import com.cash_online.Code_Challenge.services.LoanService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = {LoanController.class})
public class LoanControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private LoanService loanService;

    @MockBean
    private LoanRepository loanRepository;

    private List<Loan> LOAN_LIST;

    @BeforeEach
    public void mockLoanRepository() {
        LOAN_LIST = new ArrayList<>();

        LOAN_LIST.add(new Loan(1230.00, 1L));
        LOAN_LIST.add(new Loan(1000.00, 1L));
        LOAN_LIST.add(new Loan(3000.00, 2L));

    }


    @Test
    public void getLoans_WithPageAndSizeAndWithPageSizeAndUserId() throws Exception {
        Integer PAGE_NUMBER = 1;
        Integer PAGE_SIZE = 5;
        Long USER_ID = null;

        LoanPaginatedResponse loanExpectedResponse = LoanPaginatedResponse
                .builder()
                .items(LOAN_LIST)
                .pagination(
                        Pagination
                                .builder()
                                .page(PAGE_NUMBER)
                                .size(PAGE_SIZE)
                                .total((long) LOAN_LIST.size())
                                .build()
                )
                .build();

        when(loanService.getPaginatedLoans(PAGE_NUMBER, PAGE_SIZE, USER_ID)).thenReturn(loanExpectedResponse);

        final MvcResult result = performGetRequest(
                String.format("/loans?page=%d&size=%d", PAGE_NUMBER, PAGE_SIZE), status().isOk()).andReturn();

        final LoanPaginatedResponse loanPaginatedResponse = objectMapper.readValue(
                result.getResponse().getContentAsString(), LoanPaginatedResponse.class);

        assertEquals(loanExpectedResponse, loanPaginatedResponse);

        loanPaginatedResponse.setItems(LOAN_LIST.subList(0, 2));

        USER_ID = 1L;
        when(loanService.getPaginatedLoans(PAGE_NUMBER, PAGE_SIZE, USER_ID)).thenReturn(loanExpectedResponse);

        final MvcResult resultWithUserId = performGetRequest(
                String.format("/loans?page=%d&size=%d&user_id=%d", PAGE_NUMBER, PAGE_SIZE, USER_ID), status().isOk())
                .andReturn();

        final LoanPaginatedResponse loanPaginatedResponseWithUserID = objectMapper.readValue(
                resultWithUserId.getResponse().getContentAsString(), LoanPaginatedResponse.class);

        assertEquals(loanExpectedResponse, loanPaginatedResponseWithUserID);
    }

    @Test
    public void getLoans_returnsPageNotFoundException() throws Exception {
        Integer PAGE_NUMBER = 0;
        Integer PAGE_SIZE = 5;
        Long USER_ID = 1L;

        when(loanService.getPaginatedLoans(anyInt(), anyInt(), anyLong())).thenCallRealMethod();
        performGetRequest(
                String.format("/loans?page=%d&size=%d&user_id=%d", PAGE_NUMBER, PAGE_SIZE, USER_ID),
                status().isBadRequest());

        PAGE_NUMBER = 1;
        PAGE_SIZE = 0;

        performGetRequest(
                String.format("/loans?page=%d&size=%d&user_id=%d", PAGE_NUMBER, PAGE_SIZE, USER_ID),
                status().isBadRequest());
    }

    @Test
    public void addNewLoan_success() throws Exception {
        Loan expectedLoan = Loan.builder().id(11L).total(1000.00).userId(1L).build();

        when(loanRepository.save(any(Loan.class))).thenReturn(expectedLoan);

        LoanRequest loanRequest = LoanRequest
                .builder().userId(expectedLoan.getUserId()).total(expectedLoan.getTotal()).build();

        final MockHttpServletRequestBuilder postRequest = post("/loans")
                .accept("application/json")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(loanRequest));

        final MvcResult result = mockMvc.perform(postRequest).andExpect(status().isOk()).andReturn();
        final Loan loanResponse = objectMapper.readValue(result.getResponse().getContentAsString(), Loan.class);

        assertEquals(expectedLoan, loanResponse);
    }

    @Test
    public void deleteLoan_success() throws Exception {

        Long loanId = 1L;
        when(loanRepository.existsById(loanId)).thenReturn(true);

        final MockHttpServletRequestBuilder deleteRequest = delete(String.format("/loans/%d", loanId))
                .accept("application/json")
                .contentType("application/json");

        mockMvc.perform(deleteRequest).andExpect(status().isOk());

        verify(loanRepository, times(1)).deleteById(loanId);
    }

    @Test
    public void deleteLoan_returnsBadRequest() throws Exception {

        Long loanId = 2L;
        when(loanRepository.existsById(loanId)).thenReturn(false);

        final MockHttpServletRequestBuilder deleteRequest = delete(String.format("/loans/%d", loanId))
                .accept("application/json")
                .contentType("application/json");

        mockMvc.perform(deleteRequest).andExpect(status().isBadRequest());
    }

    private ResultActions performGetRequest(final String url, ResultMatcher resultMatcher) throws Exception {

        final MockHttpServletRequestBuilder getRequest = get(url)
                .accept("application/json")
                .contentType("application/json");

        return mockMvc.perform(getRequest).andExpect(resultMatcher);
    }
}