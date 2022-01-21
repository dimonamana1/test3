package com.example.test3;

import reactor.core.publisher.Flux;

public interface ExchangeRateService {
    Flux<ExchangeRateEntity> saveAll(ApiService.Rates exchangeRate);
}
