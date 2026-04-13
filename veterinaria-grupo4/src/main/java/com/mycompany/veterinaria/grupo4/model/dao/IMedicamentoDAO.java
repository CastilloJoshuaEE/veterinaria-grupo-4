package com.mycompany.veterinaria.grupo4.model.dao;

import com.mycompany.veterinaria.grupo4.model.entity.Medicamento;
import java.sql.SQLException;
import java.util.List;

public interface IMedicamentoDAO {
    List<Medicamento> obtenerDisponibles() throws SQLException;
    Medicamento obtenerPorId(int idMedicamento) throws SQLException;
    boolean insertarRecetado(int idAtencionMedica, int idMedicamento, String dosis, String frecuencia, String duracion) throws SQLException;
    List<Medicamento> obtenerRecetadosPorAtencion(int idAtencionMedica) throws SQLException;
}