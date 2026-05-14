package com.mycompany.veterinaria.grupo4.model.dao;

import com.mycompany.veterinaria.grupo4.model.entity.HistorialMedico;
import java.sql.SQLException;
import java.util.List;

/**
 * Interfaz DAO para la gestion del historial medico de mascotas.
 * <p>
 * Define las operaciones de acceso a datos para la entidad HistorialMedico,
 * incluyendo consulta por mascota y registro de nuevas entradas.
 * </p>
 * 
 * <p><b>Fecha de inicio del proyecto:</b> 15/04/2026</p>
 * 
 * @author CASTILLO MEREJILDO JOSHUA JAVIER – MODULO: MASCOTA
 * @version 1.0
 * @since 1.0
 */
public interface IHistorialDAO {
    
    /**
     * Obtiene el historial medico completo de una mascota.
     * 
     * @param idMascota identificador de la mascota
     * @return lista de registros del historial
     * @throws SQLException si ocurre un error en la base de datos
     */
    List<HistorialMedico> obtenerPorMascota(int idMascota) throws SQLException;
    
    /**
     * Registra una nueva entrada en el historial medico.
     * 
     * @param idMascota identificador de la mascota
     * @param idCita identificador de la cita (opcional)
     * @param idAtencionMedica identificador de la atencion medica (opcional)
     * @return true si el registro fue exitoso
     * @throws SQLException si ocurre un error en la base de datos
     */
    boolean registrar(int idMascota, Integer idCita, Integer idAtencionMedica) throws SQLException;
}