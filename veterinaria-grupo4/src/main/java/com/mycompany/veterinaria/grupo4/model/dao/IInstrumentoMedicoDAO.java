package com.mycompany.veterinaria.grupo4.model.dao;

import com.mycompany.veterinaria.grupo4.model.entity.InstrumentoMedico;
import java.sql.SQLException;
import java.util.List;

public interface IInstrumentoMedicoDAO {
    List<InstrumentoMedico> obtenerDisponibles() throws SQLException;
    InstrumentoMedico obtenerPorId(int idInstrumento) throws SQLException;
    boolean insertarUsado(int idAtencionMedica, int idInstrumento) throws SQLException;
    List<InstrumentoMedico> obtenerUsadosPorAtencion(int idAtencionMedica) throws SQLException;
}