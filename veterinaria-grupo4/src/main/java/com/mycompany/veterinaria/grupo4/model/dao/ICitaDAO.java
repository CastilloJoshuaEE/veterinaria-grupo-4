package com.mycompany.veterinaria.grupo4.model.dao;

import com.mycompany.veterinaria.grupo4.model.entity.Cita;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

/**
 * Interfaz DAO para la gestion de citas.
 * <p>
 * Define las operaciones de acceso a datos para la entidad Cita,
 * incluyendo busqueda por fecha, cliente, rango, servicio,
 * asi como agendar, actualizar, cancelar y eliminar citas.
 * </p>
 * 
 * <p><b>Fecha de inicio del proyecto:</b> 15/04/2026</p>
 * 
 * @author CHILAN CHILAN DANNY ANDRES – MODULO: AGENDAMIENTO DE CITA
 * @version 1.0
 * @since 1.0
 */
public interface ICitaDAO {
    
    /**
     * Obtiene las citas programadas para una fecha especifica.
     * 
     * @param fecha fecha a consultar
     * @return lista de citas en la fecha
     * @throws SQLException si ocurre un error en la base de datos
     */
    List<Cita> obtenerPorFecha(Date fecha) throws SQLException;
    
    /**
     * Obtiene las citas asociadas a un cliente.
     * 
     * @param idCliente identificador del cliente
     * @return lista de citas del cliente
     * @throws SQLException si ocurre un error en la base de datos
     */
    List<Cita> obtenerPorCliente(int idCliente) throws SQLException;
    
    /**
     * Obtiene una cita por su identificador.
     * 
     * @param idCita identificador de la cita
     * @return objeto Cita encontrado
     * @throws SQLException si ocurre un error en la base de datos
     */
    Cita obtenerPorId(int idCita) throws SQLException;
    
    /**
     * Agenda una nueva cita en el sistema.
     * 
     * @param cita objeto Cita a agendar
     * @return ID de la cita agendada
     * @throws SQLException si ocurre un error en la base de datos
     */
    int agendar(Cita cita) throws SQLException;
    
    /**
     * Actualiza los datos de una cita existente.
     * 
     * @param cita objeto Cita con datos actualizados
     * @return true si la actualizacion fue exitosa
     * @throws SQLException si ocurre un error en la base de datos
     */
    boolean actualizar(Cita cita) throws SQLException;
    
    /**
     * Cancela una cita con un motivo especifico.
     * 
     * @param idCita identificador de la cita
     * @param motivo razon de la cancelacion
     * @return true si la cancelacion fue exitosa
     * @throws SQLException si ocurre un error en la base de datos
     */
    boolean cancelar(int idCita, String motivo) throws SQLException;
    
    /**
     * Obtiene todas las citas registradas.
     * 
     * @return lista de todas las citas
     * @throws SQLException si ocurre un error en la base de datos
     */
    List<Cita> obtenerTodas() throws SQLException;
    
    /**
     * Obtiene citas en un rango de fechas.
     * 
     * @param fechaInicio fecha de inicio del rango
     * @param fechaFin fecha de fin del rango
     * @return lista de citas en el rango
     * @throws SQLException si ocurre un error en la base de datos
     */
    List<Cita> obtenerPorRangoFechas(Date fechaInicio, Date fechaFin) throws SQLException;
    
    /**
     * Obtiene citas filtradas por servicio, veterinario y estado.
     * 
     * @param idServicio identificador del servicio
     * @param idVeterinario identificador del veterinario
     * @param estado estado de la cita
     * @return lista de citas que coinciden con los filtros
     * @throws SQLException si ocurre un error en la base de datos
     */
    List<Cita> obtenerPorServicioYVeterinario(int idServicio, int idVeterinario, String estado) throws SQLException;
    
    /**
     * Obtiene las citas con estado pendiente.
     * 
     * @return lista de citas pendientes
     * @throws SQLException si ocurre un error en la base de datos
     */
    List<Cita> obtenerPendientes() throws SQLException;
    
    /**
     * Actualiza el estado de una cita.
     * 
     * @param idCita identificador de la cita
     * @param estado nuevo estado
     * @return true si la actualizacion fue exitosa
     * @throws SQLException si ocurre un error en la base de datos
     */
    boolean actualizarEstado(int idCita, String estado) throws SQLException;
    
    /**
     * Elimina una cita del sistema.
     * 
     * @param idCita identificador de la cita a eliminar
     * @return true si la eliminacion fue exitosa
     * @throws SQLException si ocurre un error en la base de datos
     */
    boolean eliminar(int idCita) throws SQLException;
}