package com.mycompany.veterinaria.grupo4.api.controller;

import com.mycompany.veterinaria.grupo4.model.entity.Cita;
import com.mycompany.veterinaria.grupo4.service.CitaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/cita")
@CrossOrigin(origins = "*")
public class CitaController {

    @Autowired
    private CitaService citaService;

    @GetMapping("/listar")
    public List<Cita> listarTodas() {
        return citaService.listarTodas();
    }

    @GetMapping("/fecha/{fecha}")
    public List<Cita> listarPorFecha(@PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") Date fecha) {
        return citaService.listarPorFecha(fecha);
    }

    @GetMapping("/cliente/{idCliente}")
    public List<Cita> listarPorCliente(@PathVariable int idCliente) {
        return citaService.listarPorCliente(idCliente);
    }

    @GetMapping("/{idCita}")
    public Cita obtenerPorId(@PathVariable int idCita) {
        return citaService.obtenerPorId(idCita);
    }

    @GetMapping("/rango")
    public List<Cita> listarPorRango(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date inicio,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date fin) {
        return citaService.listarPorRangoFechas(inicio, fin);
    }

    @GetMapping("/servicio-veterinario")
    public List<Cita> listarPorServicioYVeterinario(
            @RequestParam int idServicio,
            @RequestParam int idVeterinario,
            @RequestParam(defaultValue = "PENDIENTE") String estado) {
        return citaService.listarPorServicioYVeterinario(idServicio, idVeterinario, estado);
    }

    @PostMapping("/agendar")
    public int agendar(@RequestBody CitaRequest request) {
        return citaService.agendar(request.getCita());
    }

    @PutMapping("/actualizar")
    public boolean actualizar(@RequestBody CitaRequest request) {
        return citaService.actualizar(request.getCita());
    }

    @PutMapping("/cancelar/{idCita}")
    public boolean cancelar(@PathVariable int idCita, @RequestParam String motivo) {
        return citaService.cancelar(idCita, motivo);
    }

    @PutMapping("/estado/{idCita}")
    public boolean actualizarEstado(@PathVariable int idCita, @RequestParam String estado) {
        return citaService.actualizarEstado(idCita, estado);
    }

    @DeleteMapping("/eliminar/{idCita}")
    public boolean eliminar(@PathVariable int idCita) {
        return citaService.eliminar(idCita);
    }
    

    // Clase auxiliar para recibir datos
    public static class CitaRequest {
        private Cita cita;
        private String idsMascotas;

        public Cita getCita() { return cita; }
        public void setCita(Cita cita) { this.cita = cita; }
    }
    @GetMapping("/pendientes")
    public List<Cita> obtenerCitasPendientes() {
        return citaService.listarPendientes(); // Necesitas este método en el service
    }
}