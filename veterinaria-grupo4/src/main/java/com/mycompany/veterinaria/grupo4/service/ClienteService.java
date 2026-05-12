package com.mycompany.veterinaria.grupo4.service;

import com.mycompany.veterinaria.grupo4.model.dao.IClienteDAO;
import com.mycompany.veterinaria.grupo4.model.entity.Cliente;
import com.mycompany.veterinaria.grupo4.model.impl.ClienteDAOImpl;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

/**
 * Servicio para la gestion de clientes.
 * <p>
 * Proporciona la capa de servicios para las operaciones de negocio
 * relacionadas con la gestion de clientes del sistema veterinario,
 * incluyendo altas, bajas, modificaciones y consultas.
 * </p>
 * 
 * <p><b>Fecha de inicio del proyecto:</b> 15/04/2026</p>
 * 
 * @author CASTRO AVILA JONATHAN XAVIER – MODULO: CLIENTE
 * @version 1.0
 * @since 1.0
 */
@Service
public class ClienteService {
    private IClienteDAO clienteDAO = new ClienteDAOImpl();
    
    /**
     * Lista todos los clientes registrados.
     *
     * @return lista de todos los clientes
     */
    public List<Cliente> listarTodos() {
        try {
            return clienteDAO.obtenerTodos();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * Obtiene un cliente por su numero de cedula.
     *
     * @param cedula numero de cedula del cliente
     * @return objeto Cliente encontrado
     */
    public Cliente obtenerPorCedula(String cedula) {
        try {
            return clienteDAO.obtenerPorCedula(cedula);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * Obtiene un cliente por su identificador.
     *
     * @param id identificador del cliente
     * @return objeto Cliente encontrado
     */
    public Cliente obtenerPorId(int id) {
        try {
            return clienteDAO.obtenerPorId(id);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * Busca clientes por nombre.
     *
     * @param nombre termino de busqueda
     * @return lista de clientes que coinciden con el nombre
     */
    public List<Cliente> buscarPorNombre(String nombre) {
        try {
            return clienteDAO.buscarPorNombre(nombre);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * Crea un nuevo cliente en el sistema.
     *
     * @param cliente objeto Cliente a registrar
     * @return true si la creacion fue exitosa
     */
    public boolean crear(Cliente cliente) {
        try {
            return clienteDAO.insertar(cliente);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Actualiza los datos de un cliente existente.
     *
     * @param cliente objeto Cliente con datos actualizados
     * @return true si la actualizacion fue exitosa
     */
    public boolean actualizar(Cliente cliente) {
        try {
            return clienteDAO.actualizar(cliente);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Elimina un cliente del sistema.
     *
     * @param idCliente identificador del cliente a eliminar
     * @return true si la eliminacion fue exitosa
     */
    public boolean eliminar(int idCliente) {
        try {
            return clienteDAO.eliminar(idCliente);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Obtiene todas las cedulas de clientes registrados.
     *
     * @return lista de cedulas
     */
    public List<String> obtenerCedulas() {
        try {
            return clienteDAO.obtenerCedulas();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}