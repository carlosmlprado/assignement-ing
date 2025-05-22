package com.ing.mortgage.model.response;

import lombok.Builder;

import java.math.BigDecimal;
import java.time.Instant;

@Builder
public record MortgageRateResponse(Integer maturityPeriod, BigDecimal interestRate, Instant lastUpdate) {
}
