package com.mycompany.veterinaria.grupo4.model.impl;

import com.mycompany.veterinaria.grupo4.model.dao.IAtencionMedicaDAO;
import com.mycompany.veterinaria.grupo4.model.entity.AtencionMedica;
import com.mycompany.veterinaria.grupo4.util.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AtencionMedicaDAOImpl implements IAtencionMedicaDAO {

    @Override
    public int insertar(AtencionMedica atencion) throws SQLException {
        String sql = "{call SP_INSERTAR_ATENCION_MEDICA(?, ?, ?, ?)}";

        try (Connection conn = DatabaseConnection.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {

            stmt.setInt(1, atencion.getIdCita());     
            stmt.setString(2, atencion.getDiagnostico()); 
            stmt.setString(3, atencion.getTratamiento()); 
            stmt.setString(4, atencion.getObservaciones()); 

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    // Importante: El alias en el SP es ID_ATENCION_MEDICA
                    return rs.getInt("ID_ATENCION_MEDICA");
                }
            }
            return -1;
        }
    }

    @Override
    public List<AtencionMedica> obtenerTodas() throws SQLException {
        List<AtencionMedica> lista = new ArrayList<>();
        String sql = "{call SP_OBTENER_TODAS_ATENCIONES_MEDICAS}";
        
        try (Connection conn = DatabaseConnection.getConnection();
             CallableStatement stmt = conn.prepareCall(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                AtencionMedica a = new AtencionMedica();
                a.setIdAtencionMedica(rs.getInt("ID_ATENCION_MEDICA"));
                a.setIdMascota(rs.getInt("ID_MASCOTA"));
                a.setIdVeterinario(rs.getInt("ID_VETERINARIO"));
                a.setFecha(rs.getTimestamp("FECHA"));
                a.setDiagnostico(rs.getString("DIAGNOSTICO"));
                a.setTratamiento(rs.getString("TRATAMIENTO"));
                a.setObservaciones(rs.getString("OBSERVACIONES"));
                lista.add(a);
            }
        }
        return lista;
    }

    @Override
    public AtencionMedica obtenerPorId(int idAtencionMedica) throws SQLException {
        String sql = "{call SP_OBTENER_DETALLES_ATENCION_MEDICA(?)}";
        
        try (Connection conn = DatabaseConnection.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {
            stmt.setInt(1, idAtencionMedica);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                AtencionMedica a = new AtencionMedica();
                a.setIdAtencionMedica(rs.getInt("ID_ATENCION_MEDICA"));
                a.setIdCita(rs.getInt("ID_CITA"));
                a.setIdMascota(rs.getInt("ID_MASCOTA"));
                a.setIdVeterinario(rs.getInt("ID_VETERINARIO"));
                a.setFecha(rs.getTimestamp("FECHA"));
                a.setDiagnostico(rs.getString("DIAGNOSTICO"));
                a.setTratamiento(rs.getString("TRATAMIENTO"));
                a.setObservaciones(rs.getString("OBSERVACIONES"));
                return a;
            }
            return null;
        }
    }

    @Override
    public boolean eliminar(int idAtencionMedica) throws SQLException {
        String sql = "{call SP_ELIMINAR_ATENCION_MEDICA(?)}";
        
        try (Connection conn = DatabaseConnection.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {
            stmt.setInt(1, idAtencionMedica);
            return stmt.execute();
        }
    }
}