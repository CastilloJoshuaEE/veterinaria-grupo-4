package com.mycompany.veterinaria.grupo4.model.dao;

import com.mycompany.veterinaria.grupo4.model.entity.Cliente;
import java.sql.SQLException;
import java.util.List;

/**
 * Interfaz DAO para la gestion de clientes.
 * <p>
 * Define las operaciones de acceso a datos para la entidad Cliente,
 * incluyendo consultas por cedula, ID, nombre, asi como insercion,
 * actualizacion y eliminacion de clientes.
 * </p>
 * 
 * <p><b>Fecha de inicio del proyecto:</b> 15/04/2026</p>
 * 
 * @author CASTRO AVILA JONATHAN XAVIER – MODULO: CLIENTE
 * @version 1.0
 * @since 1.0
 */
public interface IClienteDAO {
    
    /**
     * Obtiene todos los clientes registrados.
     * 
     * @return lista de todos los clientes
     * @throws SQLException si ocurre un error en la base de datos
     */
    List<Cliente> obtenerTodos() throws SQLException;
    
    /**
     * Obtiene un cliente por su numero de cedula.
     * 
     * @param cedula numero de cedula del cliente
     * @return objeto Cliente encontrado
     * @throws SQLException si ocurre un error en la base de datos
     */
    Cliente obtenerPorCedula(String cedula) throws SQLException;
    
    /**
     * Obtiene un cliente por su identificador.
     * 
     * @param id identificador del cliente
     * @return objeto Cliente encontrado
     * @throws SQLException si ocurre un error en la base de datos
     */
    Cliente obtenerPorId(int id) throws SQLException;
    
    /**
     * Inserta un nuevo cliente en la base de datos.
     * 
     * @param cliente objeto Cliente a insertar
     * @return true si la insercion fue exitosa
     * @throws SQLException si ocurre un error en la base de datos
     */
    boolean insertar(Cliente cliente) throws SQLException;
    
    /**
     * Actualiza los datos de un cliente existente.
     * 
     * @param cliente objeto Cliente con datos actualizados
     * @return true si la actualizacion fue exitosa
     * @throws SQLException si ocurre un error en la base de datos
     */
    boolean actualizar(Cliente cliente) throws SQLException;
    
    /**
     * Elimina un cliente de la base de datos.
     * 
     * @param idCliente identificador del cliente a eliminar
     * @return true si la eliminacion fue exitosa
     * @throws SQLException si ocurre un error en la base de datos
     */
    boolean eliminar(int idCliente) throws SQLException;
    
    /**
     * Obtiene todas las cedulas de clientes registrados.
     * 
     * @return lista de cedulas
     * @throws SQLException si ocurre un error en la base de datos
     */
    List<String> obtenerCedulas() throws SQLException;
    
    /**
     * Busca clientes por nombre.
     * 
     * @param nombre termino de busqueda
     * @return lista de clientes que coinciden con el nombre
     * @throws SQLException si ocurre un error en la base de datos
     */
    List<Cliente> buscarPorNombre(String nombre) throws SQLException;
    
    /**
     * Busca clientes por nombre de mascota o nombre de cliente.
     * 
     * @param nombreMascota nombre de la mascota
     * @param nombreCliente nombre del cliente
     * @return lista de clientes que coinciden
     * @throws SQLException si ocurre un error en la base de datos
     */
    List<Cliente> buscarPorMascota(String nombreMascota, String nombreCliente) throws SQLException;
}