package com.mycompany.veterinaria.grupo4.model.impl;

import com.mycompany.veterinaria.grupo4.model.dao.IVeterinarioDAO;
import com.mycompany.veterinaria.grupo4.model.entity.EspecialidadVeterinaria;
import com.mycompany.veterinaria.grupo4.model.entity.Veterinario;
import com.mycompany.veterinaria.grupo4.util.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VeterinarioDAOImpl implements IVeterinarioDAO {

    @Override
    public List<Veterinario> obtenerTodos() throws SQLException {
        List<Veterinario> lista = new ArrayList<>();
        String sql = "{call SP_OBTENER_VETERINARIOS}";
        
        try (Connection conn = DatabaseConnection.getConnection();
             CallableStatement stmt = conn.prepareCall(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                Veterinario v = new Veterinario();
                v.setIdVeterinario(rs.getInt("ID_VETERINARIO"));
                v.setCedula(rs.getString("CEDULA"));
                v.setNombre(rs.getString("NOMBRE"));
                v.setApellido(rs.getString("APELLIDO"));
                v.setTelefono(rs.getString("TELEFONO"));
                v.setPagoMensual(rs.getDouble("PAGO_MENSUAL"));
                v.setDireccion(rs.getString("DIRECCION"));
                v.setCorreoElectronico(rs.getString("CORREO_ELECTRONICO"));
                v.setFechaRegistro(rs.getTimestamp("FECHA_REGISTRO"));
                lista.add(v);
            }
        }
        return lista;
    }

    @Override
    public Veterinario obtenerPorCedula(String cedula) throws SQLException {
        String sql = "{call SP_OBTENER_VETERINARIO_POR_CEDULA(?)}";
        
        try (Connection conn = DatabaseConnection.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {
            stmt.setString(1, cedula);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                Veterinario v = new Veterinario();
                v.setIdVeterinario(rs.getInt("ID_VETERINARIO"));
                v.setCedula(rs.getString("CEDULA"));
                v.setNombre(rs.getString("NOMBRE"));
                v.setApellido(rs.getString("APELLIDO"));
                v.setTelefono(rs.getString("TELEFONO"));
                v.setPagoMensual(rs.getDouble("PAGO_MENSUAL"));
                v.setDireccion(rs.getString("DIRECCION"));
                v.setCorreoElectronico(rs.getString("CORREO_ELECTRONICO"));
                v.setFechaRegistro(rs.getTimestamp("FECHA_REGISTRO"));
                return v;
            }
            return null;
        }
    }

    @Override
    public Veterinario obtenerPorId(int id) throws SQLException {
        String sql = "{call SP_OBTENER_VETERINARIO_POR_ID(?)}";
        
        try (Connection conn = DatabaseConnection.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                Veterinario v = new Veterinario();
                v.setIdVeterinario(rs.getInt("ID_VETERINARIO"));
                v.setCedula(rs.getString("CEDULA"));
                v.setNombre(rs.getString("NOMBRE"));
                v.setApellido(rs.getString("APELLIDO"));
                v.setTelefono(rs.getString("TELEFONO"));
                v.setPagoMensual(rs.getDouble("PAGO_MENSUAL"));
                v.setDireccion(rs.getString("DIRECCION"));
                v.setCorreoElectronico(rs.getString("CORREO_ELECTRONICO"));
                v.setFechaRegistro(rs.getTimestamp("FECHA_REGISTRO"));
                return v;
            }
            return null;
        }
    }

    @Override
    public boolean insertar(Veterinario veterinario) throws SQLException {
        String sql = "{call SP_INSERTAR_VETERINARIO(?, ?, ?, ?, ?, ?, ?, ?)}";
        
        try (Connection conn = DatabaseConnection.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {
            stmt.setString(1, veterinario.getCedula());
            stmt.setString(2, veterinario.getNombre());
            stmt.setString(3, veterinario.getApellido());
            stmt.setString(4, veterinario.getTelefono());
            stmt.setInt(5, veterinario.getEspecialidad().getIdEspecialidad());
            stmt.setDouble(6, veterinario.getPagoMensual());
            stmt.setString(7, veterinario.getDireccion());
            stmt.setString(8, veterinario.getCorreoElectronico());
            
            ResultSet rs = stmt.executeQuery();
            return rs.next() && rs.getInt("ID_VETERINARIO") > 0;
        }
    }

    @Override
    public boolean actualizar(Veterinario veterinario) throws SQLException {
        String sql = "{call SP_ACTUALIZAR_VETERINARIO(?, ?, ?, ?, ?, ?, ?, ?, ?)}";
        
        try (Connection conn = DatabaseConnection.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {
            stmt.setInt(1, veterinario.getIdVeterinario());
            stmt.setString(2, veterinario.getCedula());
            stmt.setString(3, veterinario.getNombre());
            stmt.setString(4, veterinario.getApellido());
            stmt.setString(5, veterinario.getTelefono());
            stmt.setInt(6, veterinario.getEspecialidad().getIdEspecialidad());
            stmt.setDouble(7, veterinario.getPagoMensual());
            stmt.setString(8, veterinario.getDireccion());
            stmt.setString(9, veterinario.getCorreoElectronico());
            
            ResultSet rs = stmt.executeQuery();
            return rs.next() && rs.getInt("FILAS_AFECTADAS") > 0;
        }
    }

    @Override
    public boolean eliminar(int idVeterinario) throws SQLException {
        String sql = "{call SP_ELIMINAR_VETERINARIO(?)}";
        
        try (Connection conn = DatabaseConnection.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {
            stmt.setInt(1, idVeterinario);
            return stmt.execute();
        }
    }

    @Override
    public List<Veterinario> buscarPorNombre(String nombre) throws SQLException {
        List<Veterinario> lista = new ArrayList<>();
        String sql = "{call SP_OBTENER_VETERINARIOS_POR_NOMBRE(?)}";
        
        try (Connection conn = DatabaseConnection.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {
            stmt.setString(1, "%" + nombre + "%");
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Veterinario v = new Veterinario();
                v.setIdVeterinario(rs.getInt("ID_VETERINARIO"));
                v.setCedula(rs.getString("CEDULA"));
                v.setNombre(rs.getString("NOMBRE"));
                v.setApellido(rs.getString("APELLIDO"));
                v.setTelefono(rs.getString("TELEFONO"));
                v.setPagoMensual(rs.getDouble("PAGO_MENSUAL"));
                v.setDireccion(rs.getString("DIRECCION"));
                v.setCorreoElectronico(rs.getString("CORREO_ELECTRONICO"));
                lista.add(v);
            }
        }
        return lista;
    }

    @Override
    public List<Veterinario> buscarPorEspecialidad(String especialidad) throws SQLException {
        List<Veterinario> lista = new ArrayList<>();
        String sql = "{call SP_OBTENER_VETERINARIOS_POR_ESPECIALIDAD(?)}";
        
        try (Connection conn = DatabaseConnection.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {
            stmt.setString(1, "%" + especialidad + "%");
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Veterinario v = new Veterinario();
                v.setIdVeterinario(rs.getInt("ID_VETERINARIO"));
                v.setCedula(rs.getString("CEDULA"));
                v.setNombre(rs.getString("NOMBRE"));
                v.setApellido(rs.getString("APELLIDO"));
                v.setTelefono(rs.getString("TELEFONO"));
                v.setPagoMensual(rs.getDouble("PAGO_MENSUAL"));
                v.setDireccion(rs.getString("DIRECCION"));
                v.setCorreoElectronico(rs.getString("CORREO_ELECTRONICO"));
                lista.add(v);
            }
        }
        return lista;
    }

    @Override
    public List<Veterinario> obtenerPorServicio(int idServicio) throws SQLException {
        List<Veterinario> lista = new ArrayList<>();
        String sql = "{call SP_OBTENER_VETERINARIOS_POR_SERVICIO(?)}";
        
        try (Connection conn = DatabaseConnection.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {
            stmt.setInt(1, idServicio);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Veterinario v = new Veterinario();
                v.setIdVeterinario(rs.getInt("ID_VETERINARIO"));
                v.setNombre(rs.getString("NOMBRE"));
                v.setApellido(rs.getString("APELLIDO"));
                lista.add(v);
            }
        }
        return lista;
    }
    
    @Override
    public List<Veterinario> buscar(String termino) throws SQLException {
        List<Veterinario> lista = new ArrayList<>();
        String sql = "{call SP_BUSCAR_VETERINARIOS(?)}";

        try (Connection conn = DatabaseConnection.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {

            stmt.setString(1, termino);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Veterinario v = new Veterinario();
                    v.setIdVeterinario(rs.getInt("ID_VETERINARIO"));
                    v.setCedula(rs.getString("CEDULA"));
                    v.setNombre(rs.getString("NOMBRE"));
                    v.setApellido(rs.getString("APELLIDO"));
                    v.setTelefono(rs.getString("TELEFONO"));
                    v.setCorreoElectronico(rs.getString("CORREO_ELECTRONICO"));

                    int idEspecialidad = rs.getInt("ID_ESPECIALIDAD");
                    if (!rs.wasNull()) {
                        EspecialidadVeterinaria especialidad = new EspecialidadVeterinaria();
                        especialidad.setIdEspecialidad(idEspecialidad);
                        especialidad.setNombreEspecialidad(rs.getString("ESPECIALIDAD"));
                        v.setEspecialidad(especialidad); 
                    }
                    lista.add(v);
                }
            }
        }
        return lista;
    }
}