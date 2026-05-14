package com.mycompany.veterinaria.grupo4.model.impl;

import com.mycompany.veterinaria.grupo4.model.dao.IEspecialidadDAO;
import com.mycompany.veterinaria.grupo4.model.entity.EspecialidadVeterinaria;
import com.mycompany.veterinaria.grupo4.util.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementacion del DAO para la gestion de especialidades veterinarias.
 * <p>
 * Esta clase implementa la interfaz IEspecialidadDAO y proporciona la logica
 * de acceso a datos para la entidad EspecialidadVeterinaria utilizando
 * procedimientos almacenados de SQL Server.
 * </p>
 * 
 * <p><b>Fecha de inicio del proyecto:</b> 15/04/2026</p>
 * 
 * @author BESILLA TOMALA ANGEL KALED – MODULO: VETERINARIO
 * @version 1.0
 * @since 1.0
 */
public class EspecialidadDAOImpl implements IEspecialidadDAO {

    /**
     * Obtiene todas las especialidades veterinarias registradas.
     *
     * @return lista de especialidades
     * @throws SQLException si ocurre un error en la base de datos
     */
    @Override
    public List<EspecialidadVeterinaria> obtenerTodas() throws SQLException {
        List<EspecialidadVeterinaria> lista = new ArrayList<>();
        String sql = "{call SP_OBTENER_ESPECIALIDADES}";

        try (Connection conn = DatabaseConnection.getConnection();
             CallableStatement stmt = conn.prepareCall(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                EspecialidadVeterinaria e = new EspecialidadVeterinaria();
                e.setIdEspecialidad(rs.getInt("ID_ESPECIALIDAD"));
                e.setNombreEspecialidad(rs.getString("NOMBRE_ESPECIALIDAD"));
                lista.add(e);
            }
        }
        return lista;
    }

    /**
     * Obtiene una especialidad por su identificador.
     *
     * @param id identificador de la especialidad
     * @return objeto EspecialidadVeterinaria encontrado
     * @throws SQLException si ocurre un error en la base de datos
     */
    @Override
    public EspecialidadVeterinaria obtenerPorId(int id) throws SQLException {
        String sql = "SELECT ID_ESPECIALIDAD, NOMBRE_ESPECIALIDAD FROM ESPECIALIDAD_VETERINARIA WHERE ID_ESPECIALIDAD = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                EspecialidadVeterinaria e = new EspecialidadVeterinaria();
                e.setIdEspecialidad(rs.getInt("ID_ESPECIALIDAD"));
                e.setNombreEspecialidad(rs.getString("NOMBRE_ESPECIALIDAD"));
                return e;
            }
            return null;
        }
    }
}