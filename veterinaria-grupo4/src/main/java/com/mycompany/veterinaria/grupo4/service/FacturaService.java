package com.mycompany.veterinaria.grupo4.service;

import com.mycompany.veterinaria.grupo4.model.dao.IFacturaDAO;
import com.mycompany.veterinaria.grupo4.model.entity.Factura;
import com.mycompany.veterinaria.grupo4.model.impl.FacturaDAOImpl;
import org.springframework.stereotype.Service;
import java.sql.SQLException;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Servicio para la gestion de facturas con validaciones de negocio.
 * <p>
 * Proporciona la capa de servicios para las operaciones de negocio
 * relacionadas con la facturacion, incluyendo listado por cliente,
 * obtencion de detalle completo de factura y generacion de facturas
 * a partir de atenciones medicas.
 * </p>
 * 
 * <p><b>Reglas de negocio implementadas:</b></p>
 * <ul>
 *   <li>La cedula del cliente debe tener formato valido</li>
 *   <li>El metodo de pago debe ser EFECTIVO o TRANSFERENCIA BANCARIA</li>
 *   <li>Para transferencias, las cuentas de origen y destino son obligatorias</li>
 *   <li>No se pueden generar facturas duplicadas para la misma atencion</li>
 * </ul>
 * 
 * <p><b>Fecha de inicio del proyecto:</b> 15/04/2026</p>
 * 
 * @author ROBLES MORALES JUAN ANDRES – MODULO: ATENCION VETERINARIA
 * @version 2.0
 * @since 1.0
 */
@Service
public class FacturaService {
    
    private static final Pattern PATRON_CEDULA = Pattern.compile("^\\d{10}$");
    private static final Pattern PATRON_CUENTA = Pattern.compile("^\\d{10,20}$");
    private static final String METODO_EFECTIVO = "EFECTIVO";
    private static final String METODO_TRANSFERENCIA = "TRANSFERENCIA BANCARIA";
    
    private IFacturaDAO facturaDAO = new FacturaDAOImpl();

    /**
     * Lista las facturas asociadas a un cliente por su cedula.
     *
     * @param cedulaCliente numero de cedula del cliente (10 digitos)
     * @return lista de facturas del cliente o null si hay error
     * @throws IllegalArgumentException si la cedula es invalida
     */
    public List<Factura> listarPorCedulaCliente(String cedulaCliente) {
        validarCedula(cedulaCliente);
        try {
            return facturaDAO.obtenerPorCedulaCliente(cedulaCliente);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Obtiene el detalle completo de una factura.
     *
     * @param idFactura identificador de la factura (debe ser > 0)
     * @return objeto Factura con el detalle completo o null si hay error
     * @throws IllegalArgumentException si el id es invalido
     */
    public Factura obtenerDetalle(int idFactura) {
        if (idFactura <= 0) {
            throw new IllegalArgumentException("ID de factura invalido: " + idFactura);
        }
        try {
            return facturaDAO.obtenerDetalleFactura(idFactura);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Valida y genera una factura a partir de una atencion medica.
     * <p>
     * <b>Reglas de negocio:</b>
     * <ul>
     *   <li>La atencion medica debe existir</li>
     *   <li>El metodo de pago debe ser valido</li>
     *   <li>Para transferencias, las cuentas son obligatorias</li>
     * </ul>
     * </p>
     *
     * @param idAtencionMedica identificador de la atencion medica
     * @param metodoPago metodo de pago utilizado (EFECTIVO o TRANSFERENCIA BANCARIA)
     * @param cuentaOrigen cuenta de origen (para transferencias)
     * @param cuentaDestino cuenta de destino (para transferencias)
     * @return ID de la factura generada o -1 si hay error
     * @throws IllegalArgumentException si los parametros son invalidos
     * @throws IllegalStateException si la atencion ya tiene factura
     */
    public int generarFacturaAtencion(int idAtencionMedica, String metodoPago, 
                                      String cuentaOrigen, String cuentaDestino) {
        
        if (idAtencionMedica <= 0) {
            throw new IllegalArgumentException("ID de atencion medica invalido: " + idAtencionMedica);
        }
        
        validarMetodoPago(metodoPago, cuentaOrigen, cuentaDestino);
        
        // Validar que la atencion medica exista
        AtencionMedicaService atencionService = new AtencionMedicaService();
        var atencion = atencionService.obtenerPorId(idAtencionMedica);
        if (atencion == null) {
            throw new IllegalArgumentException("No existe una atencion medica con ID: " + idAtencionMedica);
        }
        
        
        try {
            return facturaDAO.generarFacturaAtencion(idAtencionMedica, metodoPago, 
                                                     cuentaOrigen, cuentaDestino);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error al generar la factura", e);
        }
    }
    
    /**
     * Valida el formato de la cedula.
     *
     * @param cedula cedula a validar
     * @throws IllegalArgumentException si la cedula es invalida
     */
    private void validarCedula(String cedula) {
        if (cedula == null || cedula.trim().isEmpty()) {
            throw new IllegalArgumentException("La cedula del cliente es obligatoria");
        }
        if (!PATRON_CEDULA.matcher(cedula).matches()) {
            throw new IllegalArgumentException("La cedula debe contener exactamente 10 digitos numericos");
        }
    }
    
    /**
     * Valida el metodo de pago y las cuentas asociadas.
     *
     * @param metodoPago metodo de pago
     * @param cuentaOrigen cuenta de origen
     * @param cuentaDestino cuenta de destino
     * @throws IllegalArgumentException si los datos son invalidos
     */
    private void validarMetodoPago(String metodoPago, String cuentaOrigen, String cuentaDestino) {
        if (metodoPago == null || metodoPago.trim().isEmpty()) {
            throw new IllegalArgumentException("El metodo de pago es obligatorio");
        }
        
        String metodoUpper = metodoPago.toUpperCase().trim();
        
        if (!metodoUpper.equals(METODO_EFECTIVO) && !metodoUpper.equals(METODO_TRANSFERENCIA)) {
            throw new IllegalArgumentException("El metodo de pago debe ser EFECTIVO o TRANSFERENCIA BANCARIA");
        }
        
        if (metodoUpper.equals(METODO_TRANSFERENCIA)) {
            if (cuentaOrigen == null || cuentaOrigen.trim().isEmpty()) {
                throw new IllegalArgumentException("La cuenta de origen es obligatoria para transferencias bancarias");
            }
            if (cuentaDestino == null || cuentaDestino.trim().isEmpty()) {
                throw new IllegalArgumentException("La cuenta de destino es obligatoria para transferencias bancarias");
            }
            if (!PATRON_CUENTA.matcher(cuentaOrigen).matches()) {
                throw new IllegalArgumentException("La cuenta de origen debe tener entre 10 y 20 digitos");
            }
            if (!PATRON_CUENTA.matcher(cuentaDestino).matches()) {
                throw new IllegalArgumentException("La cuenta de destino debe tener entre 10 y 20 digitos");
            }
        }
    }

}