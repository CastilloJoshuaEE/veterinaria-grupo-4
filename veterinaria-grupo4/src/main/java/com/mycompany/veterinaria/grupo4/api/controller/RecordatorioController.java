package com.mycompany.veterinaria.grupo4.api.controller;

import com.mycompany.veterinaria.grupo4.api.dto.RecordatorioRequest;
import com.mycompany.veterinaria.grupo4.config.RecordatorioConfig;
import com.mycompany.veterinaria.grupo4.model.entity.Recordatorio;
import com.mycompany.veterinaria.grupo4.service.RecordatorioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import java.util.Date;
import java.util.List;

/**
 * Controlador REST para la gestión de recordatorios del sistema.
 * <p>
 * Proporciona endpoints para obtener recordatorios pendientes, marcar como leídos,
 * generar recordatorios automáticos, y gestionar configuraciones de recordatorios.
 * </p>
 * 
 * <p><b>Fecha de inicio del proyecto:</b> 15/04/2026</p>
 * 
 * @author CHILAN CHILAN DANNY ANDRES – MÓDULO: AGENDAMIENTO DE CITA
 * @version 1.0
 * @since 1.0
 */
@RestController
@RequestMapping("/api/recordatorio")
@CrossOrigin(origins = "*")
public class RecordatorioController {

    @Autowired
    private RecordatorioService recordatorioService;

    /**
     * Obtiene los recordatorios pendientes para un usuario específico.
     * 
     * @param idUsuario identificador del usuario
     * @return Lista de recordatorios pendientes
     */
    @GetMapping("/pendientes/{idUsuario}")
    public List<Recordatorio> obtenerPendientes(@PathVariable int idUsuario) {
        return recordatorioService.obtenerPendientes(idUsuario);
    }

    /**
     * Marca un recordatorio como leído.
     * 
     * @param idRecordatorio identificador del recordatorio
     * @return true si se marcó correctamente
     */
    @PutMapping("/leido/{idRecordatorio}")
    public boolean marcarComoLeido(@PathVariable int idRecordatorio) {
        return recordatorioService.marcarComoLeido(idRecordatorio);
    }

    /**
     * Genera recordatorios automáticos para un usuario.
     * 
     * @param idUsuario identificador del usuario
     */
    @PostMapping("/generar/{idUsuario}")
    public void generarRecordatorios(@PathVariable int idUsuario) {
        recordatorioService.generarRecordatorios(idUsuario);
    }

    /**
     * Lista todos los recordatorios en un rango de fechas.
     * 
     * @param inicio fecha de inicio del rango
     * @param fin fecha de fin del rango
     * @return Lista de recordatorios en el rango
     */
    @GetMapping("/todos")
    public List<Recordatorio> listarTodos(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date inicio,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date fin) {
        return recordatorioService.listarTodos(inicio, fin);
    }

    /**
     * Incrementa el contador de notificaciones de un recordatorio.
     * 
     * @param idRecordatorio identificador del recordatorio
     */
    @PutMapping("/incrementar-contador/{idRecordatorio}")
    public void incrementarContador(@PathVariable int idRecordatorio) {
        recordatorioService.incrementarContador(idRecordatorio);
    }

    /**
     * Obtiene el contador de un recordatorio.
     * 
     * @param idRecordatorio identificador del recordatorio
     * @return valor del contador
     */
    @GetMapping("/contador/{idRecordatorio}")
    public int obtenerContador(@PathVariable int idRecordatorio) {
        return recordatorioService.obtenerContador(idRecordatorio);
    }

    /**
     * Registra un nuevo recordatorio en el sistema.
     * 
     * @param request objeto con los datos del recordatorio
     * @return ID del recordatorio creado
     */
    @PostMapping("/crear")
    public int registrar(@RequestBody RecordatorioRequest request) {
        return recordatorioService.registrar(request.getRecordatorio(), request.getAnticipacion());
    }

    /**
     * Actualiza los datos de un recordatorio existente.
     * 
     * @param recordatorio objeto Recordatorio con los datos actualizados
     * @return true si la actualización fue exitosa
     */
    @PutMapping("/actualizar")
    public boolean actualizar(@RequestBody Recordatorio recordatorio) {
        return recordatorioService.actualizar(recordatorio);
    }

    /**
     * Elimina un recordatorio del sistema.
     * 
     * @param idRecordatorio identificador del recordatorio a eliminar
     * @return true si la eliminación fue exitosa
     */
    @DeleteMapping("/eliminar/{idRecordatorio}")
    public boolean eliminar(@PathVariable int idRecordatorio) {
        return recordatorioService.eliminar(idRecordatorio);
    }
    
    /**
     * Obtiene todas las configuraciones de recordatorios.
     * 
     * @return Lista de configuraciones de recordatorios
     */
    @GetMapping("/config/todas")
    public List<RecordatorioConfig> obtenerTodasConfiguraciones() {
        return recordatorioService.obtenerTodasConfiguraciones();
    }

    /**
     * Actualiza una configuración de recordatorio existente.
     * 
     * @param config objeto RecordatorioConfig con los datos actualizados
     * @return true si la actualización fue exitosa
     */
    @PutMapping("/config/actualizar")
    public boolean actualizarConfiguracion(@RequestBody RecordatorioConfig config) {
        return recordatorioService.actualizarConfiguracion(config);
    }
    
    /**
     * Crea una nueva configuración de recordatorio.
     * 
     * @param config objeto RecordatorioConfig con los datos de la configuración
     * @return ID de la configuración creada
     */
    @PostMapping("/config/crear")
    public int crearConfiguracion(@RequestBody RecordatorioConfig config) {
        return recordatorioService.crearConfiguracion(config);
    }
}