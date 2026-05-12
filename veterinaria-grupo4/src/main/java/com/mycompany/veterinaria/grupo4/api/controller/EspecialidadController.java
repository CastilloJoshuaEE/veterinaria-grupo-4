package com.mycompany.veterinaria.grupo4.api.controller;

import com.mycompany.veterinaria.grupo4.model.entity.EspecialidadVeterinaria;
import com.mycompany.veterinaria.grupo4.service.EspecialidadService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controlador REST para la gestión de especialidades veterinarias.
 * <p>
 * Proporciona endpoints para listar todas las especialidades disponibles.
 * </p>
 * 
 * <p><b>Fecha de inicio del proyecto:</b> 15/04/2026</p>
 * 
 * @author ROBLES MORALES JUAN ANDRES – MÓDULO: ATENCIÓN VETERINARIA
 * @version 1.0
 * @since 1.0
 */
@RestController
@RequestMapping("/api/especialidad")
@CrossOrigin(origins = "*")
public class EspecialidadController {
    
    @Autowired
    private EspecialidadService especialidadService;
    
    /**
     * Lista todas las especialidades veterinarias registradas.
     * 
     * @return Lista de objetos EspecialidadVeterinaria
     */
    @GetMapping("/listar")
    public List<EspecialidadVeterinaria> listarEspecialidades() {
        return especialidadService.listarTodas();
    }
}