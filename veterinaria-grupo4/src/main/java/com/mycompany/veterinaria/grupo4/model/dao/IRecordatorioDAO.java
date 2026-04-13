package com.mycompany.veterinaria.grupo4.model.dao;

import com.mycompany.veterinaria.grupo4.model.entity.Recordatorio;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

public interface IRecordatorioDAO {
    List<Recordatorio> obtenerPendientes(int idUsuario) throws SQLException;
    boolean marcarComoLeido(int idRecordatorio) throws SQLException;
    void generarRecordatorios(int idUsuario) throws SQLException;
    List<Recordatorio> obtenerTodos(Date fechaInicio, Date fechaFin) throws SQLException;
    void incrementarContador(int idRecordatorio) throws SQLException;
    int obtenerContador(int idRecordatorio) throws SQLException;
    int registrar(Recordatorio recordatorio, String anticipacion) throws SQLException;
    boolean actualizar(Recordatorio recordatorio) throws SQLException;
    boolean eliminar(int idRecordatorio) throws SQLException;
}