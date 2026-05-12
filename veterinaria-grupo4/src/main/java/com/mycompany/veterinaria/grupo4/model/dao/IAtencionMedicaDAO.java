package com.mycompany.veterinaria.grupo4.model.dao;

import com.mycompany.veterinaria.grupo4.model.entity.AtencionMedica;
import java.sql.SQLException;
import java.util.List;

/**
 * Interfaz DAO para la gestion de atenciones medicas.
 * <p>
 * Define las operaciones basicas de acceso a datos para la entidad AtencionMedica,
 * incluyendo insercion, consulta y eliminacion de atenciones medicas.
 * </p>
 * 
 * <p><b>Fecha de inicio del proyecto:</b> 15/04/2026</p>
 * 
 * @author ROBLES MORALES JUAN ANDRES – MODULO: ATENCION VETERINARIA
 * @version 1.0
 * @since 1.0
 */
public interface IAtencionMedicaDAO {
    
    /**
     * Inserta una nueva atencion medica en la base de datos.
     * 
     * @param atencion objeto AtencionMedica a insertar
     * @return ID generado para la atencion medica
     * @throws SQLException si ocurre un error en la base de datos
     */
    int insertar(AtencionMedica atencion) throws SQLException;
    
    /**
     * Obtiene todas las atenciones medicas registradas.
     * 
     * @return lista de todas las atenciones medicas
     * @throws SQLException si ocurre un error en la base de datos
     */
    List<AtencionMedica> obtenerTodas() throws SQLException;
    
    /**
     * Obtiene una atencion medica por su identificador.
     * 
     * @param idAtencionMedica identificador de la atencion medica
     * @return objeto AtencionMedica encontrado
     * @throws SQLException si ocurre un error en la base de datos
     */
    AtencionMedica obtenerPorId(int idAtencionMedica) throws SQLException;
    
    /**
     * Elimina una atencion medica de la base de datos.
     * 
     * @param idAtencionMedica identificador de la atencion medica a eliminar
     * @return true si la eliminacion fue exitosa
     * @throws SQLException si ocurre un error en la base de datos
     */
    boolean eliminar(int idAtencionMedica) throws SQLException;
}