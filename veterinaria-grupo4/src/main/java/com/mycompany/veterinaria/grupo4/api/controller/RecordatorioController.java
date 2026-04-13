package com.mycompany.veterinaria.grupo4.api.controller;

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

    public static class RecordatorioRequest {
        private Recordatorio recordatorio;
        private String anticipacion;

        public Recordatorio getRecordatorio() { return recordatorio; }
        public void setRecordatorio(Recordatorio recordatorio) { this.recordatorio = recordatorio; }
        public String getAnticipacion() { return anticipacion; }
        public void setAnticipacion(String anticipacion) { this.anticipacion = anticipacion; }
    }
}