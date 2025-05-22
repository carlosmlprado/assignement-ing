package com.ing.mortgage;

import com.ing.mortgage.model.request.MortgageRequest;
import com.ing.mortgage.model.response.MortgageCheckResponse;
import com.ing.mortgage.model.response.MortgageRateResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class MortgageController {

    private final MortgageService mortgageService;

    @Operation(summary = "Get all Mortgage Interest Rates")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Get all Mortgage Interest Rates"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error!")})
    @GetMapping("/interest-rates")
    public ResponseEntity<List<MortgageRateResponse>> getMortgageInterestRates() {
        return ResponseEntity.ok(mortgageService.getAllMortgagesRates());
    }

    @Operation(summary = "Create a Mortgage Check")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Mortgage check Created!",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = MortgageCheckResponse.class))}),
            @ApiResponse(responseCode = "404", description = "Maturity Period Not Found!",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Annual income not enough for this loan or Loan Value is higher then House Value!",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Server Error!",
                    content = @Content)})
    @PostMapping("/mortgage-check")
    public ResponseEntity<MortgageCheckResponse> checkMortgagePossibility(@RequestBody MortgageRequest mortgageRequest) {
        var mortgageCheckResponse = mortgageService.checkMortgagePossibility(mortgageRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(mortgageCheckResponse);
    }
}
