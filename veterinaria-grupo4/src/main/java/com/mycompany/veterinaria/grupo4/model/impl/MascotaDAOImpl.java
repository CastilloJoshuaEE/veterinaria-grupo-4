package com.mycompany.veterinaria.grupo4.model.impl;

import com.mycompany.veterinaria.grupo4.model.dao.IMascotaDAO;
import com.mycompany.veterinaria.grupo4.model.entity.Mascota;
import com.mycompany.veterinaria.grupo4.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MascotaDAOImpl implements IMascotaDAO {

    @Override
    public List<Mascota> obtenerPorCliente(int idCliente) throws SQLException {
        List<Mascota> mascotas = new ArrayList<>();
        String sql = "{call SP_OBTENER_MASCOTAS_POR_CLIENTE(?)}";
        try (Connection conn = DatabaseConnection.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {
            stmt.setInt(1, idCliente);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Mascota m = new Mascota();
                m.setIdMascota(rs.getInt("ID_MASCOTA"));
                m.setIdCliente(rs.getInt("ID_CLIENTE"));
                m.setNombre(rs.getString("NOMBRE"));
                m.setEspecie(rs.getString("ESPECIE"));
                m.setRaza(rs.getString("RAZA"));
                if (rs.getString("SEXO") != null && rs.getString("SEXO").length() > 0) {
                    m.setSexo(rs.getString("SEXO").charAt(0));
                }
                m.setFechaNacimiento(rs.getDate("FECHA_NACIMIENTO"));
                m.setPeso(rs.getDouble("PESO"));
                m.setColor(rs.getString("COLOR"));
                m.setFechaRegistro(rs.getTimestamp("FECHA_REGISTRO"));
                mascotas.add(m);
            }
        }
        return mascotas;
    }

    @Override
    public Mascota obtenerPorId(int idMascota) throws SQLException {
        String sql = "{call SP_OBTENER_MASCOTA_POR_ID(?)}";
        try (Connection conn = DatabaseConnection.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {
            stmt.setInt(1, idMascota);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Mascota m = new Mascota();
                m.setIdMascota(rs.getInt("ID_MASCOTA"));
                m.setIdCliente(rs.getInt("ID_CLIENTE"));
                m.setNombre(rs.getString("NOMBRE"));
                m.setEspecie(rs.getString("ESPECIE"));
                m.setRaza(rs.getString("RAZA"));
                if (rs.getString("SEXO") != null && rs.getString("SEXO").length() > 0) {
                    m.setSexo(rs.getString("SEXO").charAt(0));
                }
                m.setFechaNacimiento(rs.getDate("FECHA_NACIMIENTO"));
                m.setPeso(rs.getDouble("PESO"));
                m.setColor(rs.getString("COLOR"));
                m.setFechaRegistro(rs.getTimestamp("FECHA_REGISTRO"));
                return m;
            }
            return null;
        }
    }

    @Override
    public int insertar(Mascota mascota) throws SQLException {
        String sql = "{call SP_INSERTAR_MASCOTA(?, ?, ?, ?, ?, ?, ?, ?, ?)}";
        try (Connection conn = DatabaseConnection.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {
            stmt.setInt(1, mascota.getIdCliente());
            stmt.setString(2, mascota.getNombre());
            stmt.setString(3, mascota.getEspecie());
            stmt.setString(4, mascota.getRaza());
            stmt.setString(5, String.valueOf(mascota.getSexo()));
            stmt.setDate(6, mascota.getFechaNacimiento() != null ? new java.sql.Date(mascota.getFechaNacimiento().getTime()) : null);
            stmt.setDouble(7, mascota.getPeso() != null ? mascota.getPeso() : 0);
            stmt.setString(8, mascota.getColor());
            stmt.setBytes(9, mascota.getFoto());
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("ID_MASCOTA");
            }
            return -1;
        }
    }

    @Override
    public boolean actualizar(Mascota mascota) throws SQLException {
        String sql = "{call SP_ACTUALIZAR_MASCOTA(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}";
        try (Connection conn = DatabaseConnection.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {
            stmt.setInt(1, mascota.getIdMascota());
            stmt.setInt(2, mascota.getIdCliente());
            stmt.setString(3, mascota.getNombre());
            stmt.setString(4, mascota.getEspecie());
            stmt.setString(5, mascota.getRaza());
            stmt.setString(6, String.valueOf(mascota.getSexo()));
            stmt.setDate(7, mascota.getFechaNacimiento() != null ? new java.sql.Date(mascota.getFechaNacimiento().getTime()) : null);
            stmt.setDouble(8, mascota.getPeso() != null ? mascota.getPeso() : 0);
            stmt.setString(9, mascota.getColor());
            stmt.setBytes(10, mascota.getFoto());
            return stmt.executeUpdate() > 0;
        }
    }

    @Override
    public boolean eliminar(int idMascota) throws SQLException {
        String sql = "{call SP_ELIMINAR_MASCOTA(?)}";
        try (Connection conn = DatabaseConnection.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {
            stmt.setInt(1, idMascota);
            return stmt.executeUpdate() > 0;
        }
    }

    @Override
    public byte[] obtenerFoto(int idMascota) throws SQLException {
        String sql = "{call SP_OBTENER_FOTO_MASCOTA(?)}";
        try (Connection conn = DatabaseConnection.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {
            stmt.setInt(1, idMascota);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getBytes("FOTO");
            }
            return null;
        }
    }
    
    @Override
    public List<Mascota> listarTodo() throws SQLException {
        List<Mascota> mascotas = new ArrayList<>();
        // Usamos el SP que definimos para el listado general
        String sql = "{call SP_OBTENER_MASCOTAS}";

        try (Connection conn = DatabaseConnection.getConnection();
             CallableStatement stmt = conn.prepareCall(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Mascota m = new Mascota();
                // Datos básicos de la mascota
                m.setIdMascota(rs.getInt("ID_MASCOTA"));
                m.setIdCliente(rs.getInt("ID_CLIENTE"));
                m.setNombre(rs.getString("NOMBRE"));
                m.setEspecie(rs.getString("ESPECIE"));
                m.setRaza(rs.getString("RAZA"));

                // Manejo del char SEXO
                String sexoStr = rs.getString("SEXO");
                if (sexoStr != null && !sexoStr.isEmpty()) {
                    m.setSexo(sexoStr.charAt(0));
                }

                m.setFechaNacimiento(rs.getDate("FECHA_NACIMIENTO"));

                // Manejo de Double para evitar el 0.0 si es nulo en DB
                double peso = rs.getDouble("PESO");
                if (!rs.wasNull()) {
                    m.setPeso(peso);
                }

                m.setColor(rs.getString("COLOR"));

                // ── CRÍTICO: La foto para tu ModelProfile ──
                m.setFoto(rs.getBytes("FOTO")); 

                m.setFechaRegistro(rs.getTimestamp("FECHA_REGISTRO"));

                mascotas.add(m);
            }
        }
        return mascotas;
    }
    
    @Override
    public List<Mascota> buscarMascotas(String termino) throws SQLException {
        List<Mascota> lista = new ArrayList<>();
        // Llamada al nuevo SP de búsqueda dinámica
        String sql = "{call SP_BUSCAR_MASCOTAS(?)}";

        try (Connection conn = DatabaseConnection.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {

            // Seteamos el término de búsqueda (nombre, cédula, etc.)
            stmt.setString(1, termino);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Mascota m = new Mascota();

                    // Mapeo de datos básicos
                    m.setIdMascota(rs.getInt("ID_MASCOTA"));
                    m.setIdCliente(rs.getInt("ID_CLIENTE"));
                    m.setNombre(rs.getString("NOMBRE"));
                    m.setEspecie(rs.getString("ESPECIE"));
                    m.setRaza(rs.getString("RAZA"));

                    // Manejo del tipo char para SEXO
                    String sexoStr = rs.getString("SEXO");
                    if (sexoStr != null && !sexoStr.isEmpty()) {
                        m.setSexo(sexoStr.charAt(0));
                    }

                    m.setFechaNacimiento(rs.getDate("FECHA_NACIMIENTO"));

                    // Manejo de Double para evitar errores con valores nulos
                    double peso = rs.getDouble("PESO");
                    if (!rs.wasNull()) {
                        m.setPeso(peso);
                    }

                    m.setColor(rs.getString("COLOR"));

                    // ── CRÍTICO: Obtenemos la FOTO para el ModelProfile ──
                    m.setFoto(rs.getBytes("FOTO"));

                    m.setFechaRegistro(rs.getTimestamp("FECHA_REGISTRO"));

                    // Opcional: Si su objeto Mascota tiene campos para el nombre del dueño
                    // m.setNombreDueno(rs.getString("NOMBRE_CLIENTE"));

                    lista.add(m);
                }
            }
        }
        return lista;
    }
}