package com.mycompany.veterinaria.grupo4.api.controller;

import com.mycompany.veterinaria.grupo4.model.entity.HistorialMedico;
import com.mycompany.veterinaria.grupo4.service.HistorialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * Controlador REST para la gestión del historial médico de mascotas.
 * <p>
 * Proporciona endpoints para obtener el historial por mascota,
 * registrar nuevas entradas y obtener el historial completo.
 * </p>
 * 
 * <p><b>Fecha de inicio del proyecto:</b> 15/04/2026</p>
 * 
 * @author CASTILLO MEREJILDO JOSHUA JAVIER – MÓDULO: MASCOTA
 * @version 1.0
 * @since 1.0
 */
@RestController
@RequestMapping("/api/historial")
@CrossOrigin(origins = "*")
public class HistorialController {

    @Autowired
    private HistorialService historialService;

    /**
     * Obtiene el historial médico completo de una mascota.
     * 
     * @param idMascota ID de la mascota
     * @return Lista de registros del historial médico
     */
    @GetMapping("/mascota/{idMascota}")
    public List<HistorialMedico> obtenerPorMascota(@PathVariable int idMascota) {
        return historialService.obtenerPorMascota(idMascota);
    }

    /**
     * Registra una nueva entrada en el historial médico.
     * 
     * @param idMascota ID de la mascota
     * @param idCita ID de la cita (opcional)
     * @param idAtencionMedica ID de la atención médica (opcional)
     * @return true si se registró correctamente, false en caso contrario
     */
    @PostMapping("/registrar")
    public boolean registrar(
            @RequestParam int idMascota,
            @RequestParam(required = false) Integer idCita,
            @RequestParam(required = false) Integer idAtencionMedica) {
        return historialService.registrar(idMascota, idCita, idAtencionMedica);
    }

    /**
     * Obtiene el historial médico completo con todos los detalles.
     * 
     * @param idMascota ID de la mascota
     * @return Lista con la información completa del historial
     */
    @GetMapping("/completo/{idMascota}")
    public List<HistorialMedico> obtenerHistorialCompleto(@PathVariable int idMascota) {
        return historialService.obtenerPorMascota(idMascota);
    }
}