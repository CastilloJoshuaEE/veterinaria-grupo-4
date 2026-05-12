package com.mycompany.veterinaria.grupo4.model.dao;

import com.mycompany.veterinaria.grupo4.model.entity.FichaMedica;
import java.sql.SQLException;

/**
 * Interfaz DAO para la gestion de fichas medicas de mascotas.
 * <p>
 * Define las operaciones de acceso a datos para la entidad FichaMedica,
 * incluyendo consulta por mascota y actualizacion de datos clinicos.
 * </p>
 * 
 * <p><b>Fecha de inicio del proyecto:</b> 15/04/2026</p>
 * 
 * @author CASTILLO MEREJILDO JOSHUA JAVIER – MODULO: MASCOTA
 * @version 1.0
 * @since 1.0
 */
public interface IFichaMedicaDAO {
    
    /**
     * Obtiene la ficha medica de una mascota.
     * 
     * @param idMascota identificador de la mascota
     * @return objeto FichaMedica encontrado
     * @throws SQLException si ocurre un error en la base de datos
     */
    FichaMedica obtenerPorMascota(int idMascota) throws SQLException;
    
    /**
     * Actualiza los datos de la ficha medica de una mascota.
     * 
     * @param idMascota identificador de la mascota
     * @param alergias alergias de la mascota
     * @param enfermedadesCronicas enfermedades cronicas
     * @param observaciones observaciones adicionales
     * @return true si la actualizacion fue exitosa
     * @throws SQLException si ocurre un error en la base de datos
     */
    boolean actualizar(int idMascota, String alergias, String enfermedadesCronicas, String observaciones) throws SQLException;
}