/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.veterinaria.grupo4.config;

public class RecordatorioConfig {
    private int idConfig;
    private String tipoRecordatorio;
    private String anticipacion;
    private String mensaje;
    private boolean activo;

    public RecordatorioConfig() {}

    public int getIdConfig() { return idConfig; }
    public void setIdConfig(int idConfig) { this.idConfig = idConfig; }
    public String getTipoRecordatorio() { return tipoRecordatorio; }
    public void setTipoRecordatorio(String tipoRecordatorio) { this.tipoRecordatorio = tipoRecordatorio; }
    public String getAnticipacion() { return anticipacion; }
    public void setAnticipacion(String anticipacion) { this.anticipacion = anticipacion; }
    public String getMensaje() { return mensaje; }
    public void setMensaje(String mensaje) { this.mensaje = mensaje; }
    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }
}