package com.mycompany.veterinaria.grupo4.service;

import com.mycompany.veterinaria.grupo4.model.dao.IUsuarioDAO;
import com.mycompany.veterinaria.grupo4.model.entity.Usuario;
import com.mycompany.veterinaria.grupo4.model.impl.UsuarioDAOImpl;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

/**
 * Servicio para la autenticacion y gestion de usuarios.
 * <p>
 * Proporciona la capa de servicios para operaciones relacionadas con
 * la autenticacion de usuarios, registro y consulta de informacion
 * de cuentas del sistema.
 * </p>
 * 
 * <p><b>Fecha de inicio del proyecto:</b> 15/04/2026</p>
 * 
 * @author ROBLES MORALES JUAN ANDRES – MODULO: ATENCION VETERINARIA
 * @version 1.0
 * @since 1.0
 */
@Service
public class AuthService {
    private IUsuarioDAO usuarioDAO = new UsuarioDAOImpl();

    /**
     * Valida las credenciales de un usuario para el inicio de sesion.
     *
     * @param usuario nombre de usuario
     * @param password contrasena
     * @return objeto Usuario si las credenciales son correctas
     */
    public Usuario login(String usuario, String password) {
        try {
            return usuarioDAO.validarCredenciales(usuario, password);
        } catch (SQLException e) {
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
        try {
            return usuarioDAO.existeUsuario(usuario);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Registra un nuevo usuario en el sistema.
     *
     * @param usuario objeto Usuario a registrar
     * @return ID del usuario creado
     */
    public int registrarUsuario(Usuario usuario) {
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
     * @return objeto Usuario encontrado
     */
    public Usuario obtenerUsuario(String usuario) {
        try {
            return usuarioDAO.obtenerUsuario(usuario);
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
}