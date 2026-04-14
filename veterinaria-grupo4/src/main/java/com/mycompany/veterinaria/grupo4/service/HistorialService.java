package com.mycompany.veterinaria.grupo4.service;

import com.mycompany.veterinaria.grupo4.model.dao.IHistorialDAO;
import com.mycompany.veterinaria.grupo4.model.entity.HistorialMedico;
import com.mycompany.veterinaria.grupo4.model.impl.HistorialDAOImpl;
import org.springframework.stereotype.Service;
import java.sql.SQLException;
import java.util.List;

@Service
public class HistorialService {
    private IHistorialDAO historialDAO = new HistorialDAOImpl();

    public List<HistorialMedico> obtenerPorMascota(int idMascota) {
        try {
            return historialDAO.obtenerPorMascota(idMascota);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean registrar(int idMascota, Integer idCita, Integer idAtencionMedica) {
        try {
            return historialDAO.registrar(idMascota, idCita, idAtencionMedica);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}