package com.mycompany.veterinaria.grupo4.service;

import com.mycompany.veterinaria.grupo4.model.dao.IFacturaDAO;
import com.mycompany.veterinaria.grupo4.model.entity.Factura;
import com.mycompany.veterinaria.grupo4.model.impl.FacturaDAOImpl;
import org.springframework.stereotype.Service;
import java.sql.SQLException;
import java.util.List;

@Service
public class FacturaService {
    private IFacturaDAO facturaDAO = new FacturaDAOImpl();

    public List<Factura> listarPorCedulaCliente(String cedulaCliente) {
        try {
            return facturaDAO.obtenerPorCedulaCliente(cedulaCliente);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Factura obtenerDetalle(int idFactura) {
        try {
            return facturaDAO.obtenerDetalleFactura(idFactura);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

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