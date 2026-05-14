package com.mycompany.veterinaria.grupo4.api.controller;

import com.mycompany.veterinaria.grupo4.model.entity.Medicamento;
import com.mycompany.veterinaria.grupo4.service.MedicamentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador para la gestión de medicamentos y prescripciones médicas.
 * 
 * <p><b>Fecha de inicio del proyecto:</b> 15/04/2026</p>
 * 
 * @author ROBLES MORALES JUAN ANDRES – MÓDULO: ATENCIÓN VETERINARIA (Jefe)
 * @version 1.0
 * @since 1.0
 */
@RestController
@RequestMapping("/api/medicamento")
@CrossOrigin(origins = "*")
public class MedicamentoController {

    @Autowired
    private MedicamentoService medicamentoService;

    /**
     * Lista todos los medicamentos con stock disponible en farmacia.
     * 
     * @return Lista de medicamentos disponibles
     */
    @GetMapping("/disponibles")
    public List<Medicamento> listarDisponibles() {
        return medicamentoService.listarDisponibles();
    }

    /**
     * Obtiene la información detallada de un medicamento específico.
     * 
     * @param idMedicamento Identificador único del medicamento
     * @return Objeto Medicamento
     */
    @GetMapping("/{idMedicamento}")
    public Medicamento obtenerPorId(@PathVariable int idMedicamento) {
        return medicamentoService.obtenerPorId(idMedicamento);
    }

    /**
     * Registra una prescripción médica (receta) para una atención específica.
     * 
     * @param idAtencionMedica ID de la consulta actual
     * @param idMedicamento ID del medicamento a recetar
     * @param dosis Cantidad (ej: 5ml)
     * @param frecuencia Intervalo (ej: cada 8 horas)
     * @param duracion Tiempo total (ej: 7 días)
     * @return true si se registró correctamente
     */
    @PostMapping("/recetar")
    public boolean recetar(
            @RequestParam int idAtencionMedica,
            @RequestParam int idMedicamento,
            @RequestParam String dosis,
            @RequestParam String frecuencia,
            @RequestParam String duracion) {
        
        return medicamentoService.recetar(idAtencionMedica, idMedicamento, dosis, frecuencia, duracion);
    }

    /**
     * Recupera el historial de medicamentos recetados en una atención médica.
     * 
     * @param idAtencionMedica ID de la consulta
     * @return Lista de medicamentos con su información de prescripción
     */
    @GetMapping("/atencion/{idAtencionMedica}")
    public List<Medicamento> listarRecetadosPorAtencion(@PathVariable int idAtencionMedica) {
        return medicamentoService.listarRecetadosPorAtencion(idAtencionMedica);
    }
}