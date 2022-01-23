package com.example.test3;

import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.List;

@RestController
@Slf4j
public class ApiController {

    private final ApiService apiService;

    public ApiController(ApiService apiService) {
        this.apiService = apiService;
    }

    @GetMapping(value = "exchange_codes")
    public Mono<ApiService.ExchangeCodes> getSymbols() {
        return apiService.getExchangeSymbols();
    }

    @GetMapping(value = "rates")
    public Mono<ApiService.Rates> getRates(@RequestParam(defaultValue = "") List<String> exchangeCodes) {
        return apiService.getExchangeRates(exchangeCodes);
    }

    @GetMapping(value = "historical_rates")
    public Mono<ApiService.Rates> getHistoricalRates(@RequestParam
                                                     @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
                                                             LocalDate date,
                                                     @RequestParam(defaultValue = "")
                                                             List<String> exchangeCodes)
    {
        return apiService.getHistoricalExchangeRates(date, exchangeCodes);
    }
}