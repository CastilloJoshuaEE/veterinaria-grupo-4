package com.mycompany.veterinaria.grupo4.api.controller;

import com.mycompany.veterinaria.grupo4.api.dto.LoginRequest;
import com.mycompany.veterinaria.grupo4.model.entity.Usuario;
import com.mycompany.veterinaria.grupo4.service.AuthService;
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
}