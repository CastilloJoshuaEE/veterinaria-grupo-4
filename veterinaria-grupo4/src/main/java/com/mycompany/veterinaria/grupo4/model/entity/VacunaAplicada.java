package com.mycompany.veterinaria.grupo4.model.entity;

import java.util.Date;

public class VacunaAplicada {
    private int idVacuna;
    private String nombre;
    private String descripcion;
    private int periodoMeses;
    private Date fechaAplicacion;
    private Date fechaProxima;
    private int idMascota;

    public VacunaAplicada() {}

    public int getIdVacuna() { return idVacuna; }
    public void setIdVacuna(int idVacuna) { this.idVacuna = idVacuna; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public int getPeriodoMeses() { return periodoMeses; }
    public void setPeriodoMeses(int periodoMeses) { this.periodoMeses = periodoMeses; }
    public Date getFechaAplicacion() { return fechaAplicacion; }
    public void setFechaAplicacion(Date fechaAplicacion) { this.fechaAplicacion = fechaAplicacion; }
    public Date getFechaProxima() { return fechaProxima; }
    public void setFechaProxima(Date fechaProxima) { this.fechaProxima = fechaProxima; }
    public int getIdMascota() { return idMascota; }
    public void setIdMascota(int idMascota) { this.idMascota = idMascota; }
}