package com.mycompany.veterinaria.grupo4.model.dao;

import com.mycompany.veterinaria.grupo4.model.entity.Veterinario;
import java.sql.SQLException;
import java.util.List;

/**
 * Interfaz DAO para la gestion de veterinarios.
 * <p>
 * Define las operaciones de acceso a datos para la entidad Veterinario,
 * incluyendo consultas de todos, por cedula, ID, nombre, especialidad,
 * servicio, busqueda general, asi como insercion, actualizacion y eliminacion.
 * </p>
 * 
 * <p><b>Fecha de inicio del proyecto:</b> 15/04/2026</p>
 * 
 * @author BESILLA TOMALA ANGEL KALED – MODULO: VETERINARIO
 * @version 1.0
 * @since 1.0
 */
public interface IVeterinarioDAO {
    
    /**
     * Obtiene todos los veterinarios registrados.
     * 
     * @return lista de todos los veterinarios
     * @throws SQLException si ocurre un error en la base de datos
     */
    List<Veterinario> obtenerTodos() throws SQLException;
    
    /**
     * Obtiene un veterinario por su numero de cedula.
     * 
     * @param cedula numero de cedula del veterinario
     * @return objeto Veterinario encontrado
     * @throws SQLException si ocurre un error en la base de datos
     */
    Veterinario obtenerPorCedula(String cedula) throws SQLException;
    
    /**
     * Obtiene un veterinario por su identificador.
     * 
     * @param id identificador del veterinario
     * @return objeto Veterinario encontrado
     * @throws SQLException si ocurre un error en la base de datos
     */
    Veterinario obtenerPorId(int id) throws SQLException;
    
    /**
     * Inserta un nuevo veterinario en la base de datos.
     * 
     * @param veterinario objeto Veterinario a insertar
     * @return true si la insercion fue exitosa
     * @throws SQLException si ocurre un error en la base de datos
     */
    boolean insertar(Veterinario veterinario) throws SQLException;
    
    /**
     * Actualiza los datos de un veterinario existente.
     * 
     * @param veterinario objeto Veterinario con datos actualizados
     * @return true si la actualizacion fue exitosa
     * @throws SQLException si ocurre un error en la base de datos
     */
    boolean actualizar(Veterinario veterinario) throws SQLException;
    
    /**
     * Elimina un veterinario de la base de datos.
     * 
     * @param idVeterinario identificador del veterinario a eliminar
     * @return true si la eliminacion fue exitosa
     * @throws SQLException si ocurre un error en la base de datos
     */
    boolean eliminar(int idVeterinario) throws SQLException;
    
    /**
     * Busca veterinarios por nombre.
     * 
     * @param nombre termino de busqueda
     * @return lista de veterinarios que coinciden con el nombre
     * @throws SQLException si ocurre un error en la base de datos
     */
    List<Veterinario> buscarPorNombre(String nombre) throws SQLException;
    
    /**
     * Busca veterinarios por especialidad.
     * 
     * @param especialidad especialidad a buscar
     * @return lista de veterinarios con esa especialidad
     * @throws SQLException si ocurre un error en la base de datos
     */
    List<Veterinario> buscarPorEspecialidad(String especialidad) throws SQLException;
    
    /**
     * Obtiene los veterinarios asignados a un servicio especifico.
     * 
     * @param idServicio identificador del servicio
     * @return lista de veterinarios asignados al servicio
     * @throws SQLException si ocurre un error en la base de datos
     */
    List<Veterinario> obtenerPorServicio(int idServicio) throws SQLException;
    
    /**
     * Busca veterinarios por termino general (nombre, cedula o especialidad).
     * 
     * @param termino termino de busqueda
     * @return lista de veterinarios que coinciden
     * @throws SQLException si ocurre un error en la base de datos
     */
    List<Veterinario> buscar(String termino) throws SQLException;
}