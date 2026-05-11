package com.mycompany.veterinaria.grupo4.model.impl;

import com.mycompany.veterinaria.grupo4.model.dao.IServicioDAO;
import com.mycompany.veterinaria.grupo4.model.entity.EspecialidadVeterinaria;
import com.mycompany.veterinaria.grupo4.model.entity.Servicio;
import com.mycompany.veterinaria.grupo4.model.entity.Veterinario;
import com.mycompany.veterinaria.grupo4.util.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServicioDAOImpl implements IServicioDAO {

    @Override
    public List<Servicio> obtenerTodos() throws SQLException {
        List<Servicio> lista = new ArrayList<>();
        String sql = "{call SP_OBTENER_SERVICIOS}";
        
        try (Connection conn = DatabaseConnection.getConnection();
             CallableStatement stmt = conn.prepareCall(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                Servicio s = new Servicio();
                s.setIdServicio(rs.getInt("ID_SERVICIO"));
                s.setNombreServicio(rs.getString("NOMBRE_SERVICIO"));
                s.setDescripcion(rs.getString("DESCRIPCION"));
                s.setPrecio(rs.getDouble("PRECIO"));
                s.setDuracionEstimada(rs.getInt("DURACION_ESTIMADA"));
                s.setEstado(rs.getBoolean("ESTADO"));
                lista.add(s);
            }
        }
        return lista;
    }

    @Override
    public List<Servicio> obtenerActivos() throws SQLException {
        List<Servicio> lista = new ArrayList<>();
        String sql = "{call SP_OBTENER_SERVICIOS_ACTIVOS}";
        
        try (Connection conn = DatabaseConnection.getConnection();
             CallableStatement stmt = conn.prepareCall(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                Servicio s = new Servicio();
                s.setIdServicio(rs.getInt("ID_SERVICIO"));
                s.setNombreServicio(rs.getString("NOMBRE_SERVICIO"));
                s.setDescripcion(rs.getString("DESCRIPCION"));
                s.setPrecio(rs.getDouble("PRECIO"));
                s.setDuracionEstimada(rs.getInt("DURACION_ESTIMADA"));
                s.setEstado(true);
                lista.add(s);
            }
        }
        return lista;
    }

    @Override
    public Servicio obtenerPorId(int idServicio) throws SQLException {
        String sql = "{call SP_OBTENER_SERVICIO_POR_ID(?)}";
        
        try (Connection conn = DatabaseConnection.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {
            stmt.setInt(1, idServicio);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                Servicio s = new Servicio();
                s.setIdServicio(rs.getInt("ID_SERVICIO"));
                s.setNombreServicio(rs.getString("NOMBRE_SERVICIO"));
                s.setDescripcion(rs.getString("DESCRIPCION"));
                s.setPrecio(rs.getDouble("PRECIO"));
                s.setDuracionEstimada(rs.getInt("DURACION_ESTIMADA"));
                s.setEstado(rs.getBoolean("ESTADO"));
                return s;
            }
            return null;
        }
    }

    @Override
    public int insertar(Servicio servicio) throws SQLException {
        String sql = "{call SP_INSERTAR_SERVICIO(?, ?, ?, ?, ?)}";
        
        try (Connection conn = DatabaseConnection.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {
            stmt.setString(1, servicio.getNombreServicio());
            stmt.setString(2, servicio.getDescripcion());
            stmt.setDouble(3, servicio.getPrecio());
            stmt.setInt(4, servicio.getDuracionEstimada());
            stmt.setBoolean(5, servicio.isEstado());
            
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("ID_SERVICIO");
            }
            return -1;
        }
    }

    @Override
    public boolean actualizar(Servicio servicio) throws SQLException {
        String sql = "{call SP_ACTUALIZAR_SERVICIO(?, ?, ?, ?, ?, ?)}";
        
        try (Connection conn = DatabaseConnection.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {
            stmt.setInt(1, servicio.getIdServicio());
            stmt.setString(2, servicio.getNombreServicio());
            stmt.setString(3, servicio.getDescripcion());
            stmt.setDouble(4, servicio.getPrecio());
            stmt.setInt(5, servicio.getDuracionEstimada());
            stmt.setBoolean(6, servicio.isEstado());
            
            ResultSet rs = stmt.executeQuery();
            return rs.next() && rs.getInt("FILAS_AFECTADAS") > 0;
        }
    }

    @Override
    public boolean eliminar(int idServicio) throws SQLException {
        String sql = "{call SP_ELIMINAR_SERVICIO_COMPLETO(?)}";
        
        try (Connection conn = DatabaseConnection.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {
            stmt.setInt(1, idServicio);
            ResultSet rs = stmt.executeQuery();
            return rs.next() && rs.getInt("RESULTADO") == 1;
        }
    }

    @Override
    public boolean cambiarEstado(int idServicio, boolean estado) throws SQLException {
        String sql = "{call SP_CAMBIAR_ESTADO_SERVICIO(?, ?)}";
        
        try (Connection conn = DatabaseConnection.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {
            stmt.setInt(1, idServicio);
            stmt.setBoolean(2, estado);
            
            ResultSet rs = stmt.executeQuery();
            return rs.next() && rs.getInt("FILAS_AFECTADAS") > 0;
        }
    }

    @Override
public List<Veterinario> obtenerVeterinariosAsignados(int idServicio) throws SQLException {
    List<Veterinario> lista = new ArrayList<>();
    String sql = "{call SP_OBTENER_VETERINARIOS_POR_SERVICIO(?)}";

    try (Connection conn = DatabaseConnection.getConnection();
         CallableStatement stmt = conn.prepareCall(sql)) {
        stmt.setInt(1, idServicio);
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            Veterinario v = new Veterinario();
            v.setIdVeterinario(rs.getInt("ID_VETERINARIO"));
            v.setCedula(rs.getString("CEDULA"));
            v.setNombre(rs.getString("NOMBRE"));
            v.setApellido(rs.getString("APELLIDO"));
            try {
                v.setTelefono(rs.getString("TELEFONO"));
            } catch (SQLException ex) {
                v.setTelefono("");
            }
            
            // Mapear especialidad correctamente
            try {
                String nombreEspecialidad = rs.getString("ESPECIALIDAD");
                if (nombreEspecialidad != null && !nombreEspecialidad.isEmpty()) {
                    EspecialidadVeterinaria esp = new EspecialidadVeterinaria();
                    esp.setNombreEspecialidad(nombreEspecialidad);
                    // Intentar obtener el ID también si está disponible
                    try {
                        esp.setIdEspecialidad(rs.getInt("ID_ESPECIALIDAD"));
                    } catch (SQLException ex) {
                        // Si no viene ID, dejamos 0
                    }
                    v.setEspecialidad(esp);
                }
            } catch (SQLException ex) {
                // Especialidad no disponible
            }
            lista.add(v);
        }
    }
    return lista;
}


    @Override
public List<Veterinario> obtenerVeterinariosNoAsignados(int idServicio) throws SQLException {
    List<Veterinario> lista = new ArrayList<>();
    String sql = "{call SP_OBTENER_VETERINARIOS_NO_ASIGNADOS(?)}";

    try (Connection conn = DatabaseConnection.getConnection();
         CallableStatement stmt = conn.prepareCall(sql)) {
        stmt.setInt(1, idServicio);
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            Veterinario v = new Veterinario();
            v.setIdVeterinario(rs.getInt("ID_VETERINARIO"));
            v.setCedula(rs.getString("CEDULA"));
            v.setNombre(rs.getString("NOMBRE"));
            v.setApellido(rs.getString("APELLIDO"));
            try {
                v.setTelefono(rs.getString("TELEFONO"));
            } catch (SQLException ex) {
                v.setTelefono("");
            }
            
            // Mapear especialidad correctamente
            try {
                String nombreEspecialidad = rs.getString("ESPECIALIDAD");
                if (nombreEspecialidad != null && !nombreEspecialidad.isEmpty()) {
                    EspecialidadVeterinaria esp = new EspecialidadVeterinaria();
                    esp.setNombreEspecialidad(nombreEspecialidad);
                    v.setEspecialidad(esp);
                }
            } catch (SQLException ex) {
                // Especialidad no disponible
            }
            lista.add(v);
        }
    }
    return lista;
}
public List<Veterinario> obtenerTodosConEspecialidad() throws SQLException {
    List<Veterinario> lista = new ArrayList<>();
    String sql = "SELECT V.ID_VETERINARIO, V.CEDULA, V.NOMBRE, V.APELLIDO, V.TELEFONO, " +
                 "E.NOMBRE_ESPECIALIDAD AS ESPECIALIDAD " +
                 "FROM VETERINARIO V LEFT JOIN ESPECIALIDAD_VETERINARIA E " +
                 "ON V.ID_ESPECIALIDAD = E.ID_ESPECIALIDAD";

    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql);
         ResultSet rs = stmt.executeQuery()) {

        while (rs.next()) {
            Veterinario v = new Veterinario();
            v.setIdVeterinario(rs.getInt("ID_VETERINARIO"));
            v.setCedula(rs.getString("CEDULA"));
            v.setNombre(rs.getString("NOMBRE"));
            v.setApellido(rs.getString("APELLIDO"));
            v.setTelefono(rs.getString("TELEFONO"));
            
            String nombreEspecialidad = rs.getString("ESPECIALIDAD");
            if (nombreEspecialidad != null) {
                EspecialidadVeterinaria esp = new EspecialidadVeterinaria();
                esp.setNombreEspecialidad(nombreEspecialidad);
                v.setEspecialidad(esp);
            }
            lista.add(v);
        }
    }
    return lista;
}
    @Override
    public boolean asignarVeterinario(int idServicio, int idVeterinario) throws SQLException {
        String sql = "{call SP_ASIGNAR_VETERINARIO_SERVICIO(?, ?)}";
        
        try (Connection conn = DatabaseConnection.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {
            stmt.setInt(1, idServicio);
            stmt.setInt(2, idVeterinario);
            
            ResultSet rs = stmt.executeQuery();
            return rs.next() && rs.getInt("RESULTADO") == 1;
        }
    }

    @Override
    public boolean eliminarAsignacionVeterinario(int idAsignacion) throws SQLException {
        String sql = "{call SP_ELIMINAR_ASIGNACION_VETERINARIO(?)}";
        
        try (Connection conn = DatabaseConnection.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {
            stmt.setInt(1, idAsignacion);
            
            ResultSet rs = stmt.executeQuery();
            return rs.next() && rs.getInt("FILAS_AFECTADAS") > 0;
        }
    }
    
    @Override
    public List<Servicio> listarPorVeterinario(int idVeterinario) throws SQLException {
        List<Servicio> lista = new ArrayList<>();
        String sql = "{call SP_OBTENER_SERVICIOS_POR_VETERINARIO(?)}";

        try (Connection conn = DatabaseConnection.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {

            stmt.setInt(1, idVeterinario);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Servicio s = new Servicio();
                // Mapeamos los campos del SP
                s.setIdServicio(rs.getInt("ID_SERVICIO"));
                s.setNombreServicio(rs.getString("NOMBRE_SERVICIO"));
                // El ID_SERVICIO_VETERINARIO es útil si necesitas gestionar la relación
                lista.add(s);
            }
        }
        return lista;
    }
    @Override
    public List<Servicio> buscarPorNombre(String nombre) throws SQLException {
        List<Servicio> lista = new ArrayList<>();
        String sql = "SELECT ID_SERVICIO, NOMBRE_SERVICIO, DESCRIPCION, PRECIO, DURACION_ESTIMADA, ESTADO " +
                     "FROM SERVICIO WHERE NOMBRE_SERVICIO LIKE ? ORDER BY NOMBRE_SERVICIO";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, "%" + nombre + "%");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Servicio s = new Servicio();
                s.setIdServicio(rs.getInt("ID_SERVICIO"));
                s.setNombreServicio(rs.getString("NOMBRE_SERVICIO"));
                s.setDescripcion(rs.getString("DESCRIPCION"));
                s.setPrecio(rs.getDouble("PRECIO"));
                s.setDuracionEstimada(rs.getInt("DURACION_ESTIMADA"));
                s.setEstado(rs.getBoolean("ESTADO"));
                lista.add(s);
            }
        }
        return lista;
    }
    @Override
    public boolean eliminarAsignacionPorIds(int idVeterinario, int idServicio) throws SQLException {
        String sql = "DELETE FROM SERVICIO_VETERINARIO WHERE ID_VETERINARIO = ? AND ID_SERVICIO = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idVeterinario);
            stmt.setInt(2, idServicio);
            return stmt.executeUpdate() > 0;
        }
    }
}