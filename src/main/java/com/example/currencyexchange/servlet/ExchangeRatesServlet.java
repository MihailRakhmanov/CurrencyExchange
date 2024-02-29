package com.example.currencyexchange.servlet;

import com.example.currencyexchange.model.Currency;
import com.example.currencyexchange.model.ExchangeRate;
import com.example.currencyexchange.repository.CurrencyRepository;
import com.example.currencyexchange.repository.ExchangeRateRepository;
import com.example.currencyexchange.util.DataValidator;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Optional;

@WebServlet(name = "ExchangeRatesServlet", value = "/exchangeRates")
public class ExchangeRatesServlet extends HttpServlet {
    private ExchangeRateRepository exchangeRateRepository;
    private CurrencyRepository currencyRepository;

    @Override
    public void init(ServletConfig config) throws ServletException {
        exchangeRateRepository = (ExchangeRateRepository) config.getServletContext().getAttribute("exchangeRateRepository");
        currencyRepository = (CurrencyRepository) config.getServletContext().getAttribute("currencyRepository");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        new ObjectMapper().writeValue(resp.getWriter(), exchangeRateRepository.findAll());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String baseCurrencyCode = req.getParameter("baseCurrencyCode");
        String targetCurrencyCode = req.getParameter("targetCurrencyCode");
        String rate = req.getParameter("rate");

        Optional<Currency> baseCurrency = currencyRepository.findByCode(baseCurrencyCode);
        Optional<Currency> targetCurrency = currencyRepository.findByCode(targetCurrencyCode);

        if (!baseCurrency.isPresent() || !targetCurrency.isPresent()) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Одна (или обе) валюта из валютной пары не существует в БД");
            return;
        }

        if(!DataValidator.isStringDouble(rate)){
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Не правильно введен курс обмена. Пример: rate = '1.05'");
        }

        ExchangeRate exchangeRate = new ExchangeRate(baseCurrency.get(), targetCurrency.get(), new BigDecimal(rate));

        if (DataValidator.isNotValidExchangeRateArgs(exchangeRate)) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Не правильно введены данные. Пример: baseCurrency = 'USD', targetCurrency = 'EUR', rate = '1.05'");
        }

        if (exchangeRateRepository.findByCurrenciesId(baseCurrency.get().getId(), targetCurrency.get().getId()).isPresent()) {
            resp.sendError(HttpServletResponse.SC_CONFLICT, "Такая валютная пара уже существует");
        }


        exchangeRateRepository.save(exchangeRate);

        Optional<ExchangeRate> EToResponse = currencyRepository.findByCode(code);
        if (currencyToResponse.isPresent()){
            resp.getWriter().write(new ObjectMapper().writeValueAsString(currencyToResponse.get()));
        }
    }
}
