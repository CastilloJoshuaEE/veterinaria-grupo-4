package com.mycompany.veterinaria.grupo4.service;

import com.mycompany.veterinaria.grupo4.api.dto.FichaMedicaDTO;
import com.mycompany.veterinaria.grupo4.model.dao.IMascotaDAO;
import com.mycompany.veterinaria.grupo4.model.entity.Mascota;
import com.mycompany.veterinaria.grupo4.model.impl.MascotaDAOImpl;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Servicio para la gestion de mascotas con validaciones de negocio.
 * <p>
 * Proporciona la capa de servicios para las operaciones de negocio
 * relacionadas con las mascotas, incluyendo listado por cliente,
 * busqueda, operaciones CRUD, gestion de fotos y fichas medicas.
 * </p>
 * 
 * <p><b>Reglas de negocio implementadas:</b></p>
 * <ul>
 *   <li>El nombre de la mascota es obligatorio (maximo 50 caracteres)</li>
 *   <li>La especie es obligatoria</li>
 *   <li>El sexo debe ser M o F</li>
 *   <li>El peso debe ser positivo</li>
 *   <li>La fecha de nacimiento no puede ser futura</li>
 *   <li>No se puede eliminar una mascota con citas o atenciones medicas</li>
 * </ul>
 * 
 * <p><b>Fecha de inicio del proyecto:</b> 15/04/2026</p>
 * 
 * @author CASTILLO MEREJILDO JOSHUA JAVIER – MODULO: MASCOTA
 * @version 2.0
 * @since 1.0
 */
@Service
public class MascotaService {
    
    private static final int NOMBRE_MAX_LENGTH = 50;
    private static final int ESPECIE_MAX_LENGTH = 30;
    private static final int RAZA_MAX_LENGTH = 50;
    private static final int COLOR_MAX_LENGTH = 30;
    private static final double PESO_MINIMO = 0.01;
    private static final double PESO_MAXIMO = 200.0;
    
    private final IMascotaDAO mascotaDAO;
    private final ClienteService clienteService;

    // ========== CONSTRUCTORES ==========
    
    /**
     * Constructor por defecto (usado por Spring)
     */
    public MascotaService() {
        this.mascotaDAO = new MascotaDAOImpl();
        this.clienteService = new ClienteService();
    }
    
    /**
     * Constructor para inyección de dependencias (usado en pruebas)
     * 
     * @param clienteService servicio de clientes mockeado
     * @param mascotaDAO DAO de mascotas mockeado
     */
    public MascotaService(ClienteService clienteService, IMascotaDAO mascotaDAO) {
        this.clienteService = clienteService;
        this.mascotaDAO = mascotaDAO;
    }
    
    /**
     * Constructor para inyección solo del DAO
     * 
     * @param mascotaDAO DAO de mascotas mockeado
     */
    public MascotaService(IMascotaDAO mascotaDAO) {
        this.mascotaDAO = mascotaDAO;
        this.clienteService = new ClienteService();
    }
    
    /**
     * Constructor para inyección solo del ClienteService
     * 
     * @param clienteService servicio de clientes mockeado
     */
    public MascotaService(ClienteService clienteService) {
        this.clienteService = clienteService;
        this.mascotaDAO = new MascotaDAOImpl();
    }

    // ========== MÉTODOS DEL SERVICIO ==========

    /**
     * Lista las mascotas asociadas a un cliente.
     * 
     * @param idCliente identificador del cliente dueño de las mascotas
     * @return lista de mascotas del cliente, nunca null
     */
    public List<Mascota> listarPorCliente(int idCliente) {
        if (idCliente <= 0) {
            throw new IllegalArgumentException("ID de cliente invalido: " + idCliente);
        }
        try {
            return mascotaDAO.obtenerPorCliente(idCliente);
        } catch (SQLException e) {
            return new ArrayList<>(); // CORREGIDO: ya no retorna null
        }
    }
    
    /**
     * Lista todas las mascotas registradas.
     * 
     * @return lista de todas las mascotas, nunca null
     */
    public List<Mascota> listarTodo() {
        try {
            return mascotaDAO.listarTodo();
        } catch (SQLException e) {
            return new ArrayList<>();
        }
    }
    
