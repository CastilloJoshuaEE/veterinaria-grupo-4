package com.mycompany.veterinaria.grupo4.model.impl;

import com.mycompany.veterinaria.grupo4.model.dao.IClienteDAO;
import com.mycompany.veterinaria.grupo4.model.entity.Cliente;
import com.mycompany.veterinaria.grupo4.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementacion del DAO para la gestion de clientes.
 * <p>
 * Esta clase implementa la interfaz IClienteDAO y proporciona la logica
 * de acceso a datos para la entidad Cliente utilizando procedimientos
 * almacenados de SQL Server.
 * </p>
 * 
 * <p><b>Fecha de inicio del proyecto:</b> 15/04/2026</p>
 * 
 * @author CASTRO AVILA JONATHAN XAVIER – MODULO: CLIENTE
 * @version 1.0
 * @since 1.0
 */
public class ClienteDAOImpl implements IClienteDAO {

    /**
     * Obtiene todos los clientes registrados.
     *
     * @return lista de todos los clientes
     * @throws SQLException si ocurre un error en la base de datos
     */
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

    /**
     * Obtiene un cliente por su numero de cedula.
     *
     * @param cedula numero de cedula del cliente
     * @return objeto Cliente encontrado
     * @throws SQLException si ocurre un error en la base de datos
     */
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

    /**
     * Obtiene un cliente por su identificador.
     *
     * @param id identificador del cliente
     * @return objeto Cliente encontrado
     * @throws SQLException si ocurre un error en la base de datos
     */
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

    /**
     * Inserta un nuevo cliente en la base de datos.
     *
     * @param cliente objeto Cliente a insertar
     * @return true si la insercion fue exitosa
     * @throws SQLException si ocurre un error en la base de datos
     */
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

    /**
     * Actualiza los datos de un cliente existente.
     *
     * @param cliente objeto Cliente con datos actualizados
     * @return true si la actualizacion fue exitosa
     * @throws SQLException si ocurre un error en la base de datos
     */
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

    /**
     * Elimina un cliente de la base de datos.
     *
     * @param idCliente identificador del cliente a eliminar
     * @return true si la eliminacion fue exitosa
     * @throws SQLException si ocurre un error en la base de datos
     */
    @Override
    public boolean eliminar(int idCliente) throws SQLException {
        String sql = "{call SP_ELIMINAR_CLIENTE(?)}";
        try (Connection conn = DatabaseConnection.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {
            stmt.setInt(1, idCliente);
            return stmt.executeUpdate() > 0;
        }
    }

    /**
     * Obtiene todas las cedulas de clientes registrados.
     *
     * @return lista de cedulas
     * @throws SQLException si ocurre un error en la base de datos
     */
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

    /**
     * Busca clientes por nombre.
     *
     * @param nombre termino de busqueda
     * @return lista de clientes que coinciden con el nombre
     * @throws SQLException si ocurre un error en la base de datos
     */
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

    /**
     * Busca clientes por nombre de mascota o nombre de cliente.
     *
     * @param nombreMascota nombre de la mascota
     * @param nombreCliente nombre del cliente
     * @return lista de clientes que coinciden
     * @throws SQLException si ocurre un error en la base de datos
     */
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