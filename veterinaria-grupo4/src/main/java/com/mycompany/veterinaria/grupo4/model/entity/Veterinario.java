package com.mycompany.veterinaria.grupo4.model.entity;

import java.util.Date;

public class Veterinario {
    private int idVeterinario;
    private String cedula;
    private String nombre;
    private String apellido;
    private String telefono;
    private double pagoMensual;
    private String direccion;
    private String correoElectronico;
    private int idEspecialidad;
    private Date fechaRegistro;

    public Veterinario() {}

    // Getters y Setters
    public int getIdVeterinario() { return idVeterinario; }
    public void setIdVeterinario(int idVeterinario) { this.idVeterinario = idVeterinario; }
    public String getCedula() { return cedula; }
    public void setCedula(String cedula) { this.cedula = cedula; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }
    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    public double getPagoMensual() { return pagoMensual; }
    public void setPagoMensual(double pagoMensual) { this.pagoMensual = pagoMensual; }
    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }
    public String getCorreoElectronico() { return correoElectronico; }
    public void setCorreoElectronico(String correoElectronico) { this.correoElectronico = correoElectronico; }
    public int getIdEspecialidad() { return idEspecialidad; }
    public void setIdEspecialidad(int idEspecialidad) { this.idEspecialidad = idEspecialidad; }
    public Date getFechaRegistro() { return fechaRegistro; }
    public void setFechaRegistro(Date fechaRegistro) { this.fechaRegistro = fechaRegistro; }
    
    public String getNombreCompleto() {
        return nombre + " " + apellido;
    }
}