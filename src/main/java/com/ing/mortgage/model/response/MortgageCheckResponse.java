package com.ing.mortgage.model.response;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record MortgageCheckResponse(boolean feasible, BigDecimal monthlyCosts) {
}
