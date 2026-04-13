package com.mycompany.veterinaria.grupo4.model.entity;

import java.util.Date;

public class Recordatorio {
    private int idRecordatorio;
    private Integer idUsuario;
    private Integer idCita;
    private Integer idVacuna;
    private String tipo;
    private String mensaje;
    private Date fechaEnvio;
    private int contadorMostrado;
    private boolean leido;
private String correoUsuario;

    public Recordatorio() {}

    public int getIdRecordatorio() { return idRecordatorio; }
    public void setIdRecordatorio(int idRecordatorio) { this.idRecordatorio = idRecordatorio; }
    public Integer getIdUsuario() { return idUsuario; }
    public void setIdUsuario(Integer idUsuario) { this.idUsuario = idUsuario; }
    public Integer getIdCita() { return idCita; }
    public void setIdCita(Integer idCita) { this.idCita = idCita; }
    public Integer getIdVacuna() { return idVacuna; }
    public void setIdVacuna(Integer idVacuna) { this.idVacuna = idVacuna; }
    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    public String getMensaje() { return mensaje; }
    public void setMensaje(String mensaje) { this.mensaje = mensaje; }
    public Date getFechaEnvio() { return fechaEnvio; }
    public void setFechaEnvio(Date fechaEnvio) { this.fechaEnvio = fechaEnvio; }
    public int getContadorMostrado() { return contadorMostrado; }
    public void setContadorMostrado(int contadorMostrado) { this.contadorMostrado = contadorMostrado; }
    public boolean isLeido() { return leido; }
    public void setLeido(boolean leido) { this.leido = leido; }
public String getCorreoUsuario() { return correoUsuario; }
public void setCorreoUsuario(String correoUsuario) { this.correoUsuario = correoUsuario; }
}