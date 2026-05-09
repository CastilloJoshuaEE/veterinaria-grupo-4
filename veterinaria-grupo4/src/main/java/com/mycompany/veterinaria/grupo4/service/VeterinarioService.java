package com.mycompany.veterinaria.grupo4.service;

import com.mycompany.veterinaria.grupo4.model.dao.IVeterinarioDAO;
import com.mycompany.veterinaria.grupo4.model.entity.Veterinario;
import com.mycompany.veterinaria.grupo4.model.impl.VeterinarioDAOImpl;
import org.springframework.stereotype.Service;
import java.sql.SQLException;
import java.util.List;

@Service
public class VeterinarioService {
    private IVeterinarioDAO veterinarioDAO = new VeterinarioDAOImpl();

    public List<Veterinario> listarTodos() {
        try {
            return veterinarioDAO.obtenerTodos();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Veterinario obtenerPorCedula(String cedula) {
        try {
            return veterinarioDAO.obtenerPorCedula(cedula);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Veterinario obtenerPorId(int id) {
        try {
            return veterinarioDAO.obtenerPorId(id);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean crear(Veterinario veterinario) {
        try {
            return veterinarioDAO.insertar(veterinario);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean actualizar(Veterinario veterinario) {
        try {
            return veterinarioDAO.actualizar(veterinario);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean eliminar(int idVeterinario) {
        try {
            return veterinarioDAO.eliminar(idVeterinario);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Veterinario> buscarPorNombre(String nombre) {
        try {
            return veterinarioDAO.buscarPorNombre(nombre);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Veterinario> buscarPorEspecialidad(String especialidad) {
        try {
            return veterinarioDAO.buscarPorEspecialidad(especialidad);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Veterinario> obtenerPorServicio(int idServicio) {
        try {
            return veterinarioDAO.obtenerPorServicio(idServicio);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public List<Veterinario> buscar(String termino) {
        try {
            return veterinarioDAO.buscar(termino);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}