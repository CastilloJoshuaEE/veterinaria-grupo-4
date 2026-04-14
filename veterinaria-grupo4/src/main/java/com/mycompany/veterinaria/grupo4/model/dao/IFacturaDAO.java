package com.mycompany.veterinaria.grupo4.model.dao;

import com.mycompany.veterinaria.grupo4.model.entity.Factura;
import java.sql.SQLException;
import java.util.List;

public interface IFacturaDAO {
    List<Factura> obtenerPorCedulaCliente(String cedulaCliente) throws SQLException;
    Factura obtenerDetalleFactura(int idFactura) throws SQLException;
    int generarFacturaAtencion(int idAtencionMedica, String metodoPago, String cuentaOrigen, String cuentaDestino) throws SQLException;
}