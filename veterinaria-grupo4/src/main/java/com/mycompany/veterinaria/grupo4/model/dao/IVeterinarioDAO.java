package com.mycompany.veterinaria.grupo4.model.dao;

import com.mycompany.veterinaria.grupo4.model.entity.Veterinario;
import java.sql.SQLException;
import java.util.List;

public interface IVeterinarioDAO {
    List<Veterinario> obtenerTodos() throws SQLException;
    Veterinario obtenerPorCedula(String cedula) throws SQLException;
    Veterinario obtenerPorId(int id) throws SQLException;
    boolean insertar(Veterinario veterinario) throws SQLException;
    boolean actualizar(Veterinario veterinario) throws SQLException;
    boolean eliminar(int idVeterinario) throws SQLException;
    List<Veterinario> buscarPorNombre(String nombre) throws SQLException;
    List<Veterinario> buscarPorEspecialidad(String especialidad) throws SQLException;
    List<Veterinario> obtenerPorServicio(int idServicio) throws SQLException;
}