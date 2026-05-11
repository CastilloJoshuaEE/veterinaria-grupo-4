package com.mycompany.veterinaria.grupo4.model.impl;

import com.mycompany.veterinaria.grupo4.model.dao.IFacturaDAO;
import com.mycompany.veterinaria.grupo4.model.entity.Cliente;
import com.mycompany.veterinaria.grupo4.model.entity.Factura;
import com.mycompany.veterinaria.grupo4.util.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FacturaDAOImpl implements IFacturaDAO {

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
                f.setIdCliente(rs.getInt("ID_CLIENTE"));
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

    @Override
    public Factura obtenerDetalleFactura(int idFactura) throws SQLException {
        Factura factura = null;
        String sql = "{call SP_OBTENER_DETALLE_FACTURA(?)}";
        
        try (Connection conn = DatabaseConnection.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {
            stmt.setInt(1, idFactura);
            ResultSet rs = stmt.executeQuery();
            
            // Primer ResultSet - Datos del cliente y factura
            if (rs.next()) {
                factura = new Factura();
                factura.setIdFactura(idFactura);
                
                Cliente cliente = new Cliente();
                cliente.setNombre(rs.getString("NOMBRE_CLIENTE"));
                cliente.setCedula(rs.getString("CEDULA"));
                cliente.setTelefono(rs.getString("TELEFONO"));
                cliente.setDireccion(rs.getString("DIRECCION"));
                cliente.setCorreoElectronico(rs.getString("CORREO_ELECTRONICO"));
                factura.setCliente(cliente);
                factura.setFecha(rs.getTimestamp("FECHA"));
                factura.setSubtotal(rs.getDouble("SUBTOTAL"));
                factura.setIva(rs.getDouble("IVA"));
                factura.setTotal(rs.getDouble("TOTAL"));
                factura.setEstado(rs.getString("ESTADO"));
                factura.setMetodoPago(rs.getString("METODO"));
            }
            
            // Segundo ResultSet - Servicios
            if (stmt.getMoreResults()) {
                rs = stmt.getResultSet();
                List<Object[]> servicios = new ArrayList<>();
                while (rs.next()) {
                    servicios.add(new Object[]{
                        rs.getString("NOMBRE_SERVICIO"),
                        rs.getString("DESCRIPCION"),
                        String.format("$%.2f", rs.getDouble("PRECIO_UNITARIO")),
                        String.format("$%.2f", rs.getDouble("TOTAL_SERVICIO")),
                        rs.getString("VETERINARIO")
                    });
                }
                factura.setServicios(servicios);
            }
            
            // Tercer ResultSet - Mascotas
            if (stmt.getMoreResults()) {
                rs = stmt.getResultSet();
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
            }
            
            // Cuarto ResultSet - Medicamentos
            if (stmt.getMoreResults()) {
                rs = stmt.getResultSet();
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
            }
            
            // Quinto ResultSet - Instrumentos
            if (stmt.getMoreResults()) {
                rs = stmt.getResultSet();
                List<Object[]> instrumentos = new ArrayList<>();
                while (rs.next()) {
                    instrumentos.add(new Object[]{
                        rs.getString("INSTRUMENTO"),
                        String.format("$%.2f", rs.getDouble("COSTO_USO"))
                    });
                }
                factura.setInstrumentos(instrumentos);
            }
            
            // Sexto ResultSet - Vacunas
            if (stmt.getMoreResults()) {
                rs = stmt.getResultSet();
                List<Object[]> vacunas = new ArrayList<>();
                while (rs.next()) {
                    vacunas.add(new Object[]{
                        rs.getString("NOMBRE_VACUNA"),
                        rs.getDate("FECHA_APLICACION"),
                        rs.getDate("FECHA_PROXIMA"),
                        rs.getString("DESCRIPCION")
                    });
                }
                factura.setVacunas(vacunas);
            }
        }
        return factura;
    }

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
                return rs.getInt("ID_FACTURA");
            }
            return -1;
        }
    }
}