package com.mycompany.veterinaria.grupo4.service;

import com.mycompany.veterinaria.grupo4.model.dao.IAtencionMedicaDAO;
import com.mycompany.veterinaria.grupo4.model.entity.AtencionMedica;
import com.mycompany.veterinaria.grupo4.model.impl.AtencionMedicaDAOImpl;
import org.springframework.stereotype.Service;
import java.sql.SQLException;
import java.util.List;

@Service
public class AtencionMedicaService {
    private IAtencionMedicaDAO atencionDAO = new AtencionMedicaDAOImpl();

    public int guardar(AtencionMedica atencion) {
        try {
            return atencionDAO.insertar(atencion);
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    public List<AtencionMedica> listarTodas() {
        try {
            return atencionDAO.obtenerTodas();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public AtencionMedica obtenerPorId(int idAtencionMedica) {
        try {
            return atencionDAO.obtenerPorId(idAtencionMedica);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean eliminar(int idAtencionMedica) {
        try {
            return atencionDAO.eliminar(idAtencionMedica);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}