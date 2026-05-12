package com.mycompany.veterinaria.grupo4.model.entity;

import java.util.Date;

/**
 * Entidad que representa a un cliente del sistema veterinario.
 * <p>
 * Contiene la informacion personal y de contacto de los duenos de mascotas,
 * incluyendo cedula, nombre, apellido, telefono, direccion y correo electronico.
 * Esta entidad sirve como base para la gestion de propietarios de mascotas.
 * </p>
 * 
 * <p><b>Fecha de inicio del proyecto:</b> 15/04/2026</p>
 * 
 * @author CASTRO AVILA JONATHAN XAVIER – MODULO: CLIENTE
 * @version 1.0
 * @since 1.0
 */
public class Cliente {
    private int idCliente;
    private String cedula;
    private String nombre;
    private String apellido;
    private String telefono;
    private String direccion;
    private String correoElectronico;
    private Date fechaRegistro;

    /**
     * Constructor por defecto.
     */
    public Cliente() {}

    /**
     * Constructor con los campos basicos del cliente.
     * 
     * @param idCliente identificador del cliente
     * @param cedula numero de cedula
     * @param nombre nombre del cliente
     * @param apellido apellido del cliente
     * @param telefono numero de telefono
     */
    public Cliente(int idCliente, String cedula, String nombre, String apellido, String telefono) {
        this.idCliente = idCliente;
        this.cedula = cedula;
        this.nombre = nombre;
        this.apellido = apellido;
        this.telefono = telefono;
    }
    // Getters y Setters
    public int getIdCliente() { return idCliente; }
    public void setIdCliente(int idCliente) { this.idCliente = idCliente; }
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
    public String getCorreoElectronico() { return correoElectronico; }
    public void setCorreoElectronico(String correoElectronico) { this.correoElectronico = correoElectronico; }
    public Date getFechaRegistro() { return fechaRegistro; }
    public void setFechaRegistro(Date fechaRegistro) { this.fechaRegistro = fechaRegistro; }
}