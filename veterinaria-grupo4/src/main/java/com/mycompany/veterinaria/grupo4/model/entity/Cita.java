package com.mycompany.veterinaria.grupo4.model.entity;

import java.util.Date;

/**
 * Entidad que representa una cita medica programada en el sistema veterinario.
 * <p>
 * Contiene la informacion de agendamiento de consultas, incluyendo el cliente,
 * la mascota, el servicio solicitado, el veterinario asignado, la fecha y hora,
 * el estado actual y observaciones adicionales. Esta entidad es fundamental
 * para la gestion de la agenda de la clinica.
 * </p>
 * 
 * <p><b>Fecha de inicio del proyecto:</b> 15/04/2026</p>
 * 
 * @author CHILAN CHILAN DANNY ANDRES – MODULO: AGENDAMIENTO DE CITA
 * @version 1.0
 * @since 1.0
 */
public class Cita {
    private int idCita;
    private Cliente cliente;
    private Servicio servicio;
    private Mascota mascota;
    private Veterinario veterinario;
    private Date fechaHora;
    private String estado;
    private String observaciones;
    private Date fechaRegistro;

    /**
     * Constructor por defecto.
     */
    public Cita() {}

    public int getIdCita() { return idCita; }
    public void setIdCita(int idCita) { this.idCita = idCita; }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Servicio getServicio() {
        return servicio;
    }

    public void setServicio(Servicio servicio) {
        this.servicio = servicio;
    }

    public Mascota getMascota() {
        return mascota;
    }

    public void setMascota(Mascota mascota) {
        this.mascota = mascota;
    }

    public Veterinario getVeterinario() {
        return veterinario;
    }

    public void setVeterinario(Veterinario veterinario) {
        this.veterinario = veterinario;
    }
    
    public Date getFechaHora() { return fechaHora; }
    public void setFechaHora(Date fechaHora) { this.fechaHora = fechaHora; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }
    public Date getFechaRegistro() { return fechaRegistro; }
    public void setFechaRegistro(Date fechaRegistro) { this.fechaRegistro = fechaRegistro; }
}