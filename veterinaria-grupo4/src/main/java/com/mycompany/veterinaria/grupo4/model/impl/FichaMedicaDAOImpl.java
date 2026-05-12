package com.mycompany.veterinaria.grupo4.model.impl;

import com.mycompany.veterinaria.grupo4.model.dao.IFichaMedicaDAO;
import com.mycompany.veterinaria.grupo4.model.entity.FichaMedica;
import com.mycompany.veterinaria.grupo4.util.DatabaseConnection;
import java.sql.*;

/**
 * Implementacion del DAO para la gestion de fichas medicas.
 * <p>
 * Esta clase implementa la interfaz IFichaMedicaDAO y proporciona la logica
 * de acceso a datos para la entidad FichaMedica utilizando procedimientos
 * almacenados de SQL Server. Permite obtener y actualizar la ficha medica
 * de cada mascota.
 * </p>
 * 
 * <p><b>Fecha de inicio del proyecto:</b> 15/04/2026</p>
 * 
 * @author CASTILLO MEREJILDO JOSHUA JAVIER – MODULO: MASCOTA
 * @version 1.0
 * @since 1.0
 */
public class FichaMedicaDAOImpl implements IFichaMedicaDAO {

    /**
     * Obtiene la ficha medica de una mascota.
     *
     * @param idMascota identificador de la mascota
     * @return objeto FichaMedica encontrado
     * @throws SQLException si ocurre un error en la base de datos
     */
    @Override
    public FichaMedica obtenerPorMascota(int idMascota) throws SQLException {
        String sql = "{call SP_OBTENER_FICHA_MEDICA(?)}";
        
        try (Connection conn = DatabaseConnection.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {
            stmt.setInt(1, idMascota);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                FichaMedica f = new FichaMedica();
                f.setIdFicha(rs.getInt("ID_FICHA"));
                f.setIdMascota(rs.getInt("ID_MASCOTA"));
                f.setAlergias(rs.getString("ALERGIAS"));
                f.setEnfermedadesCronicas(rs.getString("ENFERMEDADES_CRONICAS"));
                f.setObservaciones(rs.getString("OBSERVACIONES"));
                f.setUltimaActualizacion(rs.getTimestamp("ULTIMA_ACTUALIZACION"));
                return f;
            }
            return null;
        }
    }

    /**
     * Actualiza los datos de la ficha medica de una mascota.
     *
     * @param idMascota identificador de la mascota
     * @param alergias alergias de la mascota
     * @param enfermedadesCronicas enfermedades cronicas
     * @param observaciones observaciones adicionales
     * @return true si la actualizacion fue exitosa
     * @throws SQLException si ocurre un error en la base de datos
     */
    @Override
    public boolean actualizar(int idMascota, String alergias, String enfermedadesCronicas, String observaciones) throws SQLException {
        String sql = "{call SP_ACTUALIZAR_FICHA_MEDICA(?, ?, ?, ?)}";
        
        try (Connection conn = DatabaseConnection.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {
            stmt.setInt(1, idMascota);
            stmt.setString(2, alergias);
            stmt.setString(3, enfermedadesCronicas);
            stmt.setString(4, observaciones);
            return stmt.execute();
        }
    }
}