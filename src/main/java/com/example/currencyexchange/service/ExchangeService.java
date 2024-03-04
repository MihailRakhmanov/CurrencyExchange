package com.example.currencyexchange.service;

import com.example.currencyexchange.model.ExchangeRate;
import com.example.currencyexchange.repository.ExchangeRateRepository;

import java.math.BigDecimal;
import java.util.Optional;

public class ExchangeService {
    private ExchangeRateRepository exchangeRateRepository;

    public ExchangeService(ExchangeRateRepository exchangeRateRepository) {
        this.exchangeRateRepository = exchangeRateRepository;
    }

    public BigDecimal getRate(String from, String to) {
        Optional<ExchangeRate> exchangeRate = exchangeRateRepository.findByCurrenciesCodes(from, to);
        BigDecimal rate = null;

        if (exchangeRate.isPresent())
            return exchangeRate.get().getRate();

        Optional<ExchangeRate> reverseExchangeRate = exchangeRateRepository.findByCurrenciesCodes(to, from);

        if (reverseExchangeRate.isPresent()) {
            BigDecimal one = new BigDecimal(1);
            rate = one.divide(reverseExchangeRate.get().getRate(), 2);
            return rate;
        }

        Optional<ExchangeRate> exchangeRateUSD_A = exchangeRateRepository.findByCurrenciesCodes("USD", from);
        Optional<ExchangeRate> exchangeRateUSD_B = exchangeRateRepository.findByCurrenciesCodes("USD", to);

        if (exchangeRateUSD_A.isPresent() && exchangeRateUSD_B.isPresent()) {
            return exchangeRateUSD_B.get().getRate().divide(exchangeRateUSD_A.get().getRate());
        }

        return null;
    }
}
