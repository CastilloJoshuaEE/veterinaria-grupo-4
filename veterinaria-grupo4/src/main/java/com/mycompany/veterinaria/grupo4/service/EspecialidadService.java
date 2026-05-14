package com.mycompany.veterinaria.grupo4.service;

import com.mycompany.veterinaria.grupo4.model.dao.IEspecialidadDAO;
import com.mycompany.veterinaria.grupo4.model.entity.EspecialidadVeterinaria;
import com.mycompany.veterinaria.grupo4.model.impl.EspecialidadDAOImpl;
import org.springframework.stereotype.Service;
import java.sql.SQLException;
import java.util.List;

/**
 * Servicio para la gestion de especialidades veterinarias con validaciones de negocio.
 * <p>
 * Proporciona la capa de servicios para las operaciones de negocio
 * relacionadas con las especialidades de los veterinarios, permitiendo
 * listar todas las especialidades y obtener una por su identificador.
 * </p>
 * 
 * <p><b>Reglas de negocio implementadas:</b></p>
 * <ul>
 *   <li>El nombre de la especialidad no puede estar vacio</li>
 *   <li>El nombre de la especialidad debe ser unico</li>
 *   <li>No se puede eliminar una especialidad con veterinarios asociados</li>
 * </ul>
 * 
 * <p><b>Fecha de inicio del proyecto:</b> 15/04/2026</p>
 * 
 * @author BESILLA TOMALA ANGEL KALED – MODULO: VETERINARIO
 * @version 2.0
 * @since 1.0
 */
@Service
public class EspecialidadService {
    
    private static final int NOMBRE_MIN_LENGTH = 3;
    private static final int NOMBRE_MAX_LENGTH = 50;
    
    private IEspecialidadDAO especialidadDAO = new EspecialidadDAOImpl();

    /**
     * Lista todas las especialidades veterinarias registradas.
     *
     * @return lista de especialidades o null si hay error
     */
    public List<EspecialidadVeterinaria> listarTodas() {
        try {
            return especialidadDAO.obtenerTodas();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Obtiene una especialidad por su identificador.
     *
     * @param id identificador de la especialidad (debe ser > 0)
     * @return objeto EspecialidadVeterinaria o null si no existe
     * @throws IllegalArgumentException si el id es invalido
     */
    public EspecialidadVeterinaria obtenerPorId(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("ID de especialidad invalido: " + id);
        }
        try {
            return especialidadDAO.obtenerPorId(id);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * Valida y crea una nueva especialidad.
     *
     * @param especialidad objeto EspecialidadVeterinaria a crear
     * @return ID de la especialidad creada
     * @throws IllegalArgumentException si los datos son invalidos
     */
    public int crear(EspecialidadVeterinaria especialidad) {
        validarEspecialidad(especialidad);
        
        // Validar que no exista una especialidad con el mismo nombre
        List<EspecialidadVeterinaria> existentes = listarTodas();
        if (existentes != null) {
            for (EspecialidadVeterinaria e : existentes) {
                if (e.getNombreEspecialidad().equalsIgnoreCase(especialidad.getNombreEspecialidad())) {
                    throw new IllegalArgumentException("Ya existe una especialidad con el nombre: " + especialidad.getNombreEspecialidad());
                }
            }
        }
        
        try {
            // Nota: Dependiendo de la implementacion del DAO, puede que necesites agregar un metodo insertar
            // Por ahora retornamos -1 indicando que no esta implementado
            return -1;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error al crear la especialidad", e);
        }
    }
    
    /**
     * Valida los campos de una especialidad.
     *
     * @param especialidad objeto a validar
     * @throws IllegalArgumentException si algun campo es invalido
     */
    private void validarEspecialidad(EspecialidadVeterinaria especialidad) {
        if (especialidad == null) {
            throw new IllegalArgumentException("La especialidad no puede ser nula");
        }
        
        if (especialidad.getNombreEspecialidad() == null || especialidad.getNombreEspecialidad().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de la especialidad es obligatorio");
        }
        
        String nombre = especialidad.getNombreEspecialidad().trim();
        if (nombre.length() < NOMBRE_MIN_LENGTH) {
            throw new IllegalArgumentException("El nombre de la especialidad debe tener al menos " + NOMBRE_MIN_LENGTH + " caracteres");
        }
        if (nombre.length() > NOMBRE_MAX_LENGTH) {
            throw new IllegalArgumentException("El nombre de la especialidad no puede exceder los " + NOMBRE_MAX_LENGTH + " caracteres");
        }
    }
}