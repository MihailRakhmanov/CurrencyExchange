package com.example.currencyexchange.listener;

import com.example.currencyexchange.repository.CurrencyRepository;
import com.example.currencyexchange.repository.ExchangeRateRepository;
import com.example.currencyexchange.util.DBConnection;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

import javax.sql.DataSource;
import java.sql.SQLException;

@WebListener
public class ContextListener implements ServletContextListener {
    private DataSource dataSource;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext context = sce.getServletContext();

        try {
            dataSource = DBConnection.getDataSource();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        CurrencyRepository currencyRepository = new CurrencyRepository(dataSource);
        ExchangeRateRepository exchangeRateRepository = new ExchangeRateRepository(dataSource, currencyRepository);

        context.setAttribute("currencyRepository", currencyRepository);
        context.setAttribute("exchangeRateRepository", exchangeRateRepository);

    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        ServletContextListener.super.contextDestroyed(sce);
    }

// Вариант подключения к БД без Hikari
/*    private DataSource makeDataSource() throws SQLException {
        SQLiteDataSource sqliteDataSource = new SQLiteDataSource();
        sqliteDataSource.setUrl("jdbc:sqlite::resource:currency");
        DataSource dataSource;

        dataSource = sqliteDataSource;


        return dataSource;
    }*/

}
