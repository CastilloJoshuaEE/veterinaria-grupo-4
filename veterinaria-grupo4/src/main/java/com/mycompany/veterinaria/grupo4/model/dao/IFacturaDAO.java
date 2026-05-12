package com.mycompany.veterinaria.grupo4.model.dao;

import com.mycompany.veterinaria.grupo4.model.entity.Factura;
import java.sql.SQLException;
import java.util.List;

/**
 * Interfaz DAO para la gestion de facturas.
 * <p>
 * Define las operaciones de acceso a datos para la entidad Factura,
 * incluyendo consulta por cedula de cliente, detalle de factura
 * y generacion de facturas a partir de atenciones medicas.
 * </p>
 * 
 * <p><b>Fecha de inicio del proyecto:</b> 15/04/2026</p>
 * 
 * @author ROBLES MORALES JUAN ANDRES – MODULO: ATENCION VETERINARIA
 * @version 1.0
 * @since 1.0
 */
public interface IFacturaDAO {
    
    /**
     * Obtiene las facturas asociadas a un cliente por su cedula.
     * 
     * @param cedulaCliente numero de cedula del cliente
     * @return lista de facturas del cliente
     * @throws SQLException si ocurre un error en la base de datos
     */
    List<Factura> obtenerPorCedulaCliente(String cedulaCliente) throws SQLException;
    
    /**
     * Obtiene el detalle completo de una factura.
     * 
     * @param idFactura identificador de la factura
     * @return objeto Factura con el detalle completo
     * @throws SQLException si ocurre un error en la base de datos
     */
    Factura obtenerDetalleFactura(int idFactura) throws SQLException;
    
    /**
     * Genera una factura a partir de una atencion medica.
     * 
     * @param idAtencionMedica identificador de la atencion medica
     * @param metodoPago metodo de pago utilizado
     * @param cuentaOrigen cuenta de origen (para transferencias)
     * @param cuentaDestino cuenta de destino (para transferencias)
     * @return ID de la factura generada
     * @throws SQLException si ocurre un error en la base de datos
     */
    int generarFacturaAtencion(int idAtencionMedica, String metodoPago, String cuentaOrigen, String cuentaDestino) throws SQLException;
}