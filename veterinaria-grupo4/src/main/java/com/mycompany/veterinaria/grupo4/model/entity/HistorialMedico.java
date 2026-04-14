package com.mycompany.veterinaria.grupo4.model.entity;

import java.util.Date;

public class HistorialMedico {
    private int idHistorial;
    private int idMascota;
    private Integer idCita;
    private Date fecha;
    private Integer idAtencionMedica;
    
    // Campos adicionales para consultas
    private String nombreServicio;
    private String nombreVeterinario;
    private String diagnostico;
    private String tratamiento;

    public HistorialMedico() {}

    // Getters y Setters
    public int getIdHistorial() { return idHistorial; }
    public void setIdHistorial(int idHistorial) { this.idHistorial = idHistorial; }
    public int getIdMascota() { return idMascota; }
    public void setIdMascota(int idMascota) { this.idMascota = idMascota; }
    public Integer getIdCita() { return idCita; }
    public void setIdCita(Integer idCita) { this.idCita = idCita; }
    public Date getFecha() { return fecha; }
    public void setFecha(Date fecha) { this.fecha = fecha; }
    public Integer getIdAtencionMedica() { return idAtencionMedica; }
    public void setIdAtencionMedica(Integer idAtencionMedica) { this.idAtencionMedica = idAtencionMedica; }
    public String getNombreServicio() { return nombreServicio; }
    public void setNombreServicio(String nombreServicio) { this.nombreServicio = nombreServicio; }
    public String getNombreVeterinario() { return nombreVeterinario; }
    public void setNombreVeterinario(String nombreVeterinario) { this.nombreVeterinario = nombreVeterinario; }
    public String getDiagnostico() { return diagnostico; }
    public void setDiagnostico(String diagnostico) { this.diagnostico = diagnostico; }
    public String getTratamiento() { return tratamiento; }
    public void setTratamiento(String tratamiento) { this.tratamiento = tratamiento; }
}