package com.mycompany.veterinaria.grupo4.service;

import com.mycompany.veterinaria.grupo4.model.dao.IServicioDAO;
import com.mycompany.veterinaria.grupo4.model.entity.Servicio;
import com.mycompany.veterinaria.grupo4.model.entity.Veterinario;
import com.mycompany.veterinaria.grupo4.model.impl.ServicioDAOImpl;
import org.springframework.stereotype.Service;
import java.sql.SQLException;
import java.util.List;

/**
 * Servicio para la gestion de servicios veterinarios.
 * <p>
 * Proporciona la capa de servicios para las operaciones de negocio
 * relacionadas con los servicios ofrecidos por la clinica, incluyendo
 * listado, busqueda, operaciones CRUD y gestion de asignaciones de
 * veterinarios a servicios.
 * </p>
 * 
 * <p><b>Fecha de inicio del proyecto:</b> 15/04/2026</p>
 * 
 * @author BESILLA TOMALA ANGEL KALED – MODULO: VETERINARIO
 * @version 1.0
 * @since 1.0
 */
@Service
public class ServicioService {
    private IServicioDAO servicioDAO = new ServicioDAOImpl();

    /**
     * Lista todos los servicios registrados.
     *
     * @return lista de todos los servicios o null si hay error
     */
    public List<Servicio> listarTodos() {
        try {
            return servicioDAO.obtenerTodos();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Lista solo los servicios activos.
     *
     * @return lista de servicios activos o null si hay error
     */
    public List<Servicio> listarActivos() {
        try {
            return servicioDAO.obtenerActivos();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Obtiene un servicio por su identificador.
     *
     * @param idServicio identificador del servicio
     * @return objeto Servicio o null si no existe
     */
    public Servicio obtenerPorId(int idServicio) {
        try {
            return servicioDAO.obtenerPorId(idServicio);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Crea un nuevo servicio en el sistema.
     *
     * @param servicio objeto Servicio a crear
     * @return ID del servicio creado o -1 si hay error
     */
    public int crear(Servicio servicio) {
        try {
            return servicioDAO.insertar(servicio);
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * Actualiza los datos de un servicio existente.
     *
     * @param servicio objeto Servicio con datos actualizados
     * @return true si la actualizacion fue exitosa
     */
    public boolean actualizar(Servicio servicio) {
        try {
            return servicioDAO.actualizar(servicio);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Elimina un servicio del sistema.
     *
     * @param idServicio identificador del servicio a eliminar
     * @return true si la eliminacion fue exitosa
     */
    public boolean eliminar(int idServicio) {
        try {
            return servicioDAO.eliminar(idServicio);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Cambia el estado (activo/inactivo) de un servicio.
     *
     * @param idServicio identificador del servicio
     * @param estado nuevo estado
     * @return true si el cambio fue exitoso
     */
    public boolean cambiarEstado(int idServicio, boolean estado) {
        try {
            return servicioDAO.cambiarEstado(idServicio, estado);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Obtiene los veterinarios asignados a un servicio.
     *
     * @param idServicio identificador del servicio
     * @return lista de veterinarios asignados o null si hay error
     */
    public List<Veterinario> obtenerVeterinariosAsignados(int idServicio) {
        try {
            return servicioDAO.obtenerVeterinariosAsignados(idServicio);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Obtiene los veterinarios no asignados a un servicio.
     *
     * @param idServicio identificador del servicio
     * @return lista de veterinarios no asignados o null si hay error
     */
    public List<Veterinario> obtenerVeterinariosNoAsignados(int idServicio) {
        try {
            return servicioDAO.obtenerVeterinariosNoAsignados(idServicio);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Asigna un veterinario a un servicio.
     *
     * @param idServicio identificador del servicio
     * @param idVeterinario identificador del veterinario
     * @return true si la asignacion fue exitosa
     */
    public boolean asignarVeterinario(int idServicio, int idVeterinario) {
        try {
            return servicioDAO.asignarVeterinario(idServicio, idVeterinario);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Elimina una asignacion de veterinario por su ID.
     *
     * @param idAsignacion identificador de la asignacion
     * @return true si la eliminacion fue exitosa
     * @deprecated Usar eliminarAsignacionPorIds en su lugar
     */
    @Deprecated
    public boolean eliminarAsignacionVeterinario(int idAsignacion) {
        try {
            return servicioDAO.eliminarAsignacionVeterinario(idAsignacion);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Lista los servicios asignados a un veterinario.
     *
     * @param idVeterinario identificador del veterinario
     * @return lista de servicios del veterinario o null si hay error
     */
    public List<Servicio> listarPorVeterinario(int idVeterinario) {
        try {
            return servicioDAO.listarPorVeterinario(idVeterinario);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * Busca servicios por nombre.
     *
     * @param nombre termino de busqueda
     * @return lista de servicios que coinciden o null si hay error
     */
    public List<Servicio> buscarPorNombre(String nombre) {
        try {
            return servicioDAO.buscarPorNombre(nombre);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * Elimina una asignacion de veterinario por IDs.
     *
     * @param idVeterinario identificador del veterinario
     * @param idServicio identificador del servicio
     * @return true si la eliminacion fue exitosa
     */
    public boolean eliminarAsignacionPorIds(int idVeterinario, int idServicio) {
        try {
            return servicioDAO.eliminarAsignacionPorIds(idVeterinario, idServicio);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}