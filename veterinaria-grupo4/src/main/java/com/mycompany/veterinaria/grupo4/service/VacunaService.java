package com.mycompany.veterinaria.grupo4.service;

import com.mycompany.veterinaria.grupo4.model.dao.IVacunaDAO;
import com.mycompany.veterinaria.grupo4.model.entity.VacunaAplicada;
import com.mycompany.veterinaria.grupo4.model.impl.VacunaDAOImpl;
import org.springframework.stereotype.Service;
import java.sql.SQLException;
import java.util.List;

@Service
public class VacunaService {
    private IVacunaDAO vacunaDAO = new VacunaDAOImpl();

    public List<VacunaAplicada> listarPorMascota(int idMascota) {
        try {
            return vacunaDAO.obtenerPorMascota(idMascota);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public int registrar(VacunaAplicada vacuna) {
        try {
            return vacunaDAO.registrar(vacuna);
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    public boolean actualizar(VacunaAplicada vacuna) {
        try {
            return vacunaDAO.actualizar(vacuna);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public VacunaAplicada verificarExistente(int idMascota, String nombreVacuna) {
        try {
            return vacunaDAO.verificarExistente(idMascota, nombreVacuna);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<VacunaAplicada> listarProximasAVencer(int diasAnticipacion) {
        try {
            return vacunaDAO.obtenerProximasAVencer(diasAnticipacion);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}