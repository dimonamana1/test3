package com.example.test3;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class ApiService {
    private static final String apiKey = "59ffef37af66b18acd1d8a3d9cbf2d07";
    private final WebClient webClient = WebClient.create("http://api.exchangeratesapi.io/v1");
    private final ExchangeRateService exchangeRateService;

    @Autowired
    public ApiService(ExchangeRateService exchangeRateService) {
        this.exchangeRateService = exchangeRateService;
    }

    public Mono<ExchangeCodes> getExchangeSymbols(){
        return webClient
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path("/symbols")
                        .queryParam("access_key", apiKey)
                        .build().normalize())
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError,
                          error -> Mono.error(new RuntimeException("API not found")))
                .onStatus(HttpStatus::is5xxServerError,
                          error -> Mono.error(new RuntimeException("Server is not responding")))
                .bodyToMono(SymbolsResponse.class)
                .map(symbolsResponse -> new ExchangeCodes(symbolsResponse.symbols.keySet().stream().toList()));
    }

    public Mono<Rates> getExchangeRates(List<String> exchangeCodes) {
        return webClient
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path("/latest")
                        .queryParam("access_key", apiKey)
                        .queryParam("symbols", String.join(",", exchangeCodes))
                        .build())
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError,
                          error -> Mono.error(new RuntimeException("API not found")))
                .onStatus(HttpStatus::is5xxServerError,
                          error -> Mono.error(new RuntimeException("Server is not responding")))
                .bodyToMono(RatesResponse.class)
                .flatMapMany(exchangeRateService::saveAll)
                .collectMap(ExchangeRateEntity::getExchangeCode, ExchangeRateEntity::getExchangeRate)
                .map(Rates::new);
    }

    public Mono<Rates> getHistoricalExchangeRates(LocalDate date, List<String> exchangeCodes) {
        return webClient
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path("/" + date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                        .queryParam("access_key", apiKey)
                        .queryParam("symbols", String.join(",", exchangeCodes))
                        .build())
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError,
                          error -> Mono.error(new RuntimeException("API not found")))
                .onStatus(HttpStatus::is5xxServerError,
                          error -> Mono.error(new RuntimeException("Server is not responding")))
                .bodyToMono(RatesResponse.class)
                .flatMapMany(exchangeRateService::saveAll)
                .collectMap(ExchangeRateEntity::getExchangeCode, ExchangeRateEntity::getExchangeRate)
                .map(Rates::new);
    }

    private record SymbolsResponse(Map<String, String> symbols) {
    }

    record RatesResponse(Long timestamp, Map<String, Double> rates) {
    }

    record Rates(Map<String, Double> rates) {
    }

    record ExchangeCodes(List<String> codes) {
    }
}

