package com.mycompany.veterinaria.grupo4.model.entity;
/**
 * Entidad que representa un servicio veterinario ofrecido por la clinica.
 * <p>
 * Define los diferentes tipos de atencion que la veterinaria proporciona,
 * como consultas, cirugias, vacunaciones, etc. Incluye informacion del
 * nombre del servicio, descripcion, precio, duracion estimada y estado
 * de disponibilidad.
 * </p>
 * 
 * <p><b>Fecha de inicio del proyecto:</b> 15/04/2026</p>
 * 
 * @author BESILLA TOMALA ANGEL KALED – MODULO: VETERINARIO
 * @version 1.0
 * @since 1.0
 */
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