/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.veterinaria.grupo4.model.dao;

import com.mycompany.veterinaria.grupo4.model.entity.Cliente;
import java.sql.SQLException;
import java.util.List;

public interface IClienteDAO {
    List<Cliente> obtenerTodos() throws SQLException;
    Cliente obtenerPorCedula(String cedula) throws SQLException;
    Cliente obtenerPorId(int id) throws SQLException;
    boolean insertar(Cliente cliente) throws SQLException;
    boolean actualizar(Cliente cliente) throws SQLException;
    boolean eliminar(int idCliente) throws SQLException;
    List<String> obtenerCedulas() throws SQLException;
    List<Cliente> buscarPorNombre(String nombre) throws SQLException;
    List<Cliente> buscarPorMascota(String nombreMascota, String nombreCliente) throws SQLException;
}