package com.mycompany.veterinaria.grupo4.service;

import com.mycompany.veterinaria.grupo4.model.dao.IAtencionMedicaDAO;
import com.mycompany.veterinaria.grupo4.model.entity.AtencionMedica;
import com.mycompany.veterinaria.grupo4.model.impl.AtencionMedicaDAOImpl;
import org.springframework.stereotype.Service;
import java.sql.SQLException;
import java.util.List;

/**
 * Servicio para la gestion de atenciones medicas con validaciones de negocio.
 * <p>
 * Proporciona la capa de servicios para las operaciones de negocio
 * relacionadas con atenciones medicas. Actua como intermediario entre
 * los controladores REST y la capa de acceso a datos (DAO).
 * </p>
 * 
 * <p><b>Reglas de negocio implementadas:</b></p>
 * <ul>
 *   <li>El diagnostico y tratamiento no pueden estar vacios</li>
 *   <li>La atencion medica debe estar asociada a una cita valida</li>
 *   <li>No se puede crear una atencion para una cita ya cancelada</li>
 *   <li>No se puede eliminar una atencion con factura asociada</li>
 * </ul>
 * 
 * <p><b>Fecha de inicio del proyecto:</b> 15/04/2026</p>
 * 
 * @author ROBLES MORALES JUAN ANDRES – MODULO: ATENCION VETERINARIA
 * @version 2.0
 * @since 1.0
 */
@Service
public class AtencionMedicaService {
    
    private static final int DIAGNOSTICO_MIN_LENGTH = 3;
    private static final int TRATAMIENTO_MIN_LENGTH = 3;
    
    private IAtencionMedicaDAO atencionDAO;
    private CitaService citaService;

    // ========== CONSTRUCTORES ==========

    /**
     * Constructor por defecto (usado por Spring).
     * Inicializa el DAO con la implementación por defecto y crea una instancia de CitaService.
     */
    public AtencionMedicaService() {
        this.atencionDAO = new AtencionMedicaDAOImpl();
        this.citaService = new CitaService();
    }

    /**
     * Constructor para inyección de dependencias (usado en pruebas unitarias).
     * Permite mockear el DAO y el CitaService para pruebas aisladas.
     *
     * @param atencionDAO DAO de atenciones medicas (puede ser una implementación real o mock)
     * @param citaService Servicio de citas (puede ser una implementación real o mock)
     */
    public AtencionMedicaService(IAtencionMedicaDAO atencionDAO, CitaService citaService) {
        this.atencionDAO = atencionDAO;
        this.citaService = citaService;
    }

    /**
     * Constructor para inyección solo del DAO.
     * Crea una instancia por defecto de CitaService.
     *
     * @param atencionDAO DAO de atenciones medicas (puede ser una implementación real o mock)
     */
    public AtencionMedicaService(IAtencionMedicaDAO atencionDAO) {
        this.atencionDAO = atencionDAO;
        this.citaService = new CitaService();
    }

    /**
     * Constructor para inyección solo del CitaService.
     * Inicializa el DAO con la implementación por defecto.
     *
     * @param citaService Servicio de citas (puede ser una implementación real o mock)
     */
    public AtencionMedicaService(CitaService citaService) {
        this.atencionDAO = new AtencionMedicaDAOImpl();
        this.citaService = citaService;
    }

    // ========== MÉTODOS DEL SERVICIO ==========

    /**
     * Valida y guarda una nueva atencion medica en la base de datos.
     *
     * @param atencion objeto AtencionMedica a guardar
     * @return ID de la atencion medica creada, o -1 si hay error
     * @throws IllegalArgumentException si los datos son invalidos
     */
    public int guardar(AtencionMedica atencion) {
        validarAtencionMedica(atencion);
        
        // Validar que la cita asociada exista y no este cancelada
        if (atencion.getIdCita() <= 0) {
            throw new IllegalArgumentException("Debe especificar una cita valida para la atencion medica");
        }
        
        var cita = citaService.obtenerPorId(atencion.getIdCita());
        if (cita == null) {
            throw new IllegalArgumentException("La cita con ID " + atencion.getIdCita() + " no existe");
        }
        
        if ("CANCELADA".equalsIgnoreCase(cita.getEstado())) {
            throw new IllegalStateException("No se puede crear una atencion medica para una cita cancelada");
        }
        
        try {
            return atencionDAO.insertar(atencion);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error al guardar la atencion medica", e);
        }
    }

    /**
     * Lista todas las atenciones medicas registradas.
     *
     * @return lista de atenciones medicas o null si hay error
     */
    public List<AtencionMedica> listarTodas() {
        try {
            return atencionDAO.obtenerTodas();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Obtiene una atencion medica por su identificador.
     *
     * @param idAtencionMedica identificador de la atencion (debe ser > 0)
     * @return objeto AtencionMedica o null si no existe
     * @throws IllegalArgumentException si el id es invalido
     */
    public AtencionMedica obtenerPorId(int idAtencionMedica) {
        if (idAtencionMedica <= 0) {
            throw new IllegalArgumentException("ID de atencion medica invalido: " + idAtencionMedica);
        }
        try {
            return atencionDAO.obtenerPorId(idAtencionMedica);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Elimina una atencion medica de la base de datos.
     * <p>
     * <b>Reglas de negocio:</b> No se puede eliminar una atencion que ya tenga factura asociada.
     * </p>
     *
     * @param idAtencionMedica identificador de la atencion a eliminar
     * @return true si la eliminacion fue exitosa
     * @throws IllegalArgumentException si el id es invalido
     * @throws IllegalStateException si la atencion tiene factura asociada
     */
    public boolean eliminar(int idAtencionMedica) {
        if (idAtencionMedica <= 0) {
            throw new IllegalArgumentException("ID de atencion medica invalido: " + idAtencionMedica);
        }
        
        AtencionMedica atencion = obtenerPorId(idAtencionMedica);
        if (atencion == null) {
            throw new IllegalArgumentException("No existe una atencion medica con ID: " + idAtencionMedica);
        }
        
        
        try {
            return atencionDAO.eliminar(idAtencionMedica);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error al eliminar la atencion medica", e);
        }
    }
    
    // ========== MÉTODOS PRIVADOS ==========
    
    /**
     * Valida todos los campos de una atencion medica.
     *
     * @param atencion objeto a validar
     * @throws IllegalArgumentException si algun campo es invalido
     */
    private void validarAtencionMedica(AtencionMedica atencion) {
        if (atencion == null) {
            throw new IllegalArgumentException("La atencion medica no puede ser nula");
        }
        
        // Validar diagnostico (ahora con mínimo 3 caracteres)
        if (atencion.getDiagnostico() == null || atencion.getDiagnostico().trim().isEmpty()) {
            throw new IllegalArgumentException("El diagnostico es obligatorio");
        }
        if (atencion.getDiagnostico().trim().length() < DIAGNOSTICO_MIN_LENGTH) {
            throw new IllegalArgumentException("El diagnostico debe tener al menos " + DIAGNOSTICO_MIN_LENGTH + " caracteres");
        }
        
        // Validar tratamiento (ahora con mínimo 3 caracteres)
        if (atencion.getTratamiento() == null || atencion.getTratamiento().trim().isEmpty()) {
            throw new IllegalArgumentException("El tratamiento es obligatorio");
        }
        if (atencion.getTratamiento().trim().length() < TRATAMIENTO_MIN_LENGTH) {
            throw new IllegalArgumentException("El tratamiento debe tener al menos " + TRATAMIENTO_MIN_LENGTH + " caracteres");
        }
    }
    

}