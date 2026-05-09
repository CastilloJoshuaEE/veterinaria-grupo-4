package com.mycompany.veterinaria.grupo4.api.controller;

import com.mycompany.veterinaria.grupo4.api.dto.FichaMedicaDTO;
import com.mycompany.veterinaria.grupo4.model.entity.Mascota;
import com.mycompany.veterinaria.grupo4.service.MascotaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/mascota")
@CrossOrigin(origins = "*")
public class MascotaController {

    @Autowired
    private MascotaService mascotaService;

    @GetMapping("/listar/{idCliente}")
    public List<Mascota> listarPorCliente(@PathVariable int idCliente) {
        return mascotaService.listarPorCliente(idCliente);
    }
    
    @GetMapping("/listar")
    public List<Mascota> listarTodo() {
        return mascotaService.listarTodo();
    }

    @GetMapping("/buscar")
    public List<Mascota> buscarMascotas(@RequestParam(name = "termino", required = false, defaultValue = "") String termino) {
        return mascotaService.buscarMascotas(termino);
    }

    @GetMapping("/{idMascota}")
    public Mascota obtenerPorId(@PathVariable int idMascota) {
        return mascotaService.obtenerPorId(idMascota);
    }
    
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

    @DeleteMapping("/eliminar/{idMascota}")
    public void eliminar(@PathVariable int idMascota) {
        boolean resultado = mascotaService.eliminar(idMascota);
        if (!resultado) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, 
                "La mascota ya posee una cita médica realizada. No se puede eliminar.");
        }
    }

    @GetMapping("/foto/{idMascota}")
    public byte[] obtenerFoto(@PathVariable int idMascota) {
        return mascotaService.obtenerFoto(idMascota);
    }
    @PostMapping("/ficha/guardar")
public boolean guardarFichaMedica(@RequestBody FichaMedicaDTO fichaDTO) {
    try {
        // Asumiendo que tienes un FichaMedicaService
        // Si no existe, usa MascotaService
        return mascotaService.guardarFichaMedica(fichaDTO.getIdMascota(), 
            fichaDTO.getAlergias(), 
            fichaDTO.getEnfermedadesCronicas(), 
            fichaDTO.getObservaciones());
    } catch (Exception e) {
        e.printStackTrace();
        return false;
    }
}

@GetMapping("/ficha/{idMascota}")
public FichaMedicaDTO obtenerFichaMedica(@PathVariable int idMascota) {
    return mascotaService.obtenerFichaMedica(idMascota);
}

}