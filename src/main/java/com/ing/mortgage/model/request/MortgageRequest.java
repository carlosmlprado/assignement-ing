package com.ing.mortgage.model.request;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record MortgageRequest(@NotNull BigDecimal income, @NotNull Integer maturityPeriod,
                              @NotNull BigDecimal loanValue, @NotNull BigDecimal homeValue) {
}
