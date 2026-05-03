package com.mycompany.veterinaria.grupo4.api.controller;

import com.mycompany.veterinaria.grupo4.model.entity.Mascota;
import com.mycompany.veterinaria.grupo4.service.MascotaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

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
    public boolean eliminar(@PathVariable int idMascota) {
        return mascotaService.eliminar(idMascota);
    }

    @GetMapping("/foto/{idMascota}")
    public byte[] obtenerFoto(@PathVariable int idMascota) {
        return mascotaService.obtenerFoto(idMascota);
    }
}