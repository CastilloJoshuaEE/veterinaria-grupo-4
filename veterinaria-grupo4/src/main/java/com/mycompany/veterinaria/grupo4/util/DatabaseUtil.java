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
     * <p>
     * <b>Importante:</b> Para MySQL usa CALL, para SQL Server usa {call...}
     * </p>
     *
     * @param nombreSP nombre del procedimiento almacenado
     * @param parametros lista de parametros del SP
     * @return true si la ejecucion fue exitosa
     */
    public static boolean ejecutarSPNonQuery(String nombreSP, List<Parametro> parametros) {
        Connection conn = null;
        PreparedStatement stmt = null;
        boolean isMySQL = DatabaseConnection.isMySQL();
        
        try {
            conn = DatabaseConnection.getConnection();
            int numParams = (parametros != null) ? parametros.size() : 0;
            String sql = getCallSyntax(nombreSP, numParams);
            
            // Para MySQL usar PreparedStatement, para SQL Server usar CallableStatement
            if (isMySQL) {
                stmt = conn.prepareStatement(sql);
                
                if (parametros != null) {
                    for (int i = 0; i < parametros.size(); i++) {
                        Parametro p = parametros.get(i);
                        // Usar indice en lugar de nombre para MySQL
                        stmt.setObject(i + 1, p.getValor(), p.getTipoDato());
                    }
                }
                
                boolean resultado = stmt.execute();
                // Para MySQL, el SP fue exitoso si no hubo excepcion
                return true;
                
            } else {
                // SQL Server: usar CallableStatement
                CallableStatement cstmt = conn.prepareCall(sql);
                stmt = cstmt;
                
                if (parametros != null) {
                    for (Parametro p : parametros) {
                        // SQL Server puede usar nombre o indice
                        try {
                            cstmt.setObject(p.getNombre(), p.getValor(), p.getTipoDato());
                        } catch (SQLException e) {
                            // Si falla por nombre, usar indice
                            int index = parametros.indexOf(p) + 1;
                            cstmt.setObject(index, p.getValor(), p.getTipoDato());
                        }
                    }
                }
                
                cstmt.registerOutParameter("ReturnVal", Types.INTEGER);
                cstmt.execute();
                int returnValue = cstmt.getInt("ReturnVal");
                return returnValue == 1;
            }
            
        } catch (SQLException e) {
            System.err.println("Error en ejecutarSPNonQuery: " + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
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
        
        if (DatabaseConnection.isMySQL()) {
            PreparedStatement stmt = conn.prepareStatement(sql);
            if (parametros != null) {
                for (int i = 0; i < parametros.size(); i++) {
                    Parametro p = parametros.get(i);
                    stmt.setObject(i + 1, p.getValor(), p.getTipoDato());
                }
            }
            return stmt.executeQuery();
        } else {
            CallableStatement stmt = conn.prepareCall(sql);
            if (parametros != null) {
                for (Parametro p : parametros) {
                    try {
                        stmt.setObject(p.getNombre(), p.getValor(), p.getTipoDato());
                    } catch (SQLException e) {
                        int index = parametros.indexOf(p) + 1;
                        stmt.setObject(index, p.getValor(), p.getTipoDato());
                    }
                }
            }
            return stmt.executeQuery();
        }
    }
    
    /**
     * Version simplificada para ejecutar SP sin parametros de retorno complejos.
     * Recomendado para operaciones CRUD basicas.
     *
     * @param nombreSP nombre del procedimiento almacenado
     * @param parametros lista de parametros del SP
     * @return numero de filas afectadas o -1 si hay error
     */
    public static int ejecutarSPUpdate(String nombreSP, List<Parametro> parametros) {
        Connection conn = null;
        PreparedStatement stmt = null;
        boolean isMySQL = DatabaseConnection.isMySQL();
        
        try {
            conn = DatabaseConnection.getConnection();
            int numParams = (parametros != null) ? parametros.size() : 0;
            String sql = getCallSyntax(nombreSP, numParams);
            
            if (isMySQL) {
                stmt = conn.prepareStatement(sql);
                if (parametros != null) {
                    for (int i = 0; i < parametros.size(); i++) {
                        Parametro p = parametros.get(i);
                        stmt.setObject(i + 1, p.getValor(), p.getTipoDato());
                    }
                }
                stmt.execute();
                // Para MySQL, obtenemos el numero de filas afectadas
                return stmt.getUpdateCount();
            } else {
                CallableStatement cstmt = conn.prepareCall(sql);
                stmt = cstmt;
                
                if (parametros != null) {
                    for (int i = 0; i < parametros.size(); i++) {
                        Parametro p = parametros.get(i);
                        cstmt.setObject(i + 1, p.getValor(), p.getTipoDato());
                    }
                }
                
                boolean hasResultSet = cstmt.execute();
                if (!hasResultSet) {
                    return cstmt.getUpdateCount();
                }
                return 0;
            }
        } catch (SQLException e) {
            System.err.println("Error en ejecutarSPUpdate: " + e.getMessage());
            e.printStackTrace();
            return -1;
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}