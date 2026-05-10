package com.mycompany.veterinaria.grupo4.api.controller;

import com.mycompany.veterinaria.grupo4.model.entity.VacunaAplicada;
import com.mycompany.veterinaria.grupo4.service.VacunaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/vacuna")
@CrossOrigin(origins = "*")
public class VacunaController {

    @Autowired
    private VacunaService vacunaService;

    /**
     * Obtiene todas las vacunas aplicadas a una mascota
     * @param idMascota ID de la mascota
     * @return Lista de vacunas aplicadas
     */
    @GetMapping("/mascota/{idMascota}")
    public List<VacunaAplicada> obtenerPorMascota(@PathVariable int idMascota) {
        return vacunaService.listarPorMascota(idMascota);
    }

    /**
     * Registra una nueva vacuna aplicada
     */
    @PostMapping("/registrar")
    public int registrar(@RequestBody VacunaAplicada vacuna) {
        return vacunaService.registrar(vacuna);
    }

    /**
     * Actualiza una vacuna aplicada
     */
    @PutMapping("/actualizar")
    public boolean actualizar(@RequestBody VacunaAplicada vacuna) {
        return vacunaService.actualizar(vacuna);
    }

    /**
     * Obtiene vacunas próximas a vencer
     */
    @GetMapping("/proximas-a-vencer")
    public List<VacunaAplicada> obtenerProximasAVencer(@RequestParam(defaultValue = "30") int dias) {
        return vacunaService.listarProximasAVencer(dias);
    }
}