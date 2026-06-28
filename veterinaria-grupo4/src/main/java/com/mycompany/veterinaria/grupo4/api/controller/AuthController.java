package com.mycompany.veterinaria.grupo4.api.controller;

import com.mycompany.veterinaria.grupo4.api.dto.LoginRequest;
import com.mycompany.veterinaria.grupo4.model.entity.Recepcionista;
import com.mycompany.veterinaria.grupo4.model.entity.Usuario;
import com.mycompany.veterinaria.grupo4.service.AuthService;
import com.mycompany.veterinaria.grupo4.service.RecepcionistaService;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    @Autowired
    private RecepcionistaService recepcionistaService;
    /**
     * Realiza el inicio de sesión de un usuario en el sistema.
     * 
     * @param request objeto LoginRequest con usuario y contraseña
     * @return objeto Usuario si las credenciales son correctas
     */
    @PostMapping("/login")
    public Usuario login(@RequestBody LoginRequest request) {
        return authService.login(request.getEmail(), request.getPassword());
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
    @PostMapping("/restablecer-contrasena")
    public boolean restablecerContrasena(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String nuevaContrasena = request.get("nuevaContrasena");
        return authService.restablecerContrasena(email, nuevaContrasena);
    }    
    /**
     * Registra un nuevo recepcionista usando SP_REGISTRAR_RECEPCIONISTA.
     */
    @PostMapping("/registrar-recepcionista")
    public ResponseEntity<?> registrarRecepcionista(@RequestBody Recepcionista registro) {
        try {
            boolean resultado = recepcionistaService.registrarRecepcionista(registro);
            if (resultado) {
                return ResponseEntity.ok(true);
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error al registrar el recepcionista");
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(e.getMessage());
        } catch (RuntimeException e) {
            String msg = e.getMessage();
            if (msg != null && msg.contains("correo electrónico ya está registrado")) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("El correo electrónico ya está registrado");
            } else if (msg != null && msg.contains("cédula ya está registrada")) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("La cédula ya está registrada");
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error interno al registrar: " + msg);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error inesperado: " + e.getMessage());
        }
    }
  
}