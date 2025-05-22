package com.ing.mortgage;

import com.ing.mortgage.exception.LoanHigherThanHouseValueException;
import com.ing.mortgage.exception.LowIncomeForLoanException;
import com.ing.mortgage.exception.MaturityPeriodNotFoundException;
import com.ing.mortgage.model.request.MortgageRequest;
import com.ing.mortgage.model.response.MortgageCheckResponse;
import com.ing.mortgage.model.response.MortgageRateResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MortgageServiceTest {

    @Mock
    private MortgageDataInitializer mortgageDataInitializer;

    @InjectMocks
    private MortgageService mortgageService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldReturnAllMortgageRates() {
        MortgageRateResponse rate = MortgageRateResponse.builder()
                .maturityPeriod(20)
                .interestRate(BigDecimal.valueOf(0.03))
                .lastUpdate(Instant.now())
                .build();

        when(mortgageDataInitializer.getInitializedMortgageData()).thenReturn(List.of(rate));

        List<MortgageRateResponse> rates = mortgageService.getAllMortgagesRates();

        assertEquals(1, rates.size());
        assertEquals(20, rates.getFirst().maturityPeriod());
    }

    @Test
    void shouldReturnFeasibleMortgage() {
        int maturity = 20;
        BigDecimal rate = BigDecimal.valueOf(0.03);
        MortgageRequest request = new MortgageRequest(
                new BigDecimal("50000"), maturity,
                new BigDecimal("200000"), new BigDecimal("200000")
        );

        MortgageRateResponse mortgageRate = MortgageRateResponse.builder()
                .maturityPeriod(maturity)
                .interestRate(rate)
                .lastUpdate(Instant.now())
                .build();

        when(mortgageDataInitializer.getInitializedMortgageData()).thenReturn(List.of(mortgageRate));

        MortgageCheckResponse response = mortgageService.checkMortgagePossibility(request);

        assertTrue(response.feasible());
        assertNotNull(response.monthlyCosts());
    }

    @Test
    void shouldThrowWhenLoanIsTooHighForIncome() {
        MortgageRequest request = new MortgageRequest(
                new BigDecimal("2000"), 20,
                new BigDecimal("100000"), new BigDecimal("110000")
        );

        assertThrows(LowIncomeForLoanException.class, () -> mortgageService.checkMortgagePossibility(request));
    }

    @Test
    void shouldThrowWhenLoanIsHigherThanHomeValue() {
        MortgageRequest request = new MortgageRequest(
                new BigDecimal("1000000"), 20,
                new BigDecimal("300000"), new BigDecimal("250000")
        );

        assertThrows(LoanHigherThanHouseValueException.class, () -> mortgageService.checkMortgagePossibility(request));
    }

    @Test
    void shouldThrowWhenMaturityPeriodIsNotFound() {
        int maturity = 25;
        MortgageRequest request = new MortgageRequest(
                new BigDecimal("1000000"), maturity,
                new BigDecimal("300000"), new BigDecimal("350000")
        );

        when(mortgageDataInitializer.getInitializedMortgageData()).thenReturn(List.of());

        assertThrows(MaturityPeriodNotFoundException.class, () -> mortgageService.checkMortgagePossibility(request));
    }
}