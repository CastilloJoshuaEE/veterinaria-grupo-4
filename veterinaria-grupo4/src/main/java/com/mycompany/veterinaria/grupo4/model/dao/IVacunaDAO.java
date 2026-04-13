package com.mycompany.veterinaria.grupo4.model.dao;

import com.mycompany.veterinaria.grupo4.model.entity.VacunaAplicada;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

public interface IVacunaDAO {
    List<VacunaAplicada> obtenerPorMascota(int idMascota) throws SQLException;
    int registrar(VacunaAplicada vacuna) throws SQLException;
    boolean actualizar(VacunaAplicada vacuna) throws SQLException;
    VacunaAplicada verificarExistente(int idMascota, String nombreVacuna) throws SQLException;
    List<VacunaAplicada> obtenerProximasAVencer(int diasAnticipacion) throws SQLException;
}