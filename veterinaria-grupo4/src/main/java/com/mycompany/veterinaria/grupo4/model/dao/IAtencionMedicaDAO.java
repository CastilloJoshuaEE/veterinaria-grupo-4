package com.mycompany.veterinaria.grupo4.model.dao;

import com.mycompany.veterinaria.grupo4.model.entity.AtencionMedica;
import java.sql.SQLException;
import java.util.List;

public interface IAtencionMedicaDAO {
    int insertar(AtencionMedica atencion) throws SQLException;
    List<AtencionMedica> obtenerTodas() throws SQLException;
    AtencionMedica obtenerPorId(int idAtencionMedica) throws SQLException;
    boolean eliminar(int idAtencionMedica) throws SQLException;
    List<AtencionMedica> obtenerPorMascotaYCita(int idMascota, int idCita) throws SQLException;
}