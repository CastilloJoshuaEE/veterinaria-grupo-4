/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.veterinaria.grupo4.util;

import java.sql.Types;

public class Parametro {
    private String nombre;
    private int tipoDato;
    private Object valor;

    public Parametro(String nombre, int tipoDato, Object valor) {
        this.nombre = nombre;
        this.tipoDato = tipoDato;
        this.valor = valor;
    }

    // Getters y Setters
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public int getTipoDato() { return tipoDato; }
    public void setTipoDato(int tipoDato) { this.tipoDato = tipoDato; }
    public Object getValor() { return valor; }
    public void setValor(Object valor) { this.valor = valor; }
}