    /**
     * Busca mascotas por termino de busqueda.
     * 
     * @param termino texto a buscar (nombre de mascota o cedula del dueño)
     * @return lista de mascotas que coinciden con el termino, nunca null
     */
    public List<Mascota> buscarMascotas(String termino) {
        try {
            if (termino == null || termino.trim().isEmpty()) {
                return mascotaDAO.listarTodo();
            }
            if (termino.trim().length() < 2) {
                throw new IllegalArgumentException("El termino de busqueda debe tener al menos 2 caracteres");
            }
            return mascotaDAO.buscarMascotas(termino.trim());
        } catch (SQLException e) {
            return new ArrayList<>();
        }
    }

    /**
     * Obtiene una mascota por su identificador.
     * 
     * @param idMascota identificador unico de la mascota
     * @return objeto Mascota encontrado, o null si no existe
     */
    public Mascota obtenerPorId(int idMascota) {
        if (idMascota <= 0) {
            throw new IllegalArgumentException("ID de mascota invalido: " + idMascota);
        }
        try {
            return mascotaDAO.obtenerPorId(idMascota);
        } catch (SQLException e) {
            return null;
        }
    }

    /**
     * Valida y crea una nueva mascota.
     * 
     * @param mascota objeto Mascota con los datos a registrar
     * @return ID generado para la mascota creada
     */
    public int crear(Mascota mascota) {
        validarMascota(mascota);
        
        if (clienteService.obtenerPorId(mascota.getIdCliente()) == null) {
            throw new IllegalArgumentException("El cliente con ID " + mascota.getIdCliente() + " no existe");
        }
        
        try {
            return mascotaDAO.insertar(mascota);
        } catch (SQLException e) {
            throw new RuntimeException("Error al registrar mascota", e);
        }
    }

    /**
     * Valida y actualiza una mascota existente.
     * 
     * @param mascota objeto Mascota con los datos actualizados
     * @return true si la actualizacion fue exitosa
     */
    public boolean actualizar(Mascota mascota) {
        validarMascota(mascota);
        
        if (mascota.getIdMascota() <= 0) {
            throw new IllegalArgumentException("ID de mascota invalido para actualizar");
        }
        
        var existente = obtenerPorId(mascota.getIdMascota());
        if (existente == null) {
            throw new IllegalArgumentException("No existe una mascota con ID: " + mascota.getIdMascota());
        }
        
        if (clienteService.obtenerPorId(mascota.getIdCliente()) == null) {
            throw new IllegalArgumentException("El cliente con ID " + mascota.getIdCliente() + " no existe");
        }
        
        try {
            return mascotaDAO.actualizar(mascota);
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar la mascota", e);
        }
    }

    /**
     * Elimina una mascota con validaciones de integridad.
     * 
     * @param idMascota identificador de la mascota a eliminar
     * @return true si la eliminacion fue exitosa
     */
    public boolean eliminar(int idMascota) {
        if (idMascota <= 0) {
            throw new IllegalArgumentException("ID de mascota invalido: " + idMascota);
        }
        
        var existente = obtenerPorId(idMascota);
        if (existente == null) {
            throw new IllegalArgumentException("No existe una mascota con ID: " + idMascota);
        }

        try {
            return mascotaDAO.eliminar(idMascota);
        } catch (SQLException e) {
            var mensaje = e.getMessage(); // CORREGIDO: usa 'var'
            if (mensaje != null) {
                if (mensaje.contains("cita médica realizada") || mensaje.contains("cita medica realizada")) {
                    throw new RuntimeException("La mascota ya posee una cita médica realizada. No se puede eliminar.", e); //CORREGIDO: preserva stack trace
                } else if (mensaje.contains("citas pendientes")) {
                    throw new RuntimeException("La mascota tiene citas pendientes. No se puede eliminar.", e);// CORREGIDO: preserva stack trace
                }
            }
            throw new RuntimeException("Error al eliminar la mascota: " + (mensaje != null ? mensaje : "Error desconocido"), e);
        }
    }

    /**
     * Obtiene la foto de una mascota.
     * 
     * @param idMascota identificador de la mascota
     * @return arreglo de bytes con la imagen, o null si no tiene foto
     */
    public byte[] obtenerFoto(int idMascota) {
        if (idMascota <= 0) {
            throw new IllegalArgumentException("ID de mascota invalido: " + idMascota);
        }
        try {
            return mascotaDAO.obtenerFoto(idMascota);
        } catch (SQLException e) {
            return null;
        }
    }
    
