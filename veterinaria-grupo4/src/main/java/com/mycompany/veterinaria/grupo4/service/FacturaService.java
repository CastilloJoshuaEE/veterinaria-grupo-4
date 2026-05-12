package com.mycompany.veterinaria.grupo4.service;

import com.mycompany.veterinaria.grupo4.model.dao.IFacturaDAO;
import com.mycompany.veterinaria.grupo4.model.entity.Factura;
import com.mycompany.veterinaria.grupo4.model.impl.FacturaDAOImpl;
import org.springframework.stereotype.Service;
import java.sql.SQLException;
import java.util.List;

/**
 * Servicio para la gestion de facturas.
 * <p>
 * Proporciona la capa de servicios para las operaciones de negocio
 * relacionadas con la facturacion, incluyendo listado por cliente,
 * obtencion de detalle completo de factura y generacion de facturas
 * a partir de atenciones medicas.
 * </p>
 * 
 * <p><b>Fecha de inicio del proyecto:</b> 15/04/2026</p>
 * 
 * @author ROBLES MORALES JUAN ANDRES – MODULO: ATENCION VETERINARIA
 * @version 1.0
 * @since 1.0
 */
@Service
public class FacturaService {
    private IFacturaDAO facturaDAO = new FacturaDAOImpl();

    /**
     * Lista las facturas asociadas a un cliente por su cedula.
     *
     * @param cedulaCliente numero de cedula del cliente
     * @return lista de facturas del cliente o null si hay error
     */
    public List<Factura> listarPorCedulaCliente(String cedulaCliente) {
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
     * @param idFactura identificador de la factura
     * @return objeto Factura con el detalle completo o null si hay error
     */
    public Factura obtenerDetalle(int idFactura) {
        try {
            return facturaDAO.obtenerDetalleFactura(idFactura);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Genera una factura a partir de una atencion medica.
     *
     * @param idAtencionMedica identificador de la atencion medica
     * @param metodoPago metodo de pago utilizado
     * @param cuentaOrigen cuenta de origen (para transferencias)
     * @param cuentaDestino cuenta de destino (para transferencias)
     * @return ID de la factura generada o -1 si hay error
     */
    public int generarFacturaAtencion(int idAtencionMedica, String metodoPago, 
                                      String cuentaOrigen, String cuentaDestino) {
        try {
            return facturaDAO.generarFacturaAtencion(idAtencionMedica, metodoPago, 
                                                     cuentaOrigen, cuentaDestino);
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }
}