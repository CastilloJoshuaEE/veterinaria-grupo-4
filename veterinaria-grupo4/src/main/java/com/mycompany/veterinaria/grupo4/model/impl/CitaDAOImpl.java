package com.mycompany.veterinaria.grupo4.model.impl;

import com.mycompany.veterinaria.grupo4.model.dao.ICitaDAO;
import com.mycompany.veterinaria.grupo4.model.entity.Cita;
import com.mycompany.veterinaria.grupo4.util.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CitaDAOImpl implements ICitaDAO {

    @Override
    public List<Cita> obtenerPorFecha(java.util.Date fecha) throws SQLException {
        List<Cita> lista = new ArrayList<>();
        String sql = "{call SP_OBTENER_CITAS_POR_FECHA(?)}";
        
        try (Connection conn = DatabaseConnection.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {
            stmt.setDate(1, new java.sql.Date(fecha.getTime()));
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Cita c = new Cita();
                c.setIdCita(rs.getInt("ID_CITA"));
                c.setIdCliente(rs.getInt("ID_CLIENTE"));
                c.setIdServicio(rs.getInt("ID_SERVICIO"));
                c.setFechaHora(rs.getTimestamp("FECHA_HORA"));
                c.setEstado(rs.getString("ESTADO"));
                c.setObservaciones(rs.getString("OBSERVACIONES"));
                c.setFechaRegistro(rs.getTimestamp("FECHA_REGISTRO"));
                lista.add(c);
            }
        }
        return lista;
    }

    @Override
    public List<Cita> obtenerPorCliente(int idCliente) throws SQLException {
        List<Cita> lista = new ArrayList<>();
        String sql = "{call SP_OBTENER_CITAS_POR_CLIENTE(?)}";
        
        try (Connection conn = DatabaseConnection.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {
            stmt.setInt(1, idCliente);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Cita c = new Cita();
                c.setIdCita(rs.getInt("ID_CITA"));
                c.setIdCliente(rs.getInt("ID_CLIENTE"));
                c.setIdServicio(rs.getInt("ID_SERVICIO"));
                c.setFechaHora(rs.getTimestamp("FECHA_HORA"));
                c.setEstado(rs.getString("ESTADO"));
                c.setObservaciones(rs.getString("OBSERVACIONES"));
                c.setFechaRegistro(rs.getTimestamp("FECHA_REGISTRO"));
                lista.add(c);
            }
        }
        return lista;
    }

    @Override
    public Cita obtenerPorId(int idCita) throws SQLException {
        String sql = "{call SP_OBTENER_CITA_POR_ID(?)}";
        
        try (Connection conn = DatabaseConnection.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {
            stmt.setInt(1, idCita);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                Cita c = new Cita();
                c.setIdCita(rs.getInt("ID_CITA"));
                c.setIdCliente(rs.getInt("ID_CLIENTE"));
                c.setIdServicio(rs.getInt("ID_SERVICIO"));
                c.setFechaHora(rs.getTimestamp("FECHA_HORA"));
                c.setEstado(rs.getString("ESTADO"));
                c.setObservaciones(rs.getString("OBSERVACIONES"));
                c.setFechaRegistro(rs.getTimestamp("FECHA_REGISTRO"));
                return c;
            }
            return null;
        }
    }

    @Override
    public int agendar(Cita cita, String idsMascotas) throws SQLException {
        String sql = "{call SP_AGENDAR_CITA(?, ?, ?, ?, ?)}";
        
        try (Connection conn = DatabaseConnection.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {
            stmt.setInt(1, cita.getIdCliente());
            stmt.setInt(2, cita.getIdServicio());
            stmt.setTimestamp(3, new Timestamp(cita.getFechaHora().getTime()));
            stmt.setString(4, cita.getObservaciones());
            stmt.setString(5, idsMascotas);
            
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("ID_CITA");
            }
            return -1;
        }
    }

    @Override
    public boolean actualizar(Cita cita, String idsMascotas) throws SQLException {
        String sql = "{call SP_ACTUALIZAR_CITA_COMPLETA(?, ?, ?, ?, ?, ?, ?)}";
        
        try (Connection conn = DatabaseConnection.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {
            stmt.setInt(1, cita.getIdCita());
            stmt.setInt(2, cita.getIdCliente());
            stmt.setInt(3, cita.getIdServicio());
            stmt.setTimestamp(4, new Timestamp(cita.getFechaHora().getTime()));
            stmt.setString(5, cita.getEstado());
            stmt.setString(6, cita.getObservaciones());
            stmt.setString(7, idsMascotas);
            
            ResultSet rs = stmt.executeQuery();
            return rs.next() && rs.getInt("RESULTADO") == 1;
        }
    }

    @Override
    public boolean cancelar(int idCita, String motivo) throws SQLException {
        String sql = "{call SP_CANCELAR_CITA(?, ?)}";
        
        try (Connection conn = DatabaseConnection.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {
            stmt.setInt(1, idCita);
            stmt.setString(2, motivo);
            
            ResultSet rs = stmt.executeQuery();
            return rs.next() && rs.getInt("RESULTADO") == 1;
        }
    }

    @Override
    public List<Cita> obtenerTodas() throws SQLException {
        List<Cita> lista = new ArrayList<>();
        String sql = "{call SP_OBTENER_TODAS_LAS_CITAS}";
        
        try (Connection conn = DatabaseConnection.getConnection();
             CallableStatement stmt = conn.prepareCall(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                Cita c = new Cita();
                c.setIdCita(rs.getInt("ID_CITA"));
                c.setIdCliente(rs.getInt("ID_CLIENTE"));
                c.setIdServicio(rs.getInt("ID_SERVICIO"));
                c.setFechaHora(rs.getTimestamp("FECHA_HORA"));
                c.setEstado(rs.getString("ESTADO"));
                c.setObservaciones(rs.getString("OBSERVACIONES"));
                c.setFechaRegistro(rs.getTimestamp("FECHA_REGISTRO"));
                lista.add(c);
            }
        }
        return lista;
    }

    @Override
    public List<Cita> obtenerPorRangoFechas(java.util.Date fechaInicio, java.util.Date fechaFin) throws SQLException {
        List<Cita> lista = new ArrayList<>();
        String sql = "{call SP_OBTENER_CITAS_POR_RANGO_FECHAS(?, ?)}";
        
        try (Connection conn = DatabaseConnection.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {
            stmt.setDate(1, new java.sql.Date(fechaInicio.getTime()));
            stmt.setDate(2, new java.sql.Date(fechaFin.getTime()));
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Cita c = new Cita();
                c.setIdCita(rs.getInt("ID_CITA"));
                c.setIdCliente(rs.getInt("ID_CLIENTE"));
                c.setIdServicio(rs.getInt("ID_SERVICIO"));
                c.setFechaHora(rs.getTimestamp("FECHA_HORA"));
                c.setEstado(rs.getString("ESTADO"));
                c.setObservaciones(rs.getString("OBSERVACIONES"));
                lista.add(c);
            }
        }
        return lista;
    }

    @Override
    public List<Cita> obtenerPorServicioYVeterinario(int idServicio, int idVeterinario, String estado) throws SQLException {
        List<Cita> lista = new ArrayList<>();
        String sql = "{call SP_OBTENER_CITAS_POR_SERVICIO_VETERINARIO(?, ?, ?)}";
        
        try (Connection conn = DatabaseConnection.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {
            stmt.setInt(1, idServicio);
            stmt.setInt(2, idVeterinario);
            stmt.setString(3, estado);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Cita c = new Cita();
                c.setIdCita(rs.getInt("ID_CITA"));
                c.setIdCliente(rs.getInt("ID_CLIENTE"));
                c.setIdServicio(rs.getInt("ID_SERVICIO"));
                c.setFechaHora(rs.getTimestamp("FECHA_HORA"));
                c.setEstado(rs.getString("ESTADO"));
                c.setObservaciones(rs.getString("OBSERVACIONES"));
                lista.add(c);
            }
        }
        return lista;
    }

    @Override
    public boolean actualizarEstado(int idCita, String estado) throws SQLException {
        String sql = "{call SP_ACTUALIZAR_ESTADO_CITA(?, ?)}";
        
        try (Connection conn = DatabaseConnection.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {
            stmt.setInt(1, idCita);
            stmt.setString(2, estado);
            
            ResultSet rs = stmt.executeQuery();
            return rs.next() && rs.getInt("RESULTADO") == 1;
        }
    }

    @Override
    public boolean eliminar(int idCita) throws SQLException {
        String sql = "{call SP_ELIMINAR_CITA_COMPLETA(?)}";
        
        try (Connection conn = DatabaseConnection.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {
            stmt.setInt(1, idCita);
            return stmt.execute();
        }
    }
}