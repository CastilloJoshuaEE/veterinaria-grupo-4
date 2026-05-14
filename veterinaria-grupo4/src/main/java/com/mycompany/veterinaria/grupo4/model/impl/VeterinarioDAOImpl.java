package com.mycompany.veterinaria.grupo4.model.impl;

import com.mycompany.veterinaria.grupo4.model.dao.IVeterinarioDAO;
import com.mycompany.veterinaria.grupo4.model.entity.EspecialidadVeterinaria;
import com.mycompany.veterinaria.grupo4.model.entity.Veterinario;
import com.mycompany.veterinaria.grupo4.util.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementacion del DAO para la gestion de veterinarios.
 * <p>
 * Esta clase implementa la interfaz IVeterinarioDAO y proporciona la logica
 * de acceso a datos para la entidad Veterinario utilizando procedimientos
 * almacenados de SQL Server. Permite operaciones CRUD completas,
 * busquedas por diferentes criterios y obtencion de veterinarios por servicio.
 * </p>
 * 
 * <p><b>Fecha de inicio del proyecto:</b> 15/04/2026</p>
 * 
 * @author BESILLA TOMALA ANGEL KALED – MODULO: VETERINARIO
 * @version 1.0
 * @since 1.0
 */
public class VeterinarioDAOImpl implements IVeterinarioDAO {

