package com.mycompany.veterinaria.grupo4.service;

import com.mycompany.veterinaria.grupo4.model.dao.IInstrumentoMedicoDAO;
import com.mycompany.veterinaria.grupo4.model.entity.InstrumentoMedico;
import com.mycompany.veterinaria.grupo4.model.impl.InstrumentoMedicoDAOImpl;
import org.springframework.stereotype.Service;
import java.sql.SQLException;
import java.util.List;

@Service
public class InstrumentoMedicoService {
    private IInstrumentoMedicoDAO instrumentoDAO = new InstrumentoMedicoDAOImpl();

    public List<InstrumentoMedico> listarDisponibles() {
        try {
            return instrumentoDAO.obtenerDisponibles();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public InstrumentoMedico obtenerPorId(int idInstrumento) {
        try {
            return instrumentoDAO.obtenerPorId(idInstrumento);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean registrarUso(int idAtencionMedica, int idInstrumento) {
        try {
            return instrumentoDAO.insertarUsado(idAtencionMedica, idInstrumento);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<InstrumentoMedico> listarUsadosPorAtencion(int idAtencionMedica) {
        try {
            return instrumentoDAO.obtenerUsadosPorAtencion(idAtencionMedica);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}