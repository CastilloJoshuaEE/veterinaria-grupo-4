package com.mycompany.veterinaria.grupo4.api.controller;

import com.mycompany.veterinaria.grupo4.model.entity.Servicio;
import com.mycompany.veterinaria.grupo4.model.entity.Veterinario;
import com.mycompany.veterinaria.grupo4.service.ServicioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/servicio")
@CrossOrigin(origins = "*")
public class ServicioController {

    @Autowired
    private ServicioService servicioService;

    @GetMapping("/listar")
    public List<Servicio> listar() {
        return servicioService.listarTodos();
    }

    @GetMapping("/activos")
    public List<Servicio> listarActivos() {
        return servicioService.listarActivos();
    }

    @GetMapping("/{idServicio}")
    public Servicio obtenerPorId(@PathVariable int idServicio) {
        return servicioService.obtenerPorId(idServicio);
    }

    @PostMapping("/crear")
    public int crear(@RequestBody Servicio servicio) {
        return servicioService.crear(servicio);
    }

    @PutMapping("/actualizar")
    public boolean actualizar(@RequestBody Servicio servicio) {
        return servicioService.actualizar(servicio);
    }

    @DeleteMapping("/eliminar/{idServicio}")
    public boolean eliminar(@PathVariable int idServicio) {
        return servicioService.eliminar(idServicio);
    }

    @PutMapping("/estado/{idServicio}")
    public boolean cambiarEstado(@PathVariable int idServicio, @RequestParam boolean estado) {
        return servicioService.cambiarEstado(idServicio, estado);
    }

    @GetMapping("/veterinarios/asignados/{idServicio}")
    public List<Veterinario> obtenerVeterinariosAsignados(@PathVariable int idServicio) {
        return servicioService.obtenerVeterinariosAsignados(idServicio);
    }

    @GetMapping("/veterinarios/no-asignados/{idServicio}")
    public List<Veterinario> obtenerVeterinariosNoAsignados(@PathVariable int idServicio) {
        return servicioService.obtenerVeterinariosNoAsignados(idServicio);
    }

    @PostMapping("/asignar-veterinario")
    public boolean asignarVeterinario(@RequestParam int idServicio, @RequestParam int idVeterinario) {
        return servicioService.asignarVeterinario(idServicio, idVeterinario);
    }

    @DeleteMapping("/eliminar-asignacion/{idAsignacion}")
    public boolean eliminarAsignacionVeterinario(@PathVariable int idAsignacion) {
        return servicioService.eliminarAsignacionVeterinario(idAsignacion);
    }
    
    @GetMapping("/veterinario/{idVeterinario}")
    public List<Servicio> listarPorVeterinario(@PathVariable int idVeterinario) {
        return servicioService.listarPorVeterinario(idVeterinario);
    }
}