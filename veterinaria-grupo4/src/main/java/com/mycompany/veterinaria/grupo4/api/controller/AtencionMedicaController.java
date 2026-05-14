package com.mycompany.veterinaria.grupo4.api.controller;

import com.mycompany.veterinaria.grupo4.model.entity.AtencionMedica;
import com.mycompany.veterinaria.grupo4.service.AtencionMedicaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * Controlador REST para la gestión de atenciones médicas en el sistema veterinario.
 * <p>
 * Proporciona endpoints para listar, obtener, crear y eliminar atenciones médicas.
 * </p>
 * 
 * <p><b>Fecha de inicio del proyecto:</b> 15/04/2026</p>
 * 
 * @author ROBLES MORALES JUAN ANDRES – MÓDULO: ATENCIÓN VETERINARIA
 * @version 1.0
 * @since 1.0
 */
@RestController
@RequestMapping("/api/atencion-medica")
@CrossOrigin(origins = "*")
public class AtencionMedicaController {

    @Autowired
    private AtencionMedicaService atencionMedicaService;

    /**
     * Obtiene la lista completa de todas las atenciones médicas registradas.
     * 
     * @return Lista de objetos AtencionMedica
     */
    @GetMapping("/listar")
    public List<AtencionMedica> listar() {
        return atencionMedicaService.listarTodas();
    }

    /**
     * Obtiene una atención médica específica por su identificador.
     * 
     * @param idAtencionMedica identificador único de la atención médica
     * @return Objeto AtencionMedica correspondiente al ID
     */
    @GetMapping("/{idAtencionMedica}")
    public AtencionMedica obtenerPorId(@PathVariable int idAtencionMedica) {
        return atencionMedicaService.obtenerPorId(idAtencionMedica);
    }

    /**
     * Crea una nueva atención médica en el sistema.
     * 
     * @param atencion objeto AtencionMedica con los datos a registrar
     * @return ID generado para la nueva atención médica
     */
    @PostMapping("/crear")
    public int crear(@RequestBody AtencionMedica atencion) {
        return atencionMedicaService.guardar(atencion);
    }

    /**
     * Elimina una atención médica del sistema.
     * 
     * @param idAtencionMedica identificador de la atención médica a eliminar
     * @return true si la eliminación fue exitosa, false en caso contrario
     */
    @DeleteMapping("/eliminar/{idAtencionMedica}")
    public boolean eliminar(@PathVariable int idAtencionMedica) {
        return atencionMedicaService.eliminar(idAtencionMedica);
    }
}