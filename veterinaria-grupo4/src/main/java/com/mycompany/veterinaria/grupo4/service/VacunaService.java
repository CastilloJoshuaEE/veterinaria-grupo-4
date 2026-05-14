package com.mycompany.veterinaria.grupo4.service;

import com.mycompany.veterinaria.grupo4.model.dao.IVacunaDAO;
import com.mycompany.veterinaria.grupo4.model.entity.VacunaAplicada;
import com.mycompany.veterinaria.grupo4.model.impl.VacunaDAOImpl;
import org.springframework.stereotype.Service;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Servicio para la gestion de vacunas aplicadas con validaciones de negocio.
 * <p>
 * Proporciona la capa de servicios para las operaciones de negocio
 * relacionadas con las vacunas aplicadas a las mascotas, incluyendo
 * listado por mascota, registro, actualizacion, verificacion de
 * existencia y consulta de vacunas proximas a vencer.
 * </p>
 * 
 * <p><b>Reglas de negocio implementadas:</b></p>
 * <ul>
 *   <li>El nombre de la vacuna es obligatorio</li>
 *   <li>El periodo de refuerzo debe ser positivo (entre 1 y 60 meses)</li>
 *   <li>La fecha de aplicacion no puede ser futura</li>
 *   <li>No se puede registrar la misma vacuna dos veces en el mismo periodo</li>
 *   <li>La fecha proxima se calcula automaticamente</li>
 * </ul>
 * 
 * <p><b>Fecha de inicio del proyecto:</b> 15/04/2026</p>
 * 
 * @author CASTILLO MEREJILDO JOSHUA JAVIER – MODULO: MASCOTA
 * @version 2.0
 * @since 1.0
 */
@Service
public class VacunaService {
    
    private static final int NOMBRE_MAX_LENGTH = 50;
    private static final int DESCRIPCION_MAX_LENGTH = 255;
    private static final int PERIODO_MINIMO_MESES = 1;
    private static final int PERIODO_MAXIMO_MESES = 60;
    
    private IVacunaDAO vacunaDAO = new VacunaDAOImpl();

