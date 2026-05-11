package com.mycompany.veterinaria.grupo4.model.impl;

import com.mycompany.veterinaria.grupo4.model.dao.IInstrumentoMedicoDAO;
import com.mycompany.veterinaria.grupo4.model.entity.InstrumentoMedico;
import com.mycompany.veterinaria.grupo4.util.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class InstrumentoMedicoDAOImpl implements IInstrumentoMedicoDAO {

    @Override
    public List<InstrumentoMedico> obtenerDisponibles() throws SQLException {
        List<InstrumentoMedico> lista = new ArrayList<>();
        String sql = "{call SP_OBTENER_INSTRUMENTOS_DISPONIBLES}";
        
        try (Connection conn = DatabaseConnection.getConnection();
             CallableStatement stmt = conn.prepareCall(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                InstrumentoMedico i = new InstrumentoMedico();
                i.setIdInstrumento(rs.getInt("ID_INSTRUMENTO"));
                i.setNombre(rs.getString("NOMBRE"));
                i.setDescripcion(rs.getString("DESCRIPCION"));
                i.setCostoUso(rs.getDouble("COSTO_USO"));
                lista.add(i);
            }
        }
        return lista;
    }

    @Override
    public InstrumentoMedico obtenerPorId(int idInstrumento) throws SQLException {
        String sql = "{call SP_OBTENER_INSTRUMENTO_POR_ID(?)}";
        
        try (Connection conn = DatabaseConnection.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {
            stmt.setInt(1, idInstrumento);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                InstrumentoMedico i = new InstrumentoMedico();
                i.setIdInstrumento(rs.getInt("ID_INSTRUMENTO"));
                i.setNombre(rs.getString("NOMBRE"));
                i.setDescripcion(rs.getString("DESCRIPCION"));
                i.setCostoUso(rs.getDouble("COSTO_USO"));
                i.setEstado(rs.getBoolean("ESTADO"));
                return i;
            }
            return null;
        }
    }

    @Override
    public boolean insertarUsado(int idAtencionMedica, int idInstrumento) throws SQLException {
        String sql = "{call SP_INSERTAR_INSTRUMENTO_USADO(?, ?)}";
        
        try (Connection conn = DatabaseConnection.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {
            stmt.setInt(1, idAtencionMedica);
            stmt.setInt(2, idInstrumento);
            return stmt.execute();
        }
    }

    @Override
    public List<InstrumentoMedico> obtenerUsadosPorAtencion(int idAtencionMedica) throws SQLException {
        List<InstrumentoMedico> lista = new ArrayList<>();
        String sql = "{call SP_OBTENER_INSTRUMENTOS_POR_ATENCION(?)}";
        
        try (Connection conn = DatabaseConnection.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {
            stmt.setInt(1, idAtencionMedica);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                InstrumentoMedico i = new InstrumentoMedico();
                i.setNombre(rs.getString("NOMBRE"));
                i.setCostoUso(rs.getDouble("COSTO_USO"));
                lista.add(i);
            }
        }
        return lista;
    }
}