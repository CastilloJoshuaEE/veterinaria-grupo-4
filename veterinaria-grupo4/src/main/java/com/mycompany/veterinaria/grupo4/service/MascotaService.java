package com.mycompany.veterinaria.grupo4.service;

import com.mycompany.veterinaria.grupo4.model.dao.IMascotaDAO;
import com.mycompany.veterinaria.grupo4.model.entity.Mascota;
import com.mycompany.veterinaria.grupo4.model.impl.MascotaDAOImpl;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

@Service
public class MascotaService {
    private IMascotaDAO mascotaDAO = new MascotaDAOImpl();

    public List<Mascota> listarPorCliente(int idCliente) {
        try {
            return mascotaDAO.obtenerPorCliente(idCliente);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public List<Mascota> listarTodo() {
        try {
            return mascotaDAO.listarTodo();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public List<Mascota> buscarMascotas(String termino) {
        try {
            if (termino == null || termino.trim().isEmpty()) {
                return mascotaDAO.listarTodo();
            }
            return mascotaDAO.buscarMascotas(termino);
        } catch (SQLException e) {
            System.err.println("Error en la búsqueda de mascotas: " + e.getMessage());
            return null; 
        }
    }

    public Mascota obtenerPorId(int idMascota) {
        try {
            return mascotaDAO.obtenerPorId(idMascota);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public int crear(Mascota mascota) {
        try {
            return mascotaDAO.insertar(mascota);
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    public boolean actualizar(Mascota mascota) {
        try {
            return mascotaDAO.actualizar(mascota);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean eliminar(int idMascota) {
        try {
            return mascotaDAO.eliminar(idMascota);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public byte[] obtenerFoto(int idMascota) {
        try {
            return mascotaDAO.obtenerFoto(idMascota);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}