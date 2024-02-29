package com.example.currencyexchange.util;

import com.example.currencyexchange.model.Currency;
import com.example.currencyexchange.model.ExchangeRate;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import java.util.Set;

public class DataValidator {
    static ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    static Validator validator = factory.getValidator();

    public static boolean isNotValidCurrencyArgs(Currency currency){
        Set<ConstraintViolation<Currency>> constraintViolations = validator.validate(currency);

        return constraintViolations.size() > 0;
    }

    public static boolean isNotValidExchangeRateArgs(ExchangeRate exchangeRate){
        Set<ConstraintViolation<ExchangeRate>> constraintViolations = validator.validate(exchangeRate);

        return constraintViolations.size() > 0;
    }

    public static boolean isStringDouble(String d) {
        try {
            Double.parseDouble(d);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
