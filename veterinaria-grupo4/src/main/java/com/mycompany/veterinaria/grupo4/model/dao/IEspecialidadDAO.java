package com.mycompany.veterinaria.grupo4.model.dao;

import com.mycompany.veterinaria.grupo4.model.entity.EspecialidadVeterinaria;
import java.sql.SQLException;
import java.util.List;

/**
 * Interfaz DAO para la gestion de especialidades veterinarias.
 * <p>
 * Define las operaciones de acceso a datos para la entidad EspecialidadVeterinaria,
 * incluyendo la consulta de todas las especialidades y busqueda por ID.
 * </p>
 * 
 * <p><b>Fecha de inicio del proyecto:</b> 15/04/2026</p>
 * 
 * @author BESILLA TOMALA ANGEL KALED – MODULO: VETERINARIO
 * @version 1.0
 * @since 1.0
 */
public interface IEspecialidadDAO {
    
    /**
     * Obtiene todas las especialidades veterinarias registradas.
     * 
     * @return lista de especialidades
     * @throws SQLException si ocurre un error en la base de datos
     */
    List<EspecialidadVeterinaria> obtenerTodas() throws SQLException;
    
    /**
     * Obtiene una especialidad por su identificador.
     * 
     * @param id identificador de la especialidad
     * @return objeto EspecialidadVeterinaria encontrado
     * @throws SQLException si ocurre un error en la base de datos
     */
    EspecialidadVeterinaria obtenerPorId(int id) throws SQLException;
}