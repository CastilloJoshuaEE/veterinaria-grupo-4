package com.mycompany.veterinaria.grupo4.model.impl;

import com.mycompany.veterinaria.grupo4.model.dao.IFacturaDAO;
import com.mycompany.veterinaria.grupo4.model.entity.Cliente;
import com.mycompany.veterinaria.grupo4.model.entity.Factura;
import com.mycompany.veterinaria.grupo4.util.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementacion del DAO para la gestion de facturas.
 * <p>
 * Esta clase implementa la interfaz IFacturaDAO y proporciona la logica
 * de acceso a datos para la entidad Factura utilizando procedimientos
 * almacenados de SQL Server. Maneja la obtencion de facturas por cedula,
 * detalles completos de factura y generacion de facturas a partir de
 * atenciones medicas.
 * </p>
 * 
 * <p><b>Fecha de inicio del proyecto:</b> 15/04/2026</p>
 * 
 * @author ROBLES MORALES JUAN ANDRES – MODULO: ATENCION VETERINARIA
 * @version 1.0
 * @since 1.0
 */
public class FacturaDAOImpl implements IFacturaDAO {

    /**
     * Obtiene las facturas asociadas a un cliente por su cedula.
     *
     * @param cedulaCliente numero de cedula del cliente
     * @return lista de facturas del cliente
     * @throws SQLException si ocurre un error en la base de datos
     */
    @Override
    public List<Factura> obtenerPorCedulaCliente(String cedulaCliente) throws SQLException {
        List<Factura> lista = new ArrayList<>();
        String sql = "{call SP_OBTENER_FACTURAS_POR_CEDULA(?)}";
        
        try (Connection conn = DatabaseConnection.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {
            stmt.setString(1, cedulaCliente);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Factura f = new Factura();
                f.setIdFactura(rs.getInt("ID_FACTURA"));
           
                f.setFecha(rs.getTimestamp("FECHA"));
                f.setSubtotal(rs.getDouble("SUBTOTAL"));
                f.setIva(rs.getDouble("IVA"));
                f.setTotal(rs.getDouble("TOTAL"));
                f.setEstado(rs.getString("ESTADO"));
                f.setMetodoPago(rs.getString("METODO"));
                lista.add(f);
            }
        }
        return lista;
    }