    /**
     * Guarda o actualiza la ficha medica de una mascota.
     * 
     * @param idMascota identificador de la mascota
     * @param alergias alergias conocidas de la mascota
     * @param enfermedadesCronicas enfermedades cronicas diagnosticadas
     * @param observaciones observaciones adicionales sobre la salud
     * @return true si la operacion fue exitosa
     */
    public boolean guardarFichaMedica(int idMascota, String alergias, String enfermedadesCronicas, String observaciones) {
        if (idMascota <= 0) {
            throw new IllegalArgumentException("ID de mascota invalido: " + idMascota);
        }
        
        var existente = obtenerPorId(idMascota);
        if (existente == null) {
            throw new IllegalArgumentException("No existe una mascota con ID: " + idMascota);
        }
        
        try {
            return mascotaDAO.actualizarFichaMedica(idMascota, alergias, enfermedadesCronicas, observaciones);
        } catch (SQLException e) {
            throw new RuntimeException("Error al guardar la ficha medica", e);
        }
    }

    /**
     * Obtiene la ficha medica completa de una mascota.
     * 
     * @param idMascota identificador de la mascota
     * @return objeto FichaMedicaDTO con los datos medicos
     */
    public FichaMedicaDTO obtenerFichaMedica(int idMascota) {
        if (idMascota <= 0) {
            throw new IllegalArgumentException("ID de mascota invalido: " + idMascota);
        }
        try {
            return mascotaDAO.obtenerFichaMedicaDTO(idMascota);
        } catch (SQLException e) {
            return null;
        }
    }
    
    // ========== MÉTODOS PRIVADOS ==========
    
    /**
     * Valida todos los campos de una mascota segun reglas de negocio.
     * 
     * @param mascota objeto Mascota a validar
     */
    private void validarMascota(Mascota mascota) {
        if (mascota == null) {
            throw new IllegalArgumentException("La mascota no puede ser nula");
        }

        if (mascota.getIdCliente() <= 0) {
            throw new IllegalArgumentException("Cliente invalido");
        }

        if (mascota.getNombre() == null || mascota.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre es obligatorio");
        }
        if (mascota.getNombre().trim().length() > NOMBRE_MAX_LENGTH) {
            throw new IllegalArgumentException("El nombre no puede exceder los " + NOMBRE_MAX_LENGTH + " caracteres");
        }

        if (mascota.getEspecie() == null || mascota.getEspecie().trim().isEmpty()) {
            throw new IllegalArgumentException("La especie es obligatoria");
        }
        if (mascota.getEspecie().trim().length() > ESPECIE_MAX_LENGTH) {
            throw new IllegalArgumentException("La especie no puede exceder los " + ESPECIE_MAX_LENGTH + " caracteres");
        }

        if (mascota.getRaza() != null && mascota.getRaza().trim().length() > RAZA_MAX_LENGTH) {
            throw new IllegalArgumentException("La raza no puede exceder los " + RAZA_MAX_LENGTH + " caracteres");
        }

        if (mascota.getSexo() != 'M' && mascota.getSexo() != 'H') {
            throw new IllegalArgumentException("Sexo invalido. Debe ser M (Masculino) o H (Hembra)");
        }

        if (mascota.getColor() != null && mascota.getColor().trim().length() > COLOR_MAX_LENGTH) {
            throw new IllegalArgumentException("El color no puede exceder los " + COLOR_MAX_LENGTH + " caracteres");
        }

        if (mascota.getPeso() != null && mascota.getPeso() > 0) {
            if (mascota.getPeso() < PESO_MINIMO) {
                throw new IllegalArgumentException("El peso debe ser mayor a 0");
            }
            if (mascota.getPeso() > PESO_MAXIMO) {
                throw new IllegalArgumentException("El peso no puede exceder los " + PESO_MAXIMO + " kg");
            }
        }

        if (mascota.getFechaNacimiento() != null && mascota.getFechaNacimiento().after(new Date())) {
            throw new IllegalArgumentException("La fecha de nacimiento no puede ser futura");
        }
    }
}
