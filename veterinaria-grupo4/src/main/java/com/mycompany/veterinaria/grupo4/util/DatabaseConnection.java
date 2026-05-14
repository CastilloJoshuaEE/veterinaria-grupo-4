package com.mycompany.veterinaria.grupo4.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Clase utilitaria para la gestion de la conexion a la base de datos SQL Server.
 * <p>
 * Proporciona metodos estaticos para obtener y cerrar la conexion a la base de datos
 * DB_VidaAnimal utilizando autenticacion integrada o credenciales especificas.
 * Implementa un patron Singleton para mantener una unica instancia de conexion.
 * </p>
 * 
 * <p><b>Fecha de inicio del proyecto:</b> 15/04/2026</p>
 * 
 * @author ROBLES MORALES JUAN ANDRES – MODULO: ATENCION VETERINARIA
 * @version 1.0
 * @since 1.0
 */
public class DatabaseConnection {

    private static final String URL = "jdbc:sqlserver://localhost:1433;databaseName=db_veterinaria;encrypt=true;trustServerCertificate=true;";
    private static final String USER = "veterinaria_user";
    private static final String PASSWORD = "123456";
    private static Connection connection = null;

    /**
     * Obtiene la conexion activa a la base de datos.
     * Si no existe conexion o esta cerrada, crea una nueva.
     *
     * @return conexion activa a la base de datos
     * @throws SQLException si ocurre un error al conectar
     */
    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            try {
                Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
            } catch (ClassNotFoundException e) {
                throw new SQLException("Driver no encontrado", e);
            }
        }
        return connection;
    }

    /**
     * Cierra la conexion a la base de datos si esta abierta.
     */
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