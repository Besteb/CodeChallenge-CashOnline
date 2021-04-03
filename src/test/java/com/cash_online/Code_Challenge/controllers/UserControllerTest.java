package com.cash_online.Code_Challenge.controllers;

import com.cash_online.Code_Challenge.exceptionhandling.ErrorResponse;
import com.cash_online.Code_Challenge.exceptionhandling.exceptions.LoanNotFoundException;
import com.cash_online.Code_Challenge.models.Loan;
import com.cash_online.Code_Challenge.models.User;
import com.cash_online.Code_Challenge.models.UserRequest;
import com.cash_online.Code_Challenge.repositories.LoanRepository;
import com.cash_online.Code_Challenge.repositories.UserRepository;
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
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.junit.jupiter.api.Assertions.*;

import java.util.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private LoanRepository loanRepository;

    private List<Loan> LOAN_LIST;

    private User USER;

    @BeforeEach
    public void init() {
        Loan LOAN = Loan.builder().id(100L).userId(1L).total(1000.00).build();
        LOAN_LIST = new ArrayList<>(Collections.singleton(LOAN));

        USER = User
                .builder()
                .id(1L)
                .firstName("firstName")
                .lastName("lastName")
                .email("email")
                .loans(LOAN_LIST)
                .build();

        when(userRepository.findById(USER.getId())).thenReturn(Optional.of(USER));
        when(userRepository.existsById(USER.getId())).thenReturn(true);
        when(userRepository.save(any(User.class))).thenReturn(USER);
    }

    @Test
    public void returnUser_success() throws Exception {
        final MockHttpServletRequestBuilder getRequest = get(String.format("/users/%d", 1L))
                .accept("application/json")
                .contentType("application/json");

        final MvcResult result = mockMvc.perform(getRequest).andExpect(status().isOk()).andReturn();

        final User userResponse = objectMapper.readValue(result.getResponse().getContentAsString(), User.class);

        assertEquals(USER, userResponse);
    }

    @Test
    public void returnUser_returnsBadRequest() throws Exception {
        final MockHttpServletRequestBuilder getRequest = get(String.format("/users/%d", 2L))
                .accept("application/json")
                .contentType("application/json");

        mockMvc.perform(getRequest).andExpect(status().isBadRequest());
    }

    @Test
    public void createUser_success() throws Exception {
        UserRequest userRequest = UserRequest
                .builder()
                .firstName(USER.getFirstName())
                .lastName(USER.getLastName())
                .email(USER.getEmail())
                .build();

        final MockHttpServletRequestBuilder postRequest = post("/users")
                .accept("application/json")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(userRequest));

        final MvcResult result = mockMvc.perform(postRequest).andExpect(status().isOk()).andReturn();

        final User userResponse = objectMapper.readValue(result.getResponse().getContentAsString(), User.class);

        assertEquals(USER, userResponse);
    }

    @Test
    public void createUser_returnsUnprocessableEntity() throws Exception {
        when(userRepository.existsUserByEmail(USER.getEmail())).thenReturn(true);

        UserRequest userRequest = UserRequest
                .builder()
                .firstName(USER.getFirstName())
                .lastName(USER.getLastName())
                .email(USER.getEmail())
                .build();

        final MockHttpServletRequestBuilder postRequest = post("/users")
                .accept("application/json")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(userRequest));

        mockMvc.perform(postRequest).andExpect(status().isUnprocessableEntity());
    }

    @Test
    public void deleteUser_success() throws Exception {

        Long userId = 1L;

        final MockHttpServletRequestBuilder deleteRequest = delete(String.format("/users/%d", userId))
                .accept("application/json")
                .contentType("application/json");
        mockMvc.perform(deleteRequest).andExpect(status().isOk());

        verify(userRepository, times(1)).deleteById(userId);
    }

    @Test
    public void deleteUser_returnsBadRequest() throws Exception {

        Long userId = 2L;

        final MockHttpServletRequestBuilder deleteRequest = delete(String.format("/users/%d", userId))
                .accept("application/json")
                .contentType("application/json");

        mockMvc.perform(deleteRequest).andExpect(status().isBadRequest());
    }

    @Test
    public void deleteLoansById_success() throws Exception {

        Long userId = 1L;

        final MockHttpServletRequestBuilder deleteRequest = delete(String.format("/users/%d/loans", userId))
                .accept("application/json")
                .contentType("application/json");
        mockMvc.perform(deleteRequest).andExpect(status().isOk());

        verify(loanRepository, times(1)).deleteAll(USER.getLoans());
    }

    @Test
    public void deleteLoansById_returnsBadRequestUserNotFoundException() throws Exception {

        Long userId = 2L;

        final MockHttpServletRequestBuilder deleteRequest = delete(String.format("/users/%d/loans", userId))
                .accept("application/json")
                .contentType("application/json");

        mockMvc.perform(deleteRequest).andExpect(status().isBadRequest());
    }

    @Test
    public void deleteLoansById_returnsBadRequestLoanNotFoundException() throws Exception {

        User user = USER;
        user.setLoans(null);
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        final MockHttpServletRequestBuilder deleteRequest = delete(String.format("/users/%d/loans", user.getId()))
                .accept("application/json")
                .contentType("application/json");

        final MvcResult result = mockMvc.perform(deleteRequest).andExpect(status().isBadRequest()).andReturn();

        final LoanNotFoundException response =
                objectMapper.readValue(result.getResponse().getContentAsString(), LoanNotFoundException.class);

        assertEquals(
                String.format(
                        "CCA-Delete-Loans-By-User-Id-Exception: User with id : %d has no loans assigned.",
                        user.getId()),
                response.getMessage());
    }
}