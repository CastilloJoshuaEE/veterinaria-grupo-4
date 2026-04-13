package com.mycompany.veterinaria.grupo4.model.dao;

import com.mycompany.veterinaria.grupo4.model.entity.HistorialMedico;
import java.sql.SQLException;
import java.util.List;

public interface IHistorialDAO {
    List<HistorialMedico> obtenerPorMascota(int idMascota) throws SQLException;
    boolean registrar(int idMascota, Integer idCita, Integer idAtencionMedica) throws SQLException;
}