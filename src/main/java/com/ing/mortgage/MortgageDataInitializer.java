package com.ing.mortgage;

import com.ing.mortgage.model.response.MortgageRateResponse;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Getter
@Slf4j
@Service
public class MortgageDataInitializer {

    private static final Instant LAST_UPDATE = Instant.parse("2025-05-22T00:00:00Z");
    private List<MortgageRateResponse> initializedMortgageData;

    @PostConstruct
    public void initialize() {
        log.info("Initializing mortgage rates data...");
        initializedMortgageData = List.of(
            MortgageRateResponse.builder()
                .maturityPeriod(1)
                .interestRate(BigDecimal.valueOf(0.0366))
                .lastUpdate(LAST_UPDATE)
                .build(),
            MortgageRateResponse.builder()
                .maturityPeriod(2)
                .interestRate(BigDecimal.valueOf(0.0359))
                .lastUpdate(LAST_UPDATE)
                .build(),
            MortgageRateResponse.builder()
                .maturityPeriod(3)
                .interestRate(BigDecimal.valueOf(0.0345))
                .lastUpdate(LAST_UPDATE)
                .build(),
            MortgageRateResponse.builder()
                .maturityPeriod(5)
                .interestRate(BigDecimal.valueOf(0.0345))
                .lastUpdate(LAST_UPDATE)
                .build(),
            MortgageRateResponse.builder()
                .maturityPeriod(6)
                .interestRate(BigDecimal.valueOf(0.0367))
                .lastUpdate(LAST_UPDATE)
                .build(),
            MortgageRateResponse.builder()
                .maturityPeriod(7)
                .interestRate(BigDecimal.valueOf(0.0368))
                .lastUpdate(LAST_UPDATE)
                .build(),
            MortgageRateResponse.builder()
                .maturityPeriod(10)
                .interestRate(BigDecimal.valueOf(0.0375))
                .lastUpdate(LAST_UPDATE)
                .build(),
            MortgageRateResponse.builder()
                .maturityPeriod(12)
                .interestRate(BigDecimal.valueOf(0.0411))
                .lastUpdate(LAST_UPDATE)
                .build(),
            MortgageRateResponse.builder()
                .maturityPeriod(15)
                .interestRate(BigDecimal.valueOf(0.0414))
                .lastUpdate(LAST_UPDATE)
                .build(),
            MortgageRateResponse.builder()
                .maturityPeriod(20)
                .interestRate(BigDecimal.valueOf(0.0428))
                .lastUpdate(LAST_UPDATE)
                .build()
        );
        log.info("Successfully initialized {} mortgage rates", initializedMortgageData.size());
    }
}