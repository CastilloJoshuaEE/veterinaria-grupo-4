package com.mycompany.veterinaria.grupo4.model.entity;

public class Servicio {
    private int idServicio;
    private String nombreServicio;
    private String descripcion;
    private double precio;
    private int duracionEstimada;
    private boolean estado;

    public Servicio() {}

    public int getIdServicio() { return idServicio; }
    public void setIdServicio(int idServicio) { this.idServicio = idServicio; }
    public String getNombreServicio() { return nombreServicio; }
    public void setNombreServicio(String nombreServicio) { this.nombreServicio = nombreServicio; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public double getPrecio() { return precio; }
    public void setPrecio(double precio) { this.precio = precio; }
    public int getDuracionEstimada() { return duracionEstimada; }
    public void setDuracionEstimada(int duracionEstimada) { this.duracionEstimada = duracionEstimada; }
    public boolean isEstado() { return estado; }
    public void setEstado(boolean estado) { this.estado = estado; }
}