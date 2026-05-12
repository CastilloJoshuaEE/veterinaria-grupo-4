package com.mycompany.veterinaria.grupo4.model.entity;

/**
 * Entidad que representa un instrumento o insumo medico.
 * <p>
 * Contiene la informacion de los instrumentos y materiales utilizados
 * durante las atenciones medicas, incluyendo su nombre, descripcion,
 * costo de uso y estado de disponibilidad en inventario.
 * </p>
 * 
 * <p><b>Fecha de inicio del proyecto:</b> 15/04/2026</p>
 * 
 * @author ROBLES MORALES JUAN ANDRES – MODULO: ATENCION VETERINARIA
 * @version 1.0
 * @since 1.0
 */
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