package com.mycompany.veterinaria.grupo4.model.impl;

import com.mycompany.veterinaria.grupo4.config.RecordatorioConfig;
import com.mycompany.veterinaria.grupo4.model.dao.IRecordatorioDAO;
import com.mycompany.veterinaria.grupo4.model.entity.Recordatorio;
import com.mycompany.veterinaria.grupo4.util.DatabaseConnection;
import com.mycompany.veterinaria.grupo4.util.DatabaseUtil;
import com.mycompany.veterinaria.grupo4.util.Parametro;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.sql.Types;

/**
 * Implementacion del DAO para la gestion de recordatorios.
 * <p>
 * Esta clase implementa la interfaz IRecordatorioDAO y proporciona la logica
 * de acceso a datos para la entidad Recordatorio utilizando procedimientos
 * almacenados. Soporta tanto SQL Server como MySQL.
 * </p>
 * 
 * @author CHILAN CHILAN DANNY ANDRES – MODULO: AGENDAMIENTO DE CITA
 * @version 2.0 (Soporta MySQL)
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
        
        List<Parametro> parametros = new ArrayList<>();
        parametros.add(new Parametro("p_ID_USUARIO", Types.INTEGER, idUsuario));
        
        try (ResultSet rs = DatabaseUtil.ejecutarSPQuery("SP_OBTENER_RECORDATORIOS_PENDIENTES", parametros)) {
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
        List<Parametro> parametros = new ArrayList<>();
        parametros.add(new Parametro("p_ID_RECORDATORIO", Types.INTEGER, idRecordatorio));
        
        try (ResultSet rs = DatabaseUtil.ejecutarSPQuery("SP_MARCAR_RECORDATORIO_LEIDO", parametros)) {
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
        List<Parametro> parametros = new ArrayList<>();
        if (idUsuario > 0) {
            parametros.add(new Parametro("p_ID_USUARIO", Types.INTEGER, idUsuario));
        } else {
            parametros.add(new Parametro("p_ID_USUARIO", Types.INTEGER, null));
        }
        
        DatabaseUtil.ejecutarSPNonQuery("SP_VERIFICAR_RECORDATORIOS", parametros);
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
        
        List<Parametro> parametros = new ArrayList<>();
        parametros.add(new Parametro("p_FECHA_INICIO", Types.TIMESTAMP, new Timestamp(fechaInicio.getTime())));
        parametros.add(new Parametro("p_FECHA_FIN", Types.TIMESTAMP, new Timestamp(fechaFin.getTime())));
        
        try (ResultSet rs = DatabaseUtil.ejecutarSPQuery("SP_OBTENER_TODOS_RECORDATORIOS", parametros)) {
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
        List<Parametro> parametros = new ArrayList<>();
        parametros.add(new Parametro("p_ID_RECORDATORIO", Types.INTEGER, idRecordatorio));
        
        DatabaseUtil.ejecutarSPNonQuery("SP_INCREMENTAR_CONTADOR_RECORDATORIO", parametros);
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
        List<Parametro> parametros = new ArrayList<>();
        parametros.add(new Parametro("p_ID_RECORDATORIO", Types.INTEGER, idRecordatorio));
        
        try (ResultSet rs = DatabaseUtil.ejecutarSPQuery("SP_OBTENER_CONTADOR_RECORDATORIO", parametros)) {
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
        List<Parametro> parametros = new ArrayList<>();
        parametros.add(new Parametro("p_CORREO_USUARIO", Types.VARCHAR, recordatorio.getCorreoUsuario()));
        
        if (recordatorio.getIdCita() != null) {
            parametros.add(new Parametro("p_ID_CITA", Types.INTEGER, recordatorio.getIdCita()));
        } else {
            parametros.add(new Parametro("p_ID_CITA", Types.INTEGER, null));
        }
        
        if (recordatorio.getIdVacuna() != null) {
            parametros.add(new Parametro("p_ID_VACUNA", Types.INTEGER, recordatorio.getIdVacuna()));
        } else {
            parametros.add(new Parametro("p_ID_VACUNA", Types.INTEGER, null));
        }
        
        parametros.add(new Parametro("p_TIPO", Types.VARCHAR, recordatorio.getTipo()));
        parametros.add(new Parametro("p_MENSAJE", Types.VARCHAR, recordatorio.getMensaje()));
        parametros.add(new Parametro("p_ANTICIPACION", Types.VARCHAR, anticipacion));
        
        try (ResultSet rs = DatabaseUtil.ejecutarSPQuery("SP_REGISTRAR_RECORDATORIO", parametros)) {
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
        List<Parametro> parametros = new ArrayList<>();
        parametros.add(new Parametro("p_ID_RECORDATORIO", Types.INTEGER, recordatorio.getIdRecordatorio()));
        parametros.add(new Parametro("p_MENSAJE", Types.VARCHAR, recordatorio.getMensaje()));
        parametros.add(new Parametro("p_LEIDO", Types.BOOLEAN, recordatorio.isLeido()));
        
        try (ResultSet rs = DatabaseUtil.ejecutarSPQuery("SP_ACTUALIZAR_RECORDATORIO", parametros)) {
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
        List<Parametro> parametros = new ArrayList<>();
        parametros.add(new Parametro("p_ID_RECORDATORIO", Types.INTEGER, idRecordatorio));
        
        try (ResultSet rs = DatabaseUtil.ejecutarSPQuery("SP_ELIMINAR_RECORDATORIO", parametros)) {
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
        
        try (ResultSet rs = DatabaseUtil.ejecutarSPQuery("SP_OBTENER_CONFIG_RECORDATORIOS", null)) {
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
        List<Parametro> parametros = new ArrayList<>();
        parametros.add(new Parametro("p_ID_CONFIG", Types.INTEGER, config.getIdConfig()));
        parametros.add(new Parametro("p_MENSAJE", Types.VARCHAR, config.getMensaje()));
        parametros.add(new Parametro("p_ACTIVO", Types.BOOLEAN, config.isActivo()));
        
        try (ResultSet rs = DatabaseUtil.ejecutarSPQuery("SP_ACTUALIZAR_CONFIG_RECORDATORIO", parametros)) {
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
        List<Parametro> parametros = new ArrayList<>();
        parametros.add(new Parametro("p_TIPO_RECORDATORIO", Types.VARCHAR, config.getTipoRecordatorio()));
        parametros.add(new Parametro("p_ANTICIPACION", Types.VARCHAR, config.getAnticipacion()));
        parametros.add(new Parametro("p_MENSAJE", Types.VARCHAR, config.getMensaje()));
        parametros.add(new Parametro("p_ACTIVO", Types.BOOLEAN, config.isActivo()));
        
        try (ResultSet rs = DatabaseUtil.ejecutarSPQuery("SP_INSERTAR_CONFIG_RECORDATORIO", parametros)) {
            if (rs.next()) {
                return rs.getInt("ID_CONFIG");
            }
            return -1;
        }
    }
}