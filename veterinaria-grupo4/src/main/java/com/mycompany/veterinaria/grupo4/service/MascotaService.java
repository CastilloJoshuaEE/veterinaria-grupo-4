package com.mycompany.veterinaria.grupo4.service;

import com.mycompany.veterinaria.grupo4.api.dto.FichaMedicaDTO;
import com.mycompany.veterinaria.grupo4.model.dao.IMascotaDAO;
import com.mycompany.veterinaria.grupo4.model.entity.Mascota;
import com.mycompany.veterinaria.grupo4.model.impl.MascotaDAOImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
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

/**
 * Servicio para la gestion de mascotas con validaciones de negocio.
 */
@Service
public class MascotaService {
    
    private static final int NOMBRE_MAX_LENGTH = 50;
    private static final int ESPECIE_MAX_LENGTH = 30;
    private static final int RAZA_MAX_LENGTH = 50;
    private static final int COLOR_MAX_LENGTH = 30;
    private static final double PESO_MINIMO = 0.01;
    private static final double PESO_MAXIMO = 200.0;
    
    private IMascotaDAO mascotaDAO;
    private ClienteService clienteService;

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
     */
    public List<Mascota> listarPorCliente(int idCliente) {
        if (idCliente <= 0) {
            throw new IllegalArgumentException("ID de cliente invalido: " + idCliente);
        }
        try {
            return mascotaDAO.obtenerPorCliente(idCliente);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * Lista todas las mascotas registradas.
     */
    public List<Mascota> listarTodo() {
        try {
            return mascotaDAO.listarTodo();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * Busca mascotas por termino.
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
            System.err.println("Error en la busqueda de mascotas: " + e.getMessage());
            return null; 
        }
    }

    /**
     * Obtiene una mascota por su identificador.
     */
    public Mascota obtenerPorId(int idMascota) {
        if (idMascota <= 0) {
            throw new IllegalArgumentException("ID de mascota invalido: " + idMascota);
        }
        try {
            return mascotaDAO.obtenerPorId(idMascota);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Valida y crea una nueva mascota.
     */
    public int crear(Mascota mascota) {
        validarMascota(mascota);
        
        if (clienteService.obtenerPorId(mascota.getIdCliente()) == null) {
            throw new IllegalArgumentException("El cliente con ID " + mascota.getIdCliente() + " no existe");
        }
        
        try {
            return mascotaDAO.insertar(mascota);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error al registrar mascota", e);
        }
    }

    /**
     * Valida y actualiza una mascota existente.
     */
    public boolean actualizar(Mascota mascota) {
        validarMascota(mascota);
        
        if (mascota.getIdMascota() <= 0) {
            throw new IllegalArgumentException("ID de mascota invalido para actualizar");
        }
        
        Mascota existente = obtenerPorId(mascota.getIdMascota());
        if (existente == null) {
            throw new IllegalArgumentException("No existe una mascota con ID: " + mascota.getIdMascota());
        }
        
        if (clienteService.obtenerPorId(mascota.getIdCliente()) == null) {
            throw new IllegalArgumentException("El cliente con ID " + mascota.getIdCliente() + " no existe");
        }
        
        try {
            return mascotaDAO.actualizar(mascota);
        } catch (SQLException e) {
            System.err.println("ERROR SQL al actualizar mascota ID=" + mascota.getIdMascota());
            e.printStackTrace();
            throw new RuntimeException("Error al actualizar la mascota", e);
        }
    }

    /**
     * Elimina una mascota con validaciones.
     */
    public boolean eliminar(int idMascota) {
        if (idMascota <= 0) {
            throw new IllegalArgumentException("ID de mascota invalido: " + idMascota);
        }
        
        Mascota existente = obtenerPorId(idMascota);
        if (existente == null) {
            throw new IllegalArgumentException("No existe una mascota con ID: " + idMascota);
        }

        try {
            return mascotaDAO.eliminar(idMascota);
        } catch (SQLException e) {
            e.printStackTrace();
            String mensaje = e.getMessage();
            if (mensaje != null) {
                if (mensaje.contains("cita médica realizada") || mensaje.contains("cita medica realizada")) {
                    throw new RuntimeException("La mascota ya posee una cita médica realizada. No se puede eliminar.");
                } else if (mensaje.contains("citas pendientes")) {
                    throw new RuntimeException("La mascota tiene citas pendientes. No se puede eliminar.");
                }
            }
            throw new RuntimeException("Error al eliminar la mascota: " + (mensaje != null ? mensaje : "Error desconocido"));
        }
    }

    /**
     * Obtiene la foto de una mascota.
     */
    public byte[] obtenerFoto(int idMascota) {
        if (idMascota <= 0) {
            throw new IllegalArgumentException("ID de mascota invalido: " + idMascota);
        }
        try {
            return mascotaDAO.obtenerFoto(idMascota);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * Guarda o actualiza la ficha medica.
     */
    public boolean guardarFichaMedica(int idMascota, String alergias, String enfermedadesCronicas, String observaciones) {
        if (idMascota <= 0) {
            throw new IllegalArgumentException("ID de mascota invalido: " + idMascota);
        }
        
        Mascota existente = obtenerPorId(idMascota);
        if (existente == null) {
            throw new IllegalArgumentException("No existe una mascota con ID: " + idMascota);
        }
        
        try {
            return mascotaDAO.actualizarFichaMedica(idMascota, alergias, enfermedadesCronicas, observaciones);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error al guardar la ficha medica", e);
        }
    }

    /**
     * Obtiene la ficha medica.
     */
    public FichaMedicaDTO obtenerFichaMedica(int idMascota) {
        if (idMascota <= 0) {
            throw new IllegalArgumentException("ID de mascota invalido: " + idMascota);
        }
        try {
            return mascotaDAO.obtenerFichaMedicaDTO(idMascota);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    // ========== MÉTODOS PRIVADOS ==========
    
    /**
     * Valida todos los campos de una mascota.
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

        if (mascota.getSexo() != 'M' && mascota.getSexo() != 'F') {
            throw new IllegalArgumentException("Sexo invalido. Debe ser M (Masculino) o F (Femenino)");
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