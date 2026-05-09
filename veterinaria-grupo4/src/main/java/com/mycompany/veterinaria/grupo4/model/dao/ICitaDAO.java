package com.mycompany.veterinaria.grupo4.model.dao;

import com.mycompany.veterinaria.grupo4.model.entity.Cita;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

public interface ICitaDAO {
    List<Cita> obtenerPorFecha(Date fecha) throws SQLException;
    List<Cita> obtenerPorCliente(int idCliente) throws SQLException;
    Cita obtenerPorId(int idCita) throws SQLException;
    int agendar(Cita cita) throws SQLException;
    boolean actualizar(Cita cita) throws SQLException;
    boolean cancelar(int idCita, String motivo) throws SQLException;
    List<Cita> obtenerTodas() throws SQLException;
    List<Cita> obtenerPorRangoFechas(Date fechaInicio, Date fechaFin) throws SQLException;
    List<Cita> obtenerPorServicioYVeterinario(int idServicio, int idVeterinario, String estado) throws SQLException;
    boolean actualizarEstado(int idCita, String estado) throws SQLException;
    boolean eliminar(int idCita) throws SQLException;
}