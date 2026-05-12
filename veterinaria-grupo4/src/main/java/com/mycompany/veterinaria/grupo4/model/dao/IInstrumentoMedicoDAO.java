package com.mycompany.veterinaria.grupo4.model.dao;

import com.mycompany.veterinaria.grupo4.model.entity.InstrumentoMedico;
import java.sql.SQLException;
import java.util.List;

/**
 * Interfaz DAO para la gestion de instrumentos medicos.
 * <p>
 * Define las operaciones de acceso a datos para la entidad InstrumentoMedico,
 * incluyendo consulta de disponibles, busqueda por ID, registro de uso
 * y consulta de usados por atencion medica.
 * </p>
 * 
 * <p><b>Fecha de inicio del proyecto:</b> 15/04/2026</p>
 * 
 * @author ROBLES MORALES JUAN ANDRES – MODULO: ATENCION VETERINARIA
 * @version 1.0
 * @since 1.0
 */
public interface IInstrumentoMedicoDAO {
    
    /**
     * Obtiene los instrumentos medicos disponibles (stock > 0).
     * 
     * @return lista de instrumentos disponibles
     * @throws SQLException si ocurre un error en la base de datos
     */
    List<InstrumentoMedico> obtenerDisponibles() throws SQLException;
    
    /**
     * Obtiene un instrumento por su identificador.
     * 
     * @param idInstrumento identificador del instrumento
     * @return objeto InstrumentoMedico encontrado
     * @throws SQLException si ocurre un error en la base de datos
     */
    InstrumentoMedico obtenerPorId(int idInstrumento) throws SQLException;
    
    /**
     * Registra el uso de un instrumento en una atencion medica.
     * 
     * @param idAtencionMedica identificador de la atencion
     * @param idInstrumento identificador del instrumento
     * @return true si el registro fue exitoso
     * @throws SQLException si ocurre un error en la base de datos
     */
    boolean insertarUsado(int idAtencionMedica, int idInstrumento) throws SQLException;
    
    /**
     * Obtiene los instrumentos usados en una atencion medica especifica.
     * 
     * @param idAtencionMedica identificador de la atencion
     * @return lista de instrumentos usados
     * @throws SQLException si ocurre un error en la base de datos
     */
    List<InstrumentoMedico> obtenerUsadosPorAtencion(int idAtencionMedica) throws SQLException;
}