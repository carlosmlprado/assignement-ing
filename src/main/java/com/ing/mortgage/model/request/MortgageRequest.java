package com.ing.mortgage.model.request;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record MortgageRequest(BigDecimal income, Integer maturityPeriod,
                              BigDecimal loanValue, BigDecimal homeValue) {
}
