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
 * @version 1.0
 * @since 1.0
 */
public class LoginResponse {
    private int idUsuario;
    private String nombreUsuario;
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
     * @param nombreUsuario nombre del usuario
     * @param rol rol del usuario en el sistema
     * @param success indica si el login fue exitoso
     * @param message mensaje adicional sobre el resultado
     */
    public LoginResponse(int idUsuario, String nombreUsuario, String rol, boolean success, String message) {
        this.idUsuario = idUsuario;
        this.nombreUsuario = nombreUsuario;
        this.rol = rol;
        this.success = success;
        this.message = message;
    }

    public int getIdUsuario() { return idUsuario; }
    public void setIdUsuario(int idUsuario) { this.idUsuario = idUsuario; }
    public String getNombreUsuario() { return nombreUsuario; }
    public void setNombreUsuario(String nombreUsuario) { this.nombreUsuario = nombreUsuario; }
    public String getRol() { return rol; }
    public void setRol(String rol) { this.rol = rol; }
    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}