package com.example.currencyexchange.repository;

import com.example.currencyexchange.model.ExchangeRate;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;

public class ExchangeRateRepository implements CrudRepository<ExchangeRate>{
    private final DataSource dataSource;

    public ExchangeRateRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Optional<ExchangeRate> findById(Integer id) {
        return Optional.empty();
    }

    @Override
    public List<ExchangeRate> findAll() {
        return null;
    }

    @Override
    public void save(ExchangeRate entity) {

    }

    @Override
    public void update(ExchangeRate entity) {

    }

    @Override
    public void delete(Integer id) {

    }
}
