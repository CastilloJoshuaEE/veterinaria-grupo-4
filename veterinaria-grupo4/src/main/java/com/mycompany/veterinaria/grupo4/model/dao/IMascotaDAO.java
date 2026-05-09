package com.mycompany.veterinaria.grupo4.model.dao;

import com.mycompany.veterinaria.grupo4.model.entity.Mascota;
import java.sql.SQLException;
import java.util.List;

public interface IMascotaDAO {
    List<Mascota> obtenerPorCliente(int idCliente) throws SQLException;
    Mascota obtenerPorId(int idMascota) throws SQLException;
    int insertar(Mascota mascota) throws SQLException;
    boolean actualizar(Mascota mascota) throws SQLException;
    boolean eliminar(int idMascota) throws SQLException;
    byte[] obtenerFoto(int idMascota) throws SQLException;
    List<Mascota> listarTodo() throws SQLException;
    List<Mascota> buscarMascotas(String termino) throws SQLException; 
}