package com.example.news.api.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PostgresConfig {
    @Bean
    public Connection postgresConnection() {
        try{
            return DriverManager.getConnection(
                System.getenv("POSTGRES_URL"),
                System.getenv("POSTGRES_USER"),
                System.getenv("POSTGRES_PASSWORD")
            );
        }catch (SQLException e) {
            throw new IllegalStateException("Unable to create Postgres connection", e);
        }
    }
}
