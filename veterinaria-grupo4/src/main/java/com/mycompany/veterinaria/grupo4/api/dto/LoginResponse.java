package com.mycompany.veterinaria.grupo4.api.dto;

/**
 * Data Transfer Object para la respuesta del inicio de sesion.
 * <p>
 * Contiene la informacion del usuario autenticado y el resultado
 * de la operacion de login, incluyendo mensajes de exito o error.
 * </p>
 * 
 * <p><b>Fecha de inicio del proyecto:</b> 15/04/2026</p>
 * 
 * @author ROBLES MORALES JUAN ANDRES – MODULO: ATENCION VETERINARIA
 * @version 2.0
 * @since 1.0
 */
public class LoginResponse {
    private int idUsuario;
    private String email;             
    private String nombreCompleto;    
    private String rol;
    private boolean success;
    private String message;

    /**
     * Constructor por defecto.
     */
    public LoginResponse() {}

    /**
     * Constructor con todos los campos de la respuesta.
     * 
     * @param idUsuario identificador del usuario
     * @param email email del usuario
     * @param nombreCompleto nombre completo del usuario
     * @param rol rol del usuario en el sistema
     * @param success indica si el login fue exitoso
     * @param message mensaje adicional sobre el resultado
     */
    public LoginResponse(int idUsuario, String email, String nombreCompleto, String rol, boolean success, String message) {
        this.idUsuario = idUsuario;
        this.email = email;
        this.nombreCompleto = nombreCompleto;
        this.rol = rol;
        this.success = success;
        this.message = message;
    }

    // ========== GETTERS Y SETTERS ==========

    public int getIdUsuario() { return idUsuario; }
    public void setIdUsuario(int idUsuario) { this.idUsuario = idUsuario; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getNombreCompleto() { return nombreCompleto; }
    public void setNombreCompleto(String nombreCompleto) { this.nombreCompleto = nombreCompleto; }

    public String getRol() { return rol; }
    public void setRol(String rol) { this.rol = rol; }

    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}