package com.mycompany.veterinaria.grupo4.service;

import com.mycompany.veterinaria.grupo4.model.dao.IFichaMedicaDAO;
import com.mycompany.veterinaria.grupo4.model.entity.FichaMedica;
import com.mycompany.veterinaria.grupo4.model.impl.FichaMedicaDAOImpl;
import org.springframework.stereotype.Service;
import java.sql.SQLException;

@Service
public class FichaMedicaService {
    private IFichaMedicaDAO fichaMedicaDAO = new FichaMedicaDAOImpl();

    public FichaMedica obtenerPorMascota(int idMascota) {
        try {
            return fichaMedicaDAO.obtenerPorMascota(idMascota);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean actualizar(int idMascota, String alergias, String enfermedadesCronicas, String observaciones) {
        try {
            return fichaMedicaDAO.actualizar(idMascota, alergias, enfermedadesCronicas, observaciones);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}