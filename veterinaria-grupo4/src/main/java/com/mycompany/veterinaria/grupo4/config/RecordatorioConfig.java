package com.mycompany.veterinaria.grupo4.config;

/**
 * Configuracion de los recordatorios del sistema.
 * <p>
 * Define los parametros para cada tipo de recordatorio, incluyendo
 * el tiempo de anticipacion, el mensaje a mostrar y si esta activo.
 * </p>
 * 
 * <p><b>Fecha de inicio del proyecto:</b> 15/04/2026</p>
 * 
 * @author CHILAN CHILAN DANNY ANDRES – MODULO: AGENDAMIENTO DE CITA
 * @version 1.0
 * @since 1.0
 */
public class RecordatorioConfig {
    private int idConfig;
    private String tipoRecordatorio;
    private String anticipacion;
    private String mensaje;
    private boolean activo;

    /**
     * Constructor por defecto.
     */
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