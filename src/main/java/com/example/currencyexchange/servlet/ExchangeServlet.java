package com.example.currencyexchange.servlet;

import com.example.currencyexchange.dto.ExchangeDTO;
import com.example.currencyexchange.model.Currency;
import com.example.currencyexchange.repository.CurrencyRepository;
import com.example.currencyexchange.repository.ExchangeRateRepository;
import com.example.currencyexchange.service.ExchangeService;
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

@WebServlet(name = "ExchangeServlet", value = "/exchange")
public class ExchangeServlet extends HttpServlet {
    private CurrencyRepository currencyRepository;
    private ExchangeRateRepository exchangeRatesRepository;
    private ExchangeService exchangeService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        currencyRepository = (CurrencyRepository) config.getServletContext().getAttribute("currencyRepository");
        exchangeRatesRepository = (ExchangeRateRepository) config.getServletContext().getAttribute("exchangeRatesRepository");
        exchangeService = new ExchangeService(exchangeRatesRepository);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String from = req.getParameter("from");
        String to = req.getParameter("to");
        String amount = req.getParameter("amount");

        Optional<Currency> fromCurrency = currencyRepository.findByCode(from);
        Optional<Currency> toCurrency = currencyRepository.findByCode(to);

        if (!(fromCurrency.isPresent() && toCurrency.isPresent())){
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Указана не существующая валюта. Пример: /exchange?from=USD&to=RUB&amount=10");
        }

        if (!DataValidator.isStringDouble(amount)){
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Неправильно введен запрос. Пример: /exchange?from=USD&to=RUB&amount=10");
        }

        BigDecimal rate = exchangeService.getRate(from, to);

        if (rate == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Не существует курс обмена");
        }

        new ObjectMapper().writeValue(resp.getWriter(), new ExchangeDTO(
                fromCurrency.get(),
                toCurrency.get(),
                rate,
                new BigDecimal(amount),
                rate.multiply(new BigDecimal(amount))
        ));
    }
}
