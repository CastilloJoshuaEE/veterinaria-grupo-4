package com.mycompany.veterinaria.grupo4.api.controller;

import com.mycompany.veterinaria.grupo4.api.dto.LoginRequest;
import com.mycompany.veterinaria.grupo4.model.entity.Usuario;
import com.mycompany.veterinaria.grupo4.service.AuthService;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador REST para la autenticación y gestión de usuarios.
 * <p>
 * Proporciona endpoints para login, registro, verificación de usuarios
 * y obtención de correos electrónicos de usuarios activos.
 * </p>
 * 
 * <p><b>Fecha de inicio del proyecto:</b> 15/04/2026</p>
 * 
 * @author ROBLES MORALES JUAN ANDRES – MÓDULO: ATENCIÓN VETERINARIA
 * @version 1.0
 * @since 1.0
 */
@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private AuthService authService;

    /**
     * Realiza el inicio de sesión de un usuario en el sistema.
     * 
     * @param request objeto LoginRequest con usuario y contraseña
     * @return objeto Usuario si las credenciales son correctas
     */
    @PostMapping("/login")
    public Usuario login(@RequestBody LoginRequest request) {
        return authService.login(request.getUsuario(), request.getPassword());
    }

    /**
     * Verifica si existe un usuario con el nombre de usuario especificado.
     * 
     * @param usuario nombre de usuario a verificar
     * @return true si el usuario existe, false en caso contrario
     */
    @GetMapping("/existe/{usuario}")
    public boolean existeUsuario(@PathVariable String usuario) {
        return authService.existeUsuario(usuario);
    }

    /**
     * Registra un nuevo usuario en el sistema.
     * 
     * @param usuario objeto Usuario con los datos a registrar
     * @return ID generado para el nuevo usuario
     */
    @PostMapping("/registrar")
    public int registrar(@RequestBody Usuario usuario) {
        return authService.registrarUsuario(usuario);
    }

    /**
     * Obtiene un usuario por su nombre de usuario.
     * 
     * @param usuario nombre de usuario a buscar
     * @return objeto Usuario encontrado
     */
    @GetMapping("/usuario/{usuario}")
    public Usuario obtenerUsuario(@PathVariable String usuario) {
        return authService.obtenerUsuario(usuario);
    }
    
    /**
     * Obtiene la lista de correos electrónicos de todos los usuarios activos.
     * 
     * @return Lista de mapas con los correos electrónicos de usuarios activos
     */
    @GetMapping("/correos")
    public List<Map<String, String>> obtenerCorreosUsuarios() {
        List<Map<String, String>> resultado = new ArrayList<>();
        List<Usuario> usuarios = authService.obtenerTodosUsuarios();
        
        if (usuarios != null) {
            for (Usuario u : usuarios) {
                // Solo incluir usuarios activos y con correo válido
                if (u.isEstado() && u.getCorreoElectronico() != null && !u.getCorreoElectronico().isEmpty()) {
                    Map<String, String> item = new HashMap<>();
                    item.put("CORREO_ELECTRONICO", u.getCorreoElectronico());
                    resultado.add(item);
                }
            }
        }
        return resultado;
    }
}