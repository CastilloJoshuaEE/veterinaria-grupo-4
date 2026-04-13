package com.mycompany.veterinaria.grupo4.service;

import com.mycompany.veterinaria.grupo4.model.dao.IEspecialidadDAO;
import com.mycompany.veterinaria.grupo4.model.entity.EspecialidadVeterinaria;
import com.mycompany.veterinaria.grupo4.model.impl.EspecialidadDAOImpl;
import org.springframework.stereotype.Service;
import java.sql.SQLException;
import java.util.List;

@Service
public class EspecialidadService {
    private IEspecialidadDAO especialidadDAO = new EspecialidadDAOImpl();

    public List<EspecialidadVeterinaria> listarTodas() {
        try {
            return especialidadDAO.obtenerTodas();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public EspecialidadVeterinaria obtenerPorId(int id) {
        try {
            return especialidadDAO.obtenerPorId(id);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}