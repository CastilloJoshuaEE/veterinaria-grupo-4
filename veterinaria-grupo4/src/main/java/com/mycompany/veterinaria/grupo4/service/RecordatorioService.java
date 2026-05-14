package com.mycompany.veterinaria.grupo4.service;

import com.mycompany.veterinaria.grupo4.config.RecordatorioConfig;
import com.mycompany.veterinaria.grupo4.model.dao.IRecordatorioDAO;
import com.mycompany.veterinaria.grupo4.model.entity.Recordatorio;
import com.mycompany.veterinaria.grupo4.model.impl.RecordatorioDAOImpl;
import org.springframework.stereotype.Service;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

/**
 * Servicio para la gestion de recordatorios con validaciones de negocio.
 * <p>
 * Proporciona la capa de servicios para las operaciones de negocio
 * relacionadas con los recordatorios del sistema, incluyendo obtener
 * recordatorios pendientes, marcar como leidos, generar recordatorios
 * automaticos y administrar las configuraciones de recordatorios.
 * </p>
 * 
 * <p><b>Reglas de negocio implementadas:</b></p>
 * <ul>
 *   <li>El usuario debe existir en el sistema</li>
 *   <li>No se pueden generar recordatorios duplicados en el mismo dia</li>
 *   <li>Las configuraciones deben tener valores de anticipacion validos</li>
 *   <li>El mensaje del recordatorio no puede estar vacio</li>
 * </ul>
 * 
 * <p><b>Fecha de inicio del proyecto:</b> 15/04/2026</p>
 * 
 * @author CHILAN CHILAN DANNY ANDRES – MODULO: AGENDAMIENTO DE CITA
 * @version 2.0
 * @since 1.0
 */
@Service
public class RecordatorioService {
    
    private static final int MENSAJE_MAX_LENGTH = 500;
    private static final int MENSAJE_CONFIG_MAX_LENGTH = 255;
    
    private IRecordatorioDAO recordatorioDAO = new RecordatorioDAOImpl();

