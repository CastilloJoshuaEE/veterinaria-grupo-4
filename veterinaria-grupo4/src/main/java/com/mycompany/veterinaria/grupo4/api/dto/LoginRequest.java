package com.mycompany.veterinaria.grupo4.api.dto;

/**
 * Data Transfer Object para la solicitud de inicio de sesion.
 * <p>
 * Encapsula las credenciales de autenticacion enviadas por el cliente
 * para validar el acceso al sistema.
 * </p>
 * 
 * <p><b>Fecha de inicio del proyecto:</b> 15/04/2026</p>
 * 
 * @author ROBLES MORALES JUAN ANDRES – MODULO: ATENCION VETERINARIA
 * @version 1.0
 * @since 1.0
 */
public class LoginRequest {
    private String usuario;
    private String password;

    public String getUsuario() { return usuario; }
    public void setUsuario(String usuario) { this.usuario = usuario; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}