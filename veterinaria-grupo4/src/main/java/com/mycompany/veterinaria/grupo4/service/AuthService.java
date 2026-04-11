/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.veterinaria.grupo4.service;

import com.mycompany.veterinaria.grupo4.model.dao.IUsuarioDAO;
import com.mycompany.veterinaria.grupo4.model.entity.Usuario;
import com.mycompany.veterinaria.grupo4.model.impl.UsuarioDAOImpl;
import org.springframework.stereotype.Service;

import java.sql.SQLException;

@Service
public class AuthService {
    private IUsuarioDAO usuarioDAO = new UsuarioDAOImpl();

    public Usuario login(String usuario, String password) {
        try {
            return usuarioDAO.validarCredenciales(usuario, password);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean existeUsuario(String usuario) {
        try {
            return usuarioDAO.existeUsuario(usuario);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public int registrarUsuario(Usuario usuario) {
        try {
            return usuarioDAO.registrarUsuario(usuario);
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    public Usuario obtenerUsuario(String usuario) {
        try {
            return usuarioDAO.obtenerUsuario(usuario);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}