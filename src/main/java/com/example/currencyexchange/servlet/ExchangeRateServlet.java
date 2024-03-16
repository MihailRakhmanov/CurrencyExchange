package com.example.currencyexchange.servlet;

import com.example.currencyexchange.model.ExchangeRate;
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

@WebServlet(name = "ExchangeRateServlet", value = "/exchangeRate/*")
public class ExchangeRateServlet extends HttpServlet {
    private ExchangeRateRepository exchangeRateRepository;

    @Override
    public void init(ServletConfig config) {
        exchangeRateRepository = (ExchangeRateRepository) config.getServletContext().getAttribute("exchangeRateRepository");
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getMethod().equals("PATCH"))
            doPatch(req, resp);
        else
            super.service(req, resp);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        if (req.getPathInfo() == null || req.getPathInfo().equals("/")){
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Коды валют пары отсутствуют в адресе. Пример: .../exchangeRate/USDRUB");
            return;
        }

        String currenciesCodes = req.getPathInfo().replaceFirst("/", "").toUpperCase();

        if (currenciesCodes.length() != 6) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Указана не корректная пара валют. Пример: .../exchangeRate/USDRUB");
            return;
        }

        Optional<ExchangeRate> exchangeRate = exchangeRateRepository.findByCurrenciesCodes(
                currenciesCodes.substring(0, 3), currenciesCodes.substring(3, 6));

        if (exchangeRate.isEmpty()) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Обменный курс для пары не найден");
            return;
        }

        resp.getWriter().write(new ObjectMapper().writeValueAsString(exchangeRate.get()));
    }

    protected void doPatch(HttpServletRequest req, HttpServletResponse resp) throws IOException{
        //String rate = req.getParameter("rate"); - Не работает для patch
        String parameter = req.getReader().readLine();
        String rate = parameter.replace("rate=", "");

        if (rate == null || rate.isEmpty()) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Отсутствует обменный курс");
            return;
        }

        if (!DataValidator.isStringDouble(rate)) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Введите значение. Пример: 1.05 " + rate);
            return;
        }

        String currenciesCodes = req.getPathInfo().replaceFirst("/", "").toUpperCase();

        Optional<ExchangeRate> exchangeRate = exchangeRateRepository.findByCurrenciesCodes(
                currenciesCodes.substring(0, 3), currenciesCodes.substring(3, 6));

        if (exchangeRate.isPresent()) {
            exchangeRate.get().setRate(BigDecimal.valueOf(Double.parseDouble(rate)));
            exchangeRateRepository.update(exchangeRate.get());
        }

        resp.getWriter().write(new ObjectMapper().writeValueAsString(exchangeRate.get()));
    }
}
