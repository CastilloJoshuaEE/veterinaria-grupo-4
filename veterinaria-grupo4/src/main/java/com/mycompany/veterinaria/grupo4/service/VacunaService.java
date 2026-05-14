package com.mycompany.veterinaria.grupo4.service;

import com.mycompany.veterinaria.grupo4.model.dao.IVacunaDAO;
import com.mycompany.veterinaria.grupo4.model.entity.VacunaAplicada;
import com.mycompany.veterinaria.grupo4.model.impl.VacunaDAOImpl;
import org.springframework.stereotype.Service;
import java.sql.SQLException;
import java.util.List;

/**
 * Servicio para la gestion de vacunas aplicadas.
 * <p>
 * Proporciona la capa de servicios para las operaciones de negocio
 * relacionadas con las vacunas aplicadas a las mascotas, incluyendo
 * listado por mascota, registro, actualizacion, verificacion de
 * existencia y consulta de vacunas proximas a vencer.
 * </p>
 * 
 * <p><b>Fecha de inicio del proyecto:</b> 15/04/2026</p>
 * 
 * @author CASTILLO MEREJILDO JOSHUA JAVIER – MODULO: MASCOTA
 * @version 1.0
 * @since 1.0
 */
@Service
public class VacunaService {
    private IVacunaDAO vacunaDAO = new VacunaDAOImpl();

    /**
     * Lista todas las vacunas aplicadas a una mascota.
     *
     * @param idMascota identificador de la mascota
     * @return lista de vacunas aplicadas o null si hay error
     */
    public List<VacunaAplicada> listarPorMascota(int idMascota) {
        try {
            return vacunaDAO.obtenerPorMascota(idMascota);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Registra una nueva vacuna aplicada.
     *
     * @param vacuna objeto VacunaAplicada a registrar
     * @return ID de la vacuna registrada o -1 si hay error
     */
    public int registrar(VacunaAplicada vacuna) {
        try {
            return vacunaDAO.registrar(vacuna);
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * Actualiza los datos de una vacuna aplicada.
     *
     * @param vacuna objeto VacunaAplicada con datos actualizados
     * @return true si la actualizacion fue exitosa
     */
    public boolean actualizar(VacunaAplicada vacuna) {
        try {
            return vacunaDAO.actualizar(vacuna);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Verifica si ya existe una vacuna con el mismo nombre para una mascota.
     *
     * @param idMascota identificador de la mascota
     * @param nombreVacuna nombre de la vacuna
     * @return objeto VacunaAplicada si existe, null en caso contrario
     */
    public VacunaAplicada verificarExistente(int idMascota, String nombreVacuna) {
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
     * @param diasAnticipacion numero de dias de anticipacion
     * @return lista de vacunas proximas a vencer o null si hay error
     */
    public List<VacunaAplicada> listarProximasAVencer(int diasAnticipacion) {
        try {
            return vacunaDAO.obtenerProximasAVencer(diasAnticipacion);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}