package com.mycompany.veterinaria.grupo4.model.entity;

/**
 * Entidad que representa el detalle de una factura.
 * <p>
 * Contiene la informacion de cada linea de detalle de una factura,
 * incluyendo el servicio prestado, cantidad, precios, descuentos y totales.
 * Permite desglosar los costos asociados a una atencion medica o servicio.
 * </p>
 * 
 * <p><b>Fecha de inicio del proyecto:</b> 15/04/2026</p>
 * 
 * @author ROBLES MORALES JUAN ANDRES – MODULO: ATENCION VETERINARIA
 * @version 1.0
 * @since 1.0
 */
public class DetalleFactura {
    private int idDetalle;
    private int idFactura;
    private int idServicio;
    private int cantidad;
    private double precioUnitario;
    private double descuento;
    private double total;
    private Integer idAtencionMedica;
    private Integer idMascota;

    /**
     * Constructor por defecto.
     */
    public DetalleFactura() {}

    // Getters y Setters
    public int getIdDetalle() { return idDetalle; }
    public void setIdDetalle(int idDetalle) { this.idDetalle = idDetalle; }
    public int getIdFactura() { return idFactura; }
    public void setIdFactura(int idFactura) { this.idFactura = idFactura; }
    public int getIdServicio() { return idServicio; }
    public void setIdServicio(int idServicio) { this.idServicio = idServicio; }
    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }
    public double getPrecioUnitario() { return precioUnitario; }
    public void setPrecioUnitario(double precioUnitario) { this.precioUnitario = precioUnitario; }
    public double getDescuento() { return descuento; }
    public void setDescuento(double descuento) { this.descuento = descuento; }
    public double getTotal() { return total; }
    public void setTotal(double total) { this.total = total; }
    public Integer getIdAtencionMedica() { return idAtencionMedica; }
    public void setIdAtencionMedica(Integer idAtencionMedica) { this.idAtencionMedica = idAtencionMedica; }
    public Integer getIdMascota() { return idMascota; }
    public void setIdMascota(Integer idMascota) { this.idMascota = idMascota; }
}