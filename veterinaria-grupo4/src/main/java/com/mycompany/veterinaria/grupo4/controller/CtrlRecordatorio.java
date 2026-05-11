package com.mycompany.veterinaria.grupo4.controller;

import com.mycompany.veterinaria.grupo4.api.dto.RecordatorioRequest;
import com.mycompany.veterinaria.grupo4.config.RecordatorioConfig;
import com.mycompany.veterinaria.grupo4.model.entity.Recordatorio;
import org.springframework.web.client.RestTemplate;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class CtrlRecordatorio {

    private final RestTemplate restTemplate;
    private final String BASE_URL = "http://localhost:8080/api/recordatorio";

    public CtrlRecordatorio() {
        this.restTemplate = new RestTemplate();
    }

public List<Recordatorio> obtenerRecordatorios(Date inicio, Date fin) {
        try {
            String url = BASE_URL + "/todos?inicio={inicio}&fin={fin}";
            String inicioStr = new java.text.SimpleDateFormat("yyyy-MM-dd").format(inicio);
            String finStr = new java.text.SimpleDateFormat("yyyy-MM-dd").format(fin);
            
            System.out.println("Consultando recordatorios: " + inicioStr + " a " + finStr);
            
            Recordatorio[] recordatorios = restTemplate.getForObject(url, Recordatorio[].class, inicioStr, finStr);
            
            if (recordatorios == null) {
                System.out.println("No se recibieron recordatorios del servidor");
                return Collections.emptyList();
            }
            
            return Arrays.asList(recordatorios);
        } catch (Exception e) {
            System.err.println("Error al obtener recordatorios: " + e.getMessage());
            e.printStackTrace();
            return Collections.emptyList(); // Retornar lista vacía en lugar de null
        }
    }

    public void generarRecordatorios(int idUsuario) {
        String url = BASE_URL + "/generar/{idUsuario}";
        restTemplate.postForLocation(url, null, idUsuario);
    }

    public int registrarRecordatorio(Recordatorio recordatorio, String anticipacion) {
        String url = BASE_URL + "/crear";
        RecordatorioRequest request = new RecordatorioRequest();
        request.setRecordatorio(recordatorio);
        request.setAnticipacion(anticipacion);
        return restTemplate.postForObject(url, request, Integer.class);
    }

    public boolean actualizarRecordatorio(Recordatorio recordatorio) {
        String url = BASE_URL + "/actualizar";
        restTemplate.put(url, recordatorio);
        return true;
    }

    public boolean eliminarRecordatorio(int idRecordatorio) {
        String url = BASE_URL + "/eliminar/{idRecordatorio}";
        restTemplate.delete(url, idRecordatorio);
        return true;
    }

    public List<RecordatorioConfig> obtenerConfiguraciones() {
        String url = BASE_URL + "/config/todas";
        RecordatorioConfig[] configs = restTemplate.getForObject(url, RecordatorioConfig[].class);
        return Arrays.asList(configs);
    }

    public boolean actualizarConfiguracion(RecordatorioConfig config) {
        String url = BASE_URL + "/config/actualizar";
        restTemplate.put(url, config);
        return true;
    }
    public int crearConfiguracion(RecordatorioConfig config) {
        String url = BASE_URL + "/config/crear";
        return restTemplate.postForObject(url, config, Integer.class);
    }
}