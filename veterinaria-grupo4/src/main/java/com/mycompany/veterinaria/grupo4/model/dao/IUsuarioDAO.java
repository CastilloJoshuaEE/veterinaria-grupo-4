/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.veterinaria.grupo4.model.dao;

import com.mycompany.veterinaria.grupo4.model.entity.Usuario;
import java.sql.SQLException;
import java.util.List;

public interface IUsuarioDAO {
    Usuario validarCredenciales(String usuario, String password) throws SQLException;
    boolean existeUsuario(String usuario) throws SQLException;
    int registrarUsuario(Usuario usuario) throws SQLException;
    Usuario obtenerUsuario(String usuario) throws SQLException;
List<Usuario> obtenerTodos() throws SQLException; 
}