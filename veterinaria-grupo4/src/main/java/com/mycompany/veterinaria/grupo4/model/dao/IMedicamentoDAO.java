package com.mycompany.veterinaria.grupo4.model.dao;

import com.mycompany.veterinaria.grupo4.model.entity.Medicamento;
import java.sql.SQLException;
import java.util.List;

/**
 * Interfaz DAO para la gestion de medicamentos.
 * <p>
 * Define las operaciones de acceso a datos para la entidad Medicamento,
 * incluyendo consulta de disponibles, busqueda por ID, registro de recetas
 * y consulta de medicamentos recetados por atencion medica.
 * </p>
 * 
 * <p><b>Fecha de inicio del proyecto:</b> 15/04/2026</p>
 * 
 * @author ROBLES MORALES JUAN ANDRES – MODULO: ATENCION VETERINARIA
 * @version 1.0
 * @since 1.0
 */
public interface IMedicamentoDAO {
    
    /**
     * Obtiene los medicamentos disponibles (stock > 0).
     * 
     * @return lista de medicamentos disponibles
     * @throws SQLException si ocurre un error en la base de datos
     */
    List<Medicamento> obtenerDisponibles() throws SQLException;
    
    /**
     * Obtiene un medicamento por su identificador.
     * 
     * @param idMedicamento identificador del medicamento
     * @return objeto Medicamento encontrado
     * @throws SQLException si ocurre un error en la base de datos
     */
    Medicamento obtenerPorId(int idMedicamento) throws SQLException;
    
    /**
     * Registra un medicamento recetado en una atencion medica.
     * 
     * @param idAtencionMedica identificador de la atencion
     * @param idMedicamento identificador del medicamento
     * @param dosis dosis prescrita
     * @param frecuencia frecuencia de administracion
     * @param duracion duracion del tratamiento
     * @return true si el registro fue exitoso
     * @throws SQLException si ocurre un error en la base de datos
     */
    boolean insertarRecetado(int idAtencionMedica, int idMedicamento, String dosis, String frecuencia, String duracion) throws SQLException;
    
    /**
     * Obtiene los medicamentos recetados en una atencion medica.
     * 
     * @param idAtencionMedica identificador de la atencion
     * @return lista de medicamentos recetados
     * @throws SQLException si ocurre un error en la base de datos
     */
    List<Medicamento> obtenerRecetadosPorAtencion(int idAtencionMedica) throws SQLException;
}