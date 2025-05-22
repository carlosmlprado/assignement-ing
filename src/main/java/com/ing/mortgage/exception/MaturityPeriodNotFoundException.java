package com.ing.mortgage.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class MaturityPeriodNotFoundException extends RuntimeException {

    public MaturityPeriodNotFoundException() {
        super();
    }

}
