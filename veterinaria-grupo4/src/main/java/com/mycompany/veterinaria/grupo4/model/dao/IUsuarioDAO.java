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
     * Valida las credenciales de un usuario usando email y contraseña.
     * 
     * @param email correo electrónico del usuario
     * @param password contraseña del usuario
     * @return objeto Usuario si las credenciales son correctas
     * @throws SQLException si ocurre un error en la base de datos
     */
    Usuario validarCredenciales(String email, String password) throws SQLException;
    
    /**
     * Verifica si existe un usuario con el email especificado.
     * 
     * @param email correo electrónico a verificar
     * @return true si el usuario existe
     * @throws SQLException si ocurre un error en la base de datos
     */
    boolean existeUsuario(String email) throws SQLException;
    
    /**
     * Registra un nuevo usuario en el sistema.
     * 
     * @param usuario objeto Usuario a registrar
     * @return ID del usuario creado
     * @throws SQLException si ocurre un error en la base de datos
     */
    int registrarUsuario(Usuario usuario) throws SQLException;
    
    /**
     * Obtiene un usuario por su email.
     * 
     * @param email correo electrónico del usuario
     * @return objeto Usuario encontrado
     * @throws SQLException si ocurre un error en la base de datos
     */
    Usuario obtenerUsuario(String email) throws SQLException;
    
    /**
     * Obtiene todos los usuarios registrados.
     * 
     * @return lista de todos los usuarios
     * @throws SQLException si ocurre un error en la base de datos
     */
    List<Usuario> obtenerTodos() throws SQLException;
    /**
     * Actualiza la contraseña de un usuario por su email.
     * 
     * @param email correo electrónico del usuario
     * @param nuevaContrasena nueva contraseña a establecer
     * @return true si la actualización fue exitosa
     * @throws SQLException si ocurre un error en la base de datos
     */
    boolean actualizarContrasena(String email, String nuevaContrasena) throws SQLException;
    /**
     * Actualiza el correo electrónico de un usuario.
     * 
     * @param emailActual correo electrónico actual
     * @param emailNuevo nuevo correo electrónico
     * @return true si la actualización fue exitosa
     * @throws SQLException si ocurre un error en la base de datos
     */
    boolean actualizarEmail(String emailActual, String emailNuevo) throws SQLException;
}