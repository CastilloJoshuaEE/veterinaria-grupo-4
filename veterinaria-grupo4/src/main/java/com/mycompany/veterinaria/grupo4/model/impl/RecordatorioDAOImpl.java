package com.mycompany.veterinaria.grupo4.model.impl;

import com.mycompany.veterinaria.grupo4.config.RecordatorioConfig;
import com.mycompany.veterinaria.grupo4.model.dao.IRecordatorioDAO;
import com.mycompany.veterinaria.grupo4.model.entity.Recordatorio;
import com.mycompany.veterinaria.grupo4.util.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementacion del DAO para la gestion de recordatorios.
 * <p>
 * Esta clase implementa la interfaz IRecordatorioDAO y proporciona la logica
 * de acceso a datos para la entidad Recordatorio utilizando procedimientos
 * almacenados de SQL Server. Permite gestionar recordatorios pendientes,
 * marcarlos como leidos, generar recordatorios automaticos y administrar
 * las configuraciones de los mismos.
 * </p>
 * 
 * <p><b>Fecha de inicio del proyecto:</b> 15/04/2026</p>
 * 
 * @author CHILAN CHILAN DANNY ANDRES – MODULO: AGENDAMIENTO DE CITA
 * @version 1.0
 * @since 1.0
 */
public class RecordatorioDAOImpl implements IRecordatorioDAO {

    /**
     * Obtiene los recordatorios pendientes para un usuario especifico.
     *
     * @param idUsuario identificador del usuario
     * @return lista de recordatorios pendientes
     * @throws SQLException si ocurre un error en la base de datos
     */
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

    /**
     * Marca un recordatorio como leido.
     *
     * @param idRecordatorio identificador del recordatorio
     * @return true si la operacion fue exitosa
     * @throws SQLException si ocurre un error en la base de datos
     */
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

    /**
     * Genera recordatorios automaticos para un usuario.
     *
     * @param idUsuario identificador del usuario
     * @throws SQLException si ocurre un error en la base de datos
     */
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

    /**
     * Obtiene todos los recordatorios en un rango de fechas.
     *
     * @param fechaInicio fecha de inicio
     * @param fechaFin fecha de fin
     * @return lista de recordatorios en el rango
     * @throws SQLException si ocurre un error en la base de datos
     */
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

    /**
     * Incrementa el contador de notificaciones de un recordatorio.
     *
     * @param idRecordatorio identificador del recordatorio
     * @throws SQLException si ocurre un error en la base de datos
     */
    @Override
    public void incrementarContador(int idRecordatorio) throws SQLException {
        String sql = "{call SP_INCREMENTAR_CONTADOR_RECORDATORIO(?)}";
        
        try (Connection conn = DatabaseConnection.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {
            stmt.setInt(1, idRecordatorio);
            stmt.execute();
        }
    }

    /**
     * Obtiene el contador de notificaciones de un recordatorio.
     *
     * @param idRecordatorio identificador del recordatorio
     * @return valor del contador
     * @throws SQLException si ocurre un error en la base de datos
     */
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

    /**
     * Registra un nuevo recordatorio.
     *
     * @param recordatorio objeto Recordatorio a registrar
     * @param anticipacion tiempo de anticipacion
     * @return ID del recordatorio creado
     * @throws SQLException si ocurre un error en la base de datos
     */
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

    /**
     * Actualiza un recordatorio existente.
     *
     * @param recordatorio objeto Recordatorio con datos actualizados
     * @return true si la actualizacion fue exitosa
     * @throws SQLException si ocurre un error en la base de datos
     */
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

    /**
     * Elimina un recordatorio.
     *
     * @param idRecordatorio identificador del recordatorio
     * @return true si la eliminacion fue exitosa
     * @throws SQLException si ocurre un error en la base de datos
     */
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
    
    /**
     * Obtiene todas las configuraciones de recordatorios.
     *
     * @return lista de configuraciones
     * @throws SQLException si ocurre un error en la base de datos
     */
    @Override
    public List<RecordatorioConfig> obtenerTodasConfiguraciones() throws SQLException {
        List<RecordatorioConfig> lista = new ArrayList<>();
        String sql = "{call SP_OBTENER_CONFIG_RECORDATORIOS}";

        try (Connection conn = DatabaseConnection.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                RecordatorioConfig c = new RecordatorioConfig();
                c.setIdConfig(rs.getInt("ID_CONFIG"));
                c.setTipoRecordatorio(rs.getString("TIPO_RECORDATORIO"));
                c.setAnticipacion(rs.getString("ANTICIPACION"));
                c.setMensaje(rs.getString("MENSAJE"));
                c.setActivo(rs.getBoolean("ACTIVO"));
                lista.add(c);
            }
        }
        return lista;
    }

    /**
     * Actualiza una configuracion de recordatorio.
     *
     * @param config objeto RecordatorioConfig con datos actualizados
     * @return true si la actualizacion fue exitosa
     * @throws SQLException si ocurre un error en la base de datos
     */
    @Override
    public boolean actualizarConfiguracion(RecordatorioConfig config) throws SQLException {
        String sql = "{call SP_ACTUALIZAR_CONFIG_RECORDATORIO(?, ?, ?)}";

        try (Connection conn = DatabaseConnection.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {
            stmt.setInt(1, config.getIdConfig());
            stmt.setString(2, config.getMensaje());
            stmt.setBoolean(3, config.isActivo());
            ResultSet rs = stmt.executeQuery();
            return rs.next() && rs.getInt("FILAS_AFECTADAS") > 0;
        }
    }
    
    /**
     * Crea una nueva configuracion de recordatorio.
     *
     * @param config objeto RecordatorioConfig con los datos
     * @return ID de la configuracion creada
     * @throws SQLException si ocurre un error en la base de datos
     */
    @Override
    public int crearConfiguracion(RecordatorioConfig config) throws SQLException {
        String sql = "{call SP_INSERTAR_CONFIG_RECORDATORIO(?, ?, ?, ?)}";

        try (Connection conn = DatabaseConnection.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {
            stmt.setString(1, config.getTipoRecordatorio());
            stmt.setString(2, config.getAnticipacion());
            stmt.setString(3, config.getMensaje());
            stmt.setBoolean(4, config.isActivo());

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("ID_CONFIG");
            }
            return -1;
        }
    }
}