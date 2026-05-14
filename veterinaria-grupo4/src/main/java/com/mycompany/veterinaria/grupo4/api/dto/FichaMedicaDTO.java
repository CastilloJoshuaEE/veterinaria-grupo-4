package com.mycompany.veterinaria.grupo4.api.dto;

/**
 * Data Transfer Object para la ficha medica de una mascota.
 * <p>
 * Este DTO encapsula la informacion clinica relevante de una mascota,
 * incluyendo alergias, enfermedades cronicas y observaciones generales.
 * </p>
 * 
 * <p><b>Fecha de inicio del proyecto:</b> 15/04/2026</p>
 * 
 * @author CASTILLO MEREJILDO JOSHUA JAVIER – MODULO: MASCOTA
 * @version 1.0
 * @since 1.0
 */
public class FichaMedicaDTO {
    private int idMascota;
    private String alergias;
    private String enfermedadesCronicas;
    private String observaciones;

    public int getIdMascota() { return idMascota; }
    public void setIdMascota(int idMascota) { this.idMascota = idMascota; }
    public String getAlergias() { return alergias; }
    public void setAlergias(String alergias) { this.alergias = alergias; }
    public String getEnfermedadesCronicas() { return enfermedadesCronicas; }
    public void setEnfermedadesCronicas(String enfermedadesCronicas) { this.enfermedadesCronicas = enfermedadesCronicas; }
    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }
}