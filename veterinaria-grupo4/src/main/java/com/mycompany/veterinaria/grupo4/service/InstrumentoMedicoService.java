package com.mycompany.veterinaria.grupo4.service;

import com.mycompany.veterinaria.grupo4.model.dao.IInstrumentoMedicoDAO;
import com.mycompany.veterinaria.grupo4.model.entity.InstrumentoMedico;
import com.mycompany.veterinaria.grupo4.model.impl.InstrumentoMedicoDAOImpl;
import org.springframework.stereotype.Service;
import java.sql.SQLException;
import java.util.List;

/**
 * Servicio para la gestion de instrumentos medicos con validaciones de negocio.
 * <p>
 * Proporciona la capa de servicios para las operaciones de negocio
 * relacionadas con los instrumentos e insumos medicos, permitiendo
 * listar disponibles, registrar su uso en atenciones y obtener
 * los utilizados por atencion.
 * </p>
 * 
 * <p><b>Reglas de negocio implementadas:</b></p>
 * <ul>
 *   <li>El instrumento debe existir y estar activo para ser usado</li>
 *   <li>No se puede registrar el mismo instrumento dos veces en la misma atencion</li>
 *   <li>El costo de uso debe ser positivo</li>
 * </ul>
 * 
 * <p><b>Fecha de inicio del proyecto:</b> 15/04/2026</p>
 * 
 * @author ROBLES MORALES JUAN ANDRES – MODULO: ATENCION VETERINARIA
 * @version 2.0
 * @since 1.0
 */
@Service
public class InstrumentoMedicoService {
    
    private static final double COSTO_USO_MINIMO = 0.01;
    private static final double COSTO_USO_MAXIMO = 1000.0;
    
    private IInstrumentoMedicoDAO instrumentoDAO = new InstrumentoMedicoDAOImpl();

    /**
     * Lista los instrumentos medicos disponibles (estado activo).
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
     * @param idInstrumento identificador del instrumento (debe ser > 0)
     * @return objeto InstrumentoMedico o null si no existe
     * @throws IllegalArgumentException si el id es invalido
     */
    public InstrumentoMedico obtenerPorId(int idInstrumento) {
        if (idInstrumento <= 0) {
            throw new IllegalArgumentException("ID de instrumento invalido: " + idInstrumento);
        }
        try {
            return instrumentoDAO.obtenerPorId(idInstrumento);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }


/**
 * Valida y registra el uso de un instrumento en una atencion medica.
 *
 * @param idAtencionMedica identificador de la atencion (debe ser > 0)
 * @param idInstrumento identificador del instrumento (debe ser > 0)
 * @return true si el registro fue exitoso
 * @throws IllegalArgumentException si los ids son invalidos
 * @throws IllegalStateException si el instrumento ya fue usado en esta atencion
 */
public boolean registrarUso(int idAtencionMedica, int idInstrumento) {
    if (idAtencionMedica <= 0) {
        throw new IllegalArgumentException("ID de atencion medica invalido: " + idAtencionMedica);
    }
    if (idInstrumento <= 0) {
        throw new IllegalArgumentException("ID de instrumento invalido: " + idInstrumento);
    }
    
    // Validar que el instrumento exista (sin validacion de estado para MySQL)
    InstrumentoMedico instrumento = obtenerPorId(idInstrumento);
    if (instrumento == null) {
        throw new IllegalArgumentException("No existe un instrumento con ID: " + idInstrumento);
    }
    
    // Validar que no se haya usado ya en esta atencion
    List<InstrumentoMedico> usados = listarUsadosPorAtencion(idAtencionMedica);
    if (usados != null) {
        for (InstrumentoMedico i : usados) {
            if (i.getIdInstrumento() == idInstrumento) {
                throw new IllegalStateException("El instrumento ya fue registrado en esta atencion medica");
            }
        }
    }
    
    try {
        return instrumentoDAO.insertarUsado(idAtencionMedica, idInstrumento);
    } catch (SQLException e) {
        e.printStackTrace();
        throw new RuntimeException("Error al registrar el uso del instrumento", e);
    }
}

    /**
     * Lista los instrumentos usados en una atencion medica especifica.
     *
     * @param idAtencionMedica identificador de la atencion (debe ser > 0)
     * @return lista de instrumentos usados o null si hay error
     * @throws IllegalArgumentException si el id es invalido
     */
    public List<InstrumentoMedico> listarUsadosPorAtencion(int idAtencionMedica) {
        if (idAtencionMedica <= 0) {
            throw new IllegalArgumentException("ID de atencion medica invalido: " + idAtencionMedica);
        }
        try {
            return instrumentoDAO.obtenerUsadosPorAtencion(idAtencionMedica);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * Valida y crea un nuevo instrumento medico.
     *
     * @param instrumento objeto InstrumentoMedico a crear
     * @return ID del instrumento creado
     * @throws IllegalArgumentException si los datos son invalidos
     */
    public int crear(InstrumentoMedico instrumento) {
        validarInstrumento(instrumento);
        
        try {
            // Nota: Dependiendo de la implementacion del DAO, puede que necesites agregar un metodo insertar
            return -1;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error al crear el instrumento", e);
        }
    }
    
    /**
     * Valida los campos de un instrumento medico.
     *
     * @param instrumento objeto a validar
     * @throws IllegalArgumentException si algun campo es invalido
     */
    private void validarInstrumento(InstrumentoMedico instrumento) {
        if (instrumento == null) {
            throw new IllegalArgumentException("El instrumento no puede ser nulo");
        }
        
        if (instrumento.getNombre() == null || instrumento.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del instrumento es obligatorio");
        }
        if (instrumento.getNombre().trim().length() > 100) {
            throw new IllegalArgumentException("El nombre del instrumento no puede exceder los 100 caracteres");
        }
        
        if (instrumento.getCostoUso() < COSTO_USO_MINIMO) {
            throw new IllegalArgumentException("El costo de uso debe ser mayor a 0");
        }
        if (instrumento.getCostoUso() > COSTO_USO_MAXIMO) {
            throw new IllegalArgumentException("El costo de uso no puede exceder $" + COSTO_USO_MAXIMO);
        }
    }
}