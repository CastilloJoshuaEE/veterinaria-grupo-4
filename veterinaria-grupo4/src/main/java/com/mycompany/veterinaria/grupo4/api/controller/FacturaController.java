package com.mycompany.veterinaria.grupo4.api.controller;

import com.mycompany.veterinaria.grupo4.model.entity.Factura;
import com.mycompany.veterinaria.grupo4.service.FacturaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/factura")
@CrossOrigin(origins = "*")
public class FacturaController {

    @Autowired
    private FacturaService facturaService;

    @GetMapping("/cliente/{cedula}")
    public List<Factura> listarPorCedulaCliente(@PathVariable String cedula) {
        return facturaService.listarPorCedulaCliente(cedula);
    }

    @GetMapping("/detalle/{idFactura}")
    public Factura obtenerDetalle(@PathVariable int idFactura) {
        return facturaService.obtenerDetalle(idFactura);
    }

    @PostMapping("/generar")
    public int generarFacturaAtencion(
            @RequestParam int idAtencionMedica,
            @RequestParam String metodoPago,
            @RequestParam(required = false) String cuentaOrigen,
            @RequestParam(required = false) String cuentaDestino) {
        return facturaService.generarFacturaAtencion(idAtencionMedica, metodoPago, 
                                                     cuentaOrigen, cuentaDestino);
    }
}