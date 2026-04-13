package com.mycompany.veterinaria.grupo4.model.entity;

import java.util.Date;

public class AtencionMedica {
    private int idAtencionMedica;
    private int idCita;
    private int idMascota;
    private int idVeterinario;
    private Date fecha;
    private String diagnostico;
    private String tratamiento;
    private String observaciones;

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