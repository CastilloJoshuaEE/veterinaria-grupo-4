package com.mycompany.veterinaria.grupo4.util;

import java.sql.*;
import java.util.List;

/**
 * Clase utilitaria para la ejecucion de procedimientos almacenados.
 * <p>
 * Proporciona metodos estaticos para ejecutar procedimientos almacenados
 * que retornan datos (SELECT) o que realizan operaciones de manipulacion
 * (INSERT, UPDATE, DELETE) en la base de datos.
 * Soporta tanto SQL Server como MySQL.
 * </p>
 * 
 * <p><b>Fecha de inicio del proyecto:</b> 15/04/2026</p>
 * 
 * @author ROBLES MORALES JUAN ANDRES – MODULO: ATENCION VETERINARIA
 * @version 2.0 (Soporta SQL Server y MySQL)
 * @since 1.0
 */
public class DatabaseUtil {

    /**
     * Obtiene la sintaxis correcta para llamar a un procedimiento almacenado.
     * SQL Server usa {call nombreSP(?)} mientras que MySQL usa CALL nombreSP(?)
     *
     * @param nombreSP nombre del procedimiento almacenado
     * @param numParams numero de parametros
     * @return sintaxis correcta para llamar al SP
     */
    private static String getCallSyntax(String nombreSP, int numParams) {
        if (DatabaseConnection.isMySQL()) {
            StringBuilder sb = new StringBuilder("CALL ");
            sb.append(nombreSP);
            sb.append("(");
            if (numParams > 0) {
                sb.append("?,".repeat(Math.max(0, numParams)));
                sb.setLength(sb.length() - 1);
            }
            sb.append(")");
            return sb.toString();
        } else {
            StringBuilder sb = new StringBuilder("{call ");
            sb.append(nombreSP);
            sb.append("(");
            if (numParams > 0) {
                sb.append("?,".repeat(Math.max(0, numParams)));
                sb.setLength(sb.length() - 1);
            }
            sb.append(")}");
            return sb.toString();
        }
    }

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
            int numParams = (parametros != null) ? parametros.size() : 0;
            String sql = getCallSyntax(nombreSP, numParams);
            
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
            String sql = getCallSyntax(nombreSP, (parametros != null) ? parametros.size() : 0);
            stmt = conn.prepareCall(sql);
            
            if (parametros != null) {
                for (Parametro p : parametros) {
                    stmt.setObject(p.getNombre(), p.getValor(), p.getTipoDato());
                }
            }
            
            // Para SQL Server, manejar ReturnVal
            if (DatabaseConnection.isSQLServer()) {
                stmt.registerOutParameter("ReturnVal", Types.INTEGER);
                stmt.execute();
                int returnValue = stmt.getInt("ReturnVal");
                return returnValue == 1;
            } else {
                // MySQL no usa ReturnVal, ejecuta directamente
                return stmt.execute();
            }
            
        } catch (SQLException e) {
            System.err.println("Error en ejecutarSPNonQuery: " + e.getMessage());
            return false;
        } finally {
            try {
                if (stmt != null) stmt.close();
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
        int numParams = (parametros != null) ? parametros.size() : 0;
        String sql = getCallSyntax(nombreSP, numParams);
        CallableStatement stmt = conn.prepareCall(sql);
        
        if (parametros != null) {
            for (Parametro p : parametros) {
                stmt.setObject(p.getNombre(), p.getValor(), p.getTipoDato());
            }
        }
        
        return stmt.executeQuery();
    }
}