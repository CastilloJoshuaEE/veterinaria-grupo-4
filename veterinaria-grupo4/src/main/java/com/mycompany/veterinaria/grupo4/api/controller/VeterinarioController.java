package com.mycompany.veterinaria.grupo4.api.controller;

import com.mycompany.veterinaria.grupo4.model.entity.Veterinario;
import com.mycompany.veterinaria.grupo4.service.VeterinarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/veterinario")
@CrossOrigin(origins = "*")
public class VeterinarioController {

    @Autowired
    private VeterinarioService veterinarioService;

    @GetMapping("/listar")
    public List<Veterinario> listar() {
        return veterinarioService.listarTodos();
    }

    @GetMapping("/{id}")
    public Veterinario obtenerPorId(@PathVariable int id) {
        return veterinarioService.obtenerPorId(id);
    }

    @GetMapping("/cedula/{cedula}")
    public Veterinario obtenerPorCedula(@PathVariable String cedula) {
        return veterinarioService.obtenerPorCedula(cedula);
    }

    @GetMapping("/buscar/nombre")
    public List<Veterinario> buscarPorNombre(@RequestParam("nombre") String nombre) {
        return veterinarioService.buscarPorNombre(nombre);
    }

    @GetMapping("/buscar/especialidad")
    public List<Veterinario> buscarPorEspecialidad(@RequestParam("especialidad") String especialidad) {
        return veterinarioService.buscarPorEspecialidad(especialidad);
    }

    @GetMapping("/servicio/{idServicio}")
    public List<Veterinario> obtenerPorServicio(@PathVariable int idServicio) {
        return veterinarioService.obtenerPorServicio(idServicio);
    }
    
    @GetMapping("/buscar")
    public List<Veterinario> buscarVeterinarios(@RequestParam(defaultValue = "") String termino) {
        return veterinarioService.buscar(termino);
    }
    
    @PostMapping("/crear")
    public boolean crear(@RequestBody Veterinario veterinario) {
        return veterinarioService.crear(veterinario);
    }

    @PutMapping("/actualizar")
    public boolean actualizar(@RequestBody Veterinario veterinario) {
        return veterinarioService.actualizar(veterinario);
    }

    @DeleteMapping("/eliminar/{id}")
    public boolean eliminar(@PathVariable int id) {
        return veterinarioService.eliminar(id);
    }
}