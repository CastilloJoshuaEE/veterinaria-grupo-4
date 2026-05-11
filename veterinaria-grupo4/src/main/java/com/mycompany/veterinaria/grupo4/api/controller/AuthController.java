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

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public Usuario login(@RequestBody LoginRequest request) {
        return authService.login(request.getUsuario(), request.getPassword());
    }

    @GetMapping("/existe/{usuario}")
    public boolean existeUsuario(@PathVariable String usuario) {
        return authService.existeUsuario(usuario);
    }

    @PostMapping("/registrar")
    public int registrar(@RequestBody Usuario usuario) {
        return authService.registrarUsuario(usuario);
    }

    @GetMapping("/usuario/{usuario}")
    public Usuario obtenerUsuario(@PathVariable String usuario) {
        return authService.obtenerUsuario(usuario);
    }
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