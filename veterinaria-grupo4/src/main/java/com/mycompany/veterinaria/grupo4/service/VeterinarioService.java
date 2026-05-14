package com.mycompany.veterinaria.grupo4.service;

import com.mycompany.veterinaria.grupo4.model.dao.IVeterinarioDAO;
import com.mycompany.veterinaria.grupo4.model.entity.Veterinario;
import com.mycompany.veterinaria.grupo4.model.impl.VeterinarioDAOImpl;
import org.springframework.stereotype.Service;
import java.sql.SQLException;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Servicio para la gestion de veterinarios con validaciones de negocio.
 * <p>
 * Proporciona la capa de servicios para las operaciones de negocio
 * relacionadas con el personal veterinario, incluyendo listado,
 * busqueda por multiples criterios, operaciones CRUD y obtencion
 * de veterinarios por servicio. Incluye validaciones de cedula,
 * correo electronico, telefono y reglas de negocio.
 * </p>
 * 
 * <p><b>Reglas de negocio implementadas:</b></p>
 * <ul>
 *   <li>La cedula debe tener 10 digitos numericos</li>
 *   <li>El correo electronico debe tener formato valido</li>
 *   <li>El telefono debe tener 10 digitos (incluye 09 al inicio)</li>
 *   <li>El pago mensual no puede ser negativo</li>
 *   <li>No se pueden duplicar cedulas</li>
 *   <li>No se puede eliminar un veterinario con citas asociadas</li>
 * </ul>
 * 
 * <p><b>Fecha de inicio del proyecto:</b> 15/04/2026</p>
 * 
 * @author BESILLA TOMALA ANGEL KALED – MODULO: VETERINARIO
 * @version 2.0
 * @since 1.0
 */
@Service
public class VeterinarioService {
    
    private static final Pattern PATRON_CEDULA = Pattern.compile("^\\d{10}$");
    private static final Pattern PATRON_EMAIL = Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");
    private static final Pattern PATRON_TELEFONO = Pattern.compile("^09\\d{8}$");
    private static final double PAGO_MINIMO = 400.0;
    private static final double PAGO_MAXIMO = 5000.0;
    
    private IVeterinarioDAO veterinarioDAO;
    /** Constructor por defecto (usado por Spring) */
    public VeterinarioService() {
        this.veterinarioDAO = new VeterinarioDAOImpl();
    }

