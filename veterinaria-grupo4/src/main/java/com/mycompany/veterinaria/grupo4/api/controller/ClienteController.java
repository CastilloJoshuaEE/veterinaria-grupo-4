package com.mycompany.veterinaria.grupo4.api.controller;

import com.mycompany.veterinaria.grupo4.model.entity.Cliente;
import com.mycompany.veterinaria.grupo4.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * Controlador REST para la gestión de clientes del sistema veterinario.
 * <p>
 * Proporciona endpoints para listar, buscar por ID, cédula o nombre,
 * crear, actualizar y eliminar clientes.
 * </p>
 * 
 * <p><b>Fecha de inicio del proyecto:</b> 15/04/2026</p>
 * 
 * @author CASTRO AVILA JONATHAN XAVIER – MÓDULO: CLIENTE
 * @version 1.0
 * @since 1.0
 */
@RestController
@RequestMapping("/api/cliente")
@CrossOrigin(origins = "*")
public class ClienteController {
    
    @Autowired
    private ClienteService clienteService;
    
    /**
     * Lista todos los clientes registrados en el sistema.
     * 
     * @return Lista completa de objetos Cliente
     */
    @GetMapping("/listar")
    public List<Cliente> listar() {
        return clienteService.listarTodos();
    }
    
    /**
     * Obtiene la lista de todas las cédulas de clientes registrados.
     * 
     * @return Lista de cédulas como cadenas de texto
     */
    @GetMapping("/cedulas")
    public List<String> obtenerCedulas() {
        return clienteService.obtenerCedulas();
    }
    
    /**
     * Obtiene un cliente por su identificador único.
     * 
     * @param id identificador del cliente
     * @return Objeto Cliente correspondiente
     */
    @GetMapping("/{id}")
    public Cliente obtenerPorId(@PathVariable int id) {
        return clienteService.obtenerPorId(id);
    }
    
    /**
     * Obtiene un cliente por su número de cédula.
     * 
     * @param cedula número de cédula del cliente
     * @return Objeto Cliente correspondiente
     */
    @GetMapping("/cedula/{cedula}")
    public Cliente obtenerPorCedula(@PathVariable String cedula) {
        return clienteService.obtenerPorCedula(cedula);
    }
    
    /**
     * Busca clientes cuyo nombre coincida con el término de búsqueda.
     * 
     * @param nombre término de búsqueda para el nombre del cliente
     * @return Lista de clientes que coinciden con el nombre
     */
    @GetMapping("/buscar/nombre")
    public List<Cliente> buscarPorNombre(@RequestParam("nombre") String nombre) {
        return clienteService.buscarPorNombre(nombre);
    }
    
    /**
     * Crea un nuevo cliente en el sistema.
     * 
     * @param cliente objeto Cliente con los datos a registrar
     * @return true si la creación fue exitosa
     */
    @PostMapping("/crear")
    public ResponseEntity<?> crear(@RequestBody Cliente cliente) {
        try {
            boolean resultado = clienteService.crear(cliente);
            return ResponseEntity.ok(resultado);
        } catch (IllegalArgumentException e) {
            // Retornar 409 Conflict con mensaje amigable
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }
    
    /**
     * Actualiza los datos de un cliente existente.
     * 
     * @param cliente objeto Cliente con los datos actualizados
     * @return true si la actualización fue exitosa
     */
    @PutMapping("/actualizar")
    public boolean actualizar(@RequestBody Cliente cliente) {
        return clienteService.actualizar(cliente);
    }
    
    /**
     * Elimina un cliente del sistema.
     * 
     * @param id identificador del cliente a eliminar
     * @return true si la eliminación fue exitosa
     */
    @DeleteMapping("/eliminar/{id}")
    public boolean eliminar(@PathVariable int id) {
        return clienteService.eliminar(id);
    }
}