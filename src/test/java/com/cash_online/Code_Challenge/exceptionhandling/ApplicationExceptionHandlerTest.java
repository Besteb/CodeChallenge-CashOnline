package com.cash_online.Code_Challenge.exceptionhandling;

import com.cash_online.Code_Challenge.controllers.UserController;
import com.cash_online.Code_Challenge.models.User;
import com.cash_online.Code_Challenge.repositories.LoanRepository;
import com.cash_online.Code_Challenge.repositories.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = {ApplicationExceptionHandler.class, UserController.class})
class ApplicationExceptionHandlerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private LoanRepository loanRepository;

    @Test
    void handleBadRequest() throws Exception {

        final MockHttpServletRequestBuilder getRequest = get(String.format("/users/%s", "A"))
                .accept("application/json")
                .contentType("application/json");

        mockMvc.perform(getRequest).andExpect(status().isBadRequest());
    }

    @Test
    void catchAll() throws Exception {

        when(userRepository.existsById(anyLong())).thenReturn(true);
        doThrow(IllegalArgumentException.class).when(userRepository).deleteById(anyLong());

        final MockHttpServletRequestBuilder getRequest = delete(String.format("/users/%d", 1L ))
                .accept("application/json")
                .contentType("application/json");

        mockMvc.perform(getRequest).andExpect(status().isInternalServerError());
    }
}
