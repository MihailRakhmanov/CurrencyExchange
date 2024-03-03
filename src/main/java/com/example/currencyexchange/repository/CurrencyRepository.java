package com.example.currencyexchange.repository;

import com.example.currencyexchange.model.Currency;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CurrencyRepository implements CrudRepository<Currency>{
    private final DataSource dataSource;

    public CurrencyRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    private Currency mappedCurrency(ResultSet resultSet){
        try {
            return new Currency(
                    resultSet.getInt("id"),
                    resultSet.getString("code"),
                    resultSet.getString("fullName"),
                    resultSet.getString("sign"));
        } catch (SQLException e) {
            return null;
        }
    }


    @Override
    public Optional<Currency> findById(Integer id) {
        final String query = "SELECT id, code, fullName, sign FROM Currencies WHERE id=?";
        Currency currency = null;

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {


            statement.setInt(1, id);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                currency = mappedCurrency(resultSet);
            }


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.ofNullable(currency);

    }

    @Override
    public List<Currency> findAll() {
        final String query = "SELECT id, code, fullName, sign FROM Currencies";

        List<Currency> list = new ArrayList<>();

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)){

            //statement.execute();
            //ResultSet resultSet = statement.getResultSet();

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                list.add(mappedCurrency(resultSet));
            }

            return list;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Optional<Currency> findByCode(String code){
        final String query = "SELECT * FROM Currencies WHERE code = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, code);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(mapRow(resultSet));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }

    @Override
    public void save(Currency entity) {
        final String query = "INSERT INTO Currencies (code, fullName, sign) VALUES (?, ?, ?)";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, entity.getCode());
            statement.setString(2, entity.getFullName());
            statement.setString(3, entity.getSign());
            statement.execute();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(Currency entity) {

    }

    @Override
    public void delete(Integer id) {

    }

    private Currency mapRow(ResultSet resultSet) throws SQLException {
        return new Currency(
                resultSet.getInt("id"),
                resultSet.getString("code"),
                resultSet.getString("fullName"),
                resultSet.getString("sign"));
    }
}
