package com.mycompany.veterinaria.grupo4.api.controller;

import com.mycompany.veterinaria.grupo4.api.dto.FichaMedicaDTO;
import com.mycompany.veterinaria.grupo4.model.entity.Mascota;
import com.mycompany.veterinaria.grupo4.service.MascotaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 * Controlador REST para la gestión de mascotas del sistema veterinario.
 * <p>
 * Proporciona endpoints para listar, buscar, crear, actualizar, eliminar
 * mascotas, así como gestionar sus fotos y fichas médicas.
 * </p>
 * 
 * <p><b>Fecha de inicio del proyecto:</b> 15/04/2026</p>
 * 
 * @author CASTILLO MEREJILDO JOSHUA JAVIER – MÓDULO: MASCOTA
 * @version 1.0
 * @since 1.0
 */
@RestController
@RequestMapping("/api/mascota")
@CrossOrigin(origins = "*")
public class MascotaController {

    @Autowired
    private MascotaService mascotaService;

    /**
     * Lista las mascotas asociadas a un cliente específico.
     * 
     * @param idCliente identificador del cliente
     * @return Lista de mascotas del cliente
     */
    @GetMapping("/listar/{idCliente}")
    public List<Mascota> listarPorCliente(@PathVariable int idCliente) {
        return mascotaService.listarPorCliente(idCliente);
    }
    
    /**
     * Lista todas las mascotas registradas en el sistema.
     * 
     * @return Lista completa de mascotas
     */
    @GetMapping("/listar")
    public List<Mascota> listarTodo() {
        return mascotaService.listarTodo();
    }

    /**
     * Busca mascotas cuyo nombre coincida con el término de búsqueda.
     * 
     * @param termino término de búsqueda para el nombre de la mascota
     * @return Lista de mascotas que coinciden con el término
     */
    @GetMapping("/buscar")
    public List<Mascota> buscarMascotas(@RequestParam(name = "termino", required = false, defaultValue = "") String termino) {
        return mascotaService.buscarMascotas(termino);
    }

    /**
     * Obtiene una mascota por su identificador.
     * 
     * @param idMascota identificador de la mascota
     * @return Objeto Mascota correspondiente
     */
    @GetMapping("/{idMascota}")
    public Mascota obtenerPorId(@PathVariable int idMascota) {
        return mascotaService.obtenerPorId(idMascota);
    }
    
