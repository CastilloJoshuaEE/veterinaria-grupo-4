package com.mycompany.veterinaria.grupo4.api.controller;

import com.mycompany.veterinaria.grupo4.model.entity.Cliente;
import com.mycompany.veterinaria.grupo4.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cliente")
@CrossOrigin(origins = "*")
public class ClienteController {
    
    @Autowired
    private ClienteService clienteService;
    
    @GetMapping("/listar")
    public List<Cliente> listar() {
        return clienteService.listarTodos();
    }
    
    @GetMapping("/cedulas")
    public List<String> obtenerCedulas() {
        return clienteService.obtenerCedulas();
    }
    
    @GetMapping("/{id}")
    public Cliente obtenerPorId(@PathVariable int id) {
        return clienteService.obtenerPorId(id);
    }
    
    @GetMapping("/cedula/{cedula}")
    public Cliente obtenerPorCedula(@PathVariable String cedula) {
        return clienteService.obtenerPorCedula(cedula);
    }
    
    @GetMapping("/buscar/nombre")
    public List<Cliente> buscarPorNombre(@RequestParam("nombre") String nombre) {
        return clienteService.buscarPorNombre(nombre);
    }
    
    @PostMapping("/crear")
    public boolean crear(@RequestBody Cliente cliente) {
        return clienteService.crear(cliente);
    }
    
    @PutMapping("/actualizar")
    public boolean actualizar(@RequestBody Cliente cliente) {
        return clienteService.actualizar(cliente);
    }
    
    @DeleteMapping("/eliminar/{id}")
    public boolean eliminar(@PathVariable int id) {
        return clienteService.eliminar(id);
    }
}