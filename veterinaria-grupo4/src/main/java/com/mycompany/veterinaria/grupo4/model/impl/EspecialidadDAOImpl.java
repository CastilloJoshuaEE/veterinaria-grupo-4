package com.mycompany.veterinaria.grupo4.model.impl;

import com.mycompany.veterinaria.grupo4.model.dao.IEspecialidadDAO;
import com.mycompany.veterinaria.grupo4.model.entity.EspecialidadVeterinaria;
import com.mycompany.veterinaria.grupo4.util.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EspecialidadDAOImpl implements IEspecialidadDAO {

    @Override
    public List<EspecialidadVeterinaria> obtenerTodas() throws SQLException {
        List<EspecialidadVeterinaria> lista = new ArrayList<>();
        String sql = "SELECT ID_ESPECIALIDAD, NOMBRE_ESPECIALIDAD FROM ESPECIALIDAD_VETERINARIA ORDER BY ID_ESPECIALIDAD";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                EspecialidadVeterinaria e = new EspecialidadVeterinaria();
                e.setIdEspecialidad(rs.getInt("ID_ESPECIALIDAD"));
                e.setNombreEspecialidad(rs.getString("NOMBRE_ESPECIALIDAD"));
                lista.add(e);
            }
        }
        return lista;
    }

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