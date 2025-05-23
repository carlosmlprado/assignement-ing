package com.ing.mortgage;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ing.mortgage.model.request.MortgageRequest;
import com.ing.mortgage.model.response.MortgageCheckResponse;
import com.ing.mortgage.model.response.MortgageRateResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class MortgageControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldGetAllMortgageRates() throws Exception {
        MvcResult result = mockMvc.perform(get("/api/interest-rates"))
                .andExpect(status().isOk())
                .andReturn();

        List<MortgageRateResponse> rates = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                objectMapper.getTypeFactory().constructCollectionType(List.class, MortgageRateResponse.class)
        );

        assertFalse(rates.isEmpty());
        assertEquals(10, rates.size());
        assertTrue(rates.stream().anyMatch(rate -> rate.maturityPeriod() == 20));
    }

    @Test
    void shouldCalculateMortgageFor5YearFixedRate() throws Exception {
        MortgageRequest request = MortgageRequest.builder()
                .income(new BigDecimal("80000"))
                .maturityPeriod(5)
                .loanValue(new BigDecimal("320000.00"))
                .homeValue(new BigDecimal("320000.00"))
                .build();

        MvcResult result = mockMvc.perform(post("/api/mortgage-check")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn();

        MortgageCheckResponse response = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                MortgageCheckResponse.class
        );

        assertTrue(response.feasible());
        assertNotNull(response.monthlyCosts());
        assertEquals(BigDecimal.valueOf(1428.03), response.monthlyCosts());
    }

    @Test
    void shouldCalculateMortgageFor20YearFixedRate() throws Exception {
        MortgageRequest request = MortgageRequest.builder()
                .income(new BigDecimal("80000"))
                .maturityPeriod(20)
                .loanValue(new BigDecimal("320000.00"))
                .homeValue(new BigDecimal("320000.00"))
                .build();

        MvcResult result = mockMvc.perform(post("/api/mortgage-check")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn();

        MortgageCheckResponse response = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                MortgageCheckResponse.class
        );

        assertTrue(response.feasible());
        assertNotNull(response.monthlyCosts());
        assertEquals(BigDecimal.valueOf(1579.83), response.monthlyCosts());
    }

    @Test
    void shouldRejectLoanHigherThanHouseValue() throws Exception {
        MortgageRequest request = MortgageRequest.builder()
                .income(new BigDecimal("90000"))
                .maturityPeriod(5)
                .loanValue(new BigDecimal("350000.00"))
                .homeValue(new BigDecimal("3000.00"))
                .build();

        mockMvc.perform(post("/api/mortgage-check")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Loan higher than house value!"));
    }

    @Test
    void shouldRejectLoanHigherThanIncomeMultipliedBy4() throws Exception {
        MortgageRequest request = MortgageRequest.builder()
                .income(new BigDecimal("50000"))
                .maturityPeriod(5)
                .loanValue(new BigDecimal("250000.00"))
                .homeValue(new BigDecimal("320000.00"))
                .build();

        mockMvc.perform(post("/api/mortgage-check")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Low income for this loan!"));
    }

    @Test
    void shouldRejectInvalidMaturityPeriod() throws Exception {
        MortgageRequest request = MortgageRequest.builder()
                .income(new BigDecimal("80000"))
                .maturityPeriod(26)
                .loanValue(new BigDecimal("320000.00"))
                .homeValue(new BigDecimal("320000.00"))
                .build();

        mockMvc.perform(post("/api/mortgage-check")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Maturity period not found!"));
    }

    @Test
    void shouldThrowBadRequestWhenIncomeIsMissing() throws Exception {
        MortgageRequest request = MortgageRequest.builder()
                .maturityPeriod(26)
                .loanValue(new BigDecimal("320000.00"))
                .homeValue(new BigDecimal("320000.00"))
                .build();

        mockMvc.perform(post("/api/mortgage-check")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldThrowBadRequestWhenLoanValueIsMissing() throws Exception {
        MortgageRequest request = MortgageRequest.builder()
                .maturityPeriod(26)
                .income(new BigDecimal("80000"))
                .homeValue(new BigDecimal("320000.00"))
                .build();

        mockMvc.perform(post("/api/mortgage-check")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldThrowBadRequestWhenHomeValueIsMissing() throws Exception {
        MortgageRequest request = MortgageRequest.builder()
                .maturityPeriod(26)
                .loanValue(new BigDecimal("320000.00"))
                .income(new BigDecimal("80000"))
                .build();

        mockMvc.perform(post("/api/mortgage-check")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldThrowBadRequestWhenMaturityPeriodIsMissing() throws Exception {
        MortgageRequest request = MortgageRequest.builder()
                .income(new BigDecimal("80000"))
                .loanValue(new BigDecimal("320000.00"))
                .homeValue(new BigDecimal("320000.00"))
                .build();

        mockMvc.perform(post("/api/mortgage-check")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldThrowBadRequestWhenNegativeIncome() throws Exception {
        MortgageRequest request = MortgageRequest.builder()
                .maturityPeriod(5)
                .income(new BigDecimal("-80000"))
                .loanValue(new BigDecimal("320000.00"))
                .homeValue(new BigDecimal("320000.00"))
                .build();

        mockMvc.perform(post("/api/mortgage-check")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldThrowBadRequestWhenNegativeLoanValue() throws Exception {
        MortgageRequest request = MortgageRequest.builder()
                .maturityPeriod(5)
                .income(new BigDecimal("80000"))
                .loanValue(new BigDecimal("-320000.00"))
                .homeValue(new BigDecimal("320000.00"))
                .build();

        mockMvc.perform(post("/api/mortgage-check")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldThrowBadRequestWhenNegativeHomeValue() throws Exception {
        MortgageRequest request = MortgageRequest.builder()
                .income(new BigDecimal("80000"))
                .loanValue(new BigDecimal("320000.00"))
                .homeValue(new BigDecimal("-320000.00"))
                .build();

        mockMvc.perform(post("/api/mortgage-check")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
} 