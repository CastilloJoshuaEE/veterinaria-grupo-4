package com.mycompany.veterinaria.grupo4.model.impl;

import com.mycompany.veterinaria.grupo4.model.dao.IMedicamentoDAO;
import com.mycompany.veterinaria.grupo4.model.entity.Medicamento;
import com.mycompany.veterinaria.grupo4.util.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MedicamentoDAOImpl implements IMedicamentoDAO {

    @Override
    public List<Medicamento> obtenerDisponibles() throws SQLException {
        List<Medicamento> lista = new ArrayList<>();
        String sql = "{call SP_OBTENER_MEDICAMENTOS_DISPONIBLES}";
        
        try (Connection conn = DatabaseConnection.getConnection();
             CallableStatement stmt = conn.prepareCall(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                Medicamento m = new Medicamento();
                m.setIdMedicamento(rs.getInt("ID_MEDICAMENTO"));
                m.setNombre(rs.getString("NOMBRE"));
                m.setDescripcion(rs.getString("DESCRIPCION"));
                m.setPrecio(rs.getDouble("PRECIO"));
                m.setStock(rs.getInt("STOCK"));
                m.setEstado(rs.getBoolean("ESTADO"));
                lista.add(m);
            }
        }
        return lista;
    }

    @Override
    public Medicamento obtenerPorId(int idMedicamento) throws SQLException {
        String sql = "{call SP_OBTENER_MEDICAMENTO_POR_ID(?)}";
        
        try (Connection conn = DatabaseConnection.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {
            stmt.setInt(1, idMedicamento);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                Medicamento m = new Medicamento();
                m.setIdMedicamento(rs.getInt("ID_MEDICAMENTO"));
                m.setNombre(rs.getString("NOMBRE"));
                m.setDescripcion(rs.getString("DESCRIPCION"));
                m.setPrecio(rs.getDouble("PRECIO"));
                m.setStock(rs.getInt("STOCK"));
                m.setEstado(rs.getBoolean("ESTADO"));
                return m;
            }
            return null;
        }
    }

    @Override
    public boolean insertarRecetado(int idAtencionMedica, int idMedicamento, String dosis, String frecuencia, String duracion) throws SQLException {
        String sql = "{call SP_INSERTAR_MEDICAMENTO_RECETADO(?, ?, ?, ?, ?)}";
        
        try (Connection conn = DatabaseConnection.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {
            stmt.setInt(1, idAtencionMedica);
            stmt.setInt(2, idMedicamento);
            stmt.setString(3, dosis);
            stmt.setString(4, frecuencia);
            stmt.setString(5, duracion);
            return stmt.execute();
        }
    }

    @Override
    public List<Medicamento> obtenerRecetadosPorAtencion(int idAtencionMedica) throws SQLException {
        List<Medicamento> lista = new ArrayList<>();
        String sql = "{call SP_OBTENER_MEDICAMENTOS_POR_ATENCION(?)}";
        
        try (Connection conn = DatabaseConnection.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {
            stmt.setInt(1, idAtencionMedica);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Medicamento m = new Medicamento();
                m.setIdMedicamento(rs.getInt("ID_MEDICAMENTO"));
                m.setNombre(rs.getString("NOMBRE"));
                lista.add(m);
            }
        }
        return lista;
    }
}