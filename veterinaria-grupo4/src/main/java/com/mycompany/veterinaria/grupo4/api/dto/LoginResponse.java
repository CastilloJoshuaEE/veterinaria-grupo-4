package com.mycompany.veterinaria.grupo4.api.dto;

public class LoginResponse {
    private int idUsuario;
    private String nombreUsuario;
    private String rol;
    private boolean success;
    private String message;

    public LoginResponse() {}

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