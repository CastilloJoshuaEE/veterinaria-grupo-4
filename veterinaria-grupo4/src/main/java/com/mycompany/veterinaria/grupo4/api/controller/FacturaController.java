package com.mycompany.veterinaria.grupo4.api.controller;

import com.mycompany.veterinaria.grupo4.model.entity.Factura;
import com.mycompany.veterinaria.grupo4.service.FacturaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * Controlador REST para la gestión de facturas del sistema veterinario.
 * <p>
 * Proporciona endpoints para listar facturas por cliente, obtener detalles
 * y generar facturas a partir de atenciones médicas.
 * </p>
 * 
 * <p><b>Fecha de inicio del proyecto:</b> 15/04/2026</p>
 * 
 * @author ROBLES MORALES JUAN ANDRES – MÓDULO: ATENCIÓN VETERINARIA
 * @version 1.0
 * @since 1.0
 */
@RestController
@RequestMapping("/api/factura")
@CrossOrigin(origins = "*")
public class FacturaController {

    @Autowired
    private FacturaService facturaService;

    /**
     * Lista las facturas asociadas a un cliente por su número de cédula.
     * 
     * @param cedula número de cédula del cliente
     * @return Lista de facturas del cliente
     */
    @GetMapping("/cliente/{cedula}")
    public List<Factura> listarPorCedulaCliente(@PathVariable String cedula) {
        return facturaService.listarPorCedulaCliente(cedula);
    }

    /**
     * Obtiene el detalle completo de una factura específica.
     * 
     * @param idFactura identificador de la factura
     * @return Objeto Factura con el detalle completo
     */
    @GetMapping("/detalle/{idFactura}")
    public Factura obtenerDetalle(@PathVariable int idFactura) {
        return facturaService.obtenerDetalle(idFactura);
    }

    /**
     * Genera una factura a partir de una atención médica.
     * 
     * @param idAtencionMedica identificador de la atención médica
     * @param metodoPago método de pago utilizado
     * @param cuentaOrigen cuenta de origen (para transferencias)
     * @param cuentaDestino cuenta de destino (para transferencias)
     * @return ID de la factura generada
     */
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