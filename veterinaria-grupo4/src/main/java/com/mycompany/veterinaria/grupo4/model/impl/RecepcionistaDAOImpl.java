package com.mycompany.veterinaria.grupo4.model.impl;

import com.mycompany.veterinaria.grupo4.model.dao.IRcepcionistaDAO;
import com.mycompany.veterinaria.grupo4.model.entity.Recepcionista;
import com.mycompany.veterinaria.grupo4.util.DatabaseConnection;
import java.sql.*;

/**
 * Implementación del DAO para recepcionistas con compatibilidad SQL Server y MySQL.
 * 
 * @author juan
 */
public class RecepcionistaDAOImpl implements IRcepcionistaDAO {

    @Override
    public boolean registrarRecepcionista(Recepcionista registro) throws SQLException {
        if (DatabaseConnection.isMySQL()) {
            return registrarRecepcionistaMySQL(registro);
        } else {
            return registrarRecepcionistaSQLServer(registro);
        }
    }

    /**
     * Registra un recepcionista en SQL Server usando SP_REGISTRAR_RECEPCIONISTA.
     * Usa el RETURN del SP para obtener el resultado.
     */
    private boolean registrarRecepcionistaSQLServer(Recepcionista registro) throws SQLException {
        String sql = "{? = call SP_REGISTRAR_RECEPCIONISTA(?, ?, ?, ?, ?, ?, ?)}";
        
        try (Connection conn = DatabaseConnection.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {
            
            // Registrar el parámetro de retorno
            stmt.registerOutParameter(1, Types.INTEGER);
            
            stmt.setString(2, registro.getCedula());
            stmt.setString(3, registro.getNombre());
            stmt.setString(4, registro.getApellido());
            stmt.setString(5, registro.getTelefono());
            stmt.setString(6, registro.getDireccion());
            stmt.setString(7, registro.getEmail());
            stmt.setString(8, registro.getContrasena());
            
            stmt.execute();
            
            int resultado = stmt.getInt(1);
            
            if (resultado == 1) {
                return true;
            } else if (resultado == -1) {
                throw new SQLException("El correo electrónico ya está registrado");
            } else if (resultado == -2) {
                throw new SQLException("La cédula ya está registrada");
            } else {
                throw new SQLException("Error al registrar el recepcionista (código: " + resultado + ")");
            }
        }
    }

    /**
     * Registra un recepcionista en MySQL usando SP_REGISTRAR_RECEPCIONISTA.
     * Usa el OUT parameter para obtener el resultado.
     */
    private boolean registrarRecepcionistaMySQL(Recepcionista registro) throws SQLException {
        String sql = "{call SP_REGISTRAR_RECEPCIONISTA(?, ?, ?, ?, ?, ?, ?, ?)}";
        
        try (Connection conn = DatabaseConnection.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {
            
            stmt.setString(1, registro.getCedula());
            stmt.setString(2, registro.getNombre());
            stmt.setString(3, registro.getApellido());
            stmt.setString(4, registro.getTelefono());
            stmt.setString(5, registro.getDireccion());
            stmt.setString(6, registro.getEmail());
            stmt.setString(7, registro.getContrasena());
            
            // Registrar el parámetro de salida
            stmt.registerOutParameter(8, Types.INTEGER);
            
            stmt.execute();
            
            int resultado = stmt.getInt(8);
            
            if (resultado == 1) {
                return true;
            } else if (resultado == -1) {
                throw new SQLException("El correo electrónico ya está registrado");
            } else if (resultado == -2) {
                throw new SQLException("La cédula ya está registrada");
            } else {
                throw new SQLException("Error al registrar el recepcionista (código: " + resultado + ")");
            }
        }
    }
}