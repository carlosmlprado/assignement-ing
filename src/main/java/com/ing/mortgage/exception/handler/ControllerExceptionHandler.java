package com.ing.mortgage.exception.handler;

import com.ing.mortgage.exception.LoanHigherThanHouseValueException;
import com.ing.mortgage.exception.LowIncomeForLoanException;
import com.ing.mortgage.exception.MaturityPeriodNotFoundException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ControllerExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {LoanHigherThanHouseValueException.class})
    protected ResponseEntity<Object> handleHigherLoanThanHouseValue(final RuntimeException ex, final WebRequest request) throws LoanHigherThanHouseValueException {
        final String bodyOfResponse = "Loan higher than house value!";
        return handleExceptionInternal(ex, bodyOfResponse, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(value = {LowIncomeForLoanException.class})
    protected ResponseEntity<Object> handleLowIncomeForLoan(final RuntimeException ex, final WebRequest request) throws LoanHigherThanHouseValueException {
        final String bodyOfResponse = "Low income for this loan!";
        return handleExceptionInternal(ex, bodyOfResponse, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(value = {MaturityPeriodNotFoundException.class})
    protected ResponseEntity<Object> maturityPeriodNotFound(final RuntimeException ex, final WebRequest request) throws LoanHigherThanHouseValueException {
        final String bodyOfResponse = "Maturity period not found!";
        return handleExceptionInternal(ex, bodyOfResponse, new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }
}
