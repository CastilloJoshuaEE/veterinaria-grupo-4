package com.mycompany.veterinaria.grupo4.model.entity;
/**
 * Entidad que representa un metodo de pago utilizado en facturacion.
 * <p>
 * Contiene la informacion del metodo de pago empleado para una transaccion,
 * incluyendo el tipo de metodo (efectivo, transferencia), el valor total,
 * y los datos de cuenta origen y destino para transferencias bancarias.
 * </p>
 * 
 * <p><b>Fecha de inicio del proyecto:</b> 15/04/2026</p>
 * 
 * @author ROBLES MORALES JUAN ANDRES – MODULO: ATENCION VETERINARIA
 * @version 1.0
 * @since 1.0
 */
public class MetodoPago {
    private int idMetodoPago;
    private String metodo;
    private double valorTotal;
    private String cuentaOrigen;
    private String cuentaDestino;

    public MetodoPago() {}

    public int getIdMetodoPago() { return idMetodoPago; }
    public void setIdMetodoPago(int idMetodoPago) { this.idMetodoPago = idMetodoPago; }
    public String getMetodo() { return metodo; }
    public void setMetodo(String metodo) { this.metodo = metodo; }
    public double getValorTotal() { return valorTotal; }
    public void setValorTotal(double valorTotal) { this.valorTotal = valorTotal; }
    public String getCuentaOrigen() { return cuentaOrigen; }
    public void setCuentaOrigen(String cuentaOrigen) { this.cuentaOrigen = cuentaOrigen; }
    public String getCuentaDestino() { return cuentaDestino; }
    public void setCuentaDestino(String cuentaDestino) { this.cuentaDestino = cuentaDestino; }
}