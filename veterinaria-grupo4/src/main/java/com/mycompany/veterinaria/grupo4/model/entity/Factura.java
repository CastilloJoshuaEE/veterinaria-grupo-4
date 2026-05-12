package com.mycompany.veterinaria.grupo4.model.entity;

import java.util.Date;
import java.util.List;

/**
 * Entidad que representa una factura emitida en el sistema.
 * <p>
 * Contiene la informacion financiera de una transaccion, incluyendo
 * subtotal, IVA, total, metodo de pago y listas detalladas de servicios,
 * mascotas, medicamentos, instrumentos y vacunas facturados.
 * </p>
 * 
 * <p><b>Fecha de inicio del proyecto:</b> 15/04/2026</p>
 * 
 * @author ROBLES MORALES JUAN ANDRES – MODULO: ATENCION VETERINARIA
 * @version 1.0
 * @since 1.0
 */
public class Factura {
    private int idFactura;
    private int idCliente;
    private Date fecha;
    private double subtotal;
    private double iva;
    private double total;
    private String estado;
    private int idMetodoPago;
    private String metodoPago;
    private Cliente cliente;
    private List<Object[]> servicios;
    private List<Object[]> mascotas;
    private List<Object[]> medicamentos;
    private List<Object[]> instrumentos;
    private List<Object[]> vacunas;
    
    /**
     * Constructor por defecto.
     */
    public Factura() {}

    public int getIdFactura() { return idFactura; }
    public void setIdFactura(int idFactura) { this.idFactura = idFactura; }
    public int getIdCliente() { return idCliente; }
    public void setIdCliente(int idCliente) { this.idCliente = idCliente; }
    public Date getFecha() { return fecha; }
    public void setFecha(Date fecha) { this.fecha = fecha; }
    public double getSubtotal() { return subtotal; }
    public void setSubtotal(double subtotal) { this.subtotal = subtotal; }
    public double getIva() { return iva; }
    public void setIva(double iva) { this.iva = iva; }
    public double getTotal() { return total; }
    public void setTotal(double total) { this.total = total; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public int getIdMetodoPago() { return idMetodoPago; }
    public void setIdMetodoPago(int idMetodoPago) { this.idMetodoPago = idMetodoPago; }

    public String getMetodoPago() { return metodoPago; }
    public void setMetodoPago(String metodoPago) { this.metodoPago = metodoPago; }
    public Cliente getCliente() { return cliente; }
    public void setCliente(Cliente cliente) { this.cliente = cliente; }
    public List<Object[]> getServicios() { return servicios; }
    public void setServicios(List<Object[]> servicios) { this.servicios = servicios; }
    public List<Object[]> getMascotas() { return mascotas; }
    public void setMascotas(List<Object[]> mascotas) { this.mascotas = mascotas; }
    public List<Object[]> getMedicamentos() { return medicamentos; }
    public void setMedicamentos(List<Object[]> medicamentos) { this.medicamentos = medicamentos; }
    public List<Object[]> getInstrumentos() { return instrumentos; }
    public void setInstrumentos(List<Object[]> instrumentos) { this.instrumentos = instrumentos; }
    public List<Object[]> getVacunas() { return vacunas; }
    public void setVacunas(List<Object[]> vacunas) { this.vacunas = vacunas; }

}