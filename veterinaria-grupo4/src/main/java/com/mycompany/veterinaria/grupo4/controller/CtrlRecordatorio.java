package com.mycompany.veterinaria.grupo4.controller;

import com.mycompany.veterinaria.grupo4.api.dto.RecordatorioRequest;
import com.mycompany.veterinaria.grupo4.config.RecordatorioConfig;
import com.mycompany.veterinaria.grupo4.model.entity.Recordatorio;
import org.springframework.web.client.RestTemplate;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Controlador para la gestion de recordatorios del sistema.
 * <p>
 * Proporciona metodos para obtener, generar, registrar, actualizar y eliminar
 * recordatorios, asi como gestionar las configuraciones de los mismos.
 * Se comunica con la API REST de recordatorios.
 * </p>
 * 
 * <p><b>Fecha de inicio del proyecto:</b> 15/04/2026</p>
 * 
 * @author CHILAN CHILAN DANNY ANDRES – MODULO: AGENDAMIENTO DE CITA
 * @version 1.0
 * @since 1.0
 */
public class CtrlRecordatorio {

    private final RestTemplate restTemplate;
    private final String BASE_URL = "http://localhost:8080/api/recordatorio";

    /**
     * Constructor del controlador de recordatorios.
     * Inicializa el RestTemplate para las llamadas a la API.
     */
    public CtrlRecordatorio() {
        this.restTemplate = new RestTemplate();
    }

    /**
     * Obtiene la lista de recordatorios en un rango de fechas especificado.
     *
     * @param inicio fecha de inicio del rango
     * @param fin fecha de fin del rango
     * @return lista de recordatorios en el rango, o lista vacia si no hay resultados
     */
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
            return Collections.emptyList();
        }
    }

    /**
     * Genera recordatorios automaticos para un usuario especifico.
     *
     * @param idUsuario identificador del usuario
     */
    public void generarRecordatorios(int idUsuario) {
        String url = BASE_URL + "/generar/{idUsuario}";
        restTemplate.postForLocation(url, null, idUsuario);
    }

    /**
     * Registra un nuevo recordatorio en el sistema.
     *
     * @param recordatorio objeto Recordatorio a registrar
     * @param anticipacion tiempo de anticipacion para la notificacion
     * @return ID del recordatorio creado
     */
    public int registrarRecordatorio(Recordatorio recordatorio, String anticipacion) {
        String url = BASE_URL + "/crear";
        RecordatorioRequest request = new RecordatorioRequest();
        request.setRecordatorio(recordatorio);
        request.setAnticipacion(anticipacion);
        return restTemplate.postForObject(url, request, Integer.class);
    }

    /**
     * Actualiza un recordatorio existente.
     *
     * @param recordatorio objeto Recordatorio con los datos actualizados
     * @return true si la actualizacion fue exitosa
     */
    public boolean actualizarRecordatorio(Recordatorio recordatorio) {
        String url = BASE_URL + "/actualizar";
        restTemplate.put(url, recordatorio);
        return true;
    }

    /**
     * Elimina un recordatorio del sistema.
     *
     * @param idRecordatorio identificador del recordatorio a eliminar
     * @return true si la eliminacion fue exitosa
     */
    public boolean eliminarRecordatorio(int idRecordatorio) {
        String url = BASE_URL + "/eliminar/{idRecordatorio}";
        restTemplate.delete(url, idRecordatorio);
        return true;
    }

    /**
     * Obtiene todas las configuraciones de recordatorios disponibles.
     *
     * @return lista de configuraciones de recordatorios
     */
    public List<RecordatorioConfig> obtenerConfiguraciones() {
        String url = BASE_URL + "/config/todas";
        RecordatorioConfig[] configs = restTemplate.getForObject(url, RecordatorioConfig[].class);
        return Arrays.asList(configs);
    }

    /**
     * Actualiza una configuracion de recordatorio existente.
     *
     * @param config objeto RecordatorioConfig con los datos actualizados
     * @return true si la actualizacion fue exitosa
     */
    public boolean actualizarConfiguracion(RecordatorioConfig config) {
        String url = BASE_URL + "/config/actualizar";
        restTemplate.put(url, config);
        return true;
    }

    /**
     * Crea una nueva configuracion de recordatorio.
     *
     * @param config objeto RecordatorioConfig con los datos de la nueva configuracion
     * @return ID de la configuracion creada
     */
    public int crearConfiguracion(RecordatorioConfig config) {
        String url = BASE_URL + "/config/crear";
        return restTemplate.postForObject(url, config, Integer.class);
    }
}