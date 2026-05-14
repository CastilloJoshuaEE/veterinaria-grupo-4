package com.mycompany.veterinaria.grupo4.service;

import com.mycompany.veterinaria.grupo4.model.dao.IClienteDAO;
import com.mycompany.veterinaria.grupo4.model.entity.Cliente;
import com.mycompany.veterinaria.grupo4.model.impl.ClienteDAOImpl;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Servicio para la gestion de clientes con validaciones de negocio.
 * <p>
 * Proporciona la capa de servicios para las operaciones de negocio
 * relacionadas con la gestion de clientes del sistema veterinario,
 * incluyendo altas, bajas, modificaciones y consultas.
 * </p>
 * 
 * <p><b>Reglas de negocio implementadas:</b></p>
 * <ul>
 *   <li>La cedula debe tener 10 digitos (validacion flexible para datos de prueba)</li>
 *   <li>El correo electronico debe tener formato valido (opcional)</li>
 *   <li>El telefono debe tener 10 digitos</li>
 *   <li>No se pueden duplicar cedulas</li>
 *   <li>No se puede eliminar un cliente con mascotas registradas</li>
 * </ul>
 * 
 * <p><b>Fecha de inicio del proyecto:</b> 15/04/2026</p>
 * 
 * @author CASTRO AVILA JONATHAN XAVIER – MODULO: CLIENTE
 * @version 2.0
 * @since 1.0
 */
@Service
public class ClienteService {
    
    private static final Pattern PATRON_CEDULA = Pattern.compile("^\\d{10}$");
    private static final Pattern PATRON_EMAIL = Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");
    private static final Pattern PATRON_TELEFONO = Pattern.compile("^\\d{10}$");
    
    private IClienteDAO clienteDAO;
    /**
     * Constructor por defecto (usado por Spring).
     * Inicializa el DAO con la implementación por defecto.
     */
    public ClienteService() {
        this.clienteDAO = new ClienteDAOImpl();
    }
    
