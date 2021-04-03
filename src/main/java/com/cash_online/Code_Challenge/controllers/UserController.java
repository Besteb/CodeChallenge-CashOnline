package com.cash_online.Code_Challenge.controllers;

import com.cash_online.Code_Challenge.exceptionhandling.exceptions.LoanNotFoundException;
import com.cash_online.Code_Challenge.exceptionhandling.exceptions.UserAlreadyExistsException;
import com.cash_online.Code_Challenge.exceptionhandling.exceptions.UserNotFoundException;
import com.cash_online.Code_Challenge.models.Loan;
import com.cash_online.Code_Challenge.models.User;
import com.cash_online.Code_Challenge.models.UserRequest;
import com.cash_online.Code_Challenge.repositories.LoanRepository;
import com.cash_online.Code_Challenge.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RequestMapping("/users")
@RestController
public class UserController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LoanRepository loanRepository;

    @PostMapping
    public User createUser(@RequestBody UserRequest userRequest) throws UserAlreadyExistsException {
        if (userRepository.existsUserByEmail(userRequest.getEmail()))
            throw new UserAlreadyExistsException(
                    String.format(
                            "CCA-Add-User-Exception: User with email %s already exists.", userRequest.getEmail()
                    )
            );
        return userRepository.save(
                User.builder()
                        .email(userRequest.getEmail())
                        .firstName(userRequest.getFirstName())
                        .lastName(userRequest.getLastName())
                        .build()
        );
    }

    @GetMapping("/{id}")
    public User returnUser(@PathVariable Long id) throws UserNotFoundException {
        return userRepository
                .findById(id)
                .orElseThrow(
                        () ->
                                new UserNotFoundException(
                                        String.format(
                                                "CCA-Get-User-Exception: User not found with id : %s", id.toString()
                                        )
                                )
                );
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) throws UserNotFoundException {
        if (!userRepository.existsById(id))
            throw new UserNotFoundException(
                    String.format("CCA-Delete-User-Exception: No user with id %s exists.", id));
        userRepository.deleteById(id);
    }

    @DeleteMapping("/{id}/loans")
    public void deleteLoansByUserId(@PathVariable Long id) throws UserNotFoundException, LoanNotFoundException {
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(
                String.format("CCA-Delete-Loans-By-User-Id-Exception:  User not found with id : %s.", id)));

        if (user.getLoans() == null || user.getLoans().equals(Collections.emptyList()))
            throw new LoanNotFoundException(
                    String.format(
                            "CCA-Delete-Loans-By-User-Id-Exception: User with id : %d has no loans assigned.", id));

        loanRepository.deleteAll(user.getLoans());
    }
}
