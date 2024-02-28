package com.example.currencyexchange.servlet;

import com.example.currencyexchange.model.Currency;
import com.example.currencyexchange.repository.CurrencyRepository;
import com.example.currencyexchange.util.DataValidator;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Optional;

@WebServlet(name = "CurrenciesServlet", value = "/currencies")
public class CurrenciesServlet extends HttpServlet {
    private CurrencyRepository currencyRepository;

    @Override
    public void init(ServletConfig config) throws ServletException {
        currencyRepository = (CurrencyRepository) config.getServletContext().getAttribute("currencyRepository");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.writeValue(resp.getWriter(), currencyRepository.findAll());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String code = req.getParameter("code");
        String name = req.getParameter("name");
        String sign = req.getParameter("sign");

        Currency currency = new Currency(code, name, sign);

        if (DataValidator.isNotValidCurrenciesArgs(currency)) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Не правильно введены данные. Пример: code = 'USD', name = 'US Dollar', sign = '$'");
        }

        if (currencyRepository.findByCode(code).isPresent()) {
            resp.sendError(HttpServletResponse.SC_CONFLICT, "Валюта с таким кодом уже существует");

        }

        currencyRepository.save(currency);

        Optional<Currency> currencyToResponse = currencyRepository.findByCode(code);
        if (currencyToResponse.isPresent()){
            resp.getWriter().write(new ObjectMapper().writeValueAsString(currencyToResponse.get()));
        }

    }
}
