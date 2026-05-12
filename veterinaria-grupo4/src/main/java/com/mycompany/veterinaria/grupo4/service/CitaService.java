package com.mycompany.veterinaria.grupo4.service;

import com.mycompany.veterinaria.grupo4.model.dao.ICitaDAO;
import com.mycompany.veterinaria.grupo4.model.entity.Cita;
import com.mycompany.veterinaria.grupo4.model.impl.CitaDAOImpl;
import org.springframework.stereotype.Service;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

/**
 * Servicio para la gestion de citas medicas.
 * <p>
 * Proporciona la capa de servicios para las operaciones de negocio
 * relacionadas con el agendamiento, consulta, actualizacion y cancelacion
 * de citas medicas en el sistema veterinario.
 * </p>
 * 
 * <p><b>Fecha de inicio del proyecto:</b> 15/04/2026</p>
 * 
 * @author CHILAN CHILAN DANNY ANDRES – MODULO: AGENDAMIENTO DE CITA
 * @version 1.0
 * @since 1.0
 */
@Service
public class CitaService {
    private ICitaDAO citaDAO = new CitaDAOImpl();

    /**
     * Lista las citas programadas para una fecha especifica.
     *
     * @param fecha fecha a consultar
     * @return lista de citas en la fecha
     */
    public List<Cita> listarPorFecha(Date fecha) {
        try {
            return citaDAO.obtenerPorFecha(fecha);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Lista las citas asociadas a un cliente.
     *
     * @param idCliente identificador del cliente
     * @return lista de citas del cliente
     */
    public List<Cita> listarPorCliente(int idCliente) {
        try {
            return citaDAO.obtenerPorCliente(idCliente);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Obtiene una cita por su identificador.
     *
     * @param idCita identificador de la cita
     * @return objeto Cita encontrado
     */
    public Cita obtenerPorId(int idCita) {
        try {
            return citaDAO.obtenerPorId(idCita);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Agenda una nueva cita en el sistema.
     *
     * @param cita objeto Cita a agendar
     * @return ID de la cita agendada
     */
    public int agendar(Cita cita) {
        try {
            return citaDAO.agendar(cita);
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * Actualiza los datos de una cita existente.
     *
     * @param cita objeto Cita con datos actualizados
     * @return true si la actualizacion fue exitosa
     */
    public boolean actualizar(Cita cita) {
        try {
            return citaDAO.actualizar(cita);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Cancela una cita con un motivo especifico.
     *
     * @param idCita identificador de la cita
     * @param motivo razon de la cancelacion
     * @return true si la cancelacion fue exitosa
     */
    public boolean cancelar(int idCita, String motivo) {
        try {
            return citaDAO.cancelar(idCita, motivo);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Lista todas las citas registradas.
     *
     * @return lista de todas las citas
     */
    public List<Cita> listarTodas() {
        try {
            return citaDAO.obtenerTodas();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Lista las citas dentro de un rango de fechas.
     *
     * @param fechaInicio fecha de inicio del rango
     * @param fechaFin fecha de fin del rango
     * @return lista de citas en el rango
     */
    public List<Cita> listarPorRangoFechas(Date fechaInicio, Date fechaFin) {
        try {
            return citaDAO.obtenerPorRangoFechas(fechaInicio, fechaFin);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Lista las citas filtradas por servicio, veterinario y estado.
     *
     * @param idServicio identificador del servicio
     * @param idVeterinario identificador del veterinario
     * @param estado estado de la cita
     * @return lista de citas que coinciden con los filtros
     */
    public List<Cita> listarPorServicioYVeterinario(int idServicio, int idVeterinario, String estado) {
        try {
            return citaDAO.obtenerPorServicioYVeterinario(idServicio, idVeterinario, estado);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Actualiza el estado de una cita.
     *
     * @param idCita identificador de la cita
     * @param estado nuevo estado
     * @return true si la actualizacion fue exitosa
     */
    public boolean actualizarEstado(int idCita, String estado) {
        try {
            return citaDAO.actualizarEstado(idCita, estado);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Elimina una cita del sistema.
     *
     * @param idCita identificador de la cita a eliminar
     * @return true si la eliminacion fue exitosa
     */
    public boolean eliminar(int idCita) {
        try {
            return citaDAO.eliminar(idCita);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Obtiene todas las citas con estado pendiente.
     *
     * @return lista de citas pendientes
     */
    public List<Cita> listarPendientes() {
        try {
            return citaDAO.obtenerPendientes();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}