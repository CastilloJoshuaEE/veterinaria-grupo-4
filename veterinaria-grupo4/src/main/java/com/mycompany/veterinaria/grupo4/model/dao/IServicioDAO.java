package com.mycompany.veterinaria.grupo4.model.dao;

import com.mycompany.veterinaria.grupo4.model.entity.Servicio;
import com.mycompany.veterinaria.grupo4.model.entity.Veterinario;
import java.sql.SQLException;
import java.util.List;

public interface IServicioDAO {
    List<Servicio> obtenerTodos() throws SQLException;
    List<Servicio> obtenerActivos() throws SQLException;
    Servicio obtenerPorId(int idServicio) throws SQLException;
    int insertar(Servicio servicio) throws SQLException;
    boolean actualizar(Servicio servicio) throws SQLException;
    boolean eliminar(int idServicio) throws SQLException;
    boolean cambiarEstado(int idServicio, boolean estado) throws SQLException;
    List<Veterinario> obtenerVeterinariosAsignados(int idServicio) throws SQLException;
    List<Veterinario> obtenerVeterinariosNoAsignados(int idServicio) throws SQLException;
    boolean asignarVeterinario(int idServicio, int idVeterinario) throws SQLException;
    boolean eliminarAsignacionVeterinario(int idAsignacion) throws SQLException;
}