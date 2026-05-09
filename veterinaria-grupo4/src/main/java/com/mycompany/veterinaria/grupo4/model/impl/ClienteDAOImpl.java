package com.mycompany.veterinaria.grupo4.model.impl;

import com.mycompany.veterinaria.grupo4.model.dao.IClienteDAO;
import com.mycompany.veterinaria.grupo4.model.entity.Cliente;
import com.mycompany.veterinaria.grupo4.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClienteDAOImpl implements IClienteDAO {

    @Override
    public List<Cliente> obtenerTodos() throws SQLException {
        List<Cliente> clientes = new ArrayList<>();
        String sql = "{call SP_OBTENER_CLIENTES}";
        try (Connection conn = DatabaseConnection.getConnection();
             CallableStatement stmt = conn.prepareCall(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Cliente c = new Cliente();
                c.setIdCliente(rs.getInt("ID_CLIENTE"));
                c.setCedula(rs.getString("CEDULA"));
                c.setNombre(rs.getString("NOMBRE"));
                c.setApellido(rs.getString("APELLIDO"));
                c.setTelefono(rs.getString("TELEFONO"));
                c.setDireccion(rs.getString("DIRECCION"));
                c.setCorreoElectronico(rs.getString("CORREO_ELECTRONICO"));
                c.setFechaRegistro(rs.getTimestamp("FECHA_REGISTRO"));
                clientes.add(c);
            }
        }
        return clientes;
    }

    @Override
    public Cliente obtenerPorCedula(String cedula) throws SQLException {
        String sql = "{call SP_OBTENER_CLIENTE_POR_CEDULA(?)}";
        try (Connection conn = DatabaseConnection.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {
            stmt.setString(1, cedula);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Cliente c = new Cliente();
                c.setIdCliente(rs.getInt("ID_CLIENTE"));
                c.setCedula(rs.getString("CEDULA"));
                c.setNombre(rs.getString("NOMBRE"));
                c.setApellido(rs.getString("APELLIDO"));
                c.setTelefono(rs.getString("TELEFONO"));
                c.setDireccion(rs.getString("DIRECCION"));
                c.setCorreoElectronico(rs.getString("CORREO_ELECTRONICO"));
                c.setFechaRegistro(rs.getTimestamp("FECHA_REGISTRO"));
                return c;
            }
            return null;
        }
    }

    @Override
    public Cliente obtenerPorId(int id) throws SQLException {
        String sql = "{call SP_OBTENER_CLIENTE_POR_ID(?)}";
        try (Connection conn = DatabaseConnection.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Cliente c = new Cliente();
                c.setIdCliente(rs.getInt("ID_CLIENTE"));
                c.setCedula(rs.getString("CEDULA"));
                c.setNombre(rs.getString("NOMBRE"));
                c.setApellido(rs.getString("APELLIDO"));
                c.setTelefono(rs.getString("TELEFONO"));
                c.setDireccion(rs.getString("DIRECCION"));
                c.setCorreoElectronico(rs.getString("CORREO_ELECTRONICO"));
                c.setFechaRegistro(rs.getTimestamp("FECHA_REGISTRO"));
                return c;
            }
            return null;
        }
    }

    @Override
    public boolean insertar(Cliente cliente) throws SQLException {
        String sql = "{call SP_INSERTAR_CLIENTE(?, ?, ?, ?, ?, ?)}";
        try (Connection conn = DatabaseConnection.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {
            stmt.setString(1, cliente.getCedula());
            stmt.setString(2, cliente.getNombre());
            stmt.setString(3, cliente.getApellido());
            stmt.setString(4, cliente.getTelefono());
            stmt.setString(5, cliente.getDireccion());
            stmt.setString(6, cliente.getCorreoElectronico());
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int id = rs.getInt("ID_CLIENTE");
                if (id > 0) {
                    cliente.setIdCliente(id);
                    return true;
                }
            }
            return false;
        }
    }

    @Override
    public boolean actualizar(Cliente cliente) throws SQLException {
        String sql = "{call SP_ACTUALIZAR_CLIENTE(?, ?, ?, ?, ?, ?, ?)}";
        try (Connection conn = DatabaseConnection.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {
            stmt.setInt(1, cliente.getIdCliente());
            stmt.setString(2, cliente.getCedula());
            stmt.setString(3, cliente.getNombre());
            stmt.setString(4, cliente.getApellido());
            stmt.setString(5, cliente.getTelefono());
            stmt.setString(6, cliente.getDireccion());
            stmt.setString(7, cliente.getCorreoElectronico());
            ResultSet rs = stmt.executeQuery();
            return rs.next() && rs.getInt("FILAS_AFECTADAS") > 0;
        }
    }

    @Override
    public boolean eliminar(int idCliente) throws SQLException {
        String sql = "{call SP_ELIMINAR_CLIENTE(?)}";
        try (Connection conn = DatabaseConnection.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {
            stmt.setInt(1, idCliente);
            return stmt.executeUpdate() > 0;
        }
    }

    @Override
    public List<String> obtenerCedulas() throws SQLException {
        List<String> cedulas = new ArrayList<>();
        String sql = "{call SP_OBTENER_CEDULAS_CLIENTES}";
        try (Connection conn = DatabaseConnection.getConnection();
             CallableStatement stmt = conn.prepareCall(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                cedulas.add(rs.getString("CEDULA"));
            }
        }
        return cedulas;
    }

    @Override
    public List<Cliente> buscarPorNombre(String nombre) throws SQLException {
        List<Cliente> clientes = new ArrayList<>();
        String sql = "{call SP_OBTENER_CLIENTES_POR_NOMBRE(?)}";
        try (Connection conn = DatabaseConnection.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {
            stmt.setString(1, "%" + nombre + "%");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Cliente c = new Cliente();
                c.setIdCliente(rs.getInt("ID_CLIENTE"));
                c.setCedula(rs.getString("CEDULA"));
                c.setNombre(rs.getString("NOMBRE"));
                c.setApellido(rs.getString("APELLIDO"));
                c.setTelefono(rs.getString("TELEFONO"));
                c.setDireccion(rs.getString("DIRECCION"));
                c.setCorreoElectronico(rs.getString("CORREO_ELECTRONICO"));
                c.setFechaRegistro(rs.getTimestamp("FECHA_REGISTRO"));
                clientes.add(c);
            }
        }
        return clientes;
    }

    @Override
    public List<Cliente> buscarPorMascota(String nombreMascota, String nombreCliente) throws SQLException {
        List<Cliente> clientes = new ArrayList<>();
        String sql = "{call SP_OBTENER_CLIENTES_POR_MASCOTA(?, ?)}";
        try (Connection conn = DatabaseConnection.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {
            stmt.setString(1, "%" + nombreMascota + "%");
            stmt.setString(2, nombreCliente != null ? "%" + nombreCliente + "%" : null);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Cliente c = new Cliente();
                c.setIdCliente(rs.getInt("ID_CLIENTE"));
                c.setCedula(rs.getString("CEDULA"));
                c.setNombre(rs.getString("NOMBRE"));
                c.setApellido(rs.getString("APELLIDO"));
                c.setTelefono(rs.getString("TELEFONO"));
                c.setDireccion(rs.getString("DIRECCION"));
                c.setCorreoElectronico(rs.getString("CORREO_ELECTRONICO"));
                c.setFechaRegistro(rs.getTimestamp("FECHA_REGISTRO"));
                clientes.add(c);
            }
        }
        return clientes;
    }
}