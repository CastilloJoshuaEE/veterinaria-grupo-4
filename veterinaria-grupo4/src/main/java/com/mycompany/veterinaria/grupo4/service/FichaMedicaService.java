package com.mycompany.veterinaria.grupo4.service;

import com.mycompany.veterinaria.grupo4.model.dao.IFichaMedicaDAO;
import com.mycompany.veterinaria.grupo4.model.entity.FichaMedica;
import com.mycompany.veterinaria.grupo4.model.impl.FichaMedicaDAOImpl;
import org.springframework.stereotype.Service;
import java.sql.SQLException;

/**
 * Servicio para la gestion de fichas medicas con validaciones de negocio.
 * <p>
 * Proporciona la capa de servicios para las operaciones de negocio
 * relacionadas con las fichas medicas de las mascotas, permitiendo
 * obtener y actualizar la informacion clinica permanente de cada paciente.
 * </p>
 * 
 * <p><b>Reglas de negocio implementadas:</b></p>
 * <ul>
 *   <li>La mascota debe existir en el sistema</li>
 *   <li>Los campos opcionales pueden ser nulos</li>
 *   <li>Se registra automaticamente la fecha de ultima actualizacion</li>
 * </ul>
 * 
 * <p><b>Fecha de inicio del proyecto:</b> 15/04/2026</p>
 * 
 * @author CASTILLO MEREJILDO JOSHUA JAVIER – MODULO: MASCOTA
 * @version 2.0
 * @since 1.0
 */
@Service
public class FichaMedicaService {
    
    private static final int ALERGIAS_MAX_LENGTH = 255;
    private static final int ENFERMEDADES_MAX_LENGTH = 255;
    
    private IFichaMedicaDAO fichaMedicaDAO = new FichaMedicaDAOImpl();

    /**
     * Obtiene la ficha medica de una mascota.
     *
     * @param idMascota identificador de la mascota (debe ser > 0)
     * @return objeto FichaMedica o null si no existe o hay error
     * @throws IllegalArgumentException si el id es invalido
     */
    public FichaMedica obtenerPorMascota(int idMascota) {
        if (idMascota <= 0) {
            throw new IllegalArgumentException("ID de mascota invalido: " + idMascota);
        }
        try {
            return fichaMedicaDAO.obtenerPorMascota(idMascota);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Valida y actualiza los datos de la ficha medica de una mascota.
     * <p>
     * <b>Reglas de negocio:</b>
     * <ul>
     *   <li>La mascota debe existir en el sistema</li>
     *   <li>Si la mascota no tiene ficha medica, se crea automaticamente</li>
     * </ul>
     * </p>
     *
     * @param idMascota identificador de la mascota (debe ser > 0)
     * @param alergias alergias de la mascota (maximo 255 caracteres)
     * @param enfermedadesCronicas enfermedades cronicas (maximo 255 caracteres)
     * @param observaciones observaciones adicionales
     * @return true si la actualizacion fue exitosa
     * @throws IllegalArgumentException si el id es invalido o la mascota no existe
     */
    public boolean actualizar(int idMascota, String alergias, String enfermedadesCronicas, String observaciones) {
        if (idMascota <= 0) {
            throw new IllegalArgumentException("ID de mascota invalido: " + idMascota);
        }
        
        // Validar longitudes maximas
        if (alergias != null && alergias.length() > ALERGIAS_MAX_LENGTH) {
            throw new IllegalArgumentException("Las alergias no pueden exceder los " + ALERGIAS_MAX_LENGTH + " caracteres");
        }
        if (enfermedadesCronicas != null && enfermedadesCronicas.length() > ENFERMEDADES_MAX_LENGTH) {
            throw new IllegalArgumentException("Las enfermedades cronicas no pueden exceder los " + ENFERMEDADES_MAX_LENGTH + " caracteres");
        }
        
        // Verificar si la mascota existe
        MascotaService mascotaService = new MascotaService();
        if (mascotaService.obtenerPorId(idMascota) == null) {
            throw new IllegalArgumentException("La mascota con ID " + idMascota + " no existe");
        }
        
        // Limpiar valores vacios a null para almacenamiento consistente
        alergias = (alergias != null && alergias.trim().isEmpty()) ? null : alergias;
        enfermedadesCronicas = (enfermedadesCronicas != null && enfermedadesCronicas.trim().isEmpty()) ? null : enfermedadesCronicas;
        observaciones = (observaciones != null && observaciones.trim().isEmpty()) ? null : observaciones;
        
        try {
            boolean resultado = fichaMedicaDAO.actualizar(idMascota, alergias, enfermedadesCronicas, observaciones);
            
            // Verificar post-actualizacion
            FichaMedica ficha = obtenerPorMascota(idMascota);
            if (ficha == null && resultado) {
                // Si el resultado fue true pero no se encuentra la ficha, puede ser un caso borde
                System.err.println("ADVERTENCIA: No se pudo encontrar la ficha medica despues de actualizar para mascota " + idMascota);
                // No retornamos false porque la operacion pudo haber sido exitosa
            }
            
            return resultado;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error al actualizar ficha medica para mascota " + idMascota, e);
        }
    }
}