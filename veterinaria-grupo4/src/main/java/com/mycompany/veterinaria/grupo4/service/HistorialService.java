package com.mycompany.veterinaria.grupo4.service;

import com.mycompany.veterinaria.grupo4.model.dao.IHistorialDAO;
import com.mycompany.veterinaria.grupo4.model.entity.HistorialMedico;
import com.mycompany.veterinaria.grupo4.model.impl.HistorialDAOImpl;
import org.springframework.stereotype.Service;
import java.sql.SQLException;
import java.util.List;

/**
 * Servicio para la gestion del historial medico.
 * <p>
 * Proporciona la capa de servicios para las operaciones de negocio
 * relacionadas con el historial clinico de las mascotas, permitiendo
 * obtener el historial completo y registrar nuevas entradas.
 * </p>
 * 
 * <p><b>Fecha de inicio del proyecto:</b> 15/04/2026</p>
 * 
 * @author CASTILLO MEREJILDO JOSHUA JAVIER – MODULO: MASCOTA
 * @version 1.0
 * @since 1.0
 */
@Service
public class HistorialService {
    private IHistorialDAO historialDAO = new HistorialDAOImpl();

    /**
     * Obtiene el historial medico completo de una mascota.
     *
     * @param idMascota identificador de la mascota
     * @return lista de registros del historial o null si hay error
     */
    public List<HistorialMedico> obtenerPorMascota(int idMascota) {
        try {
            return historialDAO.obtenerPorMascota(idMascota);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Registra una nueva entrada en el historial medico.
     *
     * @param idMascota identificador de la mascota
     * @param idCita identificador de la cita (opcional)
     * @param idAtencionMedica identificador de la atencion medica (opcional)
     * @return true si el registro fue exitoso
     */
    public boolean registrar(int idMascota, Integer idCita, Integer idAtencionMedica) {
        try {
            return historialDAO.registrar(idMascota, idCita, idAtencionMedica);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}