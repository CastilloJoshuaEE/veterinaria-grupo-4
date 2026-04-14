package com.mycompany.veterinaria.grupo4.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;
import com.mycompany.veterinaria.grupo4.util.DatabaseConnection;

@Configuration
public class DatabaseConfig {
    
    @Bean
    public DatabaseConnection databaseConnection() {
        return new DatabaseConnection();
    }
}