    /**
     * Crea una nueva mascota en el sistema con posibilidad de adjuntar foto.
     * 
     * @param idCliente identificador del cliente dueño
     * @param nombre nombre de la mascota
     * @param especie especie de la mascota
     * @param raza raza de la mascota (opcional)
     * @param sexo sexo de la mascota ('M' o 'H')
     * @param fechaNacimiento fecha de nacimiento (opcional)
     * @param peso peso de la mascota (opcional)
     * @param color color de la mascota (opcional)
     * @param foto foto de la mascota (opcional)
     * @return ID de la mascota creada
     * @throws IOException si hay error al procesar la foto
     */
    @PostMapping(value = "/crear", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public int crear(
            @RequestParam("idCliente") int idCliente,
            @RequestParam("nombre") String nombre,
            @RequestParam("especie") String especie,
            @RequestParam(value = "raza", required = false) String raza,
            @RequestParam("sexo") char sexo,
            @RequestParam(value = "fechaNacimiento", required = false) String fechaNacimiento,
            @RequestParam(value = "peso", required = false) Double peso,
            @RequestParam(value = "color", required = false) String color,
            @RequestParam(value = "foto", required = false) MultipartFile foto) throws IOException {

        Mascota mascota = new Mascota();
        mascota.setIdCliente(idCliente);
        mascota.setNombre(nombre);
        mascota.setEspecie(especie);
        mascota.setRaza(raza);
        mascota.setSexo(sexo);
        if (fechaNacimiento != null && !fechaNacimiento.isEmpty()) {
            mascota.setFechaNacimiento(java.sql.Date.valueOf(fechaNacimiento));
        }
        mascota.setPeso(peso);
        mascota.setColor(color);
        if (foto != null && !foto.isEmpty()) {
            mascota.setFoto(foto.getBytes());
        }

        return mascotaService.crear(mascota);
    }

    /**
     * Actualiza los datos de una mascota existente.
     * 
     * @param idMascota identificador de la mascota
     * @param idCliente identificador del cliente dueño
     * @param nombre nombre de la mascota
     * @param especie especie de la mascota
     * @param raza raza de la mascota (opcional)
     * @param sexo sexo de la mascota
     * @param fechaNacimiento fecha de nacimiento (opcional)
     * @param peso peso de la mascota (opcional)
     * @param color color de la mascota (opcional)
     * @param foto foto de la mascota (opcional)
     * @return true si la actualización fue exitosa
     * @throws IOException si hay error al procesar la foto
     */
    @PutMapping(value = "/actualizar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public boolean actualizar(
            @RequestParam("idMascota") int idMascota,
            @RequestParam("idCliente") int idCliente,
            @RequestParam("nombre") String nombre,
            @RequestParam("especie") String especie,
            @RequestParam(value = "raza", required = false) String raza,
            @RequestParam("sexo") char sexo,
            @RequestParam(value = "fechaNacimiento", required = false) String fechaNacimiento,
            @RequestParam(value = "peso", required = false) Double peso,
            @RequestParam(value = "color", required = false) String color,
            @RequestParam(value = "foto", required = false) MultipartFile foto) throws IOException {

        Mascota mascota = new Mascota();
        mascota.setIdMascota(idMascota);
        mascota.setIdCliente(idCliente);
        mascota.setNombre(nombre);
        mascota.setEspecie(especie);
        mascota.setRaza(raza);
        mascota.setSexo(sexo);
        if (fechaNacimiento != null && !fechaNacimiento.isEmpty()) {
            mascota.setFechaNacimiento(java.sql.Date.valueOf(fechaNacimiento));
        }
        mascota.setPeso(peso);
        mascota.setColor(color);
        if (foto != null && !foto.isEmpty()) {
            mascota.setFoto(foto.getBytes());
        }

        return mascotaService.actualizar(mascota);
    }

    /**
     * Elimina una mascota del sistema.
     * 
     * @param idMascota identificador de la mascota a eliminar
     * @return ResponseEntity con el mensaje de resultado
     */
    @DeleteMapping("/eliminar/{idMascota}")
    public ResponseEntity<?> eliminar(@PathVariable int idMascota) {
        try {
            boolean resultado = mascotaService.eliminar(idMascota);
            if (resultado) {
                return ResponseEntity.ok().body("Mascota eliminada correctamente.");
            } else {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("No se pudo eliminar la mascota. Verifique que no tenga citas o atenciones asociadas.");
            }
        } catch (RuntimeException e) {
            String mensaje = e.getMessage();
            if (mensaje != null && mensaje.contains("cita médica realizada")) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("La mascota ya posee una cita médica realizada. No se puede eliminar.");
            } else if (mensaje != null && mensaje.contains("citas pendientes")) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("La mascota tiene citas pendientes. No se puede eliminar.");
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error al eliminar la mascota: " + mensaje);
        }
    }

    /**
     * Obtiene la foto de una mascota.
     * 
     * @param idMascota identificador de la mascota
     * @return arreglo de bytes con la imagen de la mascota
     */
    @GetMapping("/foto/{idMascota}")
    public byte[] obtenerFoto(@PathVariable int idMascota) {
        return mascotaService.obtenerFoto(idMascota);
    }
    
    /**
     * Guarda la ficha médica de una mascota.
     * 
     * @param fichaDTO objeto con los datos de la ficha médica
     * @return true si el guardado fue exitoso
     */
    @PostMapping("/ficha/guardar")
    public boolean guardarFichaMedica(@RequestBody FichaMedicaDTO fichaDTO) {
        try {
            return mascotaService.guardarFichaMedica(fichaDTO.getIdMascota(), 
                fichaDTO.getAlergias(), 
                fichaDTO.getEnfermedadesCronicas(), 
                fichaDTO.getObservaciones());
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Obtiene la ficha médica de una mascota.
     * 
     * @param idMascota identificador de la mascota
     * @return objeto FichaMedicaDTO con los datos de la ficha médica
     */
    @GetMapping("/ficha/{idMascota}")
    public FichaMedicaDTO obtenerFichaMedica(@PathVariable int idMascota) {
        return mascotaService.obtenerFichaMedica(idMascota);
    }
}