package com.mycompany.veterinaria.grupo4.util;

import java.sql.*;
import java.util.List;

/**
 * Clase utilitaria para la ejecucion de procedimientos almacenados.
 * <p>
 * Proporciona metodos estaticos para ejecutar procedimientos almacenados
 * que retornan datos (SELECT) o que realizan operaciones de manipulacion
 * (INSERT, UPDATE, DELETE) en la base de datos SQL Server.
 * </p>
 * 
 * <p><b>Fecha de inicio del proyecto:</b> 15/04/2026</p>
 * 
 * @author ROBLES MORALES JUAN ANDRES – MODULO: ATENCION VETERINARIA
 * @version 1.0
 * @since 1.0
 */
public class DatabaseUtil {

    /**
     * Ejecuta un procedimiento almacenado que retorna datos (SELECT).
     *
     * @param nombreSP nombre del procedimiento almacenado
     * @param parametros lista de parametros del SP
     * @return ResultSet con los datos obtenidos
     * @throws SQLException si ocurre un error en la ejecucion
     */
    public static ResultSet ejecutarSPQuery(String nombreSP, List<Parametro> parametros) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        
        try {
            conn = DatabaseConnection.getConnection();
            String sql = "{call " + nombreSP + "(";
            
            if (parametros != null && !parametros.isEmpty()) {
                sql += "?,".repeat(parametros.size());
                sql = sql.substring(0, sql.length() - 1);
            }
            sql += ")}";
            
            stmt = conn.prepareStatement(sql);
            
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
    }
    
    /**
     * Ejecuta un procedimiento almacenado que NO retorna datos (INSERT, UPDATE, DELETE).
     *
     * @param nombreSP nombre del procedimiento almacenado
     * @param parametros lista de parametros del SP
     * @return true si la ejecucion fue exitosa
     */
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
            
            stmt.registerOutParameter("ReturnVal", Types.INTEGER);
            
            stmt.execute();
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
    
    /**
     * Ejecuta un procedimiento almacenado que retorna un ResultSet con manejo de cierre.
     *
     * @param nombreSP nombre del procedimiento almacenado
     * @param parametros lista de parametros del SP
     * @return ResultSet con los datos obtenidos
     * @throws SQLException si ocurre un error en la ejecucion
     */
    public static ResultSet ejecutarSPQueryConCierre(String nombreSP, List<Parametro> parametros) throws SQLException {
        Connection conn = DatabaseConnection.getConnection();
        CallableStatement stmt = conn.prepareCall("{call " + nombreSP + "}");
        
        if (parametros != null) {
            for (Parametro p : parametros) {
                stmt.setObject(p.getNombre(), p.getValor(), p.getTipoDato());
            }
        }
        
        return stmt.executeQuery();
    }
}