package com.mycompany.veterinaria.grupo4.model.dao;

import com.mycompany.veterinaria.grupo4.model.entity.EspecialidadVeterinaria;
import java.sql.SQLException;
import java.util.List;

public interface IEspecialidadDAO {
    List<EspecialidadVeterinaria> obtenerTodas() throws SQLException;
    EspecialidadVeterinaria obtenerPorId(int id) throws SQLException;
}