    /**
     * Obtiene los recordatorios pendientes para un usuario.
     *
     * @param idUsuario identificador del usuario (debe ser > 0)
     * @return lista de recordatorios pendientes o null si hay error
     * @throws IllegalArgumentException si el id es invalido
     */
    public List<Recordatorio> obtenerPendientes(int idUsuario) {
        if (idUsuario <= 0) {
            throw new IllegalArgumentException("ID de usuario invalido: " + idUsuario);
        }
        try {
            return recordatorioDAO.obtenerPendientes(idUsuario);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Marca un recordatorio como leido.
     *
     * @param idRecordatorio identificador del recordatorio (debe ser > 0)
     * @return true si la operacion fue exitosa
     * @throws IllegalArgumentException si el id es invalido
     */
    public boolean marcarComoLeido(int idRecordatorio) {
        if (idRecordatorio <= 0) {
            throw new IllegalArgumentException("ID de recordatorio invalido: " + idRecordatorio);
        }
        try {
            return recordatorioDAO.marcarComoLeido(idRecordatorio);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Genera recordatorios automaticos para un usuario.
     *
     * @param idUsuario identificador del usuario (debe ser > 0)
     * @throws IllegalArgumentException si el id es invalido
     */
    public void generarRecordatorios(int idUsuario) {
        if (idUsuario <= 0) {
            throw new IllegalArgumentException("ID de usuario invalido: " + idUsuario);
        }
        try {
            recordatorioDAO.generarRecordatorios(idUsuario);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Lista todos los recordatorios en un rango de fechas.
     *
     * @param fechaInicio fecha de inicio (no puede ser nula)
     * @param fechaFin fecha de fin (no puede ser nula)
     * @return lista de recordatorios en el rango o null si hay error
     * @throws IllegalArgumentException si las fechas son invalidas
     */
    public List<Recordatorio> listarTodos(Date fechaInicio, Date fechaFin) {
        if (fechaInicio == null || fechaFin == null) {
            throw new IllegalArgumentException("Las fechas de inicio y fin son obligatorias");
        }
        if (fechaInicio.after(fechaFin)) {
            throw new IllegalArgumentException("La fecha de inicio no puede ser posterior a la fecha de fin");
        }
        try {
            return recordatorioDAO.obtenerTodos(fechaInicio, fechaFin);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Incrementa el contador de notificaciones de un recordatorio.
     *
     * @param idRecordatorio identificador del recordatorio (debe ser > 0)
     * @throws IllegalArgumentException si el id es invalido
     */
    public void incrementarContador(int idRecordatorio) {
        if (idRecordatorio <= 0) {
            throw new IllegalArgumentException("ID de recordatorio invalido: " + idRecordatorio);
        }
        try {
            recordatorioDAO.incrementarContador(idRecordatorio);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Obtiene el contador de notificaciones de un recordatorio.
     *
     * @param idRecordatorio identificador del recordatorio (debe ser > 0)
     * @return valor del contador
     * @throws IllegalArgumentException si el id es invalido
     */
    public int obtenerContador(int idRecordatorio) {
        if (idRecordatorio <= 0) {
            throw new IllegalArgumentException("ID de recordatorio invalido: " + idRecordatorio);
        }
        try {
            return recordatorioDAO.obtenerContador(idRecordatorio);
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * Valida y registra un nuevo recordatorio.
     *
     * @param recordatorio objeto Recordatorio a registrar
     * @param anticipacion tiempo de anticipacion (formato valido)
     * @return ID del recordatorio creado
     * @throws IllegalArgumentException si los datos son invalidos
     */
    public int registrar(Recordatorio recordatorio, String anticipacion) {
        validarRecordatorio(recordatorio);
        validarAnticipacion(anticipacion);
        
        try {
            return recordatorioDAO.registrar(recordatorio, anticipacion);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error al registrar el recordatorio", e);
        }
    }

    /**
     * Valida y actualiza un recordatorio existente.
     *
     * @param recordatorio objeto Recordatorio con datos actualizados
     * @return true si la actualizacion fue exitosa
     * @throws IllegalArgumentException si los datos son invalidos
     */
    public boolean actualizar(Recordatorio recordatorio) {
        if (recordatorio == null) {
            throw new IllegalArgumentException("El recordatorio no puede ser nulo");
        }
        if (recordatorio.getIdRecordatorio() <= 0) {
            throw new IllegalArgumentException("ID de recordatorio invalido");
        }
        
        String mensaje = recordatorio.getMensaje();
        if (mensaje == null || mensaje.trim().isEmpty()) {
            throw new IllegalArgumentException("El mensaje del recordatorio es obligatorio");
        }
        if (mensaje.trim().length() > MENSAJE_MAX_LENGTH) {
            throw new IllegalArgumentException("El mensaje no puede exceder los " + MENSAJE_MAX_LENGTH + " caracteres");
        }
        
        try {
            return recordatorioDAO.actualizar(recordatorio);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Elimina un recordatorio.
     *
     * @param idRecordatorio identificador del recordatorio (debe ser > 0)
     * @return true si la eliminacion fue exitosa
     * @throws IllegalArgumentException si el id es invalido
     */
    public boolean eliminar(int idRecordatorio) {
        if (idRecordatorio <= 0) {
            throw new IllegalArgumentException("ID de recordatorio invalido: " + idRecordatorio);
        }
        try {
            return recordatorioDAO.eliminar(idRecordatorio);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Obtiene todas las configuraciones de recordatorios.
     *
     * @return lista de configuraciones o null si hay error
     */
    public List<RecordatorioConfig> obtenerTodasConfiguraciones() {
        try {
            return recordatorioDAO.obtenerTodasConfiguraciones();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Valida y actualiza una configuracion de recordatorio.
     *
     * @param config objeto RecordatorioConfig con datos actualizados
     * @return true si la actualizacion fue exitosa
     * @throws IllegalArgumentException si los datos son invalidos
     */
    public boolean actualizarConfiguracion(RecordatorioConfig config) {
        validarConfiguracion(config);
        
        if (config.getIdConfig() <= 0) {
            throw new IllegalArgumentException("ID de configuracion invalido");
        }
        
        try {
            return recordatorioDAO.actualizarConfiguracion(config);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Valida y crea una nueva configuracion de recordatorio.
     *
     * @param config objeto RecordatorioConfig con los datos
     * @return ID de la configuracion creada
     * @throws IllegalArgumentException si los datos son invalidos
     */
    public int crearConfiguracion(RecordatorioConfig config) {
        validarConfiguracion(config);
        
        try {
            return recordatorioDAO.crearConfiguracion(config);
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }
    
    /**
     * Valida los campos de un recordatorio.
     *
     * @param recordatorio objeto a validar
     * @throws IllegalArgumentException si algun campo es invalido
     */
    private void validarRecordatorio(Recordatorio recordatorio) {
        if (recordatorio == null) {
            throw new IllegalArgumentException("El recordatorio no puede ser nulo");
        }
        
        if (recordatorio.getMensaje() == null || recordatorio.getMensaje().trim().isEmpty()) {
            throw new IllegalArgumentException("El mensaje del recordatorio es obligatorio");
        }
        if (recordatorio.getMensaje().trim().length() > MENSAJE_MAX_LENGTH) {
            throw new IllegalArgumentException("El mensaje no puede exceder los " + MENSAJE_MAX_LENGTH + " caracteres");
        }
        
        if (recordatorio.getTipo() == null || recordatorio.getTipo().trim().isEmpty()) {
            throw new IllegalArgumentException("El tipo de recordatorio es obligatorio");
        }
        String tipoUpper = recordatorio.getTipo().toUpperCase();
        if (!tipoUpper.equals("CITA") && !tipoUpper.equals("VACUNA")) {
            throw new IllegalArgumentException("El tipo debe ser CITA o VACUNA");
        }
    }
    
    /**
     * Valida la configuracion de recordatorio.
     *
     * @param config objeto a validar
     * @throws IllegalArgumentException si algun campo es invalido
     */
    private void validarConfiguracion(RecordatorioConfig config) {
        if (config == null) {
            throw new IllegalArgumentException("La configuracion no puede ser nula");
        }
        
        if (config.getTipoRecordatorio() == null || config.getTipoRecordatorio().trim().isEmpty()) {
            throw new IllegalArgumentException("El tipo de recordatorio es obligatorio");
        }
        String tipoUpper = config.getTipoRecordatorio().toUpperCase();
        if (!tipoUpper.equals("CITA") && !tipoUpper.equals("VACUNA")) {
            throw new IllegalArgumentException("El tipo debe ser CITA o VACUNA");
        }
        
        if (config.getAnticipacion() == null || config.getAnticipacion().trim().isEmpty()) {
            throw new IllegalArgumentException("La anticipacion es obligatoria");
        }
        
        if (config.getMensaje() == null || config.getMensaje().trim().isEmpty()) {
            throw new IllegalArgumentException("El mensaje es obligatorio");
        }
        if (config.getMensaje().trim().length() > MENSAJE_CONFIG_MAX_LENGTH) {
            throw new IllegalArgumentException("El mensaje no puede exceder los " + MENSAJE_CONFIG_MAX_LENGTH + " caracteres");
        }
    }
    
    /**
     * Valida el formato de anticipacion.
     *
     * @param anticipacion anticipacion a validar
     * @throws IllegalArgumentException si la anticipacion es invalida
     */
    private void validarAnticipacion(String anticipacion) {
        if (anticipacion == null || anticipacion.trim().isEmpty()) {
            throw new IllegalArgumentException("La anticipacion es obligatoria");
        }
        
        String[] valoresValidos = {"15_DIAS", "7_DIAS", "1_MES", "12_HORAS", "10_HORAS", "5_MINUTOS", "30_SEGUNDOS"};
        for (String valido : valoresValidos) {
            if (valido.equals(anticipacion)) {
                return;
            }
        }
        throw new IllegalArgumentException("Valor de anticipacion invalido. Valores permitidos: " + String.join(", ", valoresValidos));
    }
}