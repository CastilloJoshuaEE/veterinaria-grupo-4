package com.mycompany.veterinaria.grupo4.api.controller;

import com.mycompany.veterinaria.grupo4.model.entity.Cita;
import com.mycompany.veterinaria.grupo4.service.CitaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import java.util.Date;
import java.util.List;

/**
 * Controlador REST para la gestión de citas del sistema veterinario.
 * <p>
 * Proporciona endpoints para listar, filtrar por fecha, cliente, rango,
 * agendar, actualizar, cancelar y eliminar citas.
 * </p>
 * 
 * <p><b>Fecha de inicio del proyecto:</b> 15/04/2026</p>
 * 
 * @author CHILAN CHILAN DANNY ANDRES – MÓDULO: AGENDAMIENTO DE CITA
 * @version 1.0
 * @since 1.0
 */
@RestController
@RequestMapping("/api/cita")
@CrossOrigin(origins = "*")
public class CitaController {

    @Autowired
    private CitaService citaService;

    /**
     * Lista todas las citas registradas en el sistema.
     * 
     * @return Lista completa de objetos Cita
     */
    @GetMapping("/listar")
    public List<Cita> listarTodas() {
        return citaService.listarTodas();
    }

    /**
     * Lista las citas programadas para una fecha específica.
     * 
     * @param fecha fecha en formato yyyy-MM-dd
     * @return Lista de citas para la fecha especificada
     */
    @GetMapping("/fecha/{fecha}")
    public List<Cita> listarPorFecha(@PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") Date fecha) {
        return citaService.listarPorFecha(fecha);
    }

    /**
     * Lista las citas asociadas a un cliente específico.
     * 
     * @param idCliente identificador del cliente
     * @return Lista de citas del cliente
     */
    @GetMapping("/cliente/{idCliente}")
    public List<Cita> listarPorCliente(@PathVariable int idCliente) {
        return citaService.listarPorCliente(idCliente);
    }

    /**
     * Obtiene una cita específica por su identificador.
     * 
     * @param idCita identificador de la cita
     * @return Objeto Cita correspondiente
     */
    @GetMapping("/{idCita}")
    public Cita obtenerPorId(@PathVariable int idCita) {
        return citaService.obtenerPorId(idCita);
    }

    /**
     * Lista las citas dentro de un rango de fechas.
     * 
     * @param inicio fecha de inicio del rango
     * @param fin fecha de fin del rango
     * @return Lista de citas en el rango especificado
     */
    @GetMapping("/rango")
    public List<Cita> listarPorRango(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date inicio,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date fin) {
        return citaService.listarPorRangoFechas(inicio, fin);
    }

    /**
     * Lista citas filtradas por servicio, veterinario y estado.
     * 
     * @param idServicio identificador del servicio
     * @param idVeterinario identificador del veterinario
     * @param estado estado de la cita (por defecto "PENDIENTE")
     * @return Lista de citas que coinciden con los filtros
     */
    @GetMapping("/servicio-veterinario")
    public List<Cita> listarPorServicioYVeterinario(
            @RequestParam int idServicio,
            @RequestParam int idVeterinario,
            @RequestParam(defaultValue = "PENDIENTE") String estado) {
        return citaService.listarPorServicioYVeterinario(idServicio, idVeterinario, estado);
    }

    /**
     * Agenda una nueva cita en el sistema.
     * 
     * @param request objeto CitaRequest con los datos de la cita
     * @return ID de la cita agendada
     */
    @PostMapping("/agendar")
    public int agendar(@RequestBody CitaRequest request) {
        return citaService.agendar(request.getCita());
    }

    /**
     * Actualiza los datos de una cita existente.
     * 
     * @param request objeto CitaRequest con los datos actualizados
     * @return true si la actualización fue exitosa
     */
    @PutMapping("/actualizar")
    public boolean actualizar(@RequestBody CitaRequest request) {
        return citaService.actualizar(request.getCita());
    }

    /**
     * Cancela una cita con un motivo específico.
     * 
     * @param idCita identificador de la cita a cancelar
     * @param motivo razón de la cancelación
     * @return true si la cancelación fue exitosa
     */
    @PutMapping("/cancelar/{idCita}")
    public boolean cancelar(@PathVariable int idCita, @RequestParam String motivo) {
        return citaService.cancelar(idCita, motivo);
    }

    /**
     * Actualiza el estado de una cita.
     * 
     * @param idCita identificador de la cita
     * @param estado nuevo estado de la cita
     * @return true si la actualización fue exitosa
     */
    @PutMapping("/estado/{idCita}")
    public boolean actualizarEstado(@PathVariable int idCita, @RequestParam String estado) {
        return citaService.actualizarEstado(idCita, estado);
    }

    /**
     * Elimina una cita del sistema.
     * 
     * @param idCita identificador de la cita a eliminar
     * @return true si la eliminación fue exitosa
     */
    @DeleteMapping("/eliminar/{idCita}")
    public boolean eliminar(@PathVariable int idCita) {
        return citaService.eliminar(idCita);
    }
    
    /**
     * Obtiene todas las citas con estado pendiente.
     * 
     * @return Lista de citas pendientes
     */
    @GetMapping("/pendientes")
    public List<Cita> obtenerCitasPendientes() {
        return citaService.listarPendientes();
    }

    /**
     * Clase auxiliar para recibir datos de cita en las peticiones.
     */
    public static class CitaRequest {
        private Cita cita;
        private String idsMascotas;

        public Cita getCita() { return cita; }
        public void setCita(Cita cita) { this.cita = cita; }
    }
}