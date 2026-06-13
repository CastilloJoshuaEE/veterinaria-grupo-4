package com.mycompany.veterinaria.grupo4.model.impl;

import com.mycompany.veterinaria.grupo4.api.dto.FichaMedicaDTO;
import com.mycompany.veterinaria.grupo4.model.dao.IMascotaDAO;
import com.mycompany.veterinaria.grupo4.model.entity.Mascota;
import com.mycompany.veterinaria.grupo4.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementacion del DAO para la gestion de mascotas.
 * <p>
 * Esta clase implementa la interfaz IMascotaDAO y proporciona la logica
 * de acceso a datos para la entidad Mascota utilizando procedimientos
 * almacenados de SQL Server. Permite operaciones CRUD completas,
 * busqueda de mascotas, gestion de fotos y fichas medicas.
 * </p>
 * 
 * <p><b>Fecha de inicio del proyecto:</b> 15/04/2026</p>
 * 
 * @author CASTILLO MEREJILDO JOSHUA JAVIER – MODULO: MASCOTA
 * @version 1.0
 * @since 1.0
 */
public class MascotaDAOImpl implements IMascotaDAO {

    /**
     * Obtiene las mascotas asociadas a un cliente.
     *
     * @param idCliente identificador del cliente
     * @return lista de mascotas del cliente
     * @throws SQLException si ocurre un error en la base de datos
     */
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

    /**
     * Obtiene una mascota por su identificador.
     *
     * @param idMascota identificador de la mascota
     * @return objeto Mascota encontrado
     * @throws SQLException si ocurre un error en la base de datos
     */
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

    /**
     * Inserta una nueva mascota en la base de datos.
     *
     * @param mascota objeto Mascota a insertar
     * @return ID generado para la mascota
     * @throws SQLException si ocurre un error en la base de datos
     */
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

    /**
     * Actualiza los datos de una mascota existente.
     *
     * @param mascota objeto Mascota con datos actualizados
     * @return true si la actualizacion fue exitosa
     * @throws SQLException si ocurre un error en la base de datos
     */
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

    /**
     * Elimina una mascota de la base de datos.
     *
     * @param idMascota identificador de la mascota a eliminar
     * @return true si la eliminacion fue exitosa
     * @throws SQLException si ocurre un error en la base de datos
     */
    @Override
    public boolean eliminar(int idMascota) throws SQLException {
        String sql = "{call SP_ELIMINAR_MASCOTA(?)}";
        try (Connection conn = DatabaseConnection.getConnection();
            CallableStatement stmt = conn.prepareCall(sql)) {
            stmt.setInt(1, idMascota);
            // Ejecutar sin esperar resultset
            boolean result = stmt.execute();
            // Si no hubo excepción, la eliminación fue exitosa
            return true;
        }
    }

    /**
     * Obtiene la foto de una mascota.
     *
     * @param idMascota identificador de la mascota
     * @return arreglo de bytes con la imagen
     * @throws SQLException si ocurre un error en la base de datos
     */
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
    
    /**
     * Lista todas las mascotas registradas.
     *
     * @return lista de todas las mascotas
     * @throws SQLException si ocurre un error en la base de datos
     */
    @Override
    public List<Mascota> listarTodo() throws SQLException {
        List<Mascota> mascotas = new ArrayList<>();
        String sql = "{call SP_OBTENER_MASCOTAS}";

        try (Connection conn = DatabaseConnection.getConnection();
             CallableStatement stmt = conn.prepareCall(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Mascota m = new Mascota();
                m.setIdMascota(rs.getInt("ID_MASCOTA"));
                m.setIdCliente(rs.getInt("ID_CLIENTE"));
                m.setNombre(rs.getString("NOMBRE"));
                m.setEspecie(rs.getString("ESPECIE"));
                m.setRaza(rs.getString("RAZA"));

                String sexoStr = rs.getString("SEXO");
                if (sexoStr != null && !sexoStr.isEmpty()) {
                    m.setSexo(sexoStr.charAt(0));
                }

                m.setFechaNacimiento(rs.getDate("FECHA_NACIMIENTO"));

                double peso = rs.getDouble("PESO");
                if (!rs.wasNull()) {
                    m.setPeso(peso);
                }

                m.setColor(rs.getString("COLOR"));
                m.setFoto(rs.getBytes("FOTO")); 
                m.setFechaRegistro(rs.getTimestamp("FECHA_REGISTRO"));
                mascotas.add(m);
            }
        }
        return mascotas;
    }
    
    /**
     * Busca mascotas por termino (nombre o cedula del dueño).
     *
     * @param termino termino de busqueda
     * @return lista de mascotas que coinciden
     * @throws SQLException si ocurre un error en la base de datos
     */
    @Override
    public List<Mascota> buscarMascotas(String termino) throws SQLException {
        List<Mascota> lista = new ArrayList<>();
        String sql = "{call SP_BUSCAR_MASCOTAS(?)}";

        try (Connection conn = DatabaseConnection.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {

            stmt.setString(1, termino);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Mascota m = new Mascota();
                    m.setIdMascota(rs.getInt("ID_MASCOTA"));
                    m.setIdCliente(rs.getInt("ID_CLIENTE"));
                    m.setNombre(rs.getString("NOMBRE"));
                    m.setEspecie(rs.getString("ESPECIE"));
                    m.setRaza(rs.getString("RAZA"));

                    String sexoStr = rs.getString("SEXO");
                    if (sexoStr != null && !sexoStr.isEmpty()) {
                        m.setSexo(sexoStr.charAt(0));
                    }

                    m.setFechaNacimiento(rs.getDate("FECHA_NACIMIENTO"));

                    double peso = rs.getDouble("PESO");
                    if (!rs.wasNull()) {
                        m.setPeso(peso);
                    }

                    m.setColor(rs.getString("COLOR"));
                    m.setFoto(rs.getBytes("FOTO"));
                    m.setFechaRegistro(rs.getTimestamp("FECHA_REGISTRO"));
                    lista.add(m);
                }
            }
        }
        return lista;
    }
    
    /**
     * Actualiza la ficha medica de una mascota.
     *
     * @param idMascota identificador de la mascota
     * @param alergias alergias de la mascota
     * @param enfermedadesCronicas enfermedades cronicas
     * @param observaciones observaciones adicionales
     * @return true si la actualizacion fue exitosa
     * @throws SQLException si ocurre un error en la base de datos
     */
    @Override
    public boolean actualizarFichaMedica(int idMascota, String alergias, String enfermedadesCronicas, String observaciones) throws SQLException {
        String sql = "{call SP_ACTUALIZAR_FICHA_MEDICA(?, ?, ?, ?)}";
        try (Connection conn = DatabaseConnection.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {
            stmt.setInt(1, idMascota);
            stmt.setString(2, alergias);
            stmt.setString(3, enfermedadesCronicas);
            stmt.setString(4, observaciones);
            
            boolean hadResults = stmt.execute();
            return !hadResults;
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * Obtiene la ficha medica de una mascota como DTO.
     *
     * @param idMascota identificador de la mascota
     * @return objeto FichaMedicaDTO con los datos
     * @throws SQLException si ocurre un error en la base de datos
     */
    @Override
    public FichaMedicaDTO obtenerFichaMedicaDTO(int idMascota) throws SQLException {
        String sql = "{call SP_OBTENER_FICHA_MEDICA(?)}";
        try (Connection conn = DatabaseConnection.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {
            stmt.setInt(1, idMascota);
            ResultSet rs = stmt.executeQuery();
            
            FichaMedicaDTO ficha = new FichaMedicaDTO();
            ficha.setIdMascota(idMascota);
            
            if (rs.next()) {
                ficha.setAlergias(rs.getString("ALERGIAS"));
                ficha.setEnfermedadesCronicas(rs.getString("ENFERMEDADES_CRONICAS"));
                ficha.setObservaciones(rs.getString("OBSERVACIONES"));
            }
            return ficha;
        }
    }
}