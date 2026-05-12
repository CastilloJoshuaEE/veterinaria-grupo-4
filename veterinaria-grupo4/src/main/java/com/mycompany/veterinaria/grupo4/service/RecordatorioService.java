package com.mycompany.veterinaria.grupo4.service;

import com.mycompany.veterinaria.grupo4.config.RecordatorioConfig;
import com.mycompany.veterinaria.grupo4.model.dao.IRecordatorioDAO;
import com.mycompany.veterinaria.grupo4.model.entity.Recordatorio;
import com.mycompany.veterinaria.grupo4.model.impl.RecordatorioDAOImpl;
import org.springframework.stereotype.Service;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

/**
 * Servicio para la gestion de recordatorios.
 * <p>
 * Proporciona la capa de servicios para las operaciones de negocio
 * relacionadas con los recordatorios del sistema, incluyendo obtener
 * recordatorios pendientes, marcar como leidos, generar recordatorios
 * automaticos y administrar las configuraciones de recordatorios.
 * </p>
 * 
 * <p><b>Fecha de inicio del proyecto:</b> 15/04/2026</p>
 * 
 * @author CHILAN CHILAN DANNY ANDRES – MODULO: AGENDAMIENTO DE CITA
 * @version 1.0
 * @since 1.0
 */
@Service
public class RecordatorioService {
    private IRecordatorioDAO recordatorioDAO = new RecordatorioDAOImpl();

    /**
     * Obtiene los recordatorios pendientes para un usuario.
     *
     * @param idUsuario identificador del usuario
     * @return lista de recordatorios pendientes o null si hay error
     */
    public List<Recordatorio> obtenerPendientes(int idUsuario) {
        try {
            return recordatorioDAO.obtenerPendientes(idUsuario);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Marca un recordatorio como leido.
     *
     * @param idRecordatorio identificador del recordatorio
     * @return true si la operacion fue exitosa
     */
    public boolean marcarComoLeido(int idRecordatorio) {
        try {
            return recordatorioDAO.marcarComoLeido(idRecordatorio);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Genera recordatorios automaticos para un usuario.
     *
     * @param idUsuario identificador del usuario
     */
    public void generarRecordatorios(int idUsuario) {
        try {
            recordatorioDAO.generarRecordatorios(idUsuario);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Lista todos los recordatorios en un rango de fechas.
     *
     * @param fechaInicio fecha de inicio
     * @param fechaFin fecha de fin
     * @return lista de recordatorios en el rango o null si hay error
     */
    public List<Recordatorio> listarTodos(Date fechaInicio, Date fechaFin) {
        try {
            return recordatorioDAO.obtenerTodos(fechaInicio, fechaFin);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Incrementa el contador de notificaciones de un recordatorio.
     *
     * @param idRecordatorio identificador del recordatorio
     */
    public void incrementarContador(int idRecordatorio) {
        try {
            recordatorioDAO.incrementarContador(idRecordatorio);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Obtiene el contador de notificaciones de un recordatorio.
     *
     * @param idRecordatorio identificador del recordatorio
     * @return valor del contador o 0 si hay error
     */
    public int obtenerContador(int idRecordatorio) {
        try {
            return recordatorioDAO.obtenerContador(idRecordatorio);
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * Registra un nuevo recordatorio.
     *
     * @param recordatorio objeto Recordatorio a registrar
     * @param anticipacion tiempo de anticipacion
     * @return ID del recordatorio creado o -1 si hay error
     */
    public int registrar(Recordatorio recordatorio, String anticipacion) {
        try {
            return recordatorioDAO.registrar(recordatorio, anticipacion);
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * Actualiza un recordatorio existente.
     *
     * @param recordatorio objeto Recordatorio con datos actualizados
     * @return true si la actualizacion fue exitosa
     */
    public boolean actualizar(Recordatorio recordatorio) {
        try {
            return recordatorioDAO.actualizar(recordatorio);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Elimina un recordatorio.
     *
     * @param idRecordatorio identificador del recordatorio
     * @return true si la eliminacion fue exitosa
     */
    public boolean eliminar(int idRecordatorio) {
        try {
            return recordatorioDAO.eliminar(idRecordatorio);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Obtiene todas las configuraciones de recordatorios.
     *
     * @return lista de configuraciones o null si hay error
     */
    public List<RecordatorioConfig> obtenerTodasConfiguraciones() {
        try {
            return recordatorioDAO.obtenerTodasConfiguraciones();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Actualiza una configuracion de recordatorio.
     *
     * @param config objeto RecordatorioConfig con datos actualizados
     * @return true si la actualizacion fue exitosa
     */
    public boolean actualizarConfiguracion(RecordatorioConfig config) {
        try {
            return recordatorioDAO.actualizarConfiguracion(config);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Crea una nueva configuracion de recordatorio.
     *
     * @param config objeto RecordatorioConfig con los datos
     * @return ID de la configuracion creada o -1 si hay error
     */
    public int crearConfiguracion(RecordatorioConfig config) {
        try {
            return recordatorioDAO.crearConfiguracion(config);
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }
}