    /**
     * Lista todas las vacunas aplicadas a una mascota.
     *
     * @param idMascota identificador de la mascota (debe ser > 0)
     * @return lista de vacunas aplicadas o null si hay error
     * @throws IllegalArgumentException si el id es invalido
     */
    public List<VacunaAplicada> listarPorMascota(int idMascota) {
        if (idMascota <= 0) {
            throw new IllegalArgumentException("ID de mascota invalido: " + idMascota);
        }
        try {
            return vacunaDAO.obtenerPorMascota(idMascota);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Valida y registra una nueva vacuna aplicada.
     * <p>
     * <b>Reglas de negocio:</b>
     * <ul>
     *   <li>La mascota debe existir</li>
     *   <li>La fecha de aplicacion no puede ser futura</li>
     *   <li>El periodo de refuerzo debe ser valido</li>
     *   <li>No se puede registrar la misma vacuna antes de la fecha de proxima aplicacion</li>
     * </ul>
     * </p>
     *
     * @param vacuna objeto VacunaAplicada a registrar
     * @return ID de la vacuna registrada
     * @throws IllegalArgumentException si los datos son invalidos
     * @throws IllegalStateException si la vacuna ya fue aplicada recientemente
     */
    public int registrar(VacunaAplicada vacuna) {
        validarVacuna(vacuna);
        
        // Validar que la mascota exista
        MascotaService mascotaService = new MascotaService();
        if (mascotaService.obtenerPorId(vacuna.getIdMascota()) == null) {
            throw new IllegalArgumentException("La mascota con ID " + vacuna.getIdMascota() + " no existe");
        }
        
        // Validar fecha de aplicacion no sea futura
        Date ahora = new Date();
        if (vacuna.getFechaAplicacion().after(ahora)) {
            throw new IllegalArgumentException("La fecha de aplicacion no puede ser futura");
        }
        
        // Verificar si ya existe una vacuna con el mismo nombre que aun no requiere refuerzo
        VacunaAplicada existente = verificarExistente(vacuna.getIdMascota(), vacuna.getNombre());
        if (existente != null) {
            Date fechaProxima = existente.getFechaProxima();
            if (fechaProxima != null && fechaProxima.after(ahora)) {
                throw new IllegalStateException("La mascota ya tiene una vacuna '" + vacuna.getNombre() + 
                    "' registrada. La proxima aplicacion es el " + fechaProxima);
            }
        }
        
        // Calcular fecha proxima
        Calendar cal = Calendar.getInstance();
        cal.setTime(vacuna.getFechaAplicacion());
        cal.add(Calendar.MONTH, vacuna.getPeriodoMeses());
        vacuna.setFechaProxima(cal.getTime());
        
        try {
            return vacunaDAO.registrar(vacuna);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error al registrar la vacuna", e);
        }
    }

    /**
     * Valida y actualiza los datos de una vacuna aplicada.
     *
     * @param vacuna objeto VacunaAplicada con datos actualizados
     * @return true si la actualizacion fue exitosa
     * @throws IllegalArgumentException si los datos son invalidos
     * @throws IllegalStateException si la vacuna no existe
     */
    public boolean actualizar(VacunaAplicada vacuna) {
        validarVacuna(vacuna);
        
        if (vacuna.getIdVacuna() <= 0) {
            throw new IllegalArgumentException("ID de vacuna invalido para actualizar");
        }
        
        // Validar que la mascota exista
        MascotaService mascotaService = new MascotaService();
        if (mascotaService.obtenerPorId(vacuna.getIdMascota()) == null) {
            throw new IllegalArgumentException("La mascota con ID " + vacuna.getIdMascota() + " no existe");
        }
        
        // Recalcular fecha proxima
        Calendar cal = Calendar.getInstance();
        cal.setTime(vacuna.getFechaAplicacion());
        cal.add(Calendar.MONTH, vacuna.getPeriodoMeses());
        vacuna.setFechaProxima(cal.getTime());
        
        try {
            return vacunaDAO.actualizar(vacuna);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error al actualizar la vacuna", e);
        }
    }

    /**
     * Verifica si ya existe una vacuna con el mismo nombre para una mascota.
     *
     * @param idMascota identificador de la mascota
     * @param nombreVacuna nombre de la vacuna
     * @return objeto VacunaAplicada si existe, null en caso contrario
     * @throws IllegalArgumentException si los parametros son invalidos
     */
    public VacunaAplicada verificarExistente(int idMascota, String nombreVacuna) {
        if (idMascota <= 0) {
            throw new IllegalArgumentException("ID de mascota invalido: " + idMascota);
        }
        if (nombreVacuna == null || nombreVacuna.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de la vacuna es obligatorio");
        }
        try {
            return vacunaDAO.verificarExistente(idMascota, nombreVacuna);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Lista las vacunas proximas a vencer en los proximos dias.
     *
     * @param diasAnticipacion numero de dias de anticipacion (debe ser > 0)
     * @return lista de vacunas proximas a vencer o null si hay error
     * @throws IllegalArgumentException si los dias son invalidos
     */
    public List<VacunaAplicada> listarProximasAVencer(int diasAnticipacion) {
        if (diasAnticipacion <= 0) {
            throw new IllegalArgumentException("Los dias de anticipacion deben ser mayores a 0");
        }
        if (diasAnticipacion > 365) {
            throw new IllegalArgumentException("Los dias de anticipacion no pueden exceder 365");
        }
        try {
            return vacunaDAO.obtenerProximasAVencer(diasAnticipacion);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * Valida los campos de una vacuna aplicada.
     *
     * @param vacuna objeto a validar
     * @throws IllegalArgumentException si algun campo es invalido
     */
    private void validarVacuna(VacunaAplicada vacuna) {
        if (vacuna == null) {
            throw new IllegalArgumentException("La vacuna no puede ser nula");
        }
        
        if (vacuna.getIdMascota() <= 0) {
            throw new IllegalArgumentException("Debe especificar una mascota valida");
        }
        
        if (vacuna.getNombre() == null || vacuna.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de la vacuna es obligatorio");
        }
        if (vacuna.getNombre().trim().length() > NOMBRE_MAX_LENGTH) {
            throw new IllegalArgumentException("El nombre de la vacuna no puede exceder los " + NOMBRE_MAX_LENGTH + " caracteres");
        }
        
        if (vacuna.getDescripcion() != null && vacuna.getDescripcion().trim().length() > DESCRIPCION_MAX_LENGTH) {
            throw new IllegalArgumentException("La descripcion no puede exceder los " + DESCRIPCION_MAX_LENGTH + " caracteres");
        }
        
        if (vacuna.getPeriodoMeses() < PERIODO_MINIMO_MESES) {
            throw new IllegalArgumentException("El periodo de refuerzo debe ser al menos " + PERIODO_MINIMO_MESES + " mes");
        }
        if (vacuna.getPeriodoMeses() > PERIODO_MAXIMO_MESES) {
            throw new IllegalArgumentException("El periodo de refuerzo no puede exceder " + PERIODO_MAXIMO_MESES + " meses");
        }
        
        if (vacuna.getFechaAplicacion() == null) {
            throw new IllegalArgumentException("La fecha de aplicacion es obligatoria");
        }
    }
}