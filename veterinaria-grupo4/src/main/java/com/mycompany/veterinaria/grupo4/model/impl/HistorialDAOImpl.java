package com.mycompany.veterinaria.grupo4.model.impl;

import com.mycompany.veterinaria.grupo4.model.dao.IHistorialDAO;
import com.mycompany.veterinaria.grupo4.model.entity.HistorialMedico;
import com.mycompany.veterinaria.grupo4.util.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementacion del DAO para la gestion del historial medico.
 * <p>
 * Esta clase implementa la interfaz IHistorialDAO y proporciona la logica
 * de acceso a datos para la entidad HistorialMedico utilizando procedimientos
 * almacenados de SQL Server. Permite obtener el historial completo de una
 * mascota y registrar nuevas entradas.
 * </p>
 * 
 * <p><b>Fecha de inicio del proyecto:</b> 15/04/2026</p>
 * 
 * @author CASTILLO MEREJILDO JOSHUA JAVIER – MODULO: MASCOTA
 * @version 1.0
 * @since 1.0
 */
public class HistorialDAOImpl implements IHistorialDAO {

    /**
     * Obtiene el historial medico completo de una mascota.
     *
     * @param idMascota identificador de la mascota
     * @return lista de registros del historial
     * @throws SQLException si ocurre un error en la base de datos
     */
    @Override
    public List<HistorialMedico> obtenerPorMascota(int idMascota) throws SQLException {
        List<HistorialMedico> lista = new ArrayList<>();
        String sql = "{call SP_OBTENER_HISTORIAL_MEDICO_POR_MASCOTA(?)}";
        
        try (Connection conn = DatabaseConnection.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {
            stmt.setInt(1, idMascota);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                HistorialMedico h = new HistorialMedico();
                h.setIdHistorial(rs.getInt("ID_HISTORIAL"));
                h.setIdMascota(idMascota);
                h.setIdCita(rs.getInt("ID_CITA"));
                h.setIdAtencionMedica(rs.getInt("ID_ATENCION_MEDICA"));
                h.setFecha(rs.getTimestamp("FECHA"));
                h.setNombreServicio(rs.getString("NOMBRE_SERVICIO"));
                h.setNombreVeterinario(rs.getString("VETERINARIO"));
                h.setDiagnostico(rs.getString("DIAGNOSTICO"));
                h.setTratamiento(rs.getString("TRATAMIENTO"));
                h.setInstrumentosUsados(rs.getString("INSTRUMENTOS_USADOS"));
                h.setMedicamentosRecetados(rs.getString("MEDICAMENTOS_RECETADOS"));
            
                lista.add(h);
            }
        }
        return lista;
    }

    /**
     * Registra una nueva entrada en el historial medico.
     *
     * @param idMascota identificador de la mascota
     * @param idCita identificador de la cita (opcional)
     * @param idAtencionMedica identificador de la atencion medica (opcional)
     * @return true si el registro fue exitoso
     * @throws SQLException si ocurre un error en la base de datos
     */
    @Override
    public boolean registrar(int idMascota, Integer idCita, Integer idAtencionMedica) throws SQLException {
        String sql = "{call SP_REGISTRAR_HISTORIAL(?, ?, ?)}";
        
        try (Connection conn = DatabaseConnection.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {
            stmt.setInt(1, idMascota);
            if (idCita != null) {
                stmt.setInt(2, idCita);
            } else {
                stmt.setNull(2, Types.INTEGER);
            }
            if (idAtencionMedica != null) {
                stmt.setInt(3, idAtencionMedica);
            } else {
                stmt.setNull(3, Types.INTEGER);
            }
            ResultSet rs = stmt.executeQuery();
            return rs.next() && rs.getInt("ID_HISTORIAL") > 0;
        }
    }
}