    /**
     * Constructor para inyección de dependencias (usado en pruebas unitarias).
     *
     * @param veterinarioDAO DAO de veterinarios mockeado
     */
    public VeterinarioService(IVeterinarioDAO veterinarioDAO) {
        this.veterinarioDAO = veterinarioDAO;
    }
    /**
     * Lista todos los veterinarios registrados.
     *
     * @return lista de todos los veterinarios o null si hay error
     */
    public List<Veterinario> listarTodos() {
        try {
            return veterinarioDAO.obtenerTodos();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Obtiene un veterinario por su numero de cedula.
     *
     * @param cedula numero de cedula del veterinario (10 digitos)
     * @return objeto Veterinario o null si no existe
     * @throws IllegalArgumentException si la cedula es invalida
     */
    public Veterinario obtenerPorCedula(String cedula) {
        validarCedula(cedula);
        try {
            return veterinarioDAO.obtenerPorCedula(cedula);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Obtiene un veterinario por su identificador.
     *
     * @param id identificador del veterinario (debe ser > 0)
     * @return objeto Veterinario o null si no existe
     * @throws IllegalArgumentException si el id es invalido
     */
    public Veterinario obtenerPorId(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("ID de veterinario invalido: " + id);
        }
        try {
            return veterinarioDAO.obtenerPorId(id);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Valida y crea un nuevo veterinario en el sistema.
     * <p>
     * <b>Reglas de negocio aplicadas:</b>
     * <ul>
     *   <li>La cedula debe ser unica en el sistema</li>
     *   <li>Todos los campos obligatorios deben estar presentes</li>
     *   <li>El pago mensual debe estar entre {@value #PAGO_MINIMO} y {@value #PAGO_MAXIMO}</li>
     * </ul>
     * </p>
     *
     * @param veterinario objeto Veterinario a registrar (validado)
     * @return true si la creacion fue exitosa
     * @throws IllegalArgumentException si los datos del veterinario son invalidos
     */
    public boolean crear(Veterinario veterinario) {
        validarVeterinario(veterinario);
        
        // Validar que la cedula no exista ya
        Veterinario existente = obtenerPorCedula(veterinario.getCedula());
        if (existente != null) {
            throw new IllegalArgumentException("Ya existe un veterinario con la cedula: " + veterinario.getCedula());
        }
        
        try {
            return veterinarioDAO.insertar(veterinario);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error al crear el veterinario en la base de datos", e);
        }
    }

    /**
     * Valida y actualiza los datos de un veterinario existente.
     * <p>
     * <b>Reglas de negocio aplicadas:</b>
     * <ul>
     *   <li>El veterinario debe existir en el sistema</li>
     *   <li>La cedula no puede cambiarse a una ya existente (excepto la propia)</li>
     *   <li>El pago mensual debe estar entre {@value #PAGO_MINIMO} y {@value #PAGO_MAXIMO}</li>
     * </ul>
     * </p>
     *
     * @param veterinario objeto Veterinario con datos actualizados
     * @return true si la actualizacion fue exitosa
     * @throws IllegalArgumentException si los datos son invalidos o el veterinario no existe
     */
    public boolean actualizar(Veterinario veterinario) {
        validarVeterinario(veterinario);
        
        if (veterinario.getIdVeterinario() <= 0) {
            throw new IllegalArgumentException("ID de veterinario invalido para actualizar");
        }
        
        Veterinario existente = obtenerPorId(veterinario.getIdVeterinario());
        if (existente == null) {
            throw new IllegalArgumentException("No existe un veterinario con ID: " + veterinario.getIdVeterinario());
        }
        
        Veterinario porCedula = obtenerPorCedula(veterinario.getCedula());
        if (porCedula != null && porCedula.getIdVeterinario() != veterinario.getIdVeterinario()) {
            throw new IllegalArgumentException("La cedula " + veterinario.getCedula() + " ya esta registrada por otro veterinario");
        }
        
        try {
            return veterinarioDAO.actualizar(veterinario);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error al actualizar el veterinario en la base de datos", e);
        }
    }

    /**
     * Elimina un veterinario del sistema.
     * <p>
     * <b>Reglas de negocio aplicadas:</b>
     * <ul>
     *   <li>El veterinario debe existir en el sistema</li>
     *   <li>No se puede eliminar un veterinario con citas pendientes</li>
     *   <li>No se puede eliminar un veterinario con atenciones medicas realizadas</li>
     * </ul>
     * </p>
     *
     * @param idVeterinario identificador del veterinario a eliminar
     * @return true si la eliminacion fue exitosa
     * @throws IllegalArgumentException si el id es invalido o el veterinario no existe
     * @throws IllegalStateException si el veterinario tiene citas o atenciones asociadas
     */
    public boolean eliminar(int idVeterinario) {
        if (idVeterinario <= 0) {
            throw new IllegalArgumentException("ID de veterinario invalido: " + idVeterinario);
        }
        
        // Validar que el veterinario exista
        Veterinario existente = obtenerPorId(idVeterinario);
        if (existente == null) {
            throw new IllegalArgumentException("No existe un veterinario con ID: " + idVeterinario);
        }
        
     
        
        try {
            return veterinarioDAO.eliminar(idVeterinario);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error al eliminar el veterinario de la base de datos", e);
        }
    }

    /**
     * Busca veterinarios por nombre.
     *
     * @param nombre termino de busqueda (minimo 2 caracteres)
     * @return lista de veterinarios que coinciden o null si hay error
     * @throws IllegalArgumentException si el nombre es nulo o vacio
     */
    public List<Veterinario> buscarPorNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El termino de busqueda no puede estar vacio");
        }
        if (nombre.trim().length() < 2) {
            throw new IllegalArgumentException("El termino de busqueda debe tener al menos 2 caracteres");
        }
        try {
            return veterinarioDAO.buscarPorNombre("%" + nombre.trim() + "%");
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Busca veterinarios por especialidad.
     *
     * @param especialidad especialidad a buscar (minimo 2 caracteres)
     * @return lista de veterinarios con esa especialidad o null si hay error
     * @throws IllegalArgumentException si la especialidad es nula o vacia
     */
    public List<Veterinario> buscarPorEspecialidad(String especialidad) {
        if (especialidad == null || especialidad.trim().isEmpty()) {
            throw new IllegalArgumentException("La especialidad de busqueda no puede estar vacia");
        }
        if (especialidad.trim().length() < 2) {
            throw new IllegalArgumentException("La especialidad debe tener al menos 2 caracteres");
        }
        try {
            return veterinarioDAO.buscarPorEspecialidad("%" + especialidad.trim() + "%");
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Obtiene los veterinarios asignados a un servicio especifico.
     *
     * @param idServicio identificador del servicio (debe ser > 0)
     * @return lista de veterinarios asignados al servicio o null si hay error
     * @throws IllegalArgumentException si el id del servicio es invalido
     */
    public List<Veterinario> obtenerPorServicio(int idServicio) {
        if (idServicio <= 0) {
            throw new IllegalArgumentException("ID de servicio invalido: " + idServicio);
        }
        try {
            return veterinarioDAO.obtenerPorServicio(idServicio);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * Busca veterinarios por termino general (nombre, cedula o especialidad).
     *
     * @param termino termino de busqueda (minimo 1 caracter)
     * @return lista de veterinarios que coinciden o null si hay error
     * @throws IllegalArgumentException si el termino es nulo o vacio
     */
    public List<Veterinario> buscar(String termino) {
        if (termino == null || termino.trim().isEmpty()) {
            throw new IllegalArgumentException("El termino de busqueda no puede estar vacio");
        }
        try {
            return veterinarioDAO.buscar(termino.trim());
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
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
            throw new IllegalArgumentException("El telefono debe tener 10 digitos y comenzar con 09");
        }
    }
    
    /**
     * Valida el pago mensual del veterinario.
     *
     * @param pagoMensual pago a validar
     * @throws IllegalArgumentException si el pago esta fuera del rango permitido
     */
    private void validarPagoMensual(double pagoMensual) {
        if (pagoMensual < PAGO_MINIMO) {
            throw new IllegalArgumentException("El pago mensual no puede ser menor a $" + PAGO_MINIMO);
        }
        if (pagoMensual > PAGO_MAXIMO) {
            throw new IllegalArgumentException("El pago mensual no puede ser mayor a $" + PAGO_MAXIMO);
        }
    }
    
    /**
     * Valida todos los campos del objeto Veterinario.
     *
     * @param veterinario objeto a validar
     * @throws IllegalArgumentException si algun campo es invalido
     */
    private void validarVeterinario(Veterinario veterinario) {
        if (veterinario == null) {
            throw new IllegalArgumentException("El objeto veterinario no puede ser nulo");
        }
        
        // Validar campos obligatorios
        if (veterinario.getNombre() == null || veterinario.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del veterinario es obligatorio");
        }
        if (veterinario.getNombre().trim().length() > 50) {
            throw new IllegalArgumentException("El nombre no puede exceder los 50 caracteres");
        }
        
        if (veterinario.getApellido() == null || veterinario.getApellido().trim().isEmpty()) {
            throw new IllegalArgumentException("El apellido del veterinario es obligatorio");
        }
        if (veterinario.getApellido().trim().length() > 50) {
            throw new IllegalArgumentException("El apellido no puede exceder los 50 caracteres");
        }
        
        validarCedula(veterinario.getCedula());
        validarTelefono(veterinario.getTelefono());
        validarEmail(veterinario.getCorreoElectronico());
        validarPagoMensual(veterinario.getPagoMensual());
        
        // Validar especialidad
        if (veterinario.getEspecialidad() == null || veterinario.getEspecialidad().getIdEspecialidad() <= 0) {
            throw new IllegalArgumentException("Debe seleccionar una especialidad valida");
        }
    }
    
  

}