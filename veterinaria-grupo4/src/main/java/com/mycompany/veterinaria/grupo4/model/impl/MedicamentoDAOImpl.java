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
 * almacenados de SQL Server. Permite obtener medicamentos disponibles,
 * buscar por ID, registrar recetas y listar medicamentos recetados.
 * </p>
 * 
 * <p><b>Fecha de inicio del proyecto:</b> 15/04/2026</p>
 * 
 * @author ROBLES MORALES JUAN ANDRES – MODULO: ATENCION VETERINARIA
 * @version 1.0
 * @since 1.0
 */
public class MedicamentoDAOImpl implements IMedicamentoDAO {

    /**
     * Obtiene los medicamentos disponibles (stock > 0).
     *
     * @return lista de medicamentos disponibles
     * @throws SQLException si ocurre un error en la base de datos
     */
    @Override
    public List<Medicamento> obtenerDisponibles() throws SQLException {
        List<Medicamento> lista = new ArrayList<>();
        String sql = "{call SP_OBTENER_MEDICAMENTOS_DISPONIBLES}";
        
        try (Connection conn = DatabaseConnection.getConnection();
             CallableStatement stmt = conn.prepareCall(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                Medicamento m = new Medicamento();
                m.setIdMedicamento(rs.getInt("ID_MEDICAMENTO"));
                m.setNombre(rs.getString("NOMBRE"));
                m.setDescripcion(rs.getString("DESCRIPCION"));
                m.setPrecio(rs.getDouble("PRECIO"));
                lista.add(m);
            }
        }
        return lista;
    }

    /**
     * Obtiene un medicamento por su identificador.
     *
     * @param idMedicamento identificador del medicamento
     * @return objeto Medicamento encontrado
     * @throws SQLException si ocurre un error en la base de datos
     */
    @Override
    public Medicamento obtenerPorId(int idMedicamento) throws SQLException {
        String sql = "{call SP_OBTENER_MEDICAMENTO_POR_ID(?)}";
        
        try (Connection conn = DatabaseConnection.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {
            stmt.setInt(1, idMedicamento);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                Medicamento m = new Medicamento();
                m.setIdMedicamento(rs.getInt("ID_MEDICAMENTO"));
                m.setNombre(rs.getString("NOMBRE"));
                m.setDescripcion(rs.getString("DESCRIPCION"));
                m.setPrecio(rs.getDouble("PRECIO"));
                m.setStock(rs.getInt("STOCK"));
                m.setEstado(rs.getBoolean("ESTADO"));
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
        String sql = "{call SP_INSERTAR_MEDICAMENTO_RECETADO(?, ?, ?, ?, ?)}";
        
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
        String sql = "{call SP_OBTENER_MEDICAMENTOS_POR_ATENCION(?)}";
        
        try (Connection conn = DatabaseConnection.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {
            stmt.setInt(1, idAtencionMedica);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Medicamento m = new Medicamento();
                m.setNombre(rs.getString("NOMBRE"));
                lista.add(m);
            }
        }
        return lista;
    }
}