package com.mycompany.veterinaria.grupo4.model.entity;

/**
 * DTO para el registro de recepcionistas.
 * 
 * @author juan
 */
public class Recepcionista {
    private String cedula;
    private String nombre;
    private String apellido;
    private String telefono;
    private String direccion;
    private String email;
    private String contrasena;

    public Recepcionista() {}

    public Recepcionista(String cedula, String nombre, String apellido, 
                         String telefono, String direccion, String email, 
                         String contrasena) {
        this.cedula = cedula;
        this.nombre = nombre;
        this.apellido = apellido;
        this.telefono = telefono;
        this.direccion = direccion;
        this.email = email;
        this.contrasena = contrasena;
    }

    // Getters y Setters
    public String getCedula() { return cedula; }
    public void setCedula(String cedula) { this.cedula = cedula; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }
    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getContrasena() { return contrasena; }
    public void setContrasena(String contrasena) { this.contrasena = contrasena; }
}