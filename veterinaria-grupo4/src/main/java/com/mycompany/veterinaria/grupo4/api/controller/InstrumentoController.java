package com.mycompany.veterinaria.grupo4.api.controller;

import com.mycompany.veterinaria.grupo4.model.entity.InstrumentoMedico;
import com.mycompany.veterinaria.grupo4.service.InstrumentoMedicoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador para la gestión de insumos e instrumental médico utilizados 
 * durante las intervenciones en Pet Town.
 * 
 * <p><b>Fecha de inicio del proyecto:</b> 15/04/2026</p>
 * 
 * @author ROBLES MORALES JUAN ANDRES – MÓDULO: ATENCIÓN VETERINARIA (Jefe)
 * @version 1.0
 * @since 1.0
 */
@RestController
@RequestMapping("/api/instrumento")
@CrossOrigin(origins = "*")
public class InstrumentoController {

    @Autowired
    private InstrumentoMedicoService instrumentoService;

    /**
     * Lista los insumos disponibles en el stock actual de la clínica.
     * 
     * @return Lista de instrumentos con stock mayor a 0
     */
    @GetMapping("/disponibles")
    public List<InstrumentoMedico> listarDisponibles() {
        return instrumentoService.listarDisponibles();
    }

    /**
     * Busca un instrumento específico por su identificador.
     * 
     * @param idInstrumento ID del insumo
     * @return Detalle del instrumento
     */
    @GetMapping("/{idInstrumento}")
    public InstrumentoMedico obtenerPorId(@PathVariable int idInstrumento) {
        return instrumentoService.obtenerPorId(idInstrumento);
    }

    /**
     * Registra el uso de un insumo durante una atención médica específica.
     * 
     * @param idAtencionMedica Referencia de la atención
     * @param idInstrumento Referencia del insumo usado
     * @return true si el registro fue exitoso
     */
    @PostMapping("/usar")
    public boolean registrarUso(
            @RequestParam int idAtencionMedica, 
            @RequestParam int idInstrumento) {
        
        return instrumentoService.registrarUso(idAtencionMedica, idInstrumento);
    }

    /**
     * Lista todos los instrumentos consumidos en una atención para el desglose de costos.
     * 
     * @param idAtencionMedica ID de la consulta realizada
     * @return Lista de instrumentos asociados a la atención
     */
    @GetMapping("/atencion/{idAtencionMedica}")
    public List<InstrumentoMedico> listarUsadosPorAtencion(@PathVariable int idAtencionMedica) {
        return instrumentoService.listarUsadosPorAtencion(idAtencionMedica);
    }
}