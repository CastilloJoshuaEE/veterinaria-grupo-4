package com.mycompany.veterinaria.grupo4.util;

import java.sql.Types;

/**
 * Clase que representa un parametro para procedimientos almacenados.
 * <p>
 * Encapsula la informacion de un parametro de base de datos: nombre,
 * tipo de dato SQL y valor. Utilizada por DatabaseUtil para construir
 * llamadas a procedimientos almacenados de manera dinamica y segura.
 * </p>
 * 
 * <p><b>Fecha de inicio del proyecto:</b> 15/04/2026</p>
 * 
 * @author ROBLES MORALES JUAN ANDRES – MODULO: ATENCION VETERINARIA
 * @version 1.0
 * @since 1.0
 */
public class Parametro {
    private String nombre;
    private int tipoDato;
    private Object valor;

    /**
     * Constructor de un parametro para SP.
     *
     * @param nombre nombre del parametro
     * @param tipoDato tipo SQL (ej: Types.INTEGER, Types.VARCHAR)
     * @param valor valor del parametro
     */
    public Parametro(String nombre, int tipoDato, Object valor) {
        this.nombre = nombre;
        this.tipoDato = tipoDato;
        this.valor = valor;
    }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public int getTipoDato() { return tipoDato; }
    public void setTipoDato(int tipoDato) { this.tipoDato = tipoDato; }
    public Object getValor() { return valor; }
    public void setValor(Object valor) { this.valor = valor; }
}