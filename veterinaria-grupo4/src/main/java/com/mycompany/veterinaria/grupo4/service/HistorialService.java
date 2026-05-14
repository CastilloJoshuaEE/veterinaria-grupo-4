package com.mycompany.veterinaria.grupo4.service;

import com.mycompany.veterinaria.grupo4.model.dao.IHistorialDAO;
import com.mycompany.veterinaria.grupo4.model.entity.HistorialMedico;
import com.mycompany.veterinaria.grupo4.model.impl.HistorialDAOImpl;
import org.springframework.stereotype.Service;
import java.sql.SQLException;
import java.util.List;

/**
 * Servicio para la gestion del historial medico con validaciones de negocio.
 * <p>
 * Proporciona la capa de servicios para las operaciones de negocio
 * relacionadas con el historial clinico de las mascotas, permitiendo
 * obtener el historial completo y registrar nuevas entradas.
 * </p>
 * 
 * <p><b>Reglas de negocio implementadas:</b></p>
 * <ul>
 *   <li>La mascota debe existir en el sistema</li>
 *   <li>No se pueden registrar entradas duplicadas para la misma atencion</li>
 *   <li>Al menos uno de los IDs (cita o atencion) debe ser valido</li>
 * </ul>
 * 
 * <p><b>Fecha de inicio del proyecto:</b> 15/04/2026</p>
 * 
 * @author CASTILLO MEREJILDO JOSHUA JAVIER – MODULO: MASCOTA
 * @version 2.0
 * @since 1.0
 */
@Service
public class HistorialService {
    
    private IHistorialDAO historialDAO = new HistorialDAOImpl();

    /**
     * Obtiene el historial medico completo de una mascota.
     *
     * @param idMascota identificador de la mascota (debe ser > 0)
     * @return lista de registros del historial o null si hay error
     * @throws IllegalArgumentException si el id es invalido
     */
    public List<HistorialMedico> obtenerPorMascota(int idMascota) {
        if (idMascota <= 0) {
            throw new IllegalArgumentException("ID de mascota invalido: " + idMascota);
        }
        try {
            return historialDAO.obtenerPorMascota(idMascota);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Valida y registra una nueva entrada en el historial medico.
     *
     * @param idMascota identificador de la mascota (debe ser > 0)
     * @param idCita identificador de la cita (opcional)
     * @param idAtencionMedica identificador de la atencion medica (opcional)
     * @return true si el registro fue exitoso
     * @throws IllegalArgumentException si los parametros son invalidos
     * @throws IllegalStateException si ya existe un registro para la atencion
     */
    public boolean registrar(int idMascota, Integer idCita, Integer idAtencionMedica) {
        if (idMascota <= 0) {
            throw new IllegalArgumentException("ID de mascota invalido: " + idMascota);
        }
        
        // Validar que al menos uno de los IDs sea valido
        boolean tieneCita = (idCita != null && idCita > 0);
        boolean tieneAtencion = (idAtencionMedica != null && idAtencionMedica > 0);
        
        if (!tieneCita && !tieneAtencion) {
            throw new IllegalArgumentException("Debe proporcionar al menos un ID de cita o de atencion medica valido");
        }
        
        // Validar que la mascota exista
        MascotaService mascotaService = new MascotaService();
        if (mascotaService.obtenerPorId(idMascota) == null) {
            throw new IllegalArgumentException("La mascota con ID " + idMascota + " no existe");
        }
        
        // Validar que la cita exista si se proporciono
        if (tieneCita) {
            CitaService citaService = new CitaService();
            if (citaService.obtenerPorId(idCita) == null) {
                throw new IllegalArgumentException("La cita con ID " + idCita + " no existe");
            }
        }
        
        // Validar que la atencion medica exista si se proporciono
        if (tieneAtencion) {
            AtencionMedicaService atencionService = new AtencionMedicaService();
            if (atencionService.obtenerPorId(idAtencionMedica) == null) {
                throw new IllegalArgumentException("La atencion medica con ID " + idAtencionMedica + " no existe");
            }
        }
        
        // Verificar que no exista ya un registro para esta atencion medica
        if (tieneAtencion && yaExisteRegistro(idMascota, idAtencionMedica)) {
            throw new IllegalStateException("Ya existe un registro en el historial para esta atencion medica");
        }
        
        try {
            return historialDAO.registrar(idMascota, idCita, idAtencionMedica);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error al registrar entrada en el historial", e);
        }
    }
    
    /**
     * Verifica si ya existe un registro para una atencion medica especifica.
     *
     * @param idMascota identificador de la mascota
     * @param idAtencionMedica identificador de la atencion medica
     * @return true si ya existe
     */
    private boolean yaExisteRegistro(int idMascota, int idAtencionMedica) {
        List<HistorialMedico> historial = obtenerPorMascota(idMascota);
        if (historial != null) {
            for (HistorialMedico h : historial) {
                if (h.getIdAtencionMedica() != null && h.getIdAtencionMedica() == idAtencionMedica) {
                    return true;
                }
            }
        }
        return false;
    }
}