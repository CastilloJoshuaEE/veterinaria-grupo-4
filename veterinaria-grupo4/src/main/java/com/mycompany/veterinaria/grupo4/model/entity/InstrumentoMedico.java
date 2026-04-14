package com.mycompany.veterinaria.grupo4.model.entity;

public class InstrumentoMedico {
    private int idInstrumento;
    private String nombre;
    private String descripcion;
    private double costoUso;
    private boolean estado;

    public InstrumentoMedico() {}

    public int getIdInstrumento() { return idInstrumento; }
    public void setIdInstrumento(int idInstrumento) { this.idInstrumento = idInstrumento; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public double getCostoUso() { return costoUso; }
    public void setCostoUso(double costoUso) { this.costoUso = costoUso; }
    public boolean isEstado() { return estado; }
    public void setEstado(boolean estado) { this.estado = estado; }
}