    /**
     * Constructor para inyección de dependencias (usado en pruebas unitarias).
     * Permite mockear el DAO para pruebas aisladas.
     *
     * @param clienteDAO DAO de clientes (puede ser una implementación real o mock)
     */
    public ClienteService(IClienteDAO clienteDAO) {
        this.clienteDAO = clienteDAO;
    }    
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
     * <p>
     * <b>Nota:</b> La validacion de cedula es flexible para permitir
     * datos de prueba y cedulas semilla en la base de datos.
     * </p>
     *
     * @param cedula numero de cedula del cliente (10 digitos)
     * @return objeto Cliente encontrado
     * @throws IllegalArgumentException si la cedula es invalida
     */
    public Cliente obtenerPorCedula(String cedula) {
        validarCedulaConsulta(cedula);
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
     * @param id identificador del cliente (debe ser > 0)
     * @return objeto Cliente encontrado
     * @throws IllegalArgumentException si el id es invalido
     */
    public Cliente obtenerPorId(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("ID de cliente invalido: " + id);
        }
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
     * @param nombre termino de busqueda (minimo 2 caracteres)
     * @return lista de clientes que coinciden con el nombre
     * @throws IllegalArgumentException si el nombre es nulo o muy corto
     */
    public List<Cliente> buscarPorNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El termino de busqueda no puede estar vacio");
        }
        if (nombre.trim().length() < 2) {
            throw new IllegalArgumentException("El termino de busqueda debe tener al menos 2 caracteres");
        }
        try {
            return clienteDAO.buscarPorNombre("%" + nombre.trim() + "%");
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * Valida y crea un nuevo cliente en el sistema.
     * <p>
     * <b>Para creacion se aplica validacion estricta de cedula.</b>
     * </p>
     *
     * @param cliente objeto Cliente a registrar
     * @return true si la creacion fue exitosa
     * @throws IllegalArgumentException si los datos del cliente son invalidos
     */
    public boolean crear(Cliente cliente) {
        validarCliente(cliente);
        validarCedula(cliente.getCedula());
        
        // Validar que la cedula no exista ya
        Cliente existente = obtenerPorCedula(cliente.getCedula());
        if (existente != null) {
            throw new IllegalArgumentException("Ya existe un cliente con la cedula: " + cliente.getCedula());
        }
        
        try {
            return clienteDAO.insertar(cliente);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error al crear el cliente en la base de datos", e);
        }
    }
    
    /**
     * Valida y actualiza los datos de un cliente existente.
     *
     * @param cliente objeto Cliente con datos actualizados
     * @return true si la actualizacion fue exitosa
     * @throws IllegalArgumentException si los datos son invalidos o el cliente no existe
     */
    public boolean actualizar(Cliente cliente) {
        validarCliente(cliente);
        
        if (cliente.getIdCliente() <= 0) {
            throw new IllegalArgumentException("ID de cliente invalido para actualizar");
        }
        
        // Validar que el cliente exista
        Cliente existente = obtenerPorId(cliente.getIdCliente());
        if (existente == null) {
            throw new IllegalArgumentException("No existe un cliente con ID: " + cliente.getIdCliente());
        }
        
        // Validar que la cedula no este siendo usada por otro cliente
        Cliente porCedula = obtenerPorCedula(cliente.getCedula());
        if (porCedula != null && porCedula.getIdCliente() != cliente.getIdCliente()) {
            throw new IllegalArgumentException("La cedula " + cliente.getCedula() + " ya esta registrada por otro cliente");
        }
        
        try {
            return clienteDAO.actualizar(cliente);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error al actualizar el cliente en la base de datos", e);
        }
    }
    
    /**
     * Elimina un cliente del sistema.
     * <p>
     * <b>Reglas de negocio:</b> No se puede eliminar un cliente con mascotas registradas.
     * </p>
     *
     * @param idCliente identificador del cliente a eliminar
     * @return true si la eliminacion fue exitosa
     * @throws IllegalArgumentException si el id es invalido
     * @throws IllegalStateException si el cliente tiene mascotas asociadas
     */
    public boolean eliminar(int idCliente) {
        if (idCliente <= 0) {
            throw new IllegalArgumentException("ID de cliente invalido: " + idCliente);
        }
        
        Cliente existente = obtenerPorId(idCliente);
        if (existente == null) {
            throw new IllegalArgumentException("No existe un cliente con ID: " + idCliente);
        }
        
        // Validar que no tenga mascotas asociadas
        if (tieneMascotasAsociadas(idCliente)) {
            throw new IllegalStateException("No se puede eliminar el cliente porque tiene mascotas registradas");
        }
        
        try {
            return clienteDAO.eliminar(idCliente);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error al eliminar el cliente de la base de datos", e);
        }
    }
    
    /**
     * Obtiene todas las cedulas de clientes registrados.
     * <p>
     * <b>Nota:</b> Este metodo NO valida el digito verificador para permitir
     * mostrar todas las cedulas existentes en el sistema, incluyendo datos de prueba.
     * </p>
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
    
    /**
     * Validacion basica de cedula para consultas (solo formato, no digito verificador).
     * <p>
     * Esta validacion es mas permisiva para permitir consultar cedulas
     * que puedan existir en la base de datos como datos de prueba.
     * </p>
     *
     * @param cedula cedula a validar
     * @throws IllegalArgumentException si la cedula es invalida
     */
    private void validarCedulaConsulta(String cedula) {
        if (cedula == null || cedula.trim().isEmpty()) {
            throw new IllegalArgumentException("La cedula es obligatoria");
        }
        if (!PATRON_CEDULA.matcher(cedula).matches()) {
            throw new IllegalArgumentException("La cedula debe contener exactamente 10 digitos numericos");
        }
        // No se valida el digito verificador en consultas
    }
    
    
    /**
     * Valida la estructura de la cedula .
     *
     * @param cedula cedula a validar
     * @throws IllegalArgumentException si la cedula es invalida
     */
    private void validarCedula(String cedula) {
        if (cedula == null || cedula.trim().isEmpty()) {
            throw new IllegalArgumentException("La cedula es obligatoria");
        }
        if (!PATRON_CEDULA.matcher(cedula).matches()) {
            throw new IllegalArgumentException("La cedula debe contener exactamente 10 digitos numericos");
        }
       
    }
    /**
     * Valida el formato del correo electronico.
     *
     * @param email correo a validar
     * @throws IllegalArgumentException si el email es invalido
     */
    private void validarEmail(String email) {
        if (email != null && !email.trim().isEmpty()) {
            if (!PATRON_EMAIL.matcher(email).matches()) {
                throw new IllegalArgumentException("El formato del correo electronico es invalido");
            }
        }
    }
    
    /**
     * Valida el formato del telefono.
     *
     * @param telefono telefono a validar
     * @throws IllegalArgumentException si el telefono es invalido
     */
    private void validarTelefono(String telefono) {
        if (telefono == null || telefono.trim().isEmpty()) {
            throw new IllegalArgumentException("El telefono es obligatorio");
        }
        if (!PATRON_TELEFONO.matcher(telefono).matches()) {
            throw new IllegalArgumentException("El telefono debe tener exactamente 10 digitos numericos");
        }
    }
    
    /**
     * Valida todos los campos del objeto Cliente.
     *
     * @param cliente objeto a validar
     * @throws IllegalArgumentException si algun campo es invalido
     */
    private void validarCliente(Cliente cliente) {
        if (cliente == null) {
            throw new IllegalArgumentException("El objeto cliente no puede ser nulo");
        }
        
        if (cliente.getNombre() == null || cliente.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del cliente es obligatorio");
        }
        if (cliente.getNombre().trim().length() > 50) {
            throw new IllegalArgumentException("El nombre no puede exceder los 50 caracteres");
        }
        
        if (cliente.getApellido() == null || cliente.getApellido().trim().isEmpty()) {
            throw new IllegalArgumentException("El apellido del cliente es obligatorio");
        }
        if (cliente.getApellido().trim().length() > 50) {
            throw new IllegalArgumentException("El apellido no puede exceder los 50 caracteres");
        }
        
        validarCedula(cliente.getCedula());
        validarTelefono(cliente.getTelefono());
        validarEmail(cliente.getCorreoElectronico());
    }
    
    /**
     * Verifica si un cliente tiene mascotas asociadas.
     *
     * @param idCliente identificador del cliente
     * @return true si tiene mascotas asociadas
     */
    private boolean tieneMascotasAsociadas(int idCliente) {
        MascotaService mascotaService = new MascotaService();
        try {
            List<?> mascotas = mascotaService.listarPorCliente(idCliente);
            return mascotas != null && !mascotas.isEmpty();
        } catch (Exception e) {
            return false;
        }
    }
}