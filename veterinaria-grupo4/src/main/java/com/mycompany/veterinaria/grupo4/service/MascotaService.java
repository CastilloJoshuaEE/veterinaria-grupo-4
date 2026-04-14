/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
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