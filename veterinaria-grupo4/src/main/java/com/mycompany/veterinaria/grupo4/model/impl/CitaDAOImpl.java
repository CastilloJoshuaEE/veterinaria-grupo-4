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

public class CitaDAOImpl implements ICitaDAO {
  
    /**
     * Recupera todas las citas programadas para una fecha específica.
     * Este método es el pilar de la vista de agenda diaria, permitiendo obtener 
     * un listado cronológico de las citas, incluyendo todos los detalles del 
     * paciente, el dueño y el médico asignado.
     *
     * @param fecha La fecha calendario (sin considerar la hora) de la cual se 
     *              desean obtener los registros.
     * @return Una {@link List} de objetos {@link Cita} programados para ese día. 
     *         Si no existen registros, devuelve una lista vacía.
     * @throws SQLException Si ocurre un error en la conversión de fechas o en la 
     *                      ejecución del procedimiento {@code SP_OBTENER_CITAS_POR_FECHA}.
     */
    @Override
    public List<Cita> obtenerPorFecha(java.util.Date fecha) throws SQLException {
        List<Cita> lista = new ArrayList<>();
        String sql = "{call SP_OBTENER_CITAS_POR_FECHA(?)}";

        try (Connection conn = DatabaseConnection.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {

            // Convertimos el java.util.Date de la UI al java.sql.Date que espera SQL Server
            stmt.setDate(1, new java.sql.Date(fecha.getTime()));

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    // Hidratamos el objeto Cita completo usando el mapeador centralizado
                    lista.add(mapResultSetToCita(rs));
                }
            }
        }
        return lista;
    }

    /**
     * Obtiene el historial de citas asociadas a un cliente específico.
     * Este método recupera todas las citas (pendientes, realizadas o canceladas) 
     * vinculadas al ID del cliente proporcionado, cargando la jerarquía completa 
     * de objetos asociados para su uso en la capa de presentación.
     *
     * @param idCliente El identificador único del cliente cuyos registros se desean consultar.
     * @return Una {@link List} de objetos {@link Cita}. Si el cliente no tiene citas, 
     *         la lista se devolverá vacía pero no {@code null}.
     * @throws SQLException Si ocurre un error en la comunicación con SQL Server o si el 
     *                      procedimiento almacenado {@code SP_OBTENER_CITAS_POR_CLIENTE} 
     *                      no devuelve las columnas esperadas.
     */
    @Override
    public List<Cita> obtenerPorCliente(int idCliente) throws SQLException {
        List<Cita> lista = new ArrayList<>();
        // Nota: Asegúrate de que este SP en la DB incluya los JOINS con MASCOTA y VETERINARIO
        String sql = "{call SP_OBTENER_CITAS_POR_CLIENTE(?)}";
        
        try (Connection conn = DatabaseConnection.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {
            
            stmt.setInt(1, idCliente);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    // Aplicamos el mapeo centralizado para cumplir con la POO
                    lista.add(mapResultSetToCita(rs));
                }
            }
        }
        return lista;
    }
    
    /**
     * Recupera una cita médica específica de la base de datos por su identificador único.
     * Este método realiza un mapeo completo (Eager Loading manual), cargando los objetos
     * asociados de Cliente, Mascota, Servicio y Veterinario para cumplir con el modelo POO.
     *
     * @param idCita El identificador único de la cita en la base de datos.
     * @return Un objeto {@link Cita} con todas sus dependencias cargadas, 
     *         o {@code null} si no se encuentra ningún registro con ese ID.
     * @throws SQLException Si ocurre un error durante la ejecución del procedimiento 
     *                      almacenado o la conexión con la base de datos.
     */
    @Override
    public Cita obtenerPorId(int idCita) throws SQLException {
        String sql = "{call SP_OBTENER_CITA_COMPLETA(?)}";
        
        try (Connection conn = DatabaseConnection.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {
            
            stmt.setInt(1, idCita);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    // Reutilizamos el método de mapeo para mantener la consistencia
                    return mapResultSetToCita(rs);
                }
            }
        }
        return null;
    }

    /**
     * Registra una nueva cita médica en el sistema.
     * Este método extrae los identificadores de los objetos relacionados 
     * (Cliente, Mascota, Servicio, Veterinario) y los envía al procedimiento 
     * almacenado para su persistencia.
     * 
     * <p>La lógica de negocio en la base de datos verifica la disponibilidad 
     * del médico en el horario solicitado.</p>
     *
     * @param cita El objeto {@link Cita} que contiene toda la información a persistir.
     *             Debe tener sus objetos internos (Cliente, Mascota, etc.) debidamente instanciados.
     * @return El identificador único (ID_CITA) generado por la base de datos. 
     *         Retorna {@code -1} si el horario del médico está ocupado, 
     *         o {@code -2} si ocurre un error de integridad.
     * @throws SQLException Si ocurre un error de conectividad o si algún objeto interno es {@code null}.
     */
    @Override
    public int agendar(Cita cita) throws SQLException {
        // El SP ahora recibe 6 parámetros: Cliente, Mascota, Servicio, Veterinario, Fecha y Obs.
        String sql = "{call SP_AGENDAR_CITA(?, ?, ?, ?, ?, ?)}";
        
        try (Connection conn = DatabaseConnection.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {
            
            // Extracción de datos desde el modelo de objetos (POO)
            stmt.setInt(1, cita.getCliente().getIdCliente());
            stmt.setInt(2, cita.getMascota().getIdMascota());
            stmt.setInt(3, cita.getServicio().getIdServicio());
            stmt.setInt(4, cita.getVeterinario().getIdVeterinario());
            
            // Conversión de java.util.Date a java.sql.Timestamp
            stmt.setTimestamp(5, new Timestamp(cita.getFechaHora().getTime()));
            
            // Las observaciones pueden ser nulas
            stmt.setString(6, cita.getObservaciones());
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("ID_CITA");
                }
            }
            return -2; // Error de ejecución si no devuelve un ResultSet
        }
    }

    /**
     * Actualiza la información integral de una cita existente en la base de datos.
     * Este método sincroniza el estado del objeto {@link Cita} con el registro físico,
     * incluyendo la actualización de los entes relacionados (Mascota, Servicio, Veterinario)
     * y la auditoría de observaciones.
     *
     * @param cita El objeto {@link Cita} con los datos actualizados. Debe contener 
     *             un {@code idCita} válido que ya exista en la base de datos.
     * @return {@code true} si la actualización fue exitosa y se afectó exactamente un registro; 
     *         {@code false} en caso contrario.
     * @throws SQLException Si ocurre un error de restricción de integridad (FK) o 
     *                      problemas en la conexión con SQL Server.
     */
    @Override
    public boolean actualizar(Cita cita) throws SQLException {
        // El SP ahora recibe 8 parámetros tras incluir al Veterinario y la Mascota directa
        String sql = "{call SP_ACTUALIZAR_CITA_COMPLETA(?, ?, ?, ?, ?, ?, ?, ?)}";
        
        try (Connection conn = DatabaseConnection.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {
            
            // Mapeo de IDs desde los objetos del modelo POO
            stmt.setInt(1, cita.getIdCita());
            stmt.setInt(2, cita.getCliente().getIdCliente());
            stmt.setInt(3, cita.getMascota().getIdMascota());
            stmt.setInt(4, cita.getServicio().getIdServicio());
            stmt.setInt(5, cita.getVeterinario().getIdVeterinario());
            
            // Manejo de tiempos y estados
            stmt.setTimestamp(6, new Timestamp(cita.getFechaHora().getTime()));
            stmt.setString(7, cita.getEstado());
            stmt.setString(8, cita.getObservaciones());
            
            try (ResultSet rs = stmt.executeQuery()) {
                // El SP devuelve un SELECT 1 AS RESULTADO si todo salió bien
                return rs.next() && rs.getInt("RESULTADO") == 1;
            }
        }
    }

    /**
     * Cancela una cita médica registrada en el sistema, estableciendo su estado 
     * en 'CANCELADA' y documentando la razón de la anulación.
     * Este método invoca un procedimiento almacenado que protege la integridad 
     * de los datos al concatenar el motivo de cancelación en el campo de 
     * observaciones para fines históricos.
     *
     * @param idCita El identificador único de la cita que se desea anular.
     * @param motivo El texto descriptivo que explica la razón de la cancelación.
     * @return {@code true} si la cita se canceló correctamente en la base de datos; 
     *         {@code false} si el ID proporcionado no existe o la operación no pudo completarse.
     * @throws SQLException Si ocurre una interrupción en la conexión con SQL Server o 
     *                      error en los parámetros del procedimiento {@code SP_CANCELAR_CITA}.
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
     * Recupera el listado completo de todas las citas registradas en la base de datos.
     * Este método es fundamental para la gestión administrativa, ya que devuelve la 
     * colección total de objetos {@link Cita} con su jerarquía de objetos 
     * (Cliente, Mascota, Servicio, Veterinario) completamente hidratada.
     *
     * @return Una {@link List} que contiene todos los registros de citas. 
     *         Si no hay registros, devuelve una lista vacía.
     * @throws SQLException Si ocurre un fallo en la conexión o si el procedimiento 
     *                      almacenado {@code SP_OBTENER_TODAS_LAS_CITAS} falla.
     */
    @Override
    public List<Cita> obtenerTodas() throws SQLException {
        List<Cita> lista = new ArrayList<>();
        String sql = "{call SP_OBTENER_TODAS_LAS_CITAS}";
        
        try (Connection conn = DatabaseConnection.getConnection();
             CallableStatement stmt = conn.prepareCall(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                // Delegamos la responsabilidad de armado del objeto al método privado
                lista.add(mapResultSetToCita(rs));
            }
        }
        return lista;
    }

    /**
     * Consulta y devuelve un listado de citas comprendidas dentro de un periodo de tiempo.
     * Este método es ideal para reportes financieros o de gestión de carga de trabajo,
     * devolviendo los objetos {@link Cita} con todas sus relaciones (Cliente, Mascota, 
     * Veterinario, Servicio) cargadas mediante Eager Loading manual.
     *
     * @param fechaInicio Fecha inicial del rango de búsqueda (inclusive).
     * @param fechaFin    Fecha final del rango de búsqueda (inclusive).
     * @return Una {@link List} de citas encontradas en el rango. Si no hay resultados, 
     *         la lista estará vacía.
     * @throws SQLException Si hay errores en los parámetros de entrada o en la 
     *                      comunicación con el procedimiento {@code SP_OBTENER_CITAS_POR_RANGO_FECHAS}.
     */
    @Override
    public List<Cita> obtenerPorRangoFechas(java.util.Date fechaInicio, java.util.Date fechaFin) throws SQLException {
        List<Cita> lista = new ArrayList<>();
        String sql = "{call SP_OBTENER_CITAS_POR_RANGO_FECHAS(?, ?)}";
        
        try (Connection conn = DatabaseConnection.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {  
            // Conversión de tipos Date de Java a Date de SQL
            stmt.setDate(1, new java.sql.Date(fechaInicio.getTime()));
            stmt.setDate(2, new java.sql.Date(fechaFin.getTime()));
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    // Reutilizamos la lógica de hidratación de objetos
                    lista.add(mapResultSetToCita(rs));
                }
            }
        }
        return lista;
    }

    /**
     * Filtra y recupera una lista de citas basadas en el servicio prestado, el veterinario 
     * asignado y el estado actual de la cita. 
     * Es ideal para paneles de control médicos donde se requiere ver, por ejemplo, 
     * todas las "Consultas Generales" del "Dr. Smith" que estén en estado "PENDIENTE".
     *
     * @param idServicio    Identificador del servicio a filtrar.
     * @param idVeterinario Identificador del veterinario asignado.
     * @param estado        Estado de la cita ('PENDIENTE', 'REALIZADA', 'CANCELADA').
     * @return Una {@link List} de objetos {@link Cita} que cumplen con los criterios.
     *         Devuelve una lista vacía si no hay coincidencias.
     * @throws SQLException Si ocurre un error en la ejecución del procedimiento 
     *                      {@code SP_OBTENER_CITAS_POR_SERVICIO_VETERINARIO}.
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
                    // Hidratamos el objeto completo siguiendo el estándar POO
                    lista.add(mapResultSetToCita(rs));
                }
            }
        }
        return lista;
    }

    /**
     * Modifica exclusivamente el estado de una cita en la base de datos.
     * Este método es utilizado para transiciones rápidas de estado, como 
     * cancelaciones manuales o cambios de flujo administrativo, sin alterar 
     * el resto de la información de la cita.
     *
     * @param idCita El identificador único de la cita a modificar.
     * @param estado El nuevo estado de la cita (Ej: 'PENDIENTE', 'REALIZADA', 'CANCELADA').
     * @return {@code true} si el estado se actualizó correctamente; 
     *         {@code false} si el ID no existe o no se realizaron cambios.
     * @throws SQLException Si el estado proporcionado no es válido según la 
     *                      restricción CHECK de la base de datos.
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
     * <b>Nota de seguridad:</b> Este método realiza una eliminación física. 
     * Se recomienda validar que la cita no tenga dependencias críticas 
     * (como facturas pagadas) antes de invocar este procedimiento.
     *
     * @param idCita El identificador único de la cita que se desea eliminar.
     * @return {@code true} si la operación se completó exitosamente; 
     *         {@code false} si no se encontró el registro o no pudo ser borrado.
     * @throws SQLException Si la cita tiene integridad referencial activa con 
     *                      otras tablas que impiden su eliminación.
     */
    @Override
    public boolean eliminar(int idCita) throws SQLException {
        String sql = "{call SP_ELIMINAR_CITA_COMPLETA(?)}";
        
        try (Connection conn = DatabaseConnection.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {
            
            stmt.setInt(1, idCita);
            
            // Ejecutamos la actualización. Si el SP no devuelve ResultSet, 
            // executeUpdate nos dirá cuántas filas se borraron.
            int filasAfectadas = stmt.executeUpdate();
            return filasAfectadas > 0;
        }
    }
    
    /**
    * Método auxiliar para transformar una fila de la BD en un objeto Cita completo.
    */
    private Cita mapResultSetToCita(ResultSet rs) throws SQLException {
        Cita cita = new Cita();

        // --- CAMPOS PRINCIPALES ---
        cita.setIdCita(rs.getInt("ID_CITA"));
        cita.setFechaHora(rs.getTimestamp("FECHA_HORA"));
        cita.setObservaciones(rs.getString("OBSERVACIONES"));

        //  IMPORTANTE: Manejar ESTADO con try-catch si no existe la columna
        try {
            cita.setEstado(rs.getString("ESTADO"));
        } catch (SQLException e) {
            // Si no existe la columna ESTADO, usar valor por defecto
            cita.setEstado("PENDIENTE");
        }

        // FECHA_REGISTRO puede no estar en todos los SP
        try {
            cita.setFechaRegistro(rs.getTimestamp("FECHA_REGISTRO"));
        } catch (SQLException e) {
            // Campo no presente en este SP
        }

        // --- MAPEO DE OBJETOS ASOCIADOS ---

        // 1. Cliente
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
        try {
            cliente.setCedula(rs.getString("CEDULA_CLIENTE"));
        } catch (SQLException e) {
            // No existe la columna
        }
        try {
            cliente.setTelefono(rs.getString("TELEFONO_CLIENTE"));
        } catch (SQLException e) {
            // No existe la columna
        }
        cita.setCliente(cliente);

        // 2. Mascota
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
        try {
            mascota.setEspecie(rs.getString("ESPECIE"));
        } catch (SQLException e) {
            // No existe la columna
        }
        try {
            mascota.setRaza(rs.getString("RAZA"));
        } catch (SQLException e) {
            // No existe la columna
        }
        cita.setMascota(mascota);

        // 3. Servicio
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

        // 4. Veterinario
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
        try {
            vete.setApellido(rs.getString("APELLIDO_VETERINARIO"));
        } catch (SQLException e) {
            vete.setApellido("");
        }
        cita.setVeterinario(vete);

        return cita;
    }
   /**
 * Obtiene todas las citas con estado PENDIENTE
 * @return Lista de citas pendientes
 * @throws SQLException Si ocurre un error en la ejecución del procedimiento
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
    }
    return lista;
}
}