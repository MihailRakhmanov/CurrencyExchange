package com.example.currencyexchange.util;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;
import java.sql.SQLException;

public class DBConnection {

    private static final String DB_URL = "jdbc:sqlite::resource:currency";
    private static final String DRIVER = "org.sqlite.JDBC";
    private static HikariDataSource dataSource;

    static {
        try {
            HikariConfig config = new HikariConfig();
            config.setDriverClassName(DRIVER);
            config.setJdbcUrl(DB_URL);
            dataSource = new HikariDataSource(config);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static DataSource getDataSource() throws SQLException {
        return dataSource;
    }
}
