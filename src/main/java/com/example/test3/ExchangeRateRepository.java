package com.example.test3;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

interface ExchangeRateRepository extends ReactiveCrudRepository<ExchangeRateEntity, Long> {
}
