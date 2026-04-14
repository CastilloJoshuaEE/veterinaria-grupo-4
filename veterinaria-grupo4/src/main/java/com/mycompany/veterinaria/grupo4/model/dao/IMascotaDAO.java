/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
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
}