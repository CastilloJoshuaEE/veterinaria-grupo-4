package com.mycompany.veterinaria.grupo4.model.impl;

import com.mycompany.veterinaria.grupo4.model.dao.ICitaDAO;
import com.mycompany.veterinaria.grupo4.model.entity.Cita;
import com.mycompany.veterinaria.grupo4.model.entity.Cliente;
import com.mycompany.veterinaria.grupo4.model.entity.Mascota;
import com.mycompany.veterinaria.grupo4.model.entity.Servicio;
import com.mycompany.veterinaria.grupo4.model.entity.Veterinario;
import com.mycompany.veterinaria.grupo4.util.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementacion del DAO para la gestion de citas.
 * <p>
 * Esta clase implementa la interfaz ICitaDAO y proporciona la logica
 * de acceso a datos para la entidad Cita utilizando procedimientos
 * almacenados de SQL Server.
 * </p>
 * 
 * <p><b>Fecha de inicio del proyecto:</b> 15/04/2026</p>
 * 
 * @author CHILAN CHILAN DANNY ANDRES – MODULO: AGENDAMIENTO DE CITA
 * @version 1.0
 * @since 1.0
 */
public class CitaDAOImpl implements ICitaDAO {
  
    /**
     * Recupera todas las citas programadas para una fecha especifica.
     * Este metodo es el pilar de la vista de agenda diaria, permitiendo obtener
     * un listado cronologico de las citas, incluyendo todos los detalles del
     * paciente, el dueño y el medico asignado.
     *
     * @param fecha La fecha calendario (sin considerar la hora) de la cual se
     *              desean obtener los registros.
     * @return Una lista de objetos Cita programados para ese dia.
     * @throws SQLException Si ocurre un error en la conversion de fechas o en la
     *                      ejecucion del procedimiento.
     */
    @Override
    public List<Cita> obtenerPorFecha(java.util.Date fecha) throws SQLException {
        List<Cita> lista = new ArrayList<>();
        String sql = "{call SP_OBTENER_CITAS_POR_FECHA(?)}";

        try (Connection conn = DatabaseConnection.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {

            stmt.setDate(1, new java.sql.Date(fecha.getTime()));

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapResultSetToCita(rs));
                }
            }
        }
        return lista;
    }

    /**
     * Obtiene el historial de citas asociadas a un cliente especifico.
     *
     * @param idCliente El identificador unico del cliente cuyos registros se desean consultar.
     * @return Una lista de objetos Cita.
     * @throws SQLException Si ocurre un error en la comunicacion con SQL Server.
     */
    @Override
    public List<Cita> obtenerPorCliente(int idCliente) throws SQLException {
        List<Cita> lista = new ArrayList<>();
        String sql = "{call SP_OBTENER_CITAS_POR_CLIENTE(?)}";
        
        try (Connection conn = DatabaseConnection.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {
            
            stmt.setInt(1, idCliente);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapResultSetToCita(rs));
                }
            }
        }
        return lista;
    }
    
    /**
     * Recupera una cita medica especifica de la base de datos por su identificador unico.
     *
     * @param idCita El identificador unico de la cita en la base de datos.
     * @return Un objeto Cita con todas sus dependencias cargadas.
     * @throws SQLException Si ocurre un error durante la ejecucion.
     */
    @Override
    public Cita obtenerPorId(int idCita) throws SQLException {
        String sql = "{call SP_OBTENER_CITA_COMPLETA(?)}";
        
        try (Connection conn = DatabaseConnection.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {
            
            stmt.setInt(1, idCita);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToCita(rs);
                }
            }
        }
        return null;
    }

    /**
     * Registra una nueva cita medica en el sistema.
     *
     * @param cita El objeto Cita que contiene toda la informacion a persistir.
     * @return El identificador unico (ID_CITA) generado por la base de datos.
     * @throws SQLException Si ocurre un error de conectividad.
     */
    @Override
    public int agendar(Cita cita) throws SQLException {
        String sql = "{call SP_AGENDAR_CITA(?, ?, ?, ?, ?, ?)}";
        
        try (Connection conn = DatabaseConnection.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {
            
            stmt.setInt(1, cita.getCliente().getIdCliente());
            stmt.setInt(2, cita.getMascota().getIdMascota());
            stmt.setInt(3, cita.getServicio().getIdServicio());
            stmt.setInt(4, cita.getVeterinario().getIdVeterinario());
            stmt.setTimestamp(5, new Timestamp(cita.getFechaHora().getTime()));
            stmt.setString(6, cita.getObservaciones());
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("ID_CITA");
                }
            }
            return -2;
        }
    }

    /**
     * Actualiza la informacion integral de una cita existente.
     *
     * @param cita El objeto Cita con los datos actualizados.
     * @return true si la actualizacion fue exitosa.
     * @throws SQLException Si ocurre un error de restriccion de integridad.
     */
    @Override
    public boolean actualizar(Cita cita) throws SQLException {
        String sql = "{call SP_ACTUALIZAR_CITA_COMPLETA(?, ?, ?, ?, ?, ?, ?, ?)}";
        
        try (Connection conn = DatabaseConnection.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {
            
            stmt.setInt(1, cita.getIdCita());
            stmt.setInt(2, cita.getCliente().getIdCliente());
            stmt.setInt(3, cita.getMascota().getIdMascota());
            stmt.setInt(4, cita.getServicio().getIdServicio());
            stmt.setInt(5, cita.getVeterinario().getIdVeterinario());
            stmt.setTimestamp(6, new Timestamp(cita.getFechaHora().getTime()));
            stmt.setString(7, cita.getEstado());
            stmt.setString(8, cita.getObservaciones());
            
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() && rs.getInt("RESULTADO") == 1;
            }
        }
    }

    /**
     * Cancela una cita medica registrada en el sistema.
     *
     * @param idCita El identificador unico de la cita que se desea anular.
     * @param motivo El texto descriptivo que explica la razon de la cancelacion.
     * @return true si la cita se cancelo correctamente.
     * @throws SQLException Si ocurre una interrupcion en la conexion.
     */
    @Override
    public boolean cancelar(int idCita, String motivo) throws SQLException {
        String sql = "{call SP_CANCELAR_CITA(?, ?)}";
        
        try (Connection conn = DatabaseConnection.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {
            
            stmt.setInt(1, idCita);
            stmt.setString(2, motivo);
            
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() && rs.getInt("RESULTADO") == 1;
            }
        }
    }

    /**
     * Recupera el listado completo de todas las citas registradas.
     *
     * @return Una lista que contiene todos los registros de citas.
     * @throws SQLException Si ocurre un fallo en la conexion.
     */
    @Override
    public List<Cita> obtenerTodas() throws SQLException {
        List<Cita> lista = new ArrayList<>();
        String sql = "{call SP_OBTENER_TODAS_LAS_CITAS}";
        
        try (Connection conn = DatabaseConnection.getConnection();
             CallableStatement stmt = conn.prepareCall(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                lista.add(mapResultSetToCita(rs));
            }
        }
        return lista;
    }

    /**
     * Consulta y devuelve un listado de citas comprendidas dentro de un periodo.
     *
     * @param fechaInicio Fecha inicial del rango de busqueda.
     * @param fechaFin Fecha final del rango de busqueda.
     * @return Una lista de citas encontradas en el rango.
     * @throws SQLException Si hay errores en los parametros de entrada.
     */
    @Override
    public List<Cita> obtenerPorRangoFechas(java.util.Date fechaInicio, java.util.Date fechaFin) throws SQLException {
        List<Cita> lista = new ArrayList<>();
        String sql = "{call SP_OBTENER_CITAS_POR_RANGO_FECHAS(?, ?)}";
        
        try (Connection conn = DatabaseConnection.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {  
            stmt.setDate(1, new java.sql.Date(fechaInicio.getTime()));
            stmt.setDate(2, new java.sql.Date(fechaFin.getTime()));
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapResultSetToCita(rs));
                }
            }
        }
        return lista;
    }

    /**
     * Filtra y recupera una lista de citas basadas en servicio, veterinario y estado.
     *
     * @param idServicio Identificador del servicio a filtrar.
     * @param idVeterinario Identificador del veterinario asignado.
     * @param estado Estado de la cita.
     * @return Una lista de objetos Cita que cumplen con los criterios.
     * @throws SQLException Si ocurre un error en la ejecucion.
     */
    @Override
    public List<Cita> obtenerPorServicioYVeterinario(int idServicio, int idVeterinario, String estado) throws SQLException {
        List<Cita> lista = new ArrayList<>();
        String sql = "{call SP_OBTENER_CITAS_POR_SERVICIO_VETERINARIO(?, ?, ?)}";
        
        try (Connection conn = DatabaseConnection.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {
            
            stmt.setInt(1, idServicio);
            stmt.setInt(2, idVeterinario);
            stmt.setString(3, estado);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapResultSetToCita(rs));
                }
            }
        }
        return lista;
    }

    /**
     * Modifica exclusivamente el estado de una cita.
     *
     * @param idCita El identificador unico de la cita a modificar.
     * @param estado El nuevo estado de la cita.
     * @return true si el estado se actualizo correctamente.
     * @throws SQLException Si el estado proporcionado no es valido.
     */
    @Override
    public boolean actualizarEstado(int idCita, String estado) throws SQLException {
        String sql = "{call SP_ACTUALIZAR_ESTADO_CITA(?, ?)}";
        
        try (Connection conn = DatabaseConnection.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {
            
            stmt.setInt(1, idCita);
            stmt.setString(2, estado);
            
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() && rs.getInt("RESULTADO") == 1;
            }
        }
    }

    /**
     * Elimina de forma permanente una cita de la base de datos.
     *
     * @param idCita El identificador unico de la cita que se desea eliminar.
     * @return true si la operacion se completo exitosamente.
     * @throws SQLException Si la cita tiene integridad referencial activa.
     */
    @Override
    public boolean eliminar(int idCita) throws SQLException {
        String sql = "{call SP_ELIMINAR_CITA_COMPLETA(?)}";
        
        try (Connection conn = DatabaseConnection.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {
            
            stmt.setInt(1, idCita);
            int filasAfectadas = stmt.executeUpdate();
            return filasAfectadas > 0;
        }
    }
    
    /**
     * Recupera todas las citas medicas en estado 'PENDIENTE'.
     *
     * @return Una lista de objetos Cita que representan la cola de atencion.
     * @throws SQLException Si ocurre un error de acceso a datos.
     */
    @Override
    public List<Cita> obtenerPendientes() throws SQLException {
        List<Cita> lista = new ArrayList<>();
        String sql = "{call SP_OBTENER_CITAS_PENDIENTES}";

        try (Connection conn = DatabaseConnection.getConnection();
             CallableStatement stmt = conn.prepareCall(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                lista.add(mapResultSetToCita(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error critico en CitaDAO.obtenerPendientes: " + e.getMessage());
            throw e;
        }
        return lista;
    }
    
    /**
     * Metodo auxiliar para transformar una fila de la BD en un objeto Cita completo.
     *
     * @param rs ResultSet con los datos de la consulta
     * @return objeto Cita construido
     * @throws SQLException si hay error en la lectura de datos
     */
    private Cita mapResultSetToCita(ResultSet rs) throws SQLException {
        Cita cita = new Cita();

        cita.setIdCita(rs.getInt("ID_CITA"));
        cita.setFechaHora(rs.getTimestamp("FECHA_HORA"));
        cita.setObservaciones(rs.getString("OBSERVACIONES"));

        try {
            cita.setEstado(rs.getString("ESTADO"));
        } catch (SQLException e) {
            cita.setEstado("PENDIENTE");
        }

        try {
            cita.setFechaRegistro(rs.getTimestamp("FECHA_REGISTRO"));
        } catch (SQLException e) {
        }

        Cliente cliente = new Cliente();
        try {
            cliente.setIdCliente(rs.getInt("ID_CLIENTE"));
        } catch (SQLException e) {
            cliente.setIdCliente(0);
        }
        try {
            cliente.setNombre(rs.getString("NOMBRE_CLIENTE"));
        } catch (SQLException e) {
            cliente.setNombre("");
        }
        cita.setCliente(cliente);

        Mascota mascota = new Mascota();
        try {
            mascota.setIdMascota(rs.getInt("ID_MASCOTA"));
        } catch (SQLException e) {
            mascota.setIdMascota(0);
        }
        try {
            mascota.setNombre(rs.getString("NOMBRE_MASCOTA"));
        } catch (SQLException e) {
            mascota.setNombre("");
        }
        try { mascota.setEspecie(rs.getString("ESPECIE_MASCOTA")); }  // ✅ nuevo
        catch (SQLException e) { mascota.setEspecie(""); }
        cita.setMascota(mascota);

        Servicio servicio = new Servicio();
        try {
            servicio.setIdServicio(rs.getInt("ID_SERVICIO"));
        } catch (SQLException e) {
            servicio.setIdServicio(0);
        }
        try {
            servicio.setNombreServicio(rs.getString("NOMBRE_SERVICIO"));
        } catch (SQLException e) {
            servicio.setNombreServicio("");
        }
        cita.setServicio(servicio);

        Veterinario vete = new Veterinario();
        try {
            vete.setIdVeterinario(rs.getInt("ID_VETERINARIO"));
        } catch (SQLException e) {
            vete.setIdVeterinario(0);
        }
        try {
            vete.setNombre(rs.getString("NOMBRE_VETERINARIO"));
        } catch (SQLException e) {
            vete.setNombre("");
        }
        try { vete.setApellido(rs.getString("APELLIDO_VETERINARIO")); } 
        catch (SQLException e) { vete.setApellido(""); }
        cita.setVeterinario(vete);

        return cita;
    }
}