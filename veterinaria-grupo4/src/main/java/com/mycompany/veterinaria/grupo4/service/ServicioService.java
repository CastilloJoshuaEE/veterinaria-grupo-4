package com.mycompany.veterinaria.grupo4.service;

import com.mycompany.veterinaria.grupo4.model.dao.IServicioDAO;
import com.mycompany.veterinaria.grupo4.model.entity.Servicio;
import com.mycompany.veterinaria.grupo4.model.entity.Veterinario;
import com.mycompany.veterinaria.grupo4.model.impl.ServicioDAOImpl;
import org.springframework.stereotype.Service;
import java.sql.SQLException;
import java.util.List;

@Service
public class ServicioService {
    private IServicioDAO servicioDAO = new ServicioDAOImpl();

    public List<Servicio> listarTodos() {
        try {
            return servicioDAO.obtenerTodos();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Servicio> listarActivos() {
        try {
            return servicioDAO.obtenerActivos();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Servicio obtenerPorId(int idServicio) {
        try {
            return servicioDAO.obtenerPorId(idServicio);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public int crear(Servicio servicio) {
        try {
            return servicioDAO.insertar(servicio);
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    public boolean actualizar(Servicio servicio) {
        try {
            return servicioDAO.actualizar(servicio);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean eliminar(int idServicio) {
        try {
            return servicioDAO.eliminar(idServicio);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean cambiarEstado(int idServicio, boolean estado) {
        try {
            return servicioDAO.cambiarEstado(idServicio, estado);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Veterinario> obtenerVeterinariosAsignados(int idServicio) {
        try {
            return servicioDAO.obtenerVeterinariosAsignados(idServicio);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Veterinario> obtenerVeterinariosNoAsignados(int idServicio) {
        try {
            return servicioDAO.obtenerVeterinariosNoAsignados(idServicio);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean asignarVeterinario(int idServicio, int idVeterinario) {
        try {
            return servicioDAO.asignarVeterinario(idServicio, idVeterinario);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean eliminarAsignacionVeterinario(int idAsignacion) {
        try {
            return servicioDAO.eliminarAsignacionVeterinario(idAsignacion);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}