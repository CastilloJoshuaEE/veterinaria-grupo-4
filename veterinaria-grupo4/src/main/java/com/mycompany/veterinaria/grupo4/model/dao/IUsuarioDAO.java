package com.mycompany.veterinaria.grupo4.model.dao;

import com.mycompany.veterinaria.grupo4.model.entity.Usuario;
import java.sql.SQLException;
import java.util.List;

/**
 * Interfaz DAO para la gestion de usuarios del sistema.
 * <p>
 * Define las operaciones de acceso a datos para la entidad Usuario,
 * incluyendo validacion de credenciales, verificacion de existencia,
 * registro de nuevos usuarios y consulta de usuarios.
 * </p>
 * 
 * <p><b>Fecha de inicio del proyecto:</b> 15/04/2026</p>
 * 
 * @author ROBLES MORALES JUAN ANDRES – MODULO: ATENCION VETERINARIA
 * @version 1.0
 * @since 1.0
 */
public interface IUsuarioDAO {
    
    /**
     * Valida las credenciales de un usuario para el inicio de sesion.
     * 
     * @param usuario nombre de usuario
     * @param password contrasena
     * @return objeto Usuario si las credenciales son correctas
     * @throws SQLException si ocurre un error en la base de datos
     */
    Usuario validarCredenciales(String usuario, String password) throws SQLException;
    
    /**
     * Verifica si existe un usuario con el nombre especificado.
     * 
     * @param usuario nombre de usuario a verificar
     * @return true si el usuario existe
     * @throws SQLException si ocurre un error en la base de datos
     */
    boolean existeUsuario(String usuario) throws SQLException;
    
    /**
     * Registra un nuevo usuario en el sistema.
     * 
     * @param usuario objeto Usuario a registrar
     * @return ID del usuario creado
     * @throws SQLException si ocurre un error en la base de datos
     */
    int registrarUsuario(Usuario usuario) throws SQLException;
    
    /**
     * Obtiene un usuario por su nombre de usuario.
     * 
     * @param usuario nombre de usuario
     * @return objeto Usuario encontrado
     * @throws SQLException si ocurre un error en la base de datos
     */
    Usuario obtenerUsuario(String usuario) throws SQLException;
    
    /**
     * Obtiene todos los usuarios registrados.
     * 
     * @return lista de todos los usuarios
     * @throws SQLException si ocurre un error en la base de datos
     */
    List<Usuario> obtenerTodos() throws SQLException;
}