package com.example.carpool.service;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DatabaseConnectionService {

    @Autowired
    private DataSource dataSource;

    public boolean isDatabaseConnected() {
        try (Connection connection = dataSource.getConnection()) {
            return connection.isValid(2); // Timeout in seconds
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
