package com.mycompany.veterinaria.grupo4.api.dto;

import com.mycompany.veterinaria.grupo4.model.entity.Recordatorio;

/**
 * Data Transfer Object para la solicitud de creacion de recordatorios.
 * <p>
 * Encapsula el recordatorio a crear junto con la anticipacion 
 * (tiempo de antelacion para la notificacion).
 * </p>
 * 
 * <p><b>Fecha de inicio del proyecto:</b> 15/04/2026</p>
 * 
 * @author CHILAN CHILAN DANNY ANDRES – MODULO: AGENDAMIENTO DE CITA
 * @version 1.0
 * @since 1.0
 */
public class RecordatorioRequest {
    private Recordatorio recordatorio;
    private String anticipacion;

    public Recordatorio getRecordatorio() { return recordatorio; }
    public void setRecordatorio(Recordatorio recordatorio) { this.recordatorio = recordatorio; }
    public String getAnticipacion() { return anticipacion; }
    public void setAnticipacion(String anticipacion) { this.anticipacion = anticipacion; }
}