    /**
     * Obtiene el detalle completo de una factura.
     *
     * @param idFactura identificador de la factura
     * @return objeto Factura con el detalle completo
     * @throws SQLException si ocurre un error en la base de datos
     */
@Override
public Factura obtenerDetalleFactura(int idFactura) throws SQLException {
    Factura factura = null;
    String sql = "{call SP_OBTENER_DETALLE_FACTURA(?)}";
    
    try (Connection conn = DatabaseConnection.getConnection();
         CallableStatement stmt = conn.prepareCall(sql)) {
        stmt.setInt(1, idFactura);
        
        boolean hasResults = stmt.execute();
        
        // PRIMER ResultSet - Datos del cliente y factura
        if (hasResults) {
            ResultSet rs = stmt.getResultSet();
            if (rs.next()) {
                factura = new Factura();
                factura.setIdFactura(idFactura);
                
                // Datos de la factura - AHORA FECHA ES TIMESTAMP
                Timestamp fechaTimestamp = rs.getTimestamp("FECHA");
                if (fechaTimestamp != null) {
                    factura.setFecha(new Date(fechaTimestamp.getTime()));
                }
                factura.setSubtotal(rs.getDouble("SUBTOTAL"));
                factura.setIva(rs.getDouble("IVA"));
                factura.setTotal(rs.getDouble("TOTAL"));
                factura.setEstado(rs.getString("ESTADO"));
                factura.setMetodoPago(rs.getString("METODO_PAGO"));
                
                // Datos del cliente
                Cliente cliente = new Cliente();
                cliente.setNombre(rs.getString("NOMBRE_CLIENTE"));
                cliente.setCedula(rs.getString("CEDULA"));
                cliente.setTelefono(rs.getString("TELEFONO"));
                cliente.setDireccion(rs.getString("DIRECCION"));
                cliente.setCorreoElectronico(rs.getString("CORREO_ELECTRONICO"));
                factura.setCliente(cliente);
            }
            rs.close();
            hasResults = stmt.getMoreResults();
        }
        
        // SEGUNDO ResultSet - Servicios
        if (hasResults) {
            ResultSet rs = stmt.getResultSet();
            List<Object[]> servicios = new ArrayList<>();
            while (rs.next()) {
                servicios.add(new Object[]{
                    rs.getString("NOMBRE_SERVICIO"),
                    rs.getString("DESCRIPCION_SERVICIO"),
                    String.format("$%.2f", rs.getDouble("PRECIO_UNITARIO")),
                    String.format("$%.2f", rs.getDouble("TOTAL_SERVICIO")),
                    rs.getString("VETERINARIO")
                });
            }
            factura.setServicios(servicios);
            rs.close();
            hasResults = stmt.getMoreResults();
        }
        
        // TERCER ResultSet - Mascotas
        if (hasResults) {
            ResultSet rs = stmt.getResultSet();
            List<Object[]> mascotas = new ArrayList<>();
            while (rs.next()) {
                mascotas.add(new Object[]{
                    rs.getString("NOMBRE_MASCOTA"),
                    rs.getString("ESPECIE"),
                    rs.getString("RAZA"),
                    rs.getString("DIAGNOSTICO"),
                    rs.getString("TRATAMIENTO")
                });
            }
            factura.setMascotas(mascotas);
            rs.close();
            hasResults = stmt.getMoreResults();
        }
        
        // CUARTO ResultSet - Medicamentos
        if (hasResults) {
            ResultSet rs = stmt.getResultSet();
            List<Object[]> medicamentos = new ArrayList<>();
            while (rs.next()) {
                medicamentos.add(new Object[]{
                    rs.getString("MEDICAMENTO"),
                    rs.getString("DOSIS"),
                    rs.getString("FRECUENCIA"),
                    rs.getString("DURACION"),
                    String.format("$%.2f", rs.getDouble("PRECIO"))
                });
            }
            factura.setMedicamentos(medicamentos);
            rs.close();
            hasResults = stmt.getMoreResults();
        }
        
        // QUINTO ResultSet - Instrumentos
        if (hasResults) {
            ResultSet rs = stmt.getResultSet();
            List<Object[]> instrumentos = new ArrayList<>();
            while (rs.next()) {
                instrumentos.add(new Object[]{
                    rs.getString("INSTRUMENTO"),
                    String.format("$%.2f", rs.getDouble("COSTO_USO"))
                });
            }
            factura.setInstrumentos(instrumentos);
            rs.close();
        }
    }
    return factura;
}
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
    @Override
public int generarFacturaAtencion(int idAtencionMedica, String metodoPago, 
                                  String cuentaOrigen, String cuentaDestino) throws SQLException {
    String sql = "{call SP_GENERAR_FACTURA_ATENCION(?, ?, ?, ?)}";
    
    try (Connection conn = DatabaseConnection.getConnection();
         CallableStatement stmt = conn.prepareCall(sql)) {
        
        stmt.setInt(1, idAtencionMedica);
        stmt.setString(2, metodoPago);
        if (cuentaOrigen != null && !cuentaOrigen.isEmpty()) {
            stmt.setString(3, cuentaOrigen);
        } else {
            stmt.setNull(3, Types.VARCHAR);
        }
        if (cuentaDestino != null && !cuentaDestino.isEmpty()) {
            stmt.setString(4, cuentaDestino);
        } else {
            stmt.setNull(4, Types.VARCHAR);
        }
        
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            int resultado = rs.getInt("ID_FACTURA");
            
            // Verificar si hay mensaje de error
            try {
                String errorMsg = rs.getString("ERROR_MENSAJE");
                if (errorMsg != null && !errorMsg.isEmpty()) {
                    System.err.println("Error DB: " + errorMsg);
                    throw new SQLException("Error en base de datos: " + errorMsg);
                }
            } catch (Exception e) {
                // No hay columna ERROR_MENSAJE, ignorar
            }
            
            return resultado;
        }
        return -1;
    }
}
}