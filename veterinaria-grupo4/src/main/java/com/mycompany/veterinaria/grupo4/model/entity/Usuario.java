/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.veterinaria.grupo4.model.entity;

/**
 *
 * @author Usuario
 */

import java.util.Date;
/**
 * Entidad que representa un usuario del sistema veterinario.
 * <p>
 * Gestiona las credenciales y permisos de acceso de los diferentes
 * actores del sistema (administradores, veterinarios, recepcionistas).
 * Incluye informacion de autenticacion como nombre de usuario, contrasena,
 * correo electronico, rol, fecha de creacion y estado de actividad.
 * </p>
 * 
 * <p><b>Fecha de inicio del proyecto:</b> 15/04/2026</p>
 * 
 * @author ROBLES MORALES JUAN ANDRES – MODULO: ATENCION VETERINARIA
 * @version 1.0
 * @since 1.0
 */
public class Usuario {
    private int idUsuario;
    private String nombreUsuario;
    private String contrasena;
    private String correoElectronico;
    private String rol;
    private Date fechaCreacion;
    private boolean estado;
    /**
     * Constructor por defecto.
     */
    public Usuario() {}
    /**
     * Constructor con los campos basicos del usuario.
     * 
     * @param idUsuario identificador del usuario
     * @param nombreUsuario nombre de usuario
     * @param contrasena contrasena del usuario
     * @param correoElectronico correo electronico
     * @param rol rol del usuario (ADMINISTRADOR, VETERINARIO, etc.)
     */
    public Usuario(int idUsuario, String nombreUsuario, String contrasena, String correoElectronico, String rol) {
        this.idUsuario = idUsuario;
        this.nombreUsuario = nombreUsuario;
        this.contrasena = contrasena;
        this.correoElectronico = correoElectronico;
        this.rol = rol;
    }

    // Getters y Setters
    public int getIdUsuario() { return idUsuario; }
    public void setIdUsuario(int idUsuario) { this.idUsuario = idUsuario; }
    public String getNombreUsuario() { return nombreUsuario; }
    public void setNombreUsuario(String nombreUsuario) { this.nombreUsuario = nombreUsuario; }
    public String getContrasena() { return contrasena; }
    public void setContrasena(String contrasena) { this.contrasena = contrasena; }
    public String getCorreoElectronico() { return correoElectronico; }
    public void setCorreoElectronico(String correoElectronico) { this.correoElectronico = correoElectronico; }
    public String getRol() { return rol; }
    public void setRol(String rol) { this.rol = rol; }
    public Date getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(Date fechaCreacion) { this.fechaCreacion = fechaCreacion; }
    public boolean isEstado() { return estado; }
    public void setEstado(boolean estado) { this.estado = estado; }
}