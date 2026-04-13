package com.mycompany.veterinaria.grupo4.service;

import com.mycompany.veterinaria.grupo4.model.dao.IMedicamentoDAO;
import com.mycompany.veterinaria.grupo4.model.entity.Medicamento;
import com.mycompany.veterinaria.grupo4.model.impl.MedicamentoDAOImpl;
import org.springframework.stereotype.Service;
import java.sql.SQLException;
import java.util.List;

@Service
public class MedicamentoService {
    private IMedicamentoDAO medicamentoDAO = new MedicamentoDAOImpl();

    public List<Medicamento> listarDisponibles() {
        try {
            return medicamentoDAO.obtenerDisponibles();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Medicamento obtenerPorId(int idMedicamento) {
        try {
            return medicamentoDAO.obtenerPorId(idMedicamento);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean recetar(int idAtencionMedica, int idMedicamento, String dosis, String frecuencia, String duracion) {
        try {
            return medicamentoDAO.insertarRecetado(idAtencionMedica, idMedicamento, dosis, frecuencia, duracion);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Medicamento> listarRecetadosPorAtencion(int idAtencionMedica) {
        try {
            return medicamentoDAO.obtenerRecetadosPorAtencion(idAtencionMedica);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}