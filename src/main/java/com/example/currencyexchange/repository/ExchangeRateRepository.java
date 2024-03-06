package com.example.currencyexchange.repository;

import com.example.currencyexchange.model.ExchangeRate;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ExchangeRateRepository implements CrudRepository<ExchangeRate>{
    private final DataSource dataSource;
    private final CurrencyRepository currencyRepository;

    public ExchangeRateRepository(DataSource dataSource, CurrencyRepository currencyRepository) {
        this.dataSource = dataSource;
        this.currencyRepository = currencyRepository;
    }

    private ExchangeRate mappedExchangeRate(ResultSet resultSet){
        try {
            return new ExchangeRate(
                    resultSet.getInt("id"),
                    currencyRepository.findById(resultSet.getInt("baseCurrencyId")).get(),
                    currencyRepository.findById(resultSet.getInt("targetCurrencyId")).get(),
                    resultSet.getBigDecimal("rate"));
        } catch (SQLException e) {
            return null;
        }
    }

    @Override
    public Optional<ExchangeRate> findById(Integer id) {
        final String query = "SELECT id, baseCurrencyId, targetCurrencyId, rate FROM ExchangeRate WHERE id=?";

        ExchangeRate exchangeRate = null;
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {


            statement.setInt(1, id);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                exchangeRate = mappedExchangeRate(resultSet);
            }


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.ofNullable(exchangeRate);
    }

    @Override
    public List<ExchangeRate> findAll() {
        final String query = "SELECT id, baseCurrencyId, targetCurrencyId, rate FROM ExchangeRate";

        List<ExchangeRate> list = new ArrayList<>();

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)){

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                list.add(mappedExchangeRate(resultSet));
            }

            return list;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void save(ExchangeRate entity) {
        final String query = "INSERT INTO ExchangeRate (baseCurrencyId, targetCurrencyId, rate) VALUES (?, ?, ?)";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, entity.getBaseCurrency().getId());
            statement.setInt(2, entity.getTargetCurrency().getId());
            statement.setBigDecimal(3, entity.getRate());

            statement.execute();

        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }

    }

    @Override
    public void update(ExchangeRate entity) {
        final String query = "UPDATE ExchangeRate SET rate=? WHERE id=?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {


            statement.setBigDecimal(1, entity.getRate());
            statement.setInt(2, entity.getId());

            statement.execute();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(Integer id) {

    }

    public Optional<ExchangeRate> findByCurrenciesId(Integer baseCurrencyId, Integer targetCurrencyId) {
        ExchangeRate exchangeRate = null;

        final String query = "SELECT id, baseCurrencyId, targetCurrencyId, rate FROM ExchangeRate WHERE baseCurrencyId=? AND targetCurrencyId=?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, baseCurrencyId);
            statement.setInt(2, targetCurrencyId);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                exchangeRate = mappedExchangeRate(resultSet);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return Optional.ofNullable(exchangeRate);
    }

    public Optional<ExchangeRate> findByCurrenciesCodes(String baseCurrencyCode, String targetCurrencyCode){
        final String query = "SELECT id, baseCurrencyId, targetCurrencyId, rate FROM ExchangeRate WHERE baseCurrencyId=? AND targetCurrencyId=?";

        ExchangeRate exchangeRate = null;

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1,
                    currencyRepository.findByCode(baseCurrencyCode).get().getId());
            statement.setInt(2,
                    currencyRepository.findByCode(targetCurrencyCode).get().getId());

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                exchangeRate = mappedExchangeRate(resultSet);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return Optional.ofNullable(exchangeRate);
    }

}
