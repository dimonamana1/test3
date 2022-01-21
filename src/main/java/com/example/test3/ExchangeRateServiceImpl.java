package com.example.test3;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public class ExchangeRateServiceImpl implements ExchangeRateService {
    private final ExchangeRateRepository exchangeRateRepository;

    @Autowired
    public ExchangeRateServiceImpl(ExchangeRateRepository exchangeRateRepository) {
        this.exchangeRateRepository = exchangeRateRepository;
    }

    @Override
    public Flux<ExchangeRateEntity> saveAll(ApiService.Rates ratesToSave) {
        Long timestamp = ratesToSave.timestamp();
        return exchangeRateRepository.saveAll(ratesToSave
                                                      .rates()
                                                      .entrySet()
                                                      .stream()
                                                      .map(entry -> new ExchangeRateEntity(timestamp,
                                                                                           entry.getKey(),
                                                                                           entry.getValue())).toList());

    }
}
