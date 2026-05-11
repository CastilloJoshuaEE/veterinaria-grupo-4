/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.veterinaria.grupo4.api.dto;

import com.mycompany.veterinaria.grupo4.model.entity.Recordatorio;

/**
 *
 * @author Usuario
 */
    public class RecordatorioRequest {
        private Recordatorio recordatorio;
        private String anticipacion;

        public Recordatorio getRecordatorio() { return recordatorio; }
        public void setRecordatorio(Recordatorio recordatorio) { this.recordatorio = recordatorio; }
        public String getAnticipacion() { return anticipacion; }
        public void setAnticipacion(String anticipacion) { this.anticipacion = anticipacion; }
    }
