package com.mycompany.veterinaria.grupo4.model.dao;

import com.mycompany.veterinaria.grupo4.model.entity.VacunaAplicada;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

/**
 * Interfaz DAO para la gestion de vacunas aplicadas a mascotas.
 * <p>
 * Define las operaciones de acceso a datos para la entidad VacunaAplicada,
 * incluyendo consulta por mascota, registro, actualizacion, verificacion
 * de existencia y consulta de vacunas proximas a vencer.
 * </p>
 * 
 * <p><b>Fecha de inicio del proyecto:</b> 15/04/2026</p>
 * 
 * @author CASTILLO MEREJILDO JOSHUA JAVIER – MODULO: MASCOTA
 * @version 1.0
 * @since 1.0
 */
public interface IVacunaDAO {
    
    /**
     * Obtiene todas las vacunas aplicadas a una mascota.
     * 
     * @param idMascota identificador de la mascota
     * @return lista de vacunas aplicadas
     * @throws SQLException si ocurre un error en la base de datos
     */
    List<VacunaAplicada> obtenerPorMascota(int idMascota) throws SQLException;
    
    /**
     * Registra una nueva vacuna aplicada.
     * 
     * @param vacuna objeto VacunaAplicada a registrar
     * @return ID de la vacuna registrada
     * @throws SQLException si ocurre un error en la base de datos
     */
    int registrar(VacunaAplicada vacuna) throws SQLException;
    
    /**
     * Actualiza los datos de una vacuna aplicada.
     * 
     * @param vacuna objeto VacunaAplicada con datos actualizados
     * @return true si la actualizacion fue exitosa
     * @throws SQLException si ocurre un error en la base de datos
     */
    boolean actualizar(VacunaAplicada vacuna) throws SQLException;
    
    /**
     * Verifica si ya existe una vacuna con el mismo nombre para una mascota.
     * 
     * @param idMascota identificador de la mascota
     * @param nombreVacuna nombre de la vacuna
     * @return objeto VacunaAplicada si existe, null en caso contrario
     * @throws SQLException si ocurre un error en la base de datos
     */
    VacunaAplicada verificarExistente(int idMascota, String nombreVacuna) throws SQLException;
    
    /**
     * Obtiene las vacunas proximas a vencer en los proximos dias.
     * 
     * @param diasAnticipacion numero de dias de anticipacion
     * @return lista de vacunas proximas a vencer
     * @throws SQLException si ocurre un error en la base de datos
     */
    List<VacunaAplicada> obtenerProximasAVencer(int diasAnticipacion) throws SQLException;
}