package com.mycompany.veterinaria.grupo4.model.entity;

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