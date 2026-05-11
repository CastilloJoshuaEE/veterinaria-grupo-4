package com.mycompany.veterinaria.grupo4.api.controller;

import com.mycompany.veterinaria.grupo4.model.entity.AtencionMedica;
import com.mycompany.veterinaria.grupo4.service.AtencionMedicaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/atencion-medica")
@CrossOrigin(origins = "*")
public class AtencionMedicaController {

    @Autowired
    private AtencionMedicaService atencionMedicaService;

    @GetMapping("/listar")
    public List<AtencionMedica> listar() {
        return atencionMedicaService.listarTodas();
    }

    @GetMapping("/{idAtencionMedica}")
    public AtencionMedica obtenerPorId(@PathVariable int idAtencionMedica) {
        return atencionMedicaService.obtenerPorId(idAtencionMedica);
    }

    @PostMapping("/crear")
    public int crear(@RequestBody AtencionMedica atencion) {
        return atencionMedicaService.guardar(atencion);
    }

    @DeleteMapping("/eliminar/{idAtencionMedica}")
    public boolean eliminar(@PathVariable int idAtencionMedica) {
        return atencionMedicaService.eliminar(idAtencionMedica);
    }
}