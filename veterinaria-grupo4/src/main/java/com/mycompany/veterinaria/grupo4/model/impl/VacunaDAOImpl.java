package com.mycompany.veterinaria.grupo4.model.impl;

import com.mycompany.veterinaria.grupo4.model.dao.IVacunaDAO;
import com.mycompany.veterinaria.grupo4.model.entity.VacunaAplicada;
import com.mycompany.veterinaria.grupo4.util.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VacunaDAOImpl implements IVacunaDAO {

    @Override
    public List<VacunaAplicada> obtenerPorMascota(int idMascota) throws SQLException {
        List<VacunaAplicada> lista = new ArrayList<>();
        String sql = "{call SP_OBTENER_VACUNAS_APLICADAS_POR_MASCOTA(?)}";
        
        try (Connection conn = DatabaseConnection.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {
            stmt.setInt(1, idMascota);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                VacunaAplicada v = new VacunaAplicada();
                v.setIdVacuna(rs.getInt("ID_VACUNA"));
                v.setNombre(rs.getString("NOMBRE"));
                v.setDescripcion(rs.getString("DESCRIPCION"));
                v.setFechaAplicacion(rs.getDate("FECHA_APLICACION"));
                v.setFechaProxima(rs.getDate("FECHA_PROXIMA"));
                v.setIdMascota(idMascota);
                lista.add(v);
            }
        }
        return lista;
    }

    @Override
    public int registrar(VacunaAplicada vacuna) throws SQLException {
        String sql = "{call SP_REGISTRAR_VACUNA_APLICADA(?, ?, ?, ?, ?)}";
        
        try (Connection conn = DatabaseConnection.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {
            stmt.setInt(1, vacuna.getIdMascota());
            stmt.setString(2, vacuna.getNombre());
            stmt.setString(3, vacuna.getDescripcion());
            stmt.setInt(4, vacuna.getPeriodoMeses());
            stmt.setDate(5, new java.sql.Date(vacuna.getFechaAplicacion().getTime()));
            
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("ID_VACUNA");
            }
            return -1;
        }
    }

    @Override
    public boolean actualizar(VacunaAplicada vacuna) throws SQLException {
        String sql = "{call SP_ACTUALIZAR_VACUNA_APLICADA(?, ?, ?, ?, ?, ?)}";
        
        try (Connection conn = DatabaseConnection.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {
            stmt.setInt(1, vacuna.getIdVacuna());
            stmt.setString(2, vacuna.getNombre());
            stmt.setString(3, vacuna.getDescripcion());
            stmt.setInt(4, vacuna.getPeriodoMeses());
            stmt.setDate(5, new java.sql.Date(vacuna.getFechaAplicacion().getTime()));
            stmt.setDate(6, new java.sql.Date(vacuna.getFechaProxima().getTime()));
            
            ResultSet rs = stmt.executeQuery();
            return rs.next() && rs.getInt("FILAS_AFECTADAS") > 0;
        }
    }

    @Override
    public VacunaAplicada verificarExistente(int idMascota, String nombreVacuna) throws SQLException {
        String sql = "{call SP_VERIFICAR_VACUNA_APLICADA_POR_MASCOTA(?, ?)}";
        
        try (Connection conn = DatabaseConnection.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {
            stmt.setInt(1, idMascota);
            stmt.setString(2, nombreVacuna);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                VacunaAplicada v = new VacunaAplicada();
                v.setIdVacuna(rs.getInt("ID_VACUNA"));
                v.setNombre(rs.getString("NOMBRE"));
                v.setFechaAplicacion(rs.getDate("FECHA_APLICACION"));
                v.setFechaProxima(rs.getDate("FECHA_PROXIMA"));
                return v;
            }
            return null;
        }
    }

    @Override
    public List<VacunaAplicada> obtenerProximasAVencer(int diasAnticipacion) throws SQLException {
        List<VacunaAplicada> lista = new ArrayList<>();
        String sql = "{call SP_OBTENER_VACUNAS_PROXIMAS_A_VENCER(?)}";
        
        try (Connection conn = DatabaseConnection.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {
            stmt.setInt(1, diasAnticipacion);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                VacunaAplicada v = new VacunaAplicada();
                v.setIdVacuna(rs.getInt("ID_VACUNA"));
                v.setNombre(rs.getString("NOMBRE"));
                v.setFechaAplicacion(rs.getDate("FECHA_APLICACION"));
                v.setFechaProxima(rs.getDate("FECHA_PROXIMA"));
                v.setIdMascota(rs.getInt("ID_MASCOTA"));
                lista.add(v);
            }
        }
        return lista;
    }
}