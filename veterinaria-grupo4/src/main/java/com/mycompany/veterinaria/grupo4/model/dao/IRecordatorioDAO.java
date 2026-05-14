package com.mycompany.veterinaria.grupo4.model.dao;

import com.mycompany.veterinaria.grupo4.config.RecordatorioConfig;
import com.mycompany.veterinaria.grupo4.model.entity.Recordatorio;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

/**
 * Interfaz DAO para la gestion de recordatorios.
 * <p>
 * Define las operaciones de acceso a datos para la entidad Recordatorio
 * y sus configuraciones, incluyendo consulta de pendientes, marcado como leido,
 * generacion automatica, y gestion completa de CRUD.
 * </p>
 * 
 * <p><b>Fecha de inicio del proyecto:</b> 15/04/2026</p>
 * 
 * @author CHILAN CHILAN DANNY ANDRES – MODULO: AGENDAMIENTO DE CITA
 * @version 1.0
 * @since 1.0
 */
public interface IRecordatorioDAO {
    
    /**
     * Obtiene los recordatorios pendientes para un usuario.
     * 
     * @param idUsuario identificador del usuario
     * @return lista de recordatorios pendientes
     * @throws SQLException si ocurre un error en la base de datos
     */
    List<Recordatorio> obtenerPendientes(int idUsuario) throws SQLException;
    
    /**
     * Marca un recordatorio como leido.
     * 
     * @param idRecordatorio identificador del recordatorio
     * @return true si la operacion fue exitosa
     * @throws SQLException si ocurre un error en la base de datos
     */
    boolean marcarComoLeido(int idRecordatorio) throws SQLException;
    
    /**
     * Genera recordatorios automaticos para un usuario.
     * 
     * @param idUsuario identificador del usuario
     * @throws SQLException si ocurre un error en la base de datos
     */
    void generarRecordatorios(int idUsuario) throws SQLException;
    
    /**
     * Obtiene todos los recordatorios en un rango de fechas.
     * 
     * @param fechaInicio fecha de inicio
     * @param fechaFin fecha de fin
     * @return lista de recordatorios en el rango
     * @throws SQLException si ocurre un error en la base de datos
     */
    List<Recordatorio> obtenerTodos(Date fechaInicio, Date fechaFin) throws SQLException;
    
    /**
     * Incrementa el contador de notificaciones de un recordatorio.
     * 
     * @param idRecordatorio identificador del recordatorio
     * @throws SQLException si ocurre un error en la base de datos
     */
    void incrementarContador(int idRecordatorio) throws SQLException;
    
    /**
     * Obtiene el contador de notificaciones de un recordatorio.
     * 
     * @param idRecordatorio identificador del recordatorio
     * @return valor del contador
     * @throws SQLException si ocurre un error en la base de datos
     */
    int obtenerContador(int idRecordatorio) throws SQLException;
    
    /**
     * Registra un nuevo recordatorio.
     * 
     * @param recordatorio objeto Recordatorio a registrar
     * @param anticipacion tiempo de anticipacion
     * @return ID del recordatorio creado
     * @throws SQLException si ocurre un error en la base de datos
     */
    int registrar(Recordatorio recordatorio, String anticipacion) throws SQLException;
    
    /**
     * Actualiza un recordatorio existente.
     * 
     * @param recordatorio objeto Recordatorio con datos actualizados
     * @return true si la actualizacion fue exitosa
     * @throws SQLException si ocurre un error en la base de datos
     */
    boolean actualizar(Recordatorio recordatorio) throws SQLException;
    
    /**
     * Elimina un recordatorio.
     * 
     * @param idRecordatorio identificador del recordatorio
     * @return true si la eliminacion fue exitosa
     * @throws SQLException si ocurre un error en la base de datos
     */
    boolean eliminar(int idRecordatorio) throws SQLException;
    
    /**
     * Obtiene todas las configuraciones de recordatorios.
     * 
     * @return lista de configuraciones
     * @throws SQLException si ocurre un error en la base de datos
     */
    List<RecordatorioConfig> obtenerTodasConfiguraciones() throws SQLException;
    
    /**
     * Actualiza una configuracion de recordatorio.
     * 
     * @param config objeto RecordatorioConfig con datos actualizados
     * @return true si la actualizacion fue exitosa
     * @throws SQLException si ocurre un error en la base de datos
     */
    boolean actualizarConfiguracion(RecordatorioConfig config) throws SQLException;
    
    /**
     * Crea una nueva configuracion de recordatorio.
     * 
     * @param config objeto RecordatorioConfig con los datos
     * @return ID de la configuracion creada
     * @throws SQLException si ocurre un error en la base de datos
     */
    int crearConfiguracion(RecordatorioConfig config) throws SQLException;
}