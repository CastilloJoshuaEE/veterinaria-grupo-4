package com.mycompany.veterinaria.grupo4.model.dao;

import com.mycompany.veterinaria.grupo4.model.entity.Recepcionista;
import java.sql.SQLException;

/**
 * Interfaz DAO para la gestión de recepcionistas.
 * 
 * @author juan
 */
public interface IRcepcionistaDAO {
    
    /**
     * Registra un nuevo recepcionista usando el SP_REGISTRAR_RECEPCIONISTA.
     * 
     * @param registro Datos del recepcionista
     * @return true si el registro fue exitoso
     * @throws SQLException si ocurre un error en la base de datos
     */
    boolean registrarRecepcionista(Recepcionista registro) throws SQLException;
}