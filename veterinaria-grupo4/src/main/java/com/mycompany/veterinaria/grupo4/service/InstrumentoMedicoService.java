package com.mycompany.veterinaria.grupo4.service;

import com.mycompany.veterinaria.grupo4.model.dao.IInstrumentoMedicoDAO;
import com.mycompany.veterinaria.grupo4.model.entity.InstrumentoMedico;
import com.mycompany.veterinaria.grupo4.model.impl.InstrumentoMedicoDAOImpl;
import org.springframework.stereotype.Service;
import java.sql.SQLException;
import java.util.List;

/**
 * Servicio para la gestion de instrumentos medicos.
 * <p>
 * Proporciona la capa de servicios para las operaciones de negocio
 * relacionadas con los instrumentos e insumos medicos, permitiendo
 * listar disponibles, registrar su uso en atenciones y obtener
 * los utilizados por atencion.
 * </p>
 * 
 * <p><b>Fecha de inicio del proyecto:</b> 15/04/2026</p>
 * 
 * @author ROBLES MORALES JUAN ANDRES – MODULO: ATENCION VETERINARIA
 * @version 1.0
 * @since 1.0
 */
@Service
public class InstrumentoMedicoService {
    private IInstrumentoMedicoDAO instrumentoDAO = new InstrumentoMedicoDAOImpl();

    /**
     * Lista los instrumentos medicos disponibles (stock > 0).
     *
     * @return lista de instrumentos disponibles o null si hay error
     */
    public List<InstrumentoMedico> listarDisponibles() {
        try {
            return instrumentoDAO.obtenerDisponibles();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Obtiene un instrumento por su identificador.
     *
     * @param idInstrumento identificador del instrumento
     * @return objeto InstrumentoMedico o null si no existe
     */
    public InstrumentoMedico obtenerPorId(int idInstrumento) {
        try {
            return instrumentoDAO.obtenerPorId(idInstrumento);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Registra el uso de un instrumento en una atencion medica.
     *
     * @param idAtencionMedica identificador de la atencion
     * @param idInstrumento identificador del instrumento
     * @return true si el registro fue exitoso
     */
    public boolean registrarUso(int idAtencionMedica, int idInstrumento) {
        try {
            return instrumentoDAO.insertarUsado(idAtencionMedica, idInstrumento);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Lista los instrumentos usados en una atencion medica especifica.
     *
     * @param idAtencionMedica identificador de la atencion
     * @return lista de instrumentos usados o null si hay error
     */
    public List<InstrumentoMedico> listarUsadosPorAtencion(int idAtencionMedica) {
        try {
            return instrumentoDAO.obtenerUsadosPorAtencion(idAtencionMedica);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}