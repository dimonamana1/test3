package com.example.test3;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@NoArgsConstructor
@Table
class ExchangeRateEntity {
    @Id
    private Long id;

    private Long timestamp;

    private String exchangeCode;

    private Double exchangeRate;

    public ExchangeRateEntity(Long timestamp, String exchangeCode, Double exchangeRate){
        this.timestamp = timestamp;
        this.exchangeCode = exchangeCode;
        this.exchangeRate = exchangeRate;
    }
}
