package com.mycompany.veterinaria.grupo4.service;

import com.mycompany.veterinaria.grupo4.model.dao.IMedicamentoDAO;
import com.mycompany.veterinaria.grupo4.model.entity.Medicamento;
import com.mycompany.veterinaria.grupo4.model.impl.MedicamentoDAOImpl;
import org.springframework.stereotype.Service;
import java.sql.SQLException;
import java.util.List;

/**
 * Servicio para la gestion de medicamentos.
 * <p>
 * Proporciona la capa de servicios para las operaciones de negocio
 * relacionadas con los medicamentos, permitiendo listar disponibles,
 * obtener por ID, registrar recetas y listar medicamentos recetados
 * por atencion medica.
 * </p>
 * 
 * <p><b>Fecha de inicio del proyecto:</b> 15/04/2026</p>
 * 
 * @author ROBLES MORALES JUAN ANDRES – MODULO: ATENCION VETERINARIA
 * @version 1.0
 * @since 1.0
 */
@Service
public class MedicamentoService {
    private IMedicamentoDAO medicamentoDAO = new MedicamentoDAOImpl();

    /**
     * Lista los medicamentos disponibles (stock > 0).
     *
     * @return lista de medicamentos disponibles o null si hay error
     */
    public List<Medicamento> listarDisponibles() {
        try {
            return medicamentoDAO.obtenerDisponibles();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Obtiene un medicamento por su identificador.
     *
     * @param idMedicamento identificador del medicamento
     * @return objeto Medicamento o null si no existe
     */
    public Medicamento obtenerPorId(int idMedicamento) {
        try {
            return medicamentoDAO.obtenerPorId(idMedicamento);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Registra una receta medica para una atencion especifica.
     *
     * @param idAtencionMedica identificador de la atencion
     * @param idMedicamento identificador del medicamento
     * @param dosis dosis prescrita
     * @param frecuencia frecuencia de administracion
     * @param duracion duracion del tratamiento
     * @return true si el registro fue exitoso
     */
    public boolean recetar(int idAtencionMedica, int idMedicamento, String dosis, String frecuencia, String duracion) {
        try {
            return medicamentoDAO.insertarRecetado(idAtencionMedica, idMedicamento, dosis, frecuencia, duracion);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Lista los medicamentos recetados en una atencion medica.
     *
     * @param idAtencionMedica identificador de la atencion
     * @return lista de medicamentos recetados o null si hay error
     */
    public List<Medicamento> listarRecetadosPorAtencion(int idAtencionMedica) {
        try {
            return medicamentoDAO.obtenerRecetadosPorAtencion(idAtencionMedica);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}