    /**
     * Obtiene todos los veterinarios registrados.
     *
     * @return lista de todos los veterinarios
     * @throws SQLException si ocurre un error en la base de datos
     */
    @Override
    public List<Veterinario> obtenerTodos() throws SQLException {
        List<Veterinario> lista = new ArrayList<>();
        String sql = "{call SP_OBTENER_VETERINARIOS}";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
        
            while (rs.next()) {               
                lista.add(mapResultSetToVeterinario(rs));
            }
        }
        return lista;
    }

    /**
     * Obtiene un veterinario por su numero de cedula.
     *
     * @param cedula numero de cedula del veterinario
     * @return objeto Veterinario encontrado
     * @throws SQLException si ocurre un error en la base de datos
     */
    @Override
    public Veterinario obtenerPorCedula(String cedula) throws SQLException {
        String sql = "{call SP_OBTENER_VETERINARIO_POR_CEDULA(?)}";
        
        try (Connection conn = DatabaseConnection.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {
            stmt.setString(1, cedula);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToVeterinario(rs);
            }
            return null;
        }
    }

    /**
     * Obtiene un veterinario por su identificador.
     *
     * @param id identificador del veterinario
     * @return objeto Veterinario encontrado
     * @throws SQLException si ocurre un error en la base de datos
     */
    @Override
    public Veterinario obtenerPorId(int id) throws SQLException {
        String sql = "{call SP_OBTENER_VETERINARIO_POR_ID(?)}";
        
        try (Connection conn = DatabaseConnection.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToVeterinario(rs);
            }
            return null;
        }
    }

    /**
     * Inserta un nuevo veterinario en la base de datos.
     *
     * @param veterinario objeto Veterinario a insertar
     * @return true si la insercion fue exitosa
     * @throws SQLException si ocurre un error en la base de datos
     */
    @Override
    public boolean insertar(Veterinario veterinario) throws SQLException {
        String sql = "{call SP_INSERTAR_VETERINARIO(?, ?, ?, ?, ?, ?, ?, ?)}";
        
        try (Connection conn = DatabaseConnection.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {
            stmt.setString(1, veterinario.getCedula());
            stmt.setString(2, veterinario.getNombre());
            stmt.setString(3, veterinario.getApellido());
            stmt.setString(4, veterinario.getTelefono());
            stmt.setInt(5, veterinario.getEspecialidad().getIdEspecialidad());
            stmt.setDouble(6, veterinario.getPagoMensual());
            stmt.setString(7, veterinario.getDireccion());
            stmt.setString(8, veterinario.getCorreoElectronico());
            
            ResultSet rs = stmt.executeQuery();
            return rs.next() && rs.getInt("ID_VETERINARIO") > 0;
        }
    }

    /**
     * Actualiza los datos de un veterinario existente.
     *
     * @param veterinario objeto Veterinario con datos actualizados
     * @return true si la actualizacion fue exitosa
     * @throws SQLException si ocurre un error en la base de datos
     */
    @Override
    public boolean actualizar(Veterinario veterinario) throws SQLException {
        String sql = "{call SP_ACTUALIZAR_VETERINARIO(?, ?, ?, ?, ?, ?, ?, ?, ?)}";
        
        try (Connection conn = DatabaseConnection.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {
            stmt.setInt(1, veterinario.getIdVeterinario());
            stmt.setString(2, veterinario.getCedula());
            stmt.setString(3, veterinario.getNombre());
            stmt.setString(4, veterinario.getApellido());
            stmt.setString(5, veterinario.getTelefono());
            stmt.setInt(6, veterinario.getEspecialidad().getIdEspecialidad());
            stmt.setDouble(7, veterinario.getPagoMensual());
            stmt.setString(8, veterinario.getDireccion());
            stmt.setString(9, veterinario.getCorreoElectronico());
            
            ResultSet rs = stmt.executeQuery();
            return rs.next() && rs.getInt("FILAS_AFECTADAS") > 0;
        }
    }

    /**
     * Elimina un veterinario de la base de datos.
     *
     * @param idVeterinario identificador del veterinario a eliminar
     * @return true si la eliminacion fue exitosa
     * @throws SQLException si ocurre un error en la base de datos
     */
    @Override
    public boolean eliminar(int idVeterinario) throws SQLException {
        String sql = "{call SP_ELIMINAR_VETERINARIO(?)}";
        
        try (Connection conn = DatabaseConnection.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {
            stmt.setInt(1, idVeterinario);
            return stmt.execute();
        }
    }

    /**
     * Busca veterinarios por nombre.
     *
     * @param nombre termino de busqueda
     * @return lista de veterinarios que coinciden con el nombre
     * @throws SQLException si ocurre un error en la base de datos
     */
    @Override
    public List<Veterinario> buscarPorNombre(String nombre) throws SQLException {
        List<Veterinario> lista = new ArrayList<>();
        String sql = "{call SP_OBTENER_VETERINARIOS_POR_NOMBRE(?)}";
        
        try (Connection conn = DatabaseConnection.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {
            stmt.setString(1, "%" + nombre + "%");
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                lista.add(mapResultSetToVeterinario(rs));
            }
        }
        return lista;
    }

    /**
     * Busca veterinarios por especialidad.
     *
     * @param especialidad especialidad a buscar
     * @return lista de veterinarios con esa especialidad
     * @throws SQLException si ocurre un error en la base de datos
     */
    @Override
    public List<Veterinario> buscarPorEspecialidad(String especialidad) throws SQLException {
        List<Veterinario> lista = new ArrayList<>();
        String sql = "{call SP_OBTENER_VETERINARIOS_POR_ESPECIALIDAD(?)}";
        
        try (Connection conn = DatabaseConnection.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {
            stmt.setString(1, "%" + especialidad + "%");
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                lista.add(mapResultSetToVeterinario(rs));
            }
        }
        return lista;
    }

    /**
     * Obtiene los veterinarios asignados a un servicio especifico.
     *
     * @param idServicio identificador del servicio
     * @return lista de veterinarios asignados al servicio
     * @throws SQLException si ocurre un error en la base de datos
     */
    @Override
    public List<Veterinario> obtenerPorServicio(int idServicio) throws SQLException {
        List<Veterinario> lista = new ArrayList<>();
        String sql = "{call SP_OBTENER_VETERINARIOS_POR_SERVICIO(?)}";
        
        try (Connection conn = DatabaseConnection.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {
            stmt.setInt(1, idServicio);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                lista.add(mapResultSetToVeterinario(rs));
            }
        }
        return lista;
    }
    
    /**
     * Busca veterinarios por termino general (nombre, cedula o especialidad).
     *
     * @param termino termino de busqueda
     * @return lista de veterinarios que coinciden
     * @throws SQLException si ocurre un error en la base de datos
     */
    @Override
    public List<Veterinario> buscar(String termino) throws SQLException {
        List<Veterinario> lista = new ArrayList<>();
        String sql = "{call SP_BUSCAR_VETERINARIOS(?)}";

        try (Connection conn = DatabaseConnection.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {

            stmt.setString(1, termino);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapResultSetToVeterinario(rs));
                }
            }
        }
        return lista;
    }
    
    /**
    * Metodo auxiliar para transformar una fila del ResultSet en un objeto Veterinario completo.
    *
    * @param rs ResultSet con los datos de la consulta
    * @return objeto Veterinario construido
    * @throws SQLException si hay error critico en la lectura de datos
    */
   private Veterinario mapResultSetToVeterinario(ResultSet rs) throws SQLException {
       Veterinario v = new Veterinario();

       try { v.setIdVeterinario(rs.getInt("ID_VETERINARIO")); }
       catch (SQLException e) { v.setIdVeterinario(0); }

       try { v.setCedula(rs.getString("CEDULA")); }
       catch (SQLException e) { v.setCedula(""); }

       try { v.setNombre(rs.getString("NOMBRE")); }
       catch (SQLException e) { v.setNombre(""); }

       try { v.setApellido(rs.getString("APELLIDO")); }
       catch (SQLException e) { v.setApellido(""); }

       try { v.setTelefono(rs.getString("TELEFONO")); }
       catch (SQLException e) { v.setTelefono(""); }

       try { v.setPagoMensual(rs.getDouble("PAGO_MENSUAL")); }
       catch (SQLException e) { v.setPagoMensual(0.0); }

       try { v.setDireccion(rs.getString("DIRECCION")); }
       catch (SQLException e) { v.setDireccion(""); }

       try { v.setCorreoElectronico(rs.getString("CORREO_ELECTRONICO")); }
       catch (SQLException e) { v.setCorreoElectronico(""); }

       try { v.setFechaRegistro(rs.getTimestamp("FECHA_REGISTRO")); }
       catch (SQLException e) { v.setFechaRegistro(null); }

       try {
           int idEspecialidad = rs.getInt("ID_ESPECIALIDAD");
           if (!rs.wasNull() && idEspecialidad > 0) {
               EspecialidadVeterinaria esp = new EspecialidadVeterinaria();
               esp.setIdEspecialidad(idEspecialidad);
               try { esp.setNombreEspecialidad(rs.getString("NOMBRE_ESPECIALIDAD")); }
               catch (SQLException e) {
                   esp.setNombreEspecialidad(""); 
               }
               v.setEspecialidad(esp);
           }
       } catch (SQLException e) { /* sin especialidad */ }

       return v;
   }
}