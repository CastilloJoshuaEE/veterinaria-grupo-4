package com.mycompany.veterinaria.grupo4.api.controller;

import com.mycompany.veterinaria.grupo4.model.entity.Veterinario;
import com.mycompany.veterinaria.grupo4.service.VeterinarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import org.springframework.http.ResponseEntity;

/**
 * Controlador REST para la gestión de veterinarios del sistema.
 * <p>
 * Proporciona endpoints para listar, buscar por ID, cédula, nombre,
 * especialidad o servicio, así como crear, actualizar y eliminar veterinarios.
 * </p>
 * 
 * <p><b>Fecha de inicio del proyecto:</b> 15/04/2026</p>
 * 
 * @author BESILLA TOMALA ANGEL KALED – MÓDULO: VETERINARIO
 * @version 1.0
 * @since 1.0
 */
@RestController
@RequestMapping("/api/veterinario")
@CrossOrigin(origins = "*")
public class VeterinarioController {

    @Autowired
    private VeterinarioService veterinarioService;

    /**
     * Lista todos los veterinarios registrados.
     * 
     * @return Lista completa de veterinarios
     */
    @GetMapping("/listar")
    public List<Veterinario> listar() {
        return veterinarioService.listarTodos();
    }

    /**
     * Obtiene un veterinario por su identificador.
     * 
     * @param id identificador del veterinario
     * @return Objeto Veterinario correspondiente
     */
    @GetMapping("/{id}")
    public Veterinario obtenerPorId(@PathVariable int id) {
        return veterinarioService.obtenerPorId(id);
    }

    /**
     * Obtiene un veterinario por su número de cédula.
     * 
     * @param cedula número de cédula del veterinario
     * @return Objeto Veterinario correspondiente
     */
    @GetMapping("/cedula/{cedula}")
    public Veterinario obtenerPorCedula(@PathVariable String cedula) {
        return veterinarioService.obtenerPorCedula(cedula);
    }

    /**
     * Busca veterinarios por nombre.
     * 
     * @param nombre término de búsqueda para el nombre
     * @return Lista de veterinarios que coinciden con el nombre
     */
    @GetMapping("/buscar/nombre")
    public List<Veterinario> buscarPorNombre(@RequestParam("nombre") String nombre) {
        return veterinarioService.buscarPorNombre(nombre);
    }

    /**
     * Busca veterinarios por especialidad.
     * 
     * @param especialidad especialidad a buscar
     * @return Lista de veterinarios con esa especialidad
     */
    @GetMapping("/buscar/especialidad")
    public List<Veterinario> buscarPorEspecialidad(@RequestParam("especialidad") String especialidad) {
        return veterinarioService.buscarPorEspecialidad(especialidad);
    }

    /**
     * Obtiene veterinarios asignados a un servicio específico.
     * 
     * @param idServicio identificador del servicio
     * @return Lista de veterinarios asignados al servicio
     */
    @GetMapping("/servicio/{idServicio}")
    public List<Veterinario> obtenerPorServicio(@PathVariable int idServicio) {
        return veterinarioService.obtenerPorServicio(idServicio);
    }
    
    /**
     * Busca veterinarios por término general (nombre, cédula o especialidad).
     * 
     * @param termino término de búsqueda
     * @return Lista de veterinarios que coinciden con el término
     */
    @GetMapping("/buscar")
    public List<Veterinario> buscarVeterinarios(@RequestParam(defaultValue = "") String termino) {
        return veterinarioService.buscar(termino);
    }
    
    /**
     * Crea un nuevo veterinario en el sistema.
     * 
     * @param veterinario objeto Veterinario con los datos a registrar
     * @return true si la creación fue exitosa
     */
    @PostMapping("/crear")
public ResponseEntity<?> crear(@RequestBody Veterinario veterinario) {
    try {
        return ResponseEntity.ok(
            veterinarioService.crear(veterinario)
        );

    } catch (IllegalArgumentException e) {

        return ResponseEntity.badRequest()
                .body(e.getMessage());

    } catch (Exception e) {

        return ResponseEntity.internalServerError()
                .body(e.getMessage());
    }
}

    /**
     * Actualiza los datos de un veterinario existente.
     * 
     * @param veterinario objeto Veterinario con los datos actualizados
     * @return true si la actualización fue exitosa
     */
    @PutMapping("/actualizar")
    public boolean actualizar(@RequestBody Veterinario veterinario) {
        return veterinarioService.actualizar(veterinario);
    }

    /**
     * Elimina un veterinario del sistema.
     * 
     * @param id identificador del veterinario a eliminar
     * @return true si la eliminación fue exitosa
     */
    @DeleteMapping("/eliminar/{id}")
    public boolean eliminar(@PathVariable int id) {
        return veterinarioService.eliminar(id);
    }
}