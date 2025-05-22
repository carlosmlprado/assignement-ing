package com.ing.mortgage;

import com.ing.mortgage.exception.LoanHigherThanHouseValueException;
import com.ing.mortgage.exception.LowIncomeForLoanException;
import com.ing.mortgage.exception.MaturityPeriodNotFoundException;
import com.ing.mortgage.model.request.MortgageRequest;
import com.ing.mortgage.model.response.MortgageCheckResponse;
import com.ing.mortgage.model.response.MortgageRateResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MortgageService {

    private final MortgageDataInitializer mortgageDataInitializer;
    private static final BigDecimal ALLOWED_INCOME_MULTIPLIER = BigDecimal.valueOf(4);
    private static final int STANDARD_MORTGAGE_PERIOD_YEARS = 30;

    public List<MortgageRateResponse> getAllMortgagesRates() {
        log.info("[getAllMortgagesRates] Getting all Mortgages Rates");
        return mortgageDataInitializer.getInitializedMortgageData();
    }

    public MortgageCheckResponse checkMortgagePossibility(MortgageRequest mortgageRequest) {
        log.debug("[checkMortgagePossibility] Checking the possibility to get a mortgage for income: {}, maturityPeriod: {}, " +
                        "loanValue: {} and houseValue: {}", mortgageRequest.income(), mortgageRequest.maturityPeriod(),
                mortgageRequest.loanValue(), mortgageRequest.homeValue());

        checkMortgageRules(mortgageRequest);
        var monthlyCosts = calculateMonthlyMortgageCost(mortgageRequest.maturityPeriod(), mortgageRequest.loanValue());

        return MortgageCheckResponse.builder().feasible(true).monthlyCosts(monthlyCosts).build();
    }

    private void checkMortgageRules(MortgageRequest mortgageRequest) {
        var desiredLoanValue = mortgageRequest.loanValue();
        var income = mortgageRequest.income();
        var houseValue = mortgageRequest.homeValue();

        if (desiredLoanValue.compareTo(income.multiply(ALLOWED_INCOME_MULTIPLIER)) > 0) {
            throw new LowIncomeForLoanException();
        }

        if (desiredLoanValue.compareTo(houseValue) > 0) {
            throw new LoanHigherThanHouseValueException();
        }
    }

    private BigDecimal calculateMonthlyMortgageCost(Integer maturityPeriodYears, BigDecimal loanAmount) {
        log.info("[calculateMonthlyMortgageCost] Calculating monthly cost...");

        double monthlyRate = getMortgageInterestRate(maturityPeriodYears);
        int totalMonths = STANDARD_MORTGAGE_PERIOD_YEARS * 12;
        double doubleLoanAmount = loanAmount.doubleValue();

        double monthlyPayment = (doubleLoanAmount * monthlyRate * Math.pow(1 + monthlyRate, totalMonths)) /
                (Math.pow(1 + monthlyRate, totalMonths) - 1);

        return BigDecimal.valueOf(monthlyPayment).setScale(2, RoundingMode.HALF_EVEN);
    }

    private double getMortgageInterestRate(Integer maturityPeriod) {
        return mortgageDataInitializer.getInitializedMortgageData().stream()
                .filter(rate -> rate.maturityPeriod().equals(maturityPeriod))
                .findFirst()
                .map(rate -> rate.interestRate().divide(BigDecimal.valueOf(12), 10, RoundingMode.HALF_EVEN).doubleValue())
                .orElseThrow(MaturityPeriodNotFoundException::new);
    }
}