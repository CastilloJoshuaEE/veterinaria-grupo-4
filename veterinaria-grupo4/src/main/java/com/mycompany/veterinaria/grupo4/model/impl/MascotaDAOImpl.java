/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.veterinaria.grupo4.model.impl;

import com.mycompany.veterinaria.grupo4.model.dao.IMascotaDAO;
import com.mycompany.veterinaria.grupo4.model.entity.Mascota;
import com.mycompany.veterinaria.grupo4.util.DatabaseConnection;
import com.mycompany.veterinaria.grupo4.util.Parametro;

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
}