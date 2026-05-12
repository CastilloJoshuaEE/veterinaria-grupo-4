package com.mycompany.veterinaria.grupo4.model.impl;

import com.mycompany.veterinaria.grupo4.model.dao.IServicioDAO;
import com.mycompany.veterinaria.grupo4.model.entity.EspecialidadVeterinaria;
import com.mycompany.veterinaria.grupo4.model.entity.Servicio;
import com.mycompany.veterinaria.grupo4.model.entity.Veterinario;
import com.mycompany.veterinaria.grupo4.util.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementacion del DAO para la gestion de servicios veterinarios.
 * <p>
 * Esta clase implementa la interfaz IServicioDAO y proporciona la logica
 * de acceso a datos para la entidad Servicio utilizando procedimientos
 * almacenados de SQL Server. Permite operaciones CRUD completas,
 * gestion de asignaciones de veterinarios a servicios y busquedas.
 * </p>
 * 
 * <p><b>Fecha de inicio del proyecto:</b> 15/04/2026</p>
 * 
 * @author BESILLA TOMALA ANGEL KALED – MODULO: VETERINARIO
 * @version 1.0
 * @since 1.0
 */
public class ServicioDAOImpl implements IServicioDAO {

    /**
     * Obtiene todos los servicios registrados.
     *
     * @return lista de todos los servicios
     * @throws SQLException si ocurre un error en la base de datos
     */
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

    /**
     * Obtiene solo los servicios activos.
     *
     * @return lista de servicios activos
     * @throws SQLException si ocurre un error en la base de datos
     */
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

    /**
     * Obtiene un servicio por su identificador.
     *
     * @param idServicio identificador del servicio
     * @return objeto Servicio encontrado
     * @throws SQLException si ocurre un error en la base de datos
     */
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

    /**
     * Inserta un nuevo servicio en la base de datos.
     *
     * @param servicio objeto Servicio a insertar
     * @return ID del servicio creado
     * @throws SQLException si ocurre un error en la base de datos
     */
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

    /**
     * Actualiza los datos de un servicio existente.
     *
     * @param servicio objeto Servicio con datos actualizados
     * @return true si la actualizacion fue exitosa
     * @throws SQLException si ocurre un error en la base de datos
     */
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

    /**
     * Elimina un servicio de la base de datos.
     *
     * @param idServicio identificador del servicio a eliminar
     * @return true si la eliminacion fue exitosa
     * @throws SQLException si ocurre un error en la base de datos
     */
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

    /**
     * Cambia el estado (activo/inactivo) de un servicio.
     *
     * @param idServicio identificador del servicio
     * @param estado nuevo estado
     * @return true si el cambio fue exitoso
     * @throws SQLException si ocurre un error en la base de datos
     */
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

    /**
     * Obtiene los veterinarios asignados a un servicio.
     *
     * @param idServicio identificador del servicio
     * @return lista de veterinarios asignados
     * @throws SQLException si ocurre un error en la base de datos
     */
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
                
                try {
                    String nombreEspecialidad = rs.getString("ESPECIALIDAD");
                    if (nombreEspecialidad != null && !nombreEspecialidad.isEmpty()) {
                        EspecialidadVeterinaria esp = new EspecialidadVeterinaria();
                        esp.setNombreEspecialidad(nombreEspecialidad);
                        try {
                            esp.setIdEspecialidad(rs.getInt("ID_ESPECIALIDAD"));
                        } catch (SQLException ex) {
                        }
                        v.setEspecialidad(esp);
                    }
                } catch (SQLException ex) {
                }
                lista.add(v);
            }
        }
        return lista;
    }

    /**
     * Obtiene los veterinarios no asignados a un servicio.
     *
     * @param idServicio identificador del servicio
     * @return lista de veterinarios no asignados
     * @throws SQLException si ocurre un error en la base de datos
     */
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
                
                try {
                    String nombreEspecialidad = rs.getString("ESPECIALIDAD");
                    if (nombreEspecialidad != null && !nombreEspecialidad.isEmpty()) {
                        EspecialidadVeterinaria esp = new EspecialidadVeterinaria();
                        esp.setNombreEspecialidad(nombreEspecialidad);
                        v.setEspecialidad(esp);
                    }
                } catch (SQLException ex) {
                }
                lista.add(v);
            }
        }
        return lista;
    }

    /**
     * Obtiene todos los veterinarios con su especialidad.
     *
     * @return lista de veterinarios con especialidad
     * @throws SQLException si ocurre un error en la base de datos
     */
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
    
    /**
     * Asigna un veterinario a un servicio.
     *
     * @param idServicio identificador del servicio
     * @param idVeterinario identificador del veterinario
     * @return true si la asignacion fue exitosa
     * @throws SQLException si ocurre un error en la base de datos
     */
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

    /**
     * Elimina una asignacion de veterinario por su ID.
     *
     * @param idAsignacion identificador de la asignacion
     * @return true si la eliminacion fue exitosa
     * @throws SQLException si ocurre un error en la base de datos
     */
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
    
    /**
     * Lista los servicios asignados a un veterinario.
     *
     * @param idVeterinario identificador del veterinario
     * @return lista de servicios del veterinario
     * @throws SQLException si ocurre un error en la base de datos
     */
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
                s.setIdServicio(rs.getInt("ID_SERVICIO"));
                s.setNombreServicio(rs.getString("NOMBRE_SERVICIO"));
                lista.add(s);
            }
        }
        return lista;
    }
    
    /**
     * Busca servicios por nombre.
     *
     * @param nombre termino de busqueda
     * @return lista de servicios que coinciden
     * @throws SQLException si ocurre un error en la base de datos
     */
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
    
    /**
     * Elimina una asignacion de veterinario por IDs de veterinario y servicio.
     *
     * @param idVeterinario identificador del veterinario
     * @param idServicio identificador del servicio
     * @return true si la eliminacion fue exitosa
     * @throws SQLException si ocurre un error en la base de datos
     */
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