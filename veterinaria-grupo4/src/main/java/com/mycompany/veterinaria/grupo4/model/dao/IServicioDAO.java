package com.mycompany.veterinaria.grupo4.model.dao;

import com.mycompany.veterinaria.grupo4.model.entity.Servicio;
import com.mycompany.veterinaria.grupo4.model.entity.Veterinario;
import java.sql.SQLException;
import java.util.List;

/**
 * Interfaz DAO para la gestion de servicios veterinarios.
 * <p>
 * Define las operaciones de acceso a datos para la entidad Servicio,
 * incluyendo consultas de todos, activos, por ID, busqueda por nombre,
 * asi como insercion, actualizacion, eliminacion y gestion de
 * asignaciones de veterinarios a servicios.
 * </p>
 * 
 * <p><b>Fecha de inicio del proyecto:</b> 15/04/2026</p>
 * 
 * @author BESILLA TOMALA ANGEL KALED – MODULO: VETERINARIO
 * @version 1.0
 * @since 1.0
 */
public interface IServicioDAO {
    
    /**
     * Obtiene todos los servicios registrados.
     * 
     * @return lista de todos los servicios
     * @throws SQLException si ocurre un error en la base de datos
     */
    List<Servicio> obtenerTodos() throws SQLException;
    
    /**
     * Obtiene solo los servicios activos.
     * 
     * @return lista de servicios activos
     * @throws SQLException si ocurre un error en la base de datos
     */
    List<Servicio> obtenerActivos() throws SQLException;
    
    /**
     * Obtiene un servicio por su identificador.
     * 
     * @param idServicio identificador del servicio
     * @return objeto Servicio encontrado
     * @throws SQLException si ocurre un error en la base de datos
     */
    Servicio obtenerPorId(int idServicio) throws SQLException;
    
    /**
     * Inserta un nuevo servicio en la base de datos.
     * 
     * @param servicio objeto Servicio a insertar
     * @return ID del servicio creado
     * @throws SQLException si ocurre un error en la base de datos
     */
    int insertar(Servicio servicio) throws SQLException;
    
    /**
     * Actualiza los datos de un servicio existente.
     * 
     * @param servicio objeto Servicio con datos actualizados
     * @return true si la actualizacion fue exitosa
     * @throws SQLException si ocurre un error en la base de datos
     */
    boolean actualizar(Servicio servicio) throws SQLException;
    
    /**
     * Elimina un servicio de la base de datos.
     * 
     * @param idServicio identificador del servicio a eliminar
     * @return true si la eliminacion fue exitosa
     * @throws SQLException si ocurre un error en la base de datos
     */
    boolean eliminar(int idServicio) throws SQLException;
    
    /**
     * Cambia el estado (activo/inactivo) de un servicio.
     * 
     * @param idServicio identificador del servicio
     * @param estado nuevo estado
     * @return true si el cambio fue exitoso
     * @throws SQLException si ocurre un error en la base de datos
     */
    boolean cambiarEstado(int idServicio, boolean estado) throws SQLException;
    
    /**
     * Obtiene los veterinarios asignados a un servicio.
     * 
     * @param idServicio identificador del servicio
     * @return lista de veterinarios asignados
     * @throws SQLException si ocurre un error en la base de datos
     */
    List<Veterinario> obtenerVeterinariosAsignados(int idServicio) throws SQLException;
    
    /**
     * Obtiene los veterinarios no asignados a un servicio.
     * 
     * @param idServicio identificador del servicio
     * @return lista de veterinarios no asignados
     * @throws SQLException si ocurre un error en la base de datos
     */
    List<Veterinario> obtenerVeterinariosNoAsignados(int idServicio) throws SQLException;
    
    /**
     * Asigna un veterinario a un servicio.
     * 
     * @param idServicio identificador del servicio
     * @param idVeterinario identificador del veterinario
     * @return true si la asignacion fue exitosa
     * @throws SQLException si ocurre un error en la base de datos
     */
    boolean asignarVeterinario(int idServicio, int idVeterinario) throws SQLException;
    
    /**
     * Elimina una asignacion de veterinario por su ID.
     * 
     * @param idAsignacion identificador de la asignacion
     * @return true si la eliminacion fue exitosa
     * @throws SQLException si ocurre un error en la base de datos
     */
    boolean eliminarAsignacionVeterinario(int idAsignacion) throws SQLException;
    
    /**
     * Lista los servicios asignados a un veterinario.
     * 
     * @param idVeterinario identificador del veterinario
     * @return lista de servicios del veterinario
     * @throws SQLException si ocurre un error en la base de datos
     */
    List<Servicio> listarPorVeterinario(int idVeterinario) throws SQLException;
    
    /**
     * Busca servicios por nombre.
     * 
     * @param nombre termino de busqueda
     * @return lista de servicios que coinciden
     * @throws SQLException si ocurre un error en la base de datos
     */
    List<Servicio> buscarPorNombre(String nombre) throws SQLException;
    
    /**
     * Elimina una asignacion de veterinario por IDs de veterinario y servicio.
     * 
     * @param idVeterinario identificador del veterinario
     * @param idServicio identificador del servicio
     * @return true si la eliminacion fue exitosa
     * @throws SQLException si ocurre un error en la base de datos
     */
    boolean eliminarAsignacionPorIds(int idVeterinario, int idServicio) throws SQLException;
}