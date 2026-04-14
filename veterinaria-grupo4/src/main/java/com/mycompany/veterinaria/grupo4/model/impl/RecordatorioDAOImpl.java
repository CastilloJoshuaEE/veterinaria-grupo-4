package com.mycompany.veterinaria.grupo4.model.impl;

import com.mycompany.veterinaria.grupo4.model.dao.IRecordatorioDAO;
import com.mycompany.veterinaria.grupo4.model.entity.Recordatorio;
import com.mycompany.veterinaria.grupo4.util.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RecordatorioDAOImpl implements IRecordatorioDAO {

    @Override
    public List<Recordatorio> obtenerPendientes(int idUsuario) throws SQLException {
        List<Recordatorio> lista = new ArrayList<>();
        String sql = "{call SP_OBTENER_RECORDATORIOS_PENDIENTES(?)}";
        
        try (Connection conn = DatabaseConnection.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {
            stmt.setInt(1, idUsuario);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Recordatorio r = new Recordatorio();
                r.setIdRecordatorio(rs.getInt("ID_RECORDATORIO"));
                r.setIdUsuario(rs.getInt("ID_USUARIO"));
                if (rs.getObject("ID_CITA") != null) r.setIdCita(rs.getInt("ID_CITA"));
                if (rs.getObject("ID_VACUNA") != null) r.setIdVacuna(rs.getInt("ID_VACUNA"));
                r.setTipo(rs.getString("TIPO"));
                r.setMensaje(rs.getString("MENSAJE"));
                r.setFechaEnvio(rs.getTimestamp("FECHA_ENVIO"));
                r.setLeido(rs.getBoolean("LEIDO"));
                lista.add(r);
            }
        }
        return lista;
    }

    @Override
    public boolean marcarComoLeido(int idRecordatorio) throws SQLException {
        String sql = "{call SP_MARCAR_RECORDATORIO_LEIDO(?)}";
        
        try (Connection conn = DatabaseConnection.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {
            stmt.setInt(1, idRecordatorio);
            ResultSet rs = stmt.executeQuery();
            return rs.next() && rs.getInt("RESULTADO") == 1;
        }
    }

    @Override
    public void generarRecordatorios(int idUsuario) throws SQLException {
        String sql = "{call SP_VERIFICAR_RECORDATORIOS(?)}";
        
        try (Connection conn = DatabaseConnection.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {
            if (idUsuario > 0) {
                stmt.setInt(1, idUsuario);
            } else {
                stmt.setNull(1, Types.INTEGER);
            }
            stmt.execute();
        }
    }

    @Override
    public List<Recordatorio> obtenerTodos(java.util.Date fechaInicio, java.util.Date fechaFin) throws SQLException {
        List<Recordatorio> lista = new ArrayList<>();
        String sql = "{call SP_OBTENER_TODOS_RECORDATORIOS(?, ?)}";
        
        try (Connection conn = DatabaseConnection.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {
            stmt.setTimestamp(1, new Timestamp(fechaInicio.getTime()));
            stmt.setTimestamp(2, new Timestamp(fechaFin.getTime()));
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Recordatorio r = new Recordatorio();
                r.setIdRecordatorio(rs.getInt("ID_RECORDATORIO"));
                if (rs.getObject("ID_USUARIO") != null) r.setIdUsuario(rs.getInt("ID_USUARIO"));
                if (rs.getObject("ID_CITA") != null) r.setIdCita(rs.getInt("ID_CITA"));
                if (rs.getObject("ID_VACUNA") != null) r.setIdVacuna(rs.getInt("ID_VACUNA"));
                r.setTipo(rs.getString("TIPO"));
                r.setMensaje(rs.getString("MENSAJE"));
                r.setFechaEnvio(rs.getTimestamp("FECHA_ENVIO"));
                r.setLeido(rs.getBoolean("LEIDO"));
                r.setContadorMostrado(rs.getInt("CONTADOR_MOSTRADO"));
                lista.add(r);
            }
        }
        return lista;
    }

    @Override
    public void incrementarContador(int idRecordatorio) throws SQLException {
        String sql = "{call SP_INCREMENTAR_CONTADOR_RECORDATORIO(?)}";
        
        try (Connection conn = DatabaseConnection.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {
            stmt.setInt(1, idRecordatorio);
            stmt.execute();
        }
    }

    @Override
    public int obtenerContador(int idRecordatorio) throws SQLException {
        String sql = "{call SP_OBTENER_CONTADOR_RECORDATORIO(?)}";
        
        try (Connection conn = DatabaseConnection.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {
            stmt.setInt(1, idRecordatorio);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("CONTADOR_MOSTRADO");
            }
            return 0;
        }
    }

    @Override
    public int registrar(Recordatorio recordatorio, String anticipacion) throws SQLException {
        String sql = "{call SP_REGISTRAR_RECORDATORIO(?, ?, ?, ?, ?, ?)}";
        
        try (Connection conn = DatabaseConnection.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {
            stmt.setString(1, recordatorio.getCorreoUsuario());
            if (recordatorio.getIdCita() != null) {
                stmt.setInt(2, recordatorio.getIdCita());
            } else {
                stmt.setNull(2, Types.INTEGER);
            }
            if (recordatorio.getIdVacuna() != null) {
                stmt.setInt(3, recordatorio.getIdVacuna());
            } else {
                stmt.setNull(3, Types.INTEGER);
            }
            stmt.setString(4, recordatorio.getTipo());
            stmt.setString(5, recordatorio.getMensaje());
            stmt.setString(6, anticipacion);
            
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("ID_RECORDATORIO");
            }
            return -1;
        }
    }

    @Override
    public boolean actualizar(Recordatorio recordatorio) throws SQLException {
        String sql = "{call SP_ACTUALIZAR_RECORDATORIO(?, ?, ?)}";
        
        try (Connection conn = DatabaseConnection.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {
            stmt.setInt(1, recordatorio.getIdRecordatorio());
            stmt.setString(2, recordatorio.getMensaje());
            stmt.setBoolean(3, recordatorio.isLeido());
            
            ResultSet rs = stmt.executeQuery();
            return rs.next() && rs.getInt("FILAS_AFECTADAS") > 0;
        }
    }

    @Override
    public boolean eliminar(int idRecordatorio) throws SQLException {
        String sql = "{call SP_ELIMINAR_RECORDATORIO(?)}";
        
        try (Connection conn = DatabaseConnection.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {
            stmt.setInt(1, idRecordatorio);
            ResultSet rs = stmt.executeQuery();
            return rs.next() && rs.getInt("FILAS_AFECTADAS") > 0;
        }
    }
}