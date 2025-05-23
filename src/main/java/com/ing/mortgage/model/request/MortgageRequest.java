package com.ing.mortgage.model.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record MortgageRequest(
    @NotNull @Positive BigDecimal income,
    @NotNull Integer maturityPeriod,
    @NotNull @Positive BigDecimal loanValue,
    @NotNull @Positive BigDecimal homeValue
) {}
