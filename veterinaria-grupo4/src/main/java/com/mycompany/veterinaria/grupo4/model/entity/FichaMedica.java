package com.mycompany.veterinaria.grupo4.model.entity;

import java.util.Date;

/**
 * Entidad que representa la ficha medica de una mascota.
 * <p>
 * Contiene el historial clinico relevante de cada mascota, incluyendo
 * alergias, enfermedades cronicas y observaciones generales. Esta ficha
 * complementa el historial de atenciones medicas con informacion
 * permanente del paciente.
 * </p>
 * 
 * <p><b>Fecha de inicio del proyecto:</b> 15/04/2026</p>
 * 
 * @author CASTILLO MEREJILDO JOSHUA JAVIER – MODULO: MASCOTA
 * @version 1.0
 * @since 1.0
 */
public class FichaMedica {
    private int idFicha;
    private int idMascota;
    private String alergias;
    private String enfermedadesCronicas;
    private String observaciones;
    private Date ultimaActualizacion;

    // Getters y Setters
    public int getIdFicha() { return idFicha; }
    public void setIdFicha(int idFicha) { this.idFicha = idFicha; }
    public int getIdMascota() { return idMascota; }
    public void setIdMascota(int idMascota) { this.idMascota = idMascota; }
    public String getAlergias() { return alergias; }
    public void setAlergias(String alergias) { this.alergias = alergias; }
    public String getEnfermedadesCronicas() { return enfermedadesCronicas; }
    public void setEnfermedadesCronicas(String enfermedadesCronicas) { this.enfermedadesCronicas = enfermedadesCronicas; }
    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }
    public Date getUltimaActualizacion() { return ultimaActualizacion; }
    public void setUltimaActualizacion(Date ultimaActualizacion) { this.ultimaActualizacion = ultimaActualizacion; }
}