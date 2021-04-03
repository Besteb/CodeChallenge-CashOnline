package com.cash_online.Code_Challenge.exceptionhandling.exceptions;

public class UserNotFoundException extends Exception {
    public UserNotFoundException(final String message) {
        super(message);
    }
}
