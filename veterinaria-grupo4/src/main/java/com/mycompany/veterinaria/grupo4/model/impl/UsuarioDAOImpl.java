package com.mycompany.veterinaria.grupo4.model.impl;

import com.mycompany.veterinaria.grupo4.model.dao.IUsuarioDAO;
import com.mycompany.veterinaria.grupo4.model.entity.Usuario;
import com.mycompany.veterinaria.grupo4.util.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementacion del DAO para la gestion de usuarios.
 * <p>
 * Esta clase implementa la interfaz IUsuarioDAO y proporciona la logica
 * de acceso a datos para la entidad Usuario utilizando procedimientos
 * almacenados de SQL Server. Permite validar credenciales, registrar
 * usuarios, verificar existencia y obtener listados de usuarios.
 * </p>
 * 
 * <p><b>Fecha de inicio del proyecto:</b> 15/04/2026</p>
 * 
 * @author ROBLES MORALES JUAN ANDRES – MODULO: ATENCION VETERINARIA
 * @version 1.0
 * @since 1.0
 */
public class UsuarioDAOImpl implements IUsuarioDAO {

    /**
     * Valida las credenciales de un usuario para el inicio de sesion.
     *
     * @param usuario nombre de usuario
     * @param password contrasena
     * @return objeto Usuario si las credenciales son correctas
     * @throws SQLException si ocurre un error en la base de datos
     */
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

    /**
     * Verifica si existe un usuario con el nombre especificado.
     *
     * @param usuario nombre de usuario a verificar
     * @return true si el usuario existe
     * @throws SQLException si ocurre un error en la base de datos
     */
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

    /**
     * Registra un nuevo usuario en el sistema.
     *
     * @param usuario objeto Usuario a registrar
     * @return ID del usuario creado
     * @throws SQLException si ocurre un error en la base de datos
     */
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

    /**
     * Obtiene un usuario por su nombre de usuario.
     *
     * @param usuario nombre de usuario
     * @return objeto Usuario encontrado
     * @throws SQLException si ocurre un error en la base de datos
     */
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
    
    /**
     * Obtiene todos los usuarios registrados.
     *
     * @return lista de todos los usuarios
     * @throws SQLException si ocurre un error en la base de datos
     */
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