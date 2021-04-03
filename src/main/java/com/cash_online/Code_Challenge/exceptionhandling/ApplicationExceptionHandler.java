package com.cash_online.Code_Challenge.exceptionhandling;

import com.cash_online.Code_Challenge.exceptionhandling.exceptions.*;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice
@Slf4j
public class ApplicationExceptionHandler {

    private static final String BAD_REQUEST_CODE = "cca.bad-request";
    private static final String DEFAULT_CODE = "cca.default-error";

    @ExceptionHandler({PageNotFoundException.class, PageSizeException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponse> catchPageExceptions(final Exception exception) {
        log.error("CCA-Pagination-Error: There was an error with the page request.", exception);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(exception.getMessage()));
    }

    @ExceptionHandler(value = UserNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponse> catchUserNotFound(final UserNotFoundException exception) {
        log.error("CCA-User-Not-Found-Error: There was an error with the user request.", exception);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(exception.getMessage()));
    }

    @ExceptionHandler(value = UserAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public ResponseEntity<ErrorResponse> catchUserAlreadyExists(final UserAlreadyExistsException exception) {
        log.error("CCA-User-Already-Exists-Error: There was an error with the user request.", exception);
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                .body(new ErrorResponse(exception.getMessage()));
    }

    @ExceptionHandler(value = LoanNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponse> catchLoanNotFound(final LoanNotFoundException exception) {
        log.error("CCA-Loan-Not-Found-Error: There was an error with the loan request.", exception);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(exception.getMessage()));
    }

    /* These are the internal server errors that Spring's default exception handler handles out of the box */
    @ExceptionHandler({ConstraintViolationException.class,
            MissingServletRequestParameterException.class, MethodArgumentTypeMismatchException.class,
            HttpMessageNotReadableException.class, MethodArgumentNotValidException.class})
    public ResponseEntity<ErrorResponse> handleBadRequest(
            final Exception e) {
        log.error("CCA-Bad-Request: There was an error with the request.", e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(BAD_REQUEST_CODE));
    }

    @ExceptionHandler(value = Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ErrorResponse> catchAll(final Exception exception) {
        log.error("CCA-Internal-Server-Error: There was an uncaught exception.", exception);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse(DEFAULT_CODE));
    }
}
