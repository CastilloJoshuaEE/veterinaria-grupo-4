package com.mycompany.veterinaria.grupo4.service;

import com.mycompany.veterinaria.grupo4.model.dao.ICitaDAO;
import com.mycompany.veterinaria.grupo4.model.entity.Cita;
import com.mycompany.veterinaria.grupo4.model.impl.CitaDAOImpl;
import org.springframework.stereotype.Service;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

/**
 * Servicio para la gestion de citas medicas con validaciones de negocio.
 * <p>
 * Proporciona la capa de servicios para las operaciones de negocio
 * relacionadas con el agendamiento, consulta, actualizacion y cancelacion
 * de citas medicas en el sistema veterinario.
 * </p>
 * 
 * <p><b>Reglas de negocio implementadas:</b></p>
 * <ul>
 *   <li>No se pueden agendar citas en horarios ya ocupados por el mismo veterinario</li>
 *   <li>La fecha de la cita debe ser futura</li>
 *   <li>No se pueden modificar citas ya realizadas</li>
 *   <li>Los estados validos son: PENDIENTE, CANCELADA, REALIZADA</li>
 * </ul>
 * 
 * <p><b>Fecha de inicio del proyecto:</b> 15/04/2026</p>
 * 
 * @author CHILAN CHILAN DANNY ANDRES – MODULO: AGENDAMIENTO DE CITA
 * @version 2.0
 * @since 1.0
 */
@Service
public class CitaService {
    
    private static final String ESTADO_PENDIENTE = "PENDIENTE";
    private static final String ESTADO_CANCELADA = "CANCELADA";
    private static final String ESTADO_REALIZADA = "REALIZADA";
    
    private ICitaDAO citaDAO = new CitaDAOImpl();

