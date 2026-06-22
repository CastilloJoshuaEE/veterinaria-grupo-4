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
     * Valida las credenciales de un usuario usando email y contraseña.
     *
     * @param email correo electrónico del usuario
     * @param password contraseña del usuario
     * @return objeto Usuario si las credenciales son correctas, null en caso contrario
     * @throws SQLException si ocurre un error en la base de datos
     */
    @Override
    public Usuario validarCredenciales(String email, String password) throws SQLException {
        String sql = "{call SP_VALIDAR_USUARIO(?, ?)}";
        try (Connection conn = DatabaseConnection.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {
            stmt.setString(1, email);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Usuario u = new Usuario();
                u.setIdUsuario(rs.getInt("ID_USUARIO"));
                u.setEmail(rs.getString("EMAIL"));
                u.setRol(rs.getString("ROL"));
                u.setEstado(rs.getBoolean("ESTADO"));
                try {
                    u.setNombreCompleto(rs.getString("NOMBRE_COMPLETO"));
                } catch (SQLException e) {
                    u.setNombreCompleto("");
                }
                return u;
            }
            return null;
        }
    }

    /**
     * Verifica si existe un usuario con el email especificado.
     *
     * @param email correo electrónico a verificar
     * @return true si el usuario existe
     * @throws SQLException si ocurre un error en la base de datos
     */
    @Override
    public boolean existeUsuario(String email) throws SQLException {
        String sql = "{call SP_EXISTE_USUARIO(?)}";
        try (Connection conn = DatabaseConnection.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {
            stmt.setString(1, email);
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
        String sql = "{call SP_INSERTAR_USUARIO(?, ?, ?)}";
        try (Connection conn = DatabaseConnection.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {
            stmt.setString(1, usuario.getEmail());
            stmt.setString(2, usuario.getContrasena());
            stmt.setString(3, usuario.getRol() != null ? usuario.getRol() : "ADMINISTRADOR");
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("ID_USUARIO");
            }
            return -1;
        }
    }

    /**
     * Obtiene un usuario por su email.
     *
     * @param email correo electrónico del usuario
     * @return objeto Usuario encontrado
     * @throws SQLException si ocurre un error en la base de datos
     */
    @Override
    public Usuario obtenerUsuario(String email) throws SQLException {
        String sql = "{call SP_OBTENER_USUARIO(?)}";
        try (Connection conn = DatabaseConnection.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Usuario u = new Usuario();
                u.setIdUsuario(rs.getInt("ID_USUARIO"));
                u.setEmail(rs.getString("CORREO_ELECTRONICO"));
                u.setRol(rs.getString("ROL_EFECTIVO"));
                u.setFechaCreacion(rs.getTimestamp("FECHA_CREACION"));
                u.setEstado(rs.getBoolean("ESTADO"));
                try {
                    u.setNombreCompleto(rs.getString("NOMBRE_COMPLETO"));
                } catch (SQLException e) {
                    u.setNombreCompleto("");
                }
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
        String sql = "SELECT U.ID_USUARIO, U.CORREO_ELECTRONICO, U.CONTRASENA, " +
                     "U.ROL, U.FECHA_CREACION, U.ESTADO, " +
                     "CASE " +
                     "    WHEN EXISTS (SELECT 1 FROM VETERINARIO V WHERE V.CORREO_ELECTRONICO = U.CORREO_ELECTRONICO) " +
                     "        THEN (SELECT CONCAT(NOMBRE, ' ', APELLIDO) FROM VETERINARIO WHERE CORREO_ELECTRONICO = U.CORREO_ELECTRONICO) " +
                     "    WHEN EXISTS (SELECT 1 FROM RECEPCIONISTA R WHERE R.CORREO_ELECTRONICO = U.CORREO_ELECTRONICO) " +
                     "        THEN (SELECT CONCAT(NOMBRE, ' ', APELLIDO) FROM RECEPCIONISTA WHERE CORREO_ELECTRONICO = U.CORREO_ELECTRONICO) " +
                     "    ELSE 'Administrador' " +
                     "END AS NOMBRE_COMPLETO " +
                     "FROM USUARIO U WHERE U.ESTADO = 1 ORDER BY U.CORREO_ELECTRONICO";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Usuario u = new Usuario();
                u.setIdUsuario(rs.getInt("ID_USUARIO"));
                u.setEmail(rs.getString("CORREO_ELECTRONICO"));
                u.setContrasena(rs.getString("CONTRASENA"));
                u.setRol(rs.getString("ROL"));
                u.setFechaCreacion(rs.getTimestamp("FECHA_CREACION"));
                u.setEstado(rs.getBoolean("ESTADO"));
                try {
                    u.setNombreCompleto(rs.getString("NOMBRE_COMPLETO"));
                } catch (SQLException e) {
                    u.setNombreCompleto("");
                }
                lista.add(u);
            }
        }
        return lista;
    }
    /**
     * Actualiza la contraseña de un usuario por su email.
     */
    @Override
    public boolean actualizarContrasena(String email, String nuevaContrasena) throws SQLException {
        String sql = "UPDATE USUARIO SET CONTRASENA = ? WHERE CORREO_ELECTRONICO = ? AND ESTADO = 1";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nuevaContrasena);
            stmt.setString(2, email);
            
            int filasAfectadas = stmt.executeUpdate();
            return filasAfectadas > 0;
        }
    }    
}