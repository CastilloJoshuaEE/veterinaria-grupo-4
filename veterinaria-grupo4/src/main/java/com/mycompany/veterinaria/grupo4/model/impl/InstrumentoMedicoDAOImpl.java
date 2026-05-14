package com.mycompany.veterinaria.grupo4.model.impl;

import com.mycompany.veterinaria.grupo4.model.dao.IInstrumentoMedicoDAO;
import com.mycompany.veterinaria.grupo4.model.entity.InstrumentoMedico;
import com.mycompany.veterinaria.grupo4.util.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementacion del DAO para la gestion de instrumentos medicos.
 * <p>
 * Esta clase implementa la interfaz IInstrumentoMedicoDAO y proporciona la logica
 * de acceso a datos para la entidad InstrumentoMedico utilizando procedimientos
 * almacenados de SQL Server. Permite obtener instrumentos disponibles,
 * registrar su uso en atenciones medicas y listar los utilizados.
 * </p>
 * 
 * <p><b>Fecha de inicio del proyecto:</b> 15/04/2026</p>
 * 
 * @author ROBLES MORALES JUAN ANDRES – MODULO: ATENCION VETERINARIA
 * @version 1.0
 * @since 1.0
 */
public class InstrumentoMedicoDAOImpl implements IInstrumentoMedicoDAO {

    /**
     * Obtiene los instrumentos medicos disponibles (stock > 0).
     *
     * @return lista de instrumentos disponibles
     * @throws SQLException si ocurre un error en la base de datos
     */
    @Override
    public List<InstrumentoMedico> obtenerDisponibles() throws SQLException {
        List<InstrumentoMedico> lista = new ArrayList<>();
        String sql = "{call SP_OBTENER_INSTRUMENTOS_DISPONIBLES}";
        
        try (Connection conn = DatabaseConnection.getConnection();
             CallableStatement stmt = conn.prepareCall(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                InstrumentoMedico i = new InstrumentoMedico();
                i.setIdInstrumento(rs.getInt("ID_INSTRUMENTO"));
                i.setNombre(rs.getString("NOMBRE"));
                i.setDescripcion(rs.getString("DESCRIPCION"));
                i.setCostoUso(rs.getDouble("COSTO_USO"));
                lista.add(i);
            }
        }
        return lista;
    }

    /**
     * Obtiene un instrumento por su identificador.
     *
     * @param idInstrumento identificador del instrumento
     * @return objeto InstrumentoMedico encontrado
     * @throws SQLException si ocurre un error en la base de datos
     */
    @Override
    public InstrumentoMedico obtenerPorId(int idInstrumento) throws SQLException {
        String sql = "{call SP_OBTENER_INSTRUMENTO_POR_ID(?)}";
        
        try (Connection conn = DatabaseConnection.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {
            stmt.setInt(1, idInstrumento);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                InstrumentoMedico i = new InstrumentoMedico();
                i.setIdInstrumento(rs.getInt("ID_INSTRUMENTO"));
                i.setNombre(rs.getString("NOMBRE"));
                i.setDescripcion(rs.getString("DESCRIPCION"));
                i.setCostoUso(rs.getDouble("COSTO_USO"));
                i.setEstado(rs.getBoolean("ESTADO"));
                return i;
            }
            return null;
        }
    }

    /**
     * Registra el uso de un instrumento en una atencion medica.
     *
     * @param idAtencionMedica identificador de la atencion
     * @param idInstrumento identificador del instrumento
     * @return true si el registro fue exitoso
     * @throws SQLException si ocurre un error en la base de datos
     */
    @Override
    public boolean insertarUsado(int idAtencionMedica, int idInstrumento) throws SQLException {
        String sql = "{call SP_INSERTAR_INSTRUMENTO_USADO(?, ?)}";
        
        try (Connection conn = DatabaseConnection.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {
            stmt.setInt(1, idAtencionMedica);
            stmt.setInt(2, idInstrumento);
            return stmt.execute();
        }
    }

    /**
     * Obtiene los instrumentos usados en una atencion medica especifica.
     *
     * @param idAtencionMedica identificador de la atencion
     * @return lista de instrumentos usados
     * @throws SQLException si ocurre un error en la base de datos
     */
    @Override
    public List<InstrumentoMedico> obtenerUsadosPorAtencion(int idAtencionMedica) throws SQLException {
        List<InstrumentoMedico> lista = new ArrayList<>();
        String sql = "{call SP_OBTENER_INSTRUMENTOS_POR_ATENCION(?)}";
        
        try (Connection conn = DatabaseConnection.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {
            stmt.setInt(1, idAtencionMedica);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                InstrumentoMedico i = new InstrumentoMedico();
                i.setNombre(rs.getString("NOMBRE"));
                i.setCostoUso(rs.getDouble("COSTO_USO"));
                lista.add(i);
            }
        }
        return lista;
    }
}