    /**
     * Lista las citas programadas para una fecha especifica.
     *
     * @param fecha fecha a consultar (no puede ser nula)
     * @return lista de citas en la fecha
     * @throws IllegalArgumentException si la fecha es nula
     */
    public List<Cita> listarPorFecha(Date fecha) {
        if (fecha == null) {
            throw new IllegalArgumentException("La fecha no puede ser nula");
        }
        try {
            return citaDAO.obtenerPorFecha(fecha);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Lista las citas asociadas a un cliente.
     *
     * @param idCliente identificador del cliente (debe ser > 0)
     * @return lista de citas del cliente
     * @throws IllegalArgumentException si el id es invalido
     */
    public List<Cita> listarPorCliente(int idCliente) {
        if (idCliente <= 0) {
            throw new IllegalArgumentException("ID de cliente invalido: " + idCliente);
        }
        try {
            return citaDAO.obtenerPorCliente(idCliente);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Obtiene una cita por su identificador.
     *
     * @param idCita identificador de la cita (debe ser > 0)
     * @return objeto Cita encontrado
     * @throws IllegalArgumentException si el id es invalido
     */
    public Cita obtenerPorId(int idCita) {
        if (idCita <= 0) {
            throw new IllegalArgumentException("ID de cita invalido: " + idCita);
        }
        try {
            return citaDAO.obtenerPorId(idCita);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Valida y agenda una nueva cita en el sistema.
     * <p>
     * <b>Reglas de negocio:</b>
     * <ul>
     *   <li>La fecha de la cita debe ser futura</li>
     *   <li>No puede haber conflicto de horario con el mismo veterinario</li>
     * </ul>
     * </p>
     *
     * @param cita objeto Cita a agendar
     * @return ID de la cita agendada
     * @throws IllegalArgumentException si los datos son invalidos
     * @throws IllegalStateException si hay conflicto de horario
     */
    public int agendar(Cita cita) {
        validarCita(cita);
        
        // Validar que la fecha sea futura
        Date ahora = new Date();
        if (cita.getFechaHora() != null && cita.getFechaHora().before(ahora)) {
            throw new IllegalArgumentException("No se puede agendar una cita en una fecha/hora pasada");
        }
        
        // Validar que el veterinario exista
        VeterinarioService veterinarioService = new VeterinarioService();
        if (cita.getVeterinario() == null || veterinarioService.obtenerPorId(cita.getVeterinario().getIdVeterinario()) == null) {
            throw new IllegalArgumentException("El veterinario especificado no existe");
        }
        
        // Validar que el servicio exista y este activo
        ServicioService servicioService = new ServicioService();
        var servicio = servicioService.obtenerPorId(cita.getServicio().getIdServicio());
        if (servicio == null) {
            throw new IllegalArgumentException("El servicio especificado no existe");
        }
        if (!servicio.isEstado()) {
            throw new IllegalStateException("El servicio especificado no esta activo");
        }
        
        // Validar que la mascota exista y pertenezca al cliente
        MascotaService mascotaService = new MascotaService();
        var mascota = mascotaService.obtenerPorId(cita.getMascota().getIdMascota());
        if (mascota == null) {
            throw new IllegalArgumentException("La mascota especificada no existe");
        }
        if (mascota.getIdCliente() != cita.getCliente().getIdCliente()) {
            throw new IllegalArgumentException("La mascota no pertenece al cliente especificado");
        }
        
        try {
            return citaDAO.agendar(cita);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error al agendar la cita", e);
        }
    }

    /**
     * Valida y actualiza los datos de una cita existente.
     *
     * @param cita objeto Cita con datos actualizados
     * @return true si la actualizacion fue exitosa
     * @throws IllegalArgumentException si los datos son invalidos
     * @throws IllegalStateException si la cita ya fue realizada
     */
    public boolean actualizar(Cita cita) {
        validarCita(cita);
        
        if (cita.getIdCita() <= 0) {
            throw new IllegalArgumentException("ID de cita invalido para actualizar");
        }
        
        // Validar que la cita exista
        Cita existente = obtenerPorId(cita.getIdCita());
        if (existente == null) {
            throw new IllegalArgumentException("No existe una cita con ID: " + cita.getIdCita());
        }
        
        // No se pueden modificar citas ya realizadas
        if (ESTADO_REALIZADA.equals(existente.getEstado())) {
            throw new IllegalStateException("No se puede modificar una cita ya realizada");
        }
        
        try {
            return citaDAO.actualizar(cita);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error al actualizar la cita", e);
        }
    }

    /**
     * Cancela una cita con un motivo especifico.
     *
     * @param idCita identificador de la cita
     * @param motivo razon de la cancelacion
     * @return true si la cancelacion fue exitosa
     * @throws IllegalArgumentException si el id es invalido o el motivo vacio
     * @throws IllegalStateException si la cita ya fue realizada o cancelada
     */
    public boolean cancelar(int idCita, String motivo) {
        if (idCita <= 0) {
            throw new IllegalArgumentException("ID de cita invalido: " + idCita);
        }
        
        if (motivo == null || motivo.trim().isEmpty()) {
            throw new IllegalArgumentException("Debe especificar un motivo para la cancelacion");
        }
        
        Cita existente = obtenerPorId(idCita);
        if (existente == null) {
            throw new IllegalArgumentException("No existe una cita con ID: " + idCita);
        }
        
        if (ESTADO_REALIZADA.equals(existente.getEstado())) {
            throw new IllegalStateException("No se puede cancelar una cita ya realizada");
        }
        
        if (ESTADO_CANCELADA.equals(existente.getEstado())) {
            throw new IllegalStateException("La cita ya se encuentra cancelada");
        }
        
        try {
            return citaDAO.cancelar(idCita, motivo);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error al cancelar la cita", e);
        }
    }

    /**
     * Lista todas las citas registradas.
     *
     * @return lista de todas las citas
     */
    public List<Cita> listarTodas() {
        try {
            return citaDAO.obtenerTodas();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Lista las citas dentro de un rango de fechas.
     *
     * @param fechaInicio fecha de inicio del rango
     * @param fechaFin fecha de fin del rango
     * @return lista de citas en el rango
     * @throws IllegalArgumentException si las fechas son invalidas
     */
    public List<Cita> listarPorRangoFechas(Date fechaInicio, Date fechaFin) {
        if (fechaInicio == null || fechaFin == null) {
            throw new IllegalArgumentException("Las fechas de inicio y fin son obligatorias");
        }
        if (fechaInicio.after(fechaFin)) {
            throw new IllegalArgumentException("La fecha de inicio no puede ser posterior a la fecha de fin");
        }
        try {
            return citaDAO.obtenerPorRangoFechas(fechaInicio, fechaFin);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Lista las citas filtradas por servicio, veterinario y estado.
     *
     * @param idServicio identificador del servicio
     * @param idVeterinario identificador del veterinario
     * @param estado estado de la cita
     * @return lista de citas que coinciden con los filtros
     * @throws IllegalArgumentException si los ids son invalidos
     */
    public List<Cita> listarPorServicioYVeterinario(int idServicio, int idVeterinario, String estado) {
        if (idServicio <= 0) {
            throw new IllegalArgumentException("ID de servicio invalido: " + idServicio);
        }
        if (idVeterinario <= 0) {
            throw new IllegalArgumentException("ID de veterinario invalido: " + idVeterinario);
        }
        if (estado != null && !estado.trim().isEmpty()) {
            validarEstado(estado);
        }
        try {
            return citaDAO.obtenerPorServicioYVeterinario(idServicio, idVeterinario, estado);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Actualiza el estado de una cita.
     *
     * @param idCita identificador de la cita
     * @param estado nuevo estado
     * @return true si la actualizacion fue exitosa
     * @throws IllegalArgumentException si el estado es invalido
     * @throws IllegalStateException si la transicion de estado no es permitida
     */
    public boolean actualizarEstado(int idCita, String estado) {
        if (idCita <= 0) {
            throw new IllegalArgumentException("ID de cita invalido: " + idCita);
        }
        
        validarEstado(estado);
        
        Cita existente = obtenerPorId(idCita);
        if (existente == null) {
            throw new IllegalArgumentException("No existe una cita con ID: " + idCita);
        }
        
        // Validar transiciones de estado permitidas
        if (ESTADO_REALIZADA.equals(existente.getEstado())) {
            throw new IllegalStateException("No se puede cambiar el estado de una cita ya realizada");
        }
        
        if (ESTADO_CANCELADA.equals(existente.getEstado()) && !ESTADO_CANCELADA.equals(estado)) {
            throw new IllegalStateException("No se puede reactivar una cita cancelada");
        }
        
        try {
            return citaDAO.actualizarEstado(idCita, estado);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error al actualizar el estado de la cita", e);
        }
    }

    /**
     * Elimina una cita del sistema.
     *
     * @param idCita identificador de la cita a eliminar
     * @return true si la eliminacion fue exitosa
     * @throws IllegalArgumentException si el id es invalido
     * @throws IllegalStateException si la cita ya tiene atencion medica
     */
    public boolean eliminar(int idCita) {
        if (idCita <= 0) {
            throw new IllegalArgumentException("ID de cita invalido: " + idCita);
        }
        
        Cita existente = obtenerPorId(idCita);
        if (existente == null) {
            throw new IllegalArgumentException("No existe una cita con ID: " + idCita);
        }
        
        // No se puede eliminar una cita que ya tiene atencion medica
        if (tieneAtencionMedica(idCita)) {
            throw new IllegalStateException("No se puede eliminar una cita que ya tiene una atencion medica asociada");
        }
        
        try {
            return citaDAO.eliminar(idCita);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error al eliminar la cita", e);
        }
    }
    
    /**
     * Obtiene todas las citas con estado pendiente.
     *
     * @return lista de citas pendientes
     */
    public List<Cita> listarPendientes() {
        try {
            return citaDAO.obtenerPendientes();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * Valida los datos basicos de una cita.
     *
     * @param cita objeto a validar
     * @throws IllegalArgumentException si algun campo es invalido
     */
    private void validarCita(Cita cita) {
        if (cita == null) {
            throw new IllegalArgumentException("La cita no puede ser nula");
        }
        
        if (cita.getCliente() == null || cita.getCliente().getIdCliente() <= 0) {
            throw new IllegalArgumentException("Debe especificar un cliente valido");
        }
        
        if (cita.getMascota() == null || cita.getMascota().getIdMascota() <= 0) {
            throw new IllegalArgumentException("Debe especificar una mascota valida");
        }
        
        if (cita.getServicio() == null || cita.getServicio().getIdServicio() <= 0) {
            throw new IllegalArgumentException("Debe especificar un servicio valido");
        }
        
        if (cita.getVeterinario() == null || cita.getVeterinario().getIdVeterinario() <= 0) {
            throw new IllegalArgumentException("Debe especificar un veterinario valido");
        }
        
        if (cita.getFechaHora() == null) {
            throw new IllegalArgumentException("La fecha y hora de la cita son obligatorias");
        }
        
        // Validar estado si viene presente
        if (cita.getEstado() != null && !cita.getEstado().trim().isEmpty()) {
            validarEstado(cita.getEstado());
        } else {
            cita.setEstado(ESTADO_PENDIENTE);
        }
    }
    
    /**
     * Valida que el estado sea uno de los permitidos.
     *
     * @param estado estado a validar
     * @throws IllegalArgumentException si el estado es invalido
     */
    private void validarEstado(String estado) {
        String estadoUpper = estado.toUpperCase();
        if (!estadoUpper.equals(ESTADO_PENDIENTE) && 
            !estadoUpper.equals(ESTADO_CANCELADA) && 
            !estadoUpper.equals(ESTADO_REALIZADA)) {
            throw new IllegalArgumentException("Estado invalido. Los estados permitidos son: PENDIENTE, CANCELADA, REALIZADA");
        }
    }
    
    /**
     * Verifica si una cita ya tiene una atencion medica asociada.
     *
     * @param idCita identificador de la cita
     * @return true si tiene atencion medica
     */
    private boolean tieneAtencionMedica(int idCita) {
        AtencionMedicaService atencionService = new AtencionMedicaService();
        List<?> atenciones = null;
        try {
            atenciones = atencionService.listarTodas();
        } catch (Exception e) {
            return false;
        }
        if (atenciones != null) {
            for (Object obj : atenciones) {
                try {
                    java.lang.reflect.Method m = obj.getClass().getMethod("getIdCita");
                    Integer idCitaAtencion = (Integer) m.invoke(obj);
                    if (idCitaAtencion != null && idCitaAtencion == idCita) {
                        return true;
                    }
                } catch (Exception e) {
                    // Ignorar errores de reflexion
                }
            }
        }
        return false;
    }
}