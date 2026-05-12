package com.mycompany.veterinaria.grupo4.service;

import com.mycompany.veterinaria.grupo4.model.dao.IAtencionMedicaDAO;
import com.mycompany.veterinaria.grupo4.model.entity.AtencionMedica;
import com.mycompany.veterinaria.grupo4.model.impl.AtencionMedicaDAOImpl;
import org.springframework.stereotype.Service;
import java.sql.SQLException;
import java.util.List;

/**
 * Servicio para la gestion de atenciones medicas.
 * <p>
 * Proporciona la capa de servicios para las operaciones de negocio
 * relacionadas con atenciones medicas. Actua como intermediario entre
 * los controladores REST y la capa de acceso a datos (DAO).
 * </p>
 * 
 * <p><b>Fecha de inicio del proyecto:</b> 15/04/2026</p>
 * 
 * @author ROBLES MORALES JUAN ANDRES – MODULO: ATENCION VETERINARIA
 * @version 1.0
 * @since 1.0
 */
@Service
public class AtencionMedicaService {
    private IAtencionMedicaDAO atencionDAO = new AtencionMedicaDAOImpl();

    /**
     * Guarda una nueva atencion medica en la base de datos.
     *
     * @param atencion objeto AtencionMedica a guardar
     * @return ID de la atencion medica creada, o -1 si hay error
     */
    public int guardar(AtencionMedica atencion) {
        try {
            return atencionDAO.insertar(atencion);
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * Lista todas las atenciones medicas registradas.
     *
     * @return lista de atenciones medicas o null si hay error
     */
    public List<AtencionMedica> listarTodas() {
        try {
            return atencionDAO.obtenerTodas();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Obtiene una atencion medica por su identificador.
     *
     * @param idAtencionMedica identificador de la atencion
     * @return objeto AtencionMedica o null si no existe
     */
    public AtencionMedica obtenerPorId(int idAtencionMedica) {
        try {
            return atencionDAO.obtenerPorId(idAtencionMedica);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Elimina una atencion medica de la base de datos.
     *
     * @param idAtencionMedica identificador de la atencion a eliminar
     * @return true si la eliminacion fue exitosa
     */
    public boolean eliminar(int idAtencionMedica) {
        try {
            return atencionDAO.eliminar(idAtencionMedica);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}