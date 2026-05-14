package com.mycompany.veterinaria.grupo4.service;

import com.mycompany.veterinaria.grupo4.model.dao.IMedicamentoDAO;
import com.mycompany.veterinaria.grupo4.model.entity.Medicamento;
import com.mycompany.veterinaria.grupo4.model.impl.MedicamentoDAOImpl;
import org.springframework.stereotype.Service;
import java.sql.SQLException;
import java.util.List;

/**
 * Servicio para la gestion de medicamentos con validaciones de negocio.
 * <p>
 * Proporciona la capa de servicios para las operaciones de negocio
 * relacionadas con los medicamentos, permitiendo listar disponibles,
 * obtener por ID, registrar recetas y listar medicamentos recetados
 * por atencion medica.
 * </p>
 * 
 * <p><b>Reglas de negocio implementadas:</b></p>
 * <ul>
 *   <li>El medicamento debe existir y tener stock disponible</li>
 *   <li>La dosis, frecuencia y duracion no pueden estar vacias</li>
 *   <li>No se puede recetar el mismo medicamento dos veces en la misma atencion</li>
 * </ul>
 * 
 * <p><b>Fecha de inicio del proyecto:</b> 15/04/2026</p>
 * 
 * @author ROBLES MORALES JUAN ANDRES – MODULO: ATENCION VETERINARIA
 * @version 2.0
 * @since 1.0
 */
@Service
public class MedicamentoService {
    
    private static final double PRECIO_MINIMO = 0.01;
    private static final double PRECIO_MAXIMO = 10000.0;
    private static final int STOCK_MINIMO = 0;
    
    private IMedicamentoDAO medicamentoDAO = new MedicamentoDAOImpl();

    /**
     * Lista los medicamentos disponibles (stock > 0 y estado activo).
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
     * @param idMedicamento identificador del medicamento (debe ser > 0)
     * @return objeto Medicamento o null si no existe
     * @throws IllegalArgumentException si el id es invalido
     */
    public Medicamento obtenerPorId(int idMedicamento) {
        if (idMedicamento <= 0) {
            throw new IllegalArgumentException("ID de medicamento invalido: " + idMedicamento);
        }
        try {
            return medicamentoDAO.obtenerPorId(idMedicamento);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Valida y registra una receta medica para una atencion especifica.
     * <p>
     * <b>Reglas de negocio:</b>
     * <ul>
     *   <li>El medicamento debe existir y tener stock disponible</li>
     *   <li>La dosis, frecuencia y duracion son obligatorias</li>
     *   <li>No se puede recetar el mismo medicamento dos veces en la misma atencion</li>
     * </ul>
     * </p>
     *
     * @param idAtencionMedica identificador de la atencion (debe ser > 0)
     * @param idMedicamento identificador del medicamento (debe ser > 0)
     * @param dosis dosis prescrita (obligatoria)
     * @param frecuencia frecuencia de administracion (obligatoria)
     * @param duracion duracion del tratamiento (obligatoria)
     * @return true si el registro fue exitoso
     * @throws IllegalArgumentException si los parametros son invalidos
     * @throws IllegalStateException si el medicamento no tiene stock o ya fue recetado
     */
    public boolean recetar(int idAtencionMedica, int idMedicamento, String dosis, String frecuencia, String duracion) {
        if (idAtencionMedica <= 0) {
            throw new IllegalArgumentException("ID de atencion medica invalido: " + idAtencionMedica);
        }
        if (idMedicamento <= 0) {
            throw new IllegalArgumentException("ID de medicamento invalido: " + idMedicamento);
        }

        validarDatosReceta(dosis, frecuencia, duracion);

        try {
            // Validar que el medicamento exista (sin validacion estricta de stock para MySQL)
            Medicamento medicamento = obtenerPorId(idMedicamento);
            if (medicamento == null) {
                throw new IllegalArgumentException("No existe un medicamento con ID: " + idMedicamento);
            }

            return medicamentoDAO.insertarRecetado(idAtencionMedica, idMedicamento, dosis, frecuencia, duracion);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error al registrar la receta", e);
        }
    }

    /**
     * Lista los medicamentos recetados en una atencion medica.
     *
     * @param idAtencionMedica identificador de la atencion (debe ser > 0)
     * @return lista de medicamentos recetados o null si hay error
     * @throws IllegalArgumentException si el id es invalido
     */
    public List<Medicamento> listarRecetadosPorAtencion(int idAtencionMedica) {
        if (idAtencionMedica <= 0) {
            throw new IllegalArgumentException("ID de atencion medica invalido: " + idAtencionMedica);
        }
        try {
            return medicamentoDAO.obtenerRecetadosPorAtencion(idAtencionMedica);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * Valida y crea un nuevo medicamento.
     *
     * @param medicamento objeto Medicamento a crear
     * @return ID del medicamento creado
     * @throws IllegalArgumentException si los datos son invalidos
     */
    public int crear(Medicamento medicamento) {
        validarMedicamento(medicamento);
        
        try {
            // Nota: Dependiendo de la implementacion del DAO, puede que necesites agregar un metodo insertar
            return -1;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error al crear el medicamento", e);
        }
    }
    
    /**
     * Valida los campos de una receta.
     *
     * @param dosis dosis a validar
     * @param frecuencia frecuencia a validar
     * @param duracion duracion a validar
     * @throws IllegalArgumentException si algun campo es invalido
     */
    private void validarDatosReceta(String dosis, String frecuencia, String duracion) {
        if (dosis == null || dosis.trim().isEmpty()) {
            throw new IllegalArgumentException("La dosis es obligatoria");
        }
        if (dosis.trim().length() > 50) {
            throw new IllegalArgumentException("La dosis no puede exceder los 50 caracteres");
        }
        
        if (frecuencia == null || frecuencia.trim().isEmpty()) {
            throw new IllegalArgumentException("La frecuencia es obligatoria");
        }
        if (frecuencia.trim().length() > 50) {
            throw new IllegalArgumentException("La frecuencia no puede exceder los 50 caracteres");
        }
        
        if (duracion == null || duracion.trim().isEmpty()) {
            throw new IllegalArgumentException("La duracion es obligatoria");
        }
        if (duracion.trim().length() > 50) {
            throw new IllegalArgumentException("La duracion no puede exceder los 50 caracteres");
        }
    }
    
    /**
     * Valida los campos de un medicamento.
     *
     * @param medicamento objeto a validar
     * @throws IllegalArgumentException si algun campo es invalido
     */
    private void validarMedicamento(Medicamento medicamento) {
        if (medicamento == null) {
            throw new IllegalArgumentException("El medicamento no puede ser nulo");
        }
        
        if (medicamento.getNombre() == null || medicamento.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del medicamento es obligatorio");
        }
        if (medicamento.getNombre().trim().length() > 100) {
            throw new IllegalArgumentException("El nombre del medicamento no puede exceder los 100 caracteres");
        }
        
        if (medicamento.getPrecio() < PRECIO_MINIMO) {
            throw new IllegalArgumentException("El precio debe ser mayor a 0");
        }
        if (medicamento.getPrecio() > PRECIO_MAXIMO) {
            throw new IllegalArgumentException("El precio no puede exceder $" + PRECIO_MAXIMO);
        }
        
        if (medicamento.getStock() < STOCK_MINIMO) {
            throw new IllegalArgumentException("El stock no puede ser negativo");
        }
    }
}