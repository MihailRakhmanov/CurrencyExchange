package com.example.currencyexchange.model;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
public class ExchangeRate {
    private Integer id;
    @NonNull
    private Currency baseCurrency;
    @NonNull
    private Currency targetCurrency;
    @NonNull
    private BigDecimal rate;
}
