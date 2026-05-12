package com.mycompany.veterinaria.grupo4.api.controller;

import com.mycompany.veterinaria.grupo4.model.entity.Servicio;
import com.mycompany.veterinaria.grupo4.model.entity.Veterinario;
import com.mycompany.veterinaria.grupo4.service.ServicioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * Controlador REST para la gestión de servicios veterinarios.
 * <p>
 * Proporciona endpoints para listar, crear, actualizar, eliminar servicios,
 * así como gestionar la asignación de veterinarios a servicios.
 * </p>
 * 
 * <p><b>Fecha de inicio del proyecto:</b> 15/04/2026</p>
 * 
 * @author BESILLA TOMALA ANGEL KALED – MÓDULO: VETERINARIO
 * @version 1.0
 * @since 1.0
 */
@RestController
@RequestMapping("/api/servicio")
@CrossOrigin(origins = "*")
public class ServicioController {

    @Autowired
    private ServicioService servicioService;

    /**
     * Lista todos los servicios registrados.
     * 
     * @return Lista completa de servicios
     */
    @GetMapping("/listar")
    public List<Servicio> listar() {
        return servicioService.listarTodos();
    }

    /**
     * Lista solo los servicios activos.
     * 
     * @return Lista de servicios activos
     */
    @GetMapping("/activos")
    public List<Servicio> listarActivos() {
        return servicioService.listarActivos();
    }

    /**
     * Obtiene un servicio por su identificador.
     * 
     * @param idServicio identificador del servicio
     * @return Objeto Servicio correspondiente
     */
    @GetMapping("/{idServicio}")
    public Servicio obtenerPorId(@PathVariable int idServicio) {
        return servicioService.obtenerPorId(idServicio);
    }

    /**
     * Crea un nuevo servicio en el sistema.
     * 
     * @param servicio objeto Servicio con los datos a registrar
     * @return ID del servicio creado
     */
    @PostMapping("/crear")
    public int crear(@RequestBody Servicio servicio) {
        return servicioService.crear(servicio);
    }

    /**
     * Actualiza los datos de un servicio existente.
     * 
     * @param servicio objeto Servicio con los datos actualizados
     * @return true si la actualización fue exitosa
     */
    @PutMapping("/actualizar")
    public boolean actualizar(@RequestBody Servicio servicio) {
        return servicioService.actualizar(servicio);
    }

    /**
     * Elimina un servicio del sistema.
     * 
     * @param idServicio identificador del servicio a eliminar
     * @return true si la eliminación fue exitosa
     */
    @DeleteMapping("/eliminar/{idServicio}")
    public boolean eliminar(@PathVariable int idServicio) {
        return servicioService.eliminar(idServicio);
    }

    /**
     * Cambia el estado (activo/inactivo) de un servicio.
     * 
     * @param idServicio identificador del servicio
     * @param estado nuevo estado del servicio
     * @return true si el cambio fue exitoso
     */
    @PutMapping("/estado/{idServicio}")
    public boolean cambiarEstado(@PathVariable int idServicio, @RequestParam boolean estado) {
        return servicioService.cambiarEstado(idServicio, estado);
    }

    /**
     * Obtiene los veterinarios asignados a un servicio.
     * 
     * @param idServicio identificador del servicio
     * @return Lista de veterinarios asignados
     */
    @GetMapping("/veterinarios/asignados/{idServicio}")
    public List<Veterinario> obtenerVeterinariosAsignados(@PathVariable int idServicio) {
        return servicioService.obtenerVeterinariosAsignados(idServicio);
    }

    /**
     * Obtiene los veterinarios no asignados a un servicio.
     * 
     * @param idServicio identificador del servicio
     * @return Lista de veterinarios no asignados
     */
    @GetMapping("/veterinarios/no-asignados/{idServicio}")
    public List<Veterinario> obtenerVeterinariosNoAsignados(@PathVariable int idServicio) {
        return servicioService.obtenerVeterinariosNoAsignados(idServicio);
    }

    /**
     * Asigna un veterinario a un servicio.
     * 
     * @param idServicio identificador del servicio
     * @param idVeterinario identificador del veterinario
     * @return true si la asignación fue exitosa
     */
    @PostMapping("/asignar-veterinario")
    public boolean asignarVeterinario(@RequestParam int idServicio, @RequestParam int idVeterinario) {
        return servicioService.asignarVeterinario(idServicio, idVeterinario);
    }

    /**
     * Elimina una asignación de veterinario por su ID (método obsoleto).
     * 
     * @param idAsignacion identificador de la asignación
     * @return true si la eliminación fue exitosa
     * @deprecated Usar {@link #eliminarAsignacionPorIds(int, int)} en su lugar
     */
    @Deprecated
    @DeleteMapping("/eliminar-asignacion/{idAsignacion}")
    public boolean eliminarAsignacionVeterinario(@PathVariable int idAsignacion) {
        return servicioService.eliminarAsignacionVeterinario(idAsignacion);
    }
    
    /**
     * Lista los servicios asignados a un veterinario específico.
     * 
     * @param idVeterinario identificador del veterinario
     * @return Lista de servicios del veterinario
     */
    @GetMapping("/veterinario/{idVeterinario}")
    public List<Servicio> listarPorVeterinario(@PathVariable int idVeterinario) {
        return servicioService.listarPorVeterinario(idVeterinario);
    }
    
    /**
     * Busca servicios por nombre.
     * 
     * @param termino término de búsqueda
     * @return Lista de servicios que coinciden con el término
     */
    @GetMapping("/buscar")
    public List<Servicio> buscarServicios(@RequestParam(defaultValue = "") String termino) {
        if (termino == null || termino.trim().isEmpty()) {
            return servicioService.listarTodos();
        }
        return servicioService.buscarPorNombre(termino);
    }
    
    /**
     * Elimina una asignación de veterinario por IDs de veterinario y servicio.
     * 
     * @param idVeterinario identificador del veterinario
     * @param idServicio identificador del servicio
     * @return true si la eliminación fue exitosa
     */
    @DeleteMapping("/eliminar-asignacion/{idVeterinario}/{idServicio}")
    public boolean eliminarAsignacionPorIds(@PathVariable int idVeterinario, @PathVariable int idServicio) {
        return servicioService.eliminarAsignacionPorIds(idVeterinario, idServicio);
    }
}