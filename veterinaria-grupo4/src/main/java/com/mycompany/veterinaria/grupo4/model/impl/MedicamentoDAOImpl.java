package com.mycompany.veterinaria.grupo4.model.impl;

import com.mycompany.veterinaria.grupo4.model.dao.IMedicamentoDAO;
import com.mycompany.veterinaria.grupo4.model.entity.Medicamento;
import com.mycompany.veterinaria.grupo4.util.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementacion del DAO para la gestion de medicamentos.
 * <p>
 * Esta clase implementa la interfaz IMedicamentoDAO y proporciona la logica
 * de acceso a datos para la entidad Medicamento utilizando procedimientos
 * almacenados. Permite obtener medicamentos disponibles,
 * buscar por ID, registrar recetas y listar medicamentos recetados.
 * </p>
 * 
 * <p><b>Fecha de inicio del proyecto:</b> 15/04/2026</p>
 * 
 * @author ROBLES MORALES JUAN ANDRES – MODULO: ATENCION VETERINARIA
 * @version 2.0 (Compatibilidad con MySQL)
 * @since 1.0
 */
public class MedicamentoDAOImpl implements IMedicamentoDAO {

    /**
     * Obtiene los medicamentos disponibles (estado activo).
     * Para MySQL usa SP_OBTENER_MEDICAMENTOS_DISPONIBLES
     *
     * @return lista de medicamentos disponibles
     * @throws SQLException si ocurre un error en la base de datos
     */
    @Override
    public List<Medicamento> obtenerDisponibles() throws SQLException {
        List<Medicamento> lista = new ArrayList<>();
        String sql;
        
        if (DatabaseConnection.isMySQL()) {
            sql = "CALL SP_OBTENER_MEDICAMENTOS_DISPONIBLES()";
        } else {
            sql = "{call SP_OBTENER_MEDICAMENTOS_DISPONIBLES}";
        }
        
        try (Connection conn = DatabaseConnection.getConnection();
             CallableStatement stmt = conn.prepareCall(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                Medicamento m = new Medicamento();
                m.setIdMedicamento(rs.getInt("ID_MEDICAMENTO"));
                m.setNombre(rs.getString("NOMBRE"));
                try { m.setDescripcion(rs.getString("DESCRIPCION")); } catch (SQLException e) { m.setDescripcion(""); }
                try { m.setPrecio(rs.getDouble("PRECIO")); } catch (SQLException e) { m.setPrecio(0.0); }
                m.setEstado(true);
                m.setStock(0);
                lista.add(m);
            }
        }
        return lista;
    }

    /**
     * Obtiene un medicamento por su identificador.
     * Adaptado para MySQL donde el SP no retorna STOCK ni ESTADO.
     *
     * @param idMedicamento identificador del medicamento
     * @return objeto Medicamento encontrado
     * @throws SQLException si ocurre un error en la base de datos
     */
    @Override
    public Medicamento obtenerPorId(int idMedicamento) throws SQLException {
        String sql;
        
        if (DatabaseConnection.isMySQL()) {
            sql = "CALL SP_OBTENER_MEDICAMENTO_POR_ID(?)";
        } else {
            sql = "{call SP_OBTENER_MEDICAMENTO_POR_ID(?)}";
        }
        
        try (Connection conn = DatabaseConnection.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {
            stmt.setInt(1, idMedicamento);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                Medicamento m = new Medicamento();
                m.setIdMedicamento(rs.getInt("ID_MEDICAMENTO"));
                m.setNombre(rs.getString("NOMBRE"));
                try { m.setDescripcion(rs.getString("DESCRIPCION")); } catch (SQLException e) { m.setDescripcion(""); }
                try { m.setPrecio(rs.getDouble("PRECIO")); } catch (SQLException e) { m.setPrecio(0.0); }
                // MySQL no retorna STOCK ni ESTADO en este SP, asignar valores por defecto
                m.setStock(0);
                m.setEstado(true);
                return m;
            }
            return null;
        }
    }

    /**
     * Registra un medicamento recetado en una atencion medica.
     *
     * @param idAtencionMedica identificador de la atencion
     * @param idMedicamento identificador del medicamento
     * @param dosis dosis prescrita
     * @param frecuencia frecuencia de administracion
     * @param duracion duracion del tratamiento
     * @return true si el registro fue exitoso
     * @throws SQLException si ocurre un error en la base de datos
     */
    @Override
    public boolean insertarRecetado(int idAtencionMedica, int idMedicamento, String dosis, String frecuencia, String duracion) throws SQLException {
        String sql;
        
        if (DatabaseConnection.isMySQL()) {
            sql = "CALL SP_INSERTAR_MEDICAMENTO_RECETADO(?, ?, ?, ?, ?)";
        } else {
            sql = "{call SP_INSERTAR_MEDICAMENTO_RECETADO(?, ?, ?, ?, ?)}";
        }
        
        try (Connection conn = DatabaseConnection.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {
            stmt.setInt(1, idAtencionMedica);
            stmt.setInt(2, idMedicamento);
            stmt.setString(3, dosis);
            stmt.setString(4, frecuencia);
            stmt.setString(5, duracion);
            return stmt.execute();
        }
    }

    /**
     * Obtiene los medicamentos recetados en una atencion medica.
     *
     * @param idAtencionMedica identificador de la atencion
     * @return lista de medicamentos recetados
     * @throws SQLException si ocurre un error en la base de datos
     */
    @Override
    public List<Medicamento> obtenerRecetadosPorAtencion(int idAtencionMedica) throws SQLException {
        List<Medicamento> lista = new ArrayList<>();
        String sql;
        
        if (DatabaseConnection.isMySQL()) {
            sql = "CALL SP_OBTENER_MEDICAMENTOS_POR_ATENCION(?)";
        } else {
            sql = "{call SP_OBTENER_MEDICAMENTOS_POR_ATENCION(?)}";
        }

        try (Connection conn = DatabaseConnection.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {
            stmt.setInt(1, idAtencionMedica);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Medicamento m = new Medicamento();
                try { m.setNombre(rs.getString("NOMBRE")); } catch (SQLException e) { m.setNombre(""); }
                try { m.setPrecio(rs.getDouble("PRECIO")); } catch (SQLException e) { m.setPrecio(0.0); }
                try { m.setDosis(rs.getString("DOSIS")); } catch (SQLException e) { m.setDosis(""); }
                try { m.setFrecuencia(rs.getString("FRECUENCIA")); } catch (SQLException e) { m.setFrecuencia(""); }
                try { m.setDuracion(rs.getString("DURACION")); } catch (SQLException e) { m.setDuracion(""); }
                lista.add(m);
            }
        }
        return lista;
    }
}