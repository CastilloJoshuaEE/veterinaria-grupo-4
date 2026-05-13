package com.mycompany.veterinaria.grupo4.service;

import com.mycompany.veterinaria.grupo4.model.dao.IFichaMedicaDAO;
import com.mycompany.veterinaria.grupo4.model.entity.FichaMedica;
import com.mycompany.veterinaria.grupo4.model.impl.FichaMedicaDAOImpl;
import org.springframework.stereotype.Service;
import java.sql.SQLException;

/**
 * Servicio para la gestion de fichas medicas.
 * <p>
 * Proporciona la capa de servicios para las operaciones de negocio
 * relacionadas con las fichas medicas de las mascotas, permitiendo
 * obtener y actualizar la informacion clinica permanente de cada paciente.
 * </p>
 * 
 * <p><b>Fecha de inicio del proyecto:</b> 15/04/2026</p>
 * 
 * @author CASTILLO MEREJILDO JOSHUA JAVIER – MODULO: MASCOTA
 * @version 1.0
 * @since 1.0
 */
@Service
public class FichaMedicaService {
    private IFichaMedicaDAO fichaMedicaDAO = new FichaMedicaDAOImpl();

    /**
     * Obtiene la ficha medica de una mascota.
     *
     * @param idMascota identificador de la mascota
     * @return objeto FichaMedica o null si no existe o hay error
     */
    public FichaMedica obtenerPorMascota(int idMascota) {
        try {
            return fichaMedicaDAO.obtenerPorMascota(idMascota);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Actualiza los datos de la ficha medica de una mascota.
     *
     * @param idMascota identificador de la mascota
     * @param alergias alergias de la mascota
     * @param enfermedadesCronicas enfermedades cronicas
     * @param observaciones observaciones adicionales
     * @return true si la actualizacion fue exitosa
     */
    public boolean actualizar(int idMascota, String alergias, String enfermedadesCronicas, String observaciones) {
        // Validacion basica
        if (idMascota <= 0) {
            throw new IllegalArgumentException("ID de mascota invalido: " + idMascota);
        }
        
        try {
            // Verificar si la mascota existe
            MascotaService mascotaService = new MascotaService();
            if (mascotaService.obtenerPorId(idMascota) == null) {
                throw new IllegalArgumentException("La mascota con ID " + idMascota + " no existe");
            }
            
            boolean resultado = fichaMedicaDAO.actualizar(idMascota, alergias, enfermedadesCronicas, observaciones);
            
            // Verificar post-actualizacion si se guardo correctamente
            FichaMedica ficha = obtenerPorMascota(idMascota);
            if (ficha == null) {
                System.err.println("ADVERTENCIA: No se pudo encontrar la ficha medica despues de actualizar");
                return false;
            }
            
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error al actualizar ficha medica para mascota " + idMascota, e);
        }
    }
}