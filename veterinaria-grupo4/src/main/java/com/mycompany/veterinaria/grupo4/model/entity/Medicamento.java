package com.mycompany.veterinaria.grupo4.model.entity;
/**
 * Entidad que representa un medicamento en el inventario de la farmacia.
 * <p>
 * Contiene la informacion de los medicamentos disponibles, incluyendo nombre,
 * descripcion, precio, stock, estado, y datos adicionales para prescripciones
 * como dosis, frecuencia y duracion del tratamiento.
 * </p>
 * 
 * <p><b>Fecha de inicio del proyecto:</b> 15/04/2026</p>
 * 
 * @author ROBLES MORALES JUAN ANDRES – MODULO: ATENCION VETERINARIA
 * @version 1.0
 * @since 1.0
 */
public class Medicamento {
    private int idMedicamento;
    private String nombre;
    private String descripcion;
    private double precio;
    private int stock;
    private boolean estado;
    private String dosis;
    private String frecuencia;
    private String duracion;
    public Medicamento() {}

    public int getIdMedicamento() { return idMedicamento; }
    public void setIdMedicamento(int idMedicamento) { this.idMedicamento = idMedicamento; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public double getPrecio() { return precio; }
    public void setPrecio(double precio) { this.precio = precio; }
    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }
    public boolean isEstado() { return estado; }
    public void setEstado(boolean estado) { this.estado = estado; }
   public String getDosis() { return dosis; }
    public void setDosis(String dosis) { this.dosis = dosis; }
    public String getFrecuencia() { return frecuencia; }
    public void setFrecuencia(String frecuencia) { this.frecuencia = frecuencia; }
    public String getDuracion() { return duracion; }
    public void setDuracion(String duracion) { this.duracion = duracion; }

}