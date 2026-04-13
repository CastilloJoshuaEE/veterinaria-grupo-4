package com.mycompany.veterinaria.grupo4.model.impl;

import com.mycompany.veterinaria.grupo4.model.dao.IFichaMedicaDAO;
import com.mycompany.veterinaria.grupo4.model.entity.FichaMedica;
import com.mycompany.veterinaria.grupo4.util.DatabaseConnection;
import java.sql.*;

public class FichaMedicaDAOImpl implements IFichaMedicaDAO {

    @Override
    public FichaMedica obtenerPorMascota(int idMascota) throws SQLException {
        String sql = "{call SP_OBTENER_FICHA_MEDICA(?)}";
        
        try (Connection conn = DatabaseConnection.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {
            stmt.setInt(1, idMascota);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                FichaMedica f = new FichaMedica();
                f.setIdFicha(rs.getInt("ID_FICHA"));
                f.setIdMascota(rs.getInt("ID_MASCOTA"));
                f.setAlergias(rs.getString("ALERGIAS"));
                f.setEnfermedadesCronicas(rs.getString("ENFERMEDADES_CRONICAS"));
                f.setObservaciones(rs.getString("OBSERVACIONES"));
                f.setUltimaActualizacion(rs.getTimestamp("ULTIMA_ACTUALIZACION"));
                return f;
            }
            return null;
        }
    }

    @Override
    public boolean actualizar(int idMascota, String alergias, String enfermedadesCronicas, String observaciones) throws SQLException {
        String sql = "{call SP_ACTUALIZAR_FICHA_MEDICA(?, ?, ?, ?)}";
        
        try (Connection conn = DatabaseConnection.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {
            stmt.setInt(1, idMascota);
            stmt.setString(2, alergias);
            stmt.setString(3, enfermedadesCronicas);
            stmt.setString(4, observaciones);
            return stmt.execute();
        }
    }
}