package com.mycompany.veterinaria.grupo4.model.entity;

import java.util.Date;

/**
 * Entidad que representa una atencion medica realizada a una mascota.
 * <p>
 * Contiene toda la informacion clinica generada durante una consulta veterinaria,
 * incluyendo diagnostico, tratamiento y observaciones. Cada atencion medica
 * esta asociada a una cita previamente agendada.
 * </p>
 * 
 * <p><b>Fecha de inicio del proyecto:</b> 15/04/2026</p>
 * 
 * @author ROBLES MORALES JUAN ANDRES – MODULO: ATENCION VETERINARIA
 * @version 1.0
 * @since 1.0
 */
public class AtencionMedica {
    private int idAtencionMedica;
    private int idCita;
    private int idMascota;
    private int idVeterinario;
    private Date fecha;
    private String diagnostico;
    private String tratamiento;
    private String observaciones;

    /**
     * Constructor por defecto.
     */
    public AtencionMedica() {}

    public int getIdAtencionMedica() { return idAtencionMedica; }
    public void setIdAtencionMedica(int idAtencionMedica) { this.idAtencionMedica = idAtencionMedica; }
    public int getIdCita() { return idCita; }
    public void setIdCita(int idCita) { this.idCita = idCita; }
    public int getIdMascota() { return idMascota; }
    public void setIdMascota(int idMascota) { this.idMascota = idMascota; }
    public int getIdVeterinario() { return idVeterinario; }
    public void setIdVeterinario(int idVeterinario) { this.idVeterinario = idVeterinario; }
    public Date getFecha() { return fecha; }
    public void setFecha(Date fecha) { this.fecha = fecha; }
    public String getDiagnostico() { return diagnostico; }
    public void setDiagnostico(String diagnostico) { this.diagnostico = diagnostico; }
    public String getTratamiento() { return tratamiento; }
    public void setTratamiento(String tratamiento) { this.tratamiento = tratamiento; }
    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }
}