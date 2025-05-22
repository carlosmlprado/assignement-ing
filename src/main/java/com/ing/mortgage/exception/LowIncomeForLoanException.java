package com.ing.mortgage.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class LowIncomeForLoanException extends RuntimeException {

    public LowIncomeForLoanException() {
        super();
    }
}
