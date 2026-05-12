package com.mycompany.veterinaria.grupo4.api.controller;

import com.mycompany.veterinaria.grupo4.model.entity.VacunaAplicada;
import com.mycompany.veterinaria.grupo4.service.VacunaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * Controlador REST para la gestión de vacunas aplicadas a mascotas.
 * <p>
 * Proporciona endpoints para obtener vacunas por mascota, registrar
 * nuevas vacunas, actualizar y obtener vacunas próximas a vencer.
 * </p>
 * 
 * <p><b>Fecha de inicio del proyecto:</b> 15/04/2026</p>
 * 
 * @author CASTILLO MEREJILDO JOSHUA JAVIER – MÓDULO: MASCOTA
 * @version 1.0
 * @since 1.0
 */
@RestController
@RequestMapping("/api/vacuna")
@CrossOrigin(origins = "*")
public class VacunaController {

    @Autowired
    private VacunaService vacunaService;

    /**
     * Obtiene todas las vacunas aplicadas a una mascota.
     * 
     * @param idMascota ID de la mascota
     * @return Lista de vacunas aplicadas
     */
    @GetMapping("/mascota/{idMascota}")
    public List<VacunaAplicada> obtenerPorMascota(@PathVariable int idMascota) {
        return vacunaService.listarPorMascota(idMascota);
    }

    /**
     * Registra una nueva vacuna aplicada.
     * 
     * @param vacuna objeto VacunaAplicada con los datos de la vacuna
     * @return ID de la vacuna registrada
     */
    @PostMapping("/registrar")
    public int registrar(@RequestBody VacunaAplicada vacuna) {
        return vacunaService.registrar(vacuna);
    }

    /**
     * Actualiza una vacuna aplicada existente.
     * 
     * @param vacuna objeto VacunaAplicada con los datos actualizados
     * @return true si la actualización fue exitosa
     */
    @PutMapping("/actualizar")
    public boolean actualizar(@RequestBody VacunaAplicada vacuna) {
        return vacunaService.actualizar(vacuna);
    }

    /**
     * Obtiene vacunas próximas a vencer en los próximos días.
     * 
     * @param dias número de días para considerar como próximos a vencer (por defecto 30)
     * @return Lista de vacunas próximas a vencer
     */
    @GetMapping("/proximas-a-vencer")
    public List<VacunaAplicada> obtenerProximasAVencer(@RequestParam(defaultValue = "30") int dias) {
        return vacunaService.listarProximasAVencer(dias);
    }
}