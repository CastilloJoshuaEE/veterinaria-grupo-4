package com.mycompany.veterinaria.grupo4.service;

import com.mycompany.veterinaria.grupo4.model.dao.IVeterinarioDAO;
import com.mycompany.veterinaria.grupo4.model.entity.Veterinario;
import com.mycompany.veterinaria.grupo4.model.impl.VeterinarioDAOImpl;
import org.springframework.stereotype.Service;
import java.sql.SQLException;
import java.util.List;

/**
 * Servicio para la gestion de veterinarios.
 * <p>
 * Proporciona la capa de servicios para las operaciones de negocio
 * relacionadas con el personal veterinario, incluyendo listado,
 * busqueda por multiples criterios, operaciones CRUD y obtencion
 * de veterinarios por servicio.
 * </p>
 * 
 * <p><b>Fecha de inicio del proyecto:</b> 15/04/2026</p>
 * 
 * @author BESILLA TOMALA ANGEL KALED – MODULO: VETERINARIO
 * @version 1.0
 * @since 1.0
 */
@Service
public class VeterinarioService {
    private IVeterinarioDAO veterinarioDAO = new VeterinarioDAOImpl();

    /**
     * Lista todos los veterinarios registrados.
     *
     * @return lista de todos los veterinarios o null si hay error
     */
    public List<Veterinario> listarTodos() {
        try {
            return veterinarioDAO.obtenerTodos();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Obtiene un veterinario por su numero de cedula.
     *
     * @param cedula numero de cedula del veterinario
     * @return objeto Veterinario o null si no existe
     */
    public Veterinario obtenerPorCedula(String cedula) {
        try {
            return veterinarioDAO.obtenerPorCedula(cedula);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Obtiene un veterinario por su identificador.
     *
     * @param id identificador del veterinario
     * @return objeto Veterinario o null si no existe
     */
    public Veterinario obtenerPorId(int id) {
        try {
            return veterinarioDAO.obtenerPorId(id);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Crea un nuevo veterinario en el sistema.
     *
     * @param veterinario objeto Veterinario a registrar
     * @return true si la creacion fue exitosa
     */
    public boolean crear(Veterinario veterinario) {
        try {
            return veterinarioDAO.insertar(veterinario);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Actualiza los datos de un veterinario existente.
     *
     * @param veterinario objeto Veterinario con datos actualizados
     * @return true si la actualizacion fue exitosa
     */
    public boolean actualizar(Veterinario veterinario) {
        try {
            return veterinarioDAO.actualizar(veterinario);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Elimina un veterinario del sistema.
     *
     * @param idVeterinario identificador del veterinario a eliminar
     * @return true si la eliminacion fue exitosa
     */
    public boolean eliminar(int idVeterinario) {
        try {
            return veterinarioDAO.eliminar(idVeterinario);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Busca veterinarios por nombre.
     *
     * @param nombre termino de busqueda
     * @return lista de veterinarios que coinciden o null si hay error
     */
    public List<Veterinario> buscarPorNombre(String nombre) {
        try {
            return veterinarioDAO.buscarPorNombre(nombre);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Busca veterinarios por especialidad.
     *
     * @param especialidad especialidad a buscar
     * @return lista de veterinarios con esa especialidad o null si hay error
     */
    public List<Veterinario> buscarPorEspecialidad(String especialidad) {
        try {
            return veterinarioDAO.buscarPorEspecialidad(especialidad);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Obtiene los veterinarios asignados a un servicio especifico.
     *
     * @param idServicio identificador del servicio
     * @return lista de veterinarios asignados al servicio o null si hay error
     */
    public List<Veterinario> obtenerPorServicio(int idServicio) {
        try {
            return veterinarioDAO.obtenerPorServicio(idServicio);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * Busca veterinarios por termino general (nombre, cedula o especialidad).
     *
     * @param termino termino de busqueda
     * @return lista de veterinarios que coinciden o null si hay error
     */
    public List<Veterinario> buscar(String termino) {
        try {
            return veterinarioDAO.buscar(termino);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}