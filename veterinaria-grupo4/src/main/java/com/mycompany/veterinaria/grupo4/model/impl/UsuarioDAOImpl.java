/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.veterinaria.grupo4.model.impl;

import com.mycompany.veterinaria.grupo4.model.dao.IUsuarioDAO;
import com.mycompany.veterinaria.grupo4.model.entity.Usuario;
import com.mycompany.veterinaria.grupo4.util.DatabaseConnection;
import com.mycompany.veterinaria.grupo4.util.Parametro;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAOImpl implements IUsuarioDAO {

    @Override
    public Usuario validarCredenciales(String usuario, String password) throws SQLException {
        String sql = "{call SP_VALIDAR_USUARIO(?, ?)}";
        try (Connection conn = DatabaseConnection.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {
            stmt.setString(1, usuario);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Usuario u = new Usuario();
                u.setIdUsuario(rs.getInt("ID_USUARIO"));
                u.setNombreUsuario(rs.getString("NOMBRE_USUARIO"));
                u.setRol(rs.getString("ROL"));
                return u;
            }
            return null;
        }
    }

    @Override
    public boolean existeUsuario(String usuario) throws SQLException {
        String sql = "{call SP_EXISTE_USUARIO(?)}";
        try (Connection conn = DatabaseConnection.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {
            stmt.setString(1, usuario);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        }
    }

    @Override
    public int registrarUsuario(Usuario usuario) throws SQLException {
        String sql = "{call SP_INSERTAR_USUARIO(?, ?, ?, ?)}";
        try (Connection conn = DatabaseConnection.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {
            stmt.setString(1, usuario.getNombreUsuario());
            stmt.setString(2, usuario.getContrasena());
            stmt.setString(3, usuario.getCorreoElectronico());
            stmt.setString(4, usuario.getRol() != null ? usuario.getRol() : "ADMINISTRADOR");
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("ID_USUARIO");
            }
            return -1;
        }
    }

    @Override
    public Usuario obtenerUsuario(String usuario) throws SQLException {
        String sql = "{call SP_OBTENER_USUARIO(?)}";
        try (Connection conn = DatabaseConnection.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {
            stmt.setString(1, usuario);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Usuario u = new Usuario();
                u.setIdUsuario(rs.getInt("ID_USUARIO"));
                u.setNombreUsuario(rs.getString("NOMBRE_USUARIO"));
                u.setCorreoElectronico(rs.getString("CORREO_ELECTRONICO"));
                u.setRol(rs.getString("ROL"));
                u.setFechaCreacion(rs.getTimestamp("FECHA_CREACION"));
                u.setEstado(rs.getBoolean("ESTADO"));
                return u;
            }
            return null;
        }
    }
    @Override
    public List<Usuario> obtenerTodos() throws SQLException {
        List<Usuario> lista = new ArrayList<>();
        String sql = "SELECT ID_USUARIO, NOMBRE_USUARIO, CONTRASENA, CORREO_ELECTRONICO, " +
                     "ROL, FECHA_CREACION, ESTADO FROM USUARIO WHERE ESTADO = 1 ORDER BY NOMBRE_USUARIO";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Usuario u = new Usuario();
                u.setIdUsuario(rs.getInt("ID_USUARIO"));
                u.setNombreUsuario(rs.getString("NOMBRE_USUARIO"));
                u.setContrasena(rs.getString("CONTRASENA"));
                u.setCorreoElectronico(rs.getString("CORREO_ELECTRONICO"));
                u.setRol(rs.getString("ROL"));
                u.setFechaCreacion(rs.getTimestamp("FECHA_CREACION"));
                u.setEstado(rs.getBoolean("ESTADO"));
                lista.add(u);
            }
        }
        return lista;
    }
}