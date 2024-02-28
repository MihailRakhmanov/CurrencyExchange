package com.example.currencyexchange.util;

import com.example.currencyexchange.model.Currency;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import java.util.Set;

public class DataValidator {
    static ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    static Validator validator = factory.getValidator();

    public static boolean isNotValidCurrenciesArgs(Currency currency){
        Set<ConstraintViolation<Currency>> constraintViolations = validator.validate(currency);

        return constraintViolations.size() > 0;
    }
}
