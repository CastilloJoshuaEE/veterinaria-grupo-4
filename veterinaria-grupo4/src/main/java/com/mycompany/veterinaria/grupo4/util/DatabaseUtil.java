/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.veterinaria.grupo4.util;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseUtil {

    // Ejecuta un SP que retorna datos (SELECT)
    public static ResultSet ejecutarSPQuery(String nombreSP, List<Parametro> parametros) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        
        try {
            conn = DatabaseConnection.getConnection();
            String sql = "{call " + nombreSP + "(";
            
            // Construir la llamada al SP con parámetros
            if (parametros != null && !parametros.isEmpty()) {
                sql += "?,".repeat(parametros.size());
                sql = sql.substring(0, sql.length() - 1);
            }
            sql += ")}";
            
            stmt = conn.prepareStatement(sql);
            
            // Asignar parámetros
            if (parametros != null) {
                for (int i = 0; i < parametros.size(); i++) {
                    Parametro p = parametros.get(i);
                    stmt.setObject(i + 1, p.getValor(), p.getTipoDato());
                }
            }
            
            return stmt.executeQuery();
            
        } catch (SQLException e) {
            throw e;
        }
        // Nota: No cierras aquí porque necesitas el ResultSet abierto.
        // El llamado debe cerrar ResultSet, Statement y Connection.
    }
    
    // Ejecuta un SP que NO retorna datos (INSERT, UPDATE, DELETE)
    public static boolean ejecutarSPNonQuery(String nombreSP, List<Parametro> parametros) {
        Connection conn = null;
        CallableStatement stmt = null;
        
        try {
            conn = DatabaseConnection.getConnection();
            stmt = conn.prepareCall("{call " + nombreSP + "}");
            
            if (parametros != null) {
                for (Parametro p : parametros) {
                    stmt.setObject(p.getNombre(), p.getValor(), p.getTipoDato());
                }
            }
            
            // Registrar parámetro de retorno si el SP devuelve algo
            stmt.registerOutParameter("ReturnVal", Types.INTEGER);
            
            boolean result = stmt.execute();
            int returnValue = stmt.getInt("ReturnVal");
            
            return returnValue == 1;
            
        } catch (SQLException e) {
            System.err.println("Error en ejecutarSPNonQuery: " + e.getMessage());
            return false;
        } finally {
            try {
                if (stmt != null) stmt.close();
                if (conn != null) DatabaseConnection.closeConnection();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    
    // Ejecuta un SP que retorna un ResultSet (con manejo de cierre)
    public static ResultSet ejecutarSPQueryConCierre(String nombreSP, List<Parametro> parametros) throws SQLException {
        Connection conn = DatabaseConnection.getConnection();
        CallableStatement stmt = conn.prepareCall("{call " + nombreSP + "}");
        
        if (parametros != null) {
            for (Parametro p : parametros) {
                stmt.setObject(p.getNombre(), p.getValor(), p.getTipoDato());
            }
        }
        
        return stmt.executeQuery();
        // El llamado debe cerrar ResultSet, Statement y Connection
    }
}