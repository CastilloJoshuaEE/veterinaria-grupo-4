package com.mycompany.veterinaria.grupo4.util;

import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class DatabaseConnection {
    // server=DESKTOP-B0CFGB3_sql_server; database=DB_VidaAnimal;
    // CASTILLO: DESKTOP-B0CFGB3
private static final String URL =
"jdbc:sqlserver://TU_SERVIDOR:1433;databaseName=DB_VidaAnimal;encrypt=true;trustServerCertificate=true;";    
    // No se necesita usuario/contraseña con autenticación integrada
    private static final String USER = "veterinaria_user";
    private static final String PASSWORD = "123456";

    private static Connection connection = null;

    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            try {
                Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
                // Con autenticación integrada, no se pasan usuario/contraseña
connection = DriverManager.getConnection(URL, USER, PASSWORD);
            } catch (ClassNotFoundException e) {
                throw new SQLException("Driver no encontrado", e);
            }
        }
        return connection;
    }

    public static void closeConnection() {
        if (connection != null) {
            try {
                if (!connection.isClosed()) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}