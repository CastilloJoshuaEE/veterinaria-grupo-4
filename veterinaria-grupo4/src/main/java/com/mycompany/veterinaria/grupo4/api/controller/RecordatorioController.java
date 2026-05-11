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

@RestController
@RequestMapping("/api/recordatorio")
@CrossOrigin(origins = "*")
public class RecordatorioController {

    @Autowired
    private RecordatorioService recordatorioService;

    @GetMapping("/pendientes/{idUsuario}")
    public List<Recordatorio> obtenerPendientes(@PathVariable int idUsuario) {
        return recordatorioService.obtenerPendientes(idUsuario);
    }

    @PutMapping("/leido/{idRecordatorio}")
    public boolean marcarComoLeido(@PathVariable int idRecordatorio) {
        return recordatorioService.marcarComoLeido(idRecordatorio);
    }

    @PostMapping("/generar/{idUsuario}")
    public void generarRecordatorios(@PathVariable int idUsuario) {
        recordatorioService.generarRecordatorios(idUsuario);
    }

    @GetMapping("/todos")
    public List<Recordatorio> listarTodos(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date inicio,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date fin) {
        return recordatorioService.listarTodos(inicio, fin);
    }

    @PutMapping("/incrementar-contador/{idRecordatorio}")
    public void incrementarContador(@PathVariable int idRecordatorio) {
        recordatorioService.incrementarContador(idRecordatorio);
    }

    @GetMapping("/contador/{idRecordatorio}")
    public int obtenerContador(@PathVariable int idRecordatorio) {
        return recordatorioService.obtenerContador(idRecordatorio);
    }

    @PostMapping("/crear")
    public int registrar(@RequestBody RecordatorioRequest request) {
        return recordatorioService.registrar(request.getRecordatorio(), request.getAnticipacion());
    }

    @PutMapping("/actualizar")
    public boolean actualizar(@RequestBody Recordatorio recordatorio) {
        return recordatorioService.actualizar(recordatorio);
    }

    @DeleteMapping("/eliminar/{idRecordatorio}")
    public boolean eliminar(@PathVariable int idRecordatorio) {
        return recordatorioService.eliminar(idRecordatorio);
    }
    @GetMapping("/config/todas")
    public List<RecordatorioConfig> obtenerTodasConfiguraciones() {
        // Implementa este método en tu service
        return recordatorioService.obtenerTodasConfiguraciones();
    }

    @PutMapping("/config/actualizar")
    public boolean actualizarConfiguracion(@RequestBody RecordatorioConfig config) {
        return recordatorioService.actualizarConfiguracion(config);
    }
    @PostMapping("/config/crear")
    public int crearConfiguracion(@RequestBody RecordatorioConfig config) {
        return recordatorioService.crearConfiguracion(config);
    }
}