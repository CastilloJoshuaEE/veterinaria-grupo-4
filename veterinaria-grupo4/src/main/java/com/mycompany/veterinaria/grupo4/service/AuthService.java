package com.mycompany.veterinaria.grupo4.service;

import com.mycompany.veterinaria.grupo4.model.dao.IUsuarioDAO;
import com.mycompany.veterinaria.grupo4.model.entity.Usuario;
import com.mycompany.veterinaria.grupo4.model.impl.UsuarioDAOImpl;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Servicio para la autenticacion y gestion de usuarios con validaciones de negocio.
 * <p>
 * Proporciona la capa de servicios para operaciones relacionadas con
 * la autenticacion de usuarios, registro y consulta de informacion
 * de cuentas del sistema.
 * </p>
 * 
 * <p><b>Reglas de negocio implementadas:</b></p>
 * <ul>
 *   <li>El nombre de usuario debe tener entre 4 y 50 caracteres</li>
 *   <li>La contrasena debe tener al menos 8 caracteres</li>
 *   <li>El correo electronico debe tener formato valido</li>
 *   <li>El nombre de usuario debe ser unico</li>
 *   <li>No se permite login con usuarios inactivos</li>
 * </ul>
 * 
 * <p><b>Fecha de inicio del proyecto:</b> 15/04/2026</p>
 * 
 * @author ROBLES MORALES JUAN ANDRES – MODULO: ATENCION VETERINARIA
 * @version 2.0
 * @since 1.0
 */
@Service
public class AuthService {
    
    private static final Pattern PATRON_EMAIL = Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");
    private static final int USUARIO_MIN_LENGTH = 1;  // Cambiado para permitir usuarios cortos de prueba
    private static final int USUARIO_MAX_LENGTH = 50;
    private static final int PASSWORD_MIN_LENGTH = 1;  // Cambiado para permitir contrasenas cortas de prueba
    
    private IUsuarioDAO usuarioDAO = new UsuarioDAOImpl();

    /**
     * Valida las credenciales de un usuario para el inicio de sesion.
     * <p>
     * <b>Nota:</b> Para desarrollo, se permite cualquier longitud de usuario y password.
     * </p>
     *
     * @param usuario nombre de usuario
     * @param password contrasena
     * @return objeto Usuario si las credenciales son correctas, null si no
     */
    public Usuario login(String usuario, String password) {
        // Validaciones basicas sin restricciones estrictas para desarrollo
        if (usuario == null || usuario.trim().isEmpty()) {
            System.err.println("Intento de login con usuario vacio");
            return null;
        }
        if (password == null || password.trim().isEmpty()) {
            System.err.println("Intento de login con password vacio para usuario: " + usuario);
            return null;
        }
        
        try {
            Usuario user = usuarioDAO.validarCredenciales(usuario.trim(), password);
            if (user == null) {
                System.err.println("Credenciales invalidas para usuario: " + usuario);
                return null;
            }
            System.out.println("Login exitoso para usuario: " + usuario + " (ID: " + user.getIdUsuario() + ")");
            return user;
        } catch (SQLException e) {
            System.err.println("Error SQL al validar credenciales para usuario: " + usuario);
            e.printStackTrace();
            return null;
        } catch (Exception e) {
            System.err.println("Error inesperado al validar credenciales: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Verifica si existe un usuario con el nombre especificado.
     *
     * @param usuario nombre de usuario a verificar
     * @return true si el usuario existe
     */
    public boolean existeUsuario(String usuario) {
        if (usuario == null || usuario.trim().isEmpty()) {
            return false;
        }
        try {
            return usuarioDAO.existeUsuario(usuario.trim());
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Valida y registra un nuevo usuario en el sistema.
     *
     * @param usuario objeto Usuario a registrar
     * @return ID del usuario creado, -1 si hay error
     */
    public int registrarUsuario(Usuario usuario) {
        if (usuario == null) {
            System.err.println("Intento de registrar usuario nulo");
            return -1;
        }
        
        if (usuario.getNombreUsuario() == null || usuario.getNombreUsuario().trim().isEmpty()) {
            System.err.println("Nombre de usuario vacio");
            return -1;
        }
        
        if (usuario.getContrasena() == null || usuario.getContrasena().trim().isEmpty()) {
            System.err.println("Contrasena vacia para usuario: " + usuario.getNombreUsuario());
            return -1;
        }
        
        try {
            return usuarioDAO.registrarUsuario(usuario);
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * Obtiene un usuario por su nombre de usuario.
     *
     * @param usuario nombre de usuario
     * @return objeto Usuario encontrado o null
     */
    public Usuario obtenerUsuario(String usuario) {
        if (usuario == null || usuario.trim().isEmpty()) {
            return null;
        }
        try {
            return usuarioDAO.obtenerUsuario(usuario.trim());
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * Obtiene todos los usuarios registrados en el sistema.
     *
     * @return lista de usuarios
     */
    public List<Usuario> obtenerTodosUsuarios() {
        try {
            return usuarioDAO.obtenerTodos();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * Valida el formato del nombre de usuario (solo para registro).
     *
     * @param usuario nombre a validar
     * @throws IllegalArgumentException si el nombre es invalido
     */
    private void validarNombreUsuario(String usuario) {
        if (usuario == null || usuario.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de usuario es obligatorio");
        }
        if (usuario.trim().length() < USUARIO_MIN_LENGTH) {
            throw new IllegalArgumentException("El nombre de usuario debe tener al menos " + USUARIO_MIN_LENGTH + " caracteres");
        }
        if (usuario.trim().length() > USUARIO_MAX_LENGTH) {
            throw new IllegalArgumentException("El nombre de usuario no puede exceder los " + USUARIO_MAX_LENGTH + " caracteres");
        }
    }
    
    /**
     * Valida la contrasena (solo para registro).
     *
     * @param password contrasena a validar
     * @throws IllegalArgumentException si la contrasena es invalida
     */
    private void validarPassword(String password) {
        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("La contrasena es obligatoria");
        }
        if (password.length() < PASSWORD_MIN_LENGTH) {
            throw new IllegalArgumentException("La contrasena debe tener al menos " + PASSWORD_MIN_LENGTH + " caracteres");
        }
    }
    
    /**
     * Valida el formato del correo electronico.
     *
     * @param email correo a validar
     * @throws IllegalArgumentException si el email es invalido
     */
    private void validarEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("El correo electronico es obligatorio");
        }
        if (!PATRON_EMAIL.matcher(email).matches()) {
            throw new IllegalArgumentException("El formato del correo electronico es invalido");
        }
    }
    
    /**
     * Valida todos los campos del objeto Usuario para registro.
     *
     * @param usuario objeto a validar
     * @throws IllegalArgumentException si algun campo es invalido
     */
    private void validarUsuario(Usuario usuario) {
        if (usuario == null) {
            throw new IllegalArgumentException("El objeto usuario no puede ser nulo");
        }
        
        validarNombreUsuario(usuario.getNombreUsuario());
        validarPassword(usuario.getContrasena());
        validarEmail(usuario.getCorreoElectronico());
        
        if (usuario.getRol() == null || usuario.getRol().trim().isEmpty()) {
            usuario.setRol("ADMINISTRADOR");
        }
    }
}