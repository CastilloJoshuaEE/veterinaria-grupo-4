package com.mycompany.veterinaria.grupo4.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;
import com.mycompany.veterinaria.grupo4.util.DatabaseConnection;

/**
 * Configuracion de la base de datos para la aplicacion Spring Boot.
 * <p>
 * Define los beans necesarios para la conexion y gestion de la base de datos,
 * permitiendo la inyeccion de dependencias en toda la aplicacion.
 * </p>
 * 
 * <p><b>Fecha de inicio del proyecto:</b> 15/04/2026</p>
 * 
 * @author ROBLES MORALES JUAN ANDRES – MODULO: ATENCION VETERINARIA
 * @version 1.0
 * @since 1.0
 */
@Configuration
public class DatabaseConfig {
    
    /**
     * Crea y configura el bean de conexion a la base de datos.
     * 
     * @return instancia de DatabaseConnection lista para ser inyectada
     */
    @Bean
    public DatabaseConnection databaseConnection() {
        return new DatabaseConnection();
    }
}