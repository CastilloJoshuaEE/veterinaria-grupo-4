package com.mycompany.veterinaria.grupo4.model.entity;

import java.util.Date;
/**
 * Entidad que representa una mascota registrada en el sistema.
 * <p>
 * Contiene toda la informacion del paciente animal, incluyendo datos basicos
 * como nombre, especie, raza, sexo, fecha de nacimiento, peso, color,
 * fotografia y fecha de registro. Esta asociada a un cliente propietario.
 * </p>
 * 
 * <p><b>Fecha de inicio del proyecto:</b> 15/04/2026</p>
 * 
 * @author CASTILLO MEREJILDO JOSHUA JAVIER – MODULO: MASCOTA
 * @version 1.0
 * @since 1.0
 */
public class Mascota {
    private int idMascota;
    private int idCliente;
    private String nombre;
    private String especie;
    private String raza;
    private char sexo;
    private Date fechaNacimiento;
    private Double peso;
    private String color;
    private byte[] foto;
    private Date fechaRegistro;

    // Constructor vacío
    public Mascota() {}

    // Getters y Setters
    public int getIdMascota() { return idMascota; }
    public void setIdMascota(int idMascota) { this.idMascota = idMascota; }
    public int getIdCliente() { return idCliente; }
    public void setIdCliente(int idCliente) { this.idCliente = idCliente; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getEspecie() { return especie; }
    public void setEspecie(String especie) { this.especie = especie; }
    public String getRaza() { return raza; }
    public void setRaza(String raza) { this.raza = raza; }
    public char getSexo() { return sexo; }
    public void setSexo(char sexo) { this.sexo = sexo; }
    public Date getFechaNacimiento() { return fechaNacimiento; }
    public void setFechaNacimiento(Date fechaNacimiento) { this.fechaNacimiento = fechaNacimiento; }
    public Double getPeso() { return peso; }
    public void setPeso(Double peso) { this.peso = peso; }
    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }
    public byte[] getFoto() { return foto; }
    public void setFoto(byte[] foto) { this.foto = foto; }
    public Date getFechaRegistro() { return fechaRegistro; }
    public void setFechaRegistro(Date fechaRegistro) { this.fechaRegistro = fechaRegistro; }
}