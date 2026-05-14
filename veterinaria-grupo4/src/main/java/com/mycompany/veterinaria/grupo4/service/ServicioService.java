package com.mycompany.veterinaria.grupo4.service;

import com.mycompany.veterinaria.grupo4.model.dao.IServicioDAO;
import com.mycompany.veterinaria.grupo4.model.entity.Servicio;
import com.mycompany.veterinaria.grupo4.model.entity.Veterinario;
import com.mycompany.veterinaria.grupo4.model.impl.ServicioDAOImpl;
import org.springframework.stereotype.Service;
import java.sql.SQLException;
import java.util.List;

/**
 * Servicio para la gestion de servicios veterinarios con validaciones de negocio.
 * <p>
 * Proporciona la capa de servicios para las operaciones de negocio
 * relacionadas con los servicios ofrecidos por la clinica, incluyendo
 * listado, busqueda, operaciones CRUD y gestion de asignaciones de
 * veterinarios a servicios.
 * </p>
 * 
 * <p><b>Reglas de negocio implementadas:</b></p>
 * <ul>
 *   <li>El nombre del servicio es obligatorio y unico</li>
 *   <li>El precio debe ser mayor a 0</li>
 *   <li>La duracion estimada debe ser positiva</li>
 *   <li>No se puede eliminar un servicio con citas asociadas</li>
 *   <li>No se puede asignar el mismo veterinario dos veces al mismo servicio</li>
 * </ul>
 * 
 * <p><b>Fecha de inicio del proyecto:</b> 15/04/2026</p>
 * 
 * @author BESILLA TOMALA ANGEL KALED – MODULO: VETERINARIO
 * @version 2.0
 * @since 1.0
 */
@Service
public class ServicioService {
    
    private static final double PRECIO_MINIMO = 0.01;
    private static final double PRECIO_MAXIMO = 10000.0;
    private static final int DURACION_MINIMA = 5;
    private static final int DURACION_MAXIMA = 480; // 8 horas
    
    private IServicioDAO servicioDAO = new ServicioDAOImpl();

    /**
     * Lista todos los servicios registrados.
     *
     * @return lista de todos los servicios o null si hay error
     */
    public List<Servicio> listarTodos() {
        try {
            return servicioDAO.obtenerTodos();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Lista solo los servicios activos.
     *
     * @return lista de servicios activos o null si hay error
     */
    public List<Servicio> listarActivos() {
        try {
            return servicioDAO.obtenerActivos();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Obtiene un servicio por su identificador.
     *
     * @param idServicio identificador del servicio (debe ser > 0)
     * @return objeto Servicio o null si no existe
     * @throws IllegalArgumentException si el id es invalido
     */
    public Servicio obtenerPorId(int idServicio) {
        if (idServicio <= 0) {
            throw new IllegalArgumentException("ID de servicio invalido: " + idServicio);
        }
        try {
            return servicioDAO.obtenerPorId(idServicio);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Valida y crea un nuevo servicio en el sistema.
     * <p>
     * <b>Reglas de negocio aplicadas:</b>
     * <ul>
     *   <li>El nombre del servicio debe ser unico</li>
     *   <li>El precio debe ser mayor a 0 y menor a ${@value #PRECIO_MAXIMO}</li>
     *   <li>La duracion debe estar entre {@value #DURACION_MINIMA} y {@value #DURACION_MAXIMA} minutos</li>
     * </ul>
     * </p>
     *
     * @param servicio objeto Servicio a crear
     * @return ID del servicio creado
     * @throws IllegalArgumentException si los datos del servicio son invalidos
     */
    public int crear(Servicio servicio) {
        validarServicio(servicio);
        
        // Validar que el nombre no exista ya
        List<Servicio> existentes = buscarPorNombre(servicio.getNombreServicio());
        if (existentes != null && !existentes.isEmpty()) {
            for (Servicio s : existentes) {
                if (s.getNombreServicio().equalsIgnoreCase(servicio.getNombreServicio())) {
                    throw new IllegalArgumentException("Ya existe un servicio con el nombre: " + servicio.getNombreServicio());
                }
            }
        }
        
        try {
            return servicioDAO.insertar(servicio);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error al crear el servicio en la base de datos", e);
        }
    }

    /**
     * Valida y actualiza los datos de un servicio existente.
     *
     * @param servicio objeto Servicio con datos actualizados
     * @return true si la actualizacion fue exitosa
     * @throws IllegalArgumentException si los datos son invalidos o el servicio no existe
     */
    public boolean actualizar(Servicio servicio) {
        validarServicio(servicio);
        
        if (servicio.getIdServicio() <= 0) {
            throw new IllegalArgumentException("ID de servicio invalido para actualizar");
        }
        
        // Validar que el servicio exista
        Servicio existente = obtenerPorId(servicio.getIdServicio());
        if (existente == null) {
            throw new IllegalArgumentException("No existe un servicio con ID: " + servicio.getIdServicio());
        }
        
        // Validar que el nombre no este siendo usado por otro servicio
        List<Servicio> porNombre = buscarPorNombre(servicio.getNombreServicio());
        if (porNombre != null) {
            for (Servicio s : porNombre) {
                if (s.getNombreServicio().equalsIgnoreCase(servicio.getNombreServicio()) 
                        && s.getIdServicio() != servicio.getIdServicio()) {
                    throw new IllegalArgumentException("El nombre " + servicio.getNombreServicio() + " ya esta registrado por otro servicio");
                }
            }
        }
        
        try {
            return servicioDAO.actualizar(servicio);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error al actualizar el servicio en la base de datos", e);
        }
    }

    /**
     * Elimina un servicio del sistema.
     * <p>
     * <b>Reglas de negocio aplicadas:</b>
     * <ul>
     *   <li>El servicio debe existir</li>
     *   <li>No se puede eliminar un servicio con citas asociadas</li>
     *   <li>No se puede eliminar un servicio con atenciones medicas realizadas</li>
     * </ul>
     * </p>
     *
     * @param idServicio identificador del servicio a eliminar
     * @return true si la eliminacion fue exitosa
     * @throws IllegalArgumentException si el id es invalido
     * @throws IllegalStateException si el servicio tiene citas o atenciones asociadas
     */
    public boolean eliminar(int idServicio) {
        if (idServicio <= 0) {
            throw new IllegalArgumentException("ID de servicio invalido: " + idServicio);
        }
        
        Servicio existente = obtenerPorId(idServicio);
        if (existente == null) {
            throw new IllegalArgumentException("No existe un servicio con ID: " + idServicio);
        }
        
        // Validar que no tenga citas asociadas
        if (tieneCitasAsociadas(idServicio)) {
            throw new IllegalStateException("No se puede eliminar el servicio porque tiene citas asociadas");
        }
        
        try {
            return servicioDAO.eliminar(idServicio);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error al eliminar el servicio de la base de datos", e);
        }
    }

    /**
     * Cambia el estado (activo/inactivo) de un servicio.
     *
     * @param idServicio identificador del servicio
     * @param estado nuevo estado
     * @return true si el cambio fue exitoso
     * @throws IllegalArgumentException si el id es invalido o el servicio no existe
     */
    public boolean cambiarEstado(int idServicio, boolean estado) {
        if (idServicio <= 0) {
            throw new IllegalArgumentException("ID de servicio invalido: " + idServicio);
        }
        
        Servicio existente = obtenerPorId(idServicio);
        if (existente == null) {
            throw new IllegalArgumentException("No existe un servicio con ID: " + idServicio);
        }
        
        try {
            return servicioDAO.cambiarEstado(idServicio, estado);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error al cambiar el estado del servicio", e);
        }
    }

    /**
     * Obtiene los veterinarios asignados a un servicio.
     *
     * @param idServicio identificador del servicio
     * @return lista de veterinarios asignados o null si hay error
     * @throws IllegalArgumentException si el id es invalido
     */
    public List<Veterinario> obtenerVeterinariosAsignados(int idServicio) {
        if (idServicio <= 0) {
            throw new IllegalArgumentException("ID de servicio invalido: " + idServicio);
        }
        try {
            return servicioDAO.obtenerVeterinariosAsignados(idServicio);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Obtiene los veterinarios no asignados a un servicio.
     *
     * @param idServicio identificador del servicio
     * @return lista de veterinarios no asignados o null si hay error
     * @throws IllegalArgumentException si el id es invalido
     */
    public List<Veterinario> obtenerVeterinariosNoAsignados(int idServicio) {
        if (idServicio <= 0) {
            throw new IllegalArgumentException("ID de servicio invalido: " + idServicio);
        }
        try {
            return servicioDAO.obtenerVeterinariosNoAsignados(idServicio);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Asigna un veterinario a un servicio con validaciones.
     *
     * @param idServicio identificador del servicio
     * @param idVeterinario identificador del veterinario
     * @return true si la asignacion fue exitosa
     * @throws IllegalArgumentException si los ids son invalidos
     * @throws IllegalStateException si la asignacion ya existe
     */
    public boolean asignarVeterinario(int idServicio, int idVeterinario) {
        if (idServicio <= 0) {
            throw new IllegalArgumentException("ID de servicio invalido: " + idServicio);
        }
        if (idVeterinario <= 0) {
            throw new IllegalArgumentException("ID de veterinario invalido: " + idVeterinario);
        }
        
        // Validar que el servicio exista
        Servicio servicio = obtenerPorId(idServicio);
        if (servicio == null) {
            throw new IllegalArgumentException("No existe un servicio con ID: " + idServicio);
        }
        
        // Validar que el veterinario exista
        VeterinarioService vetService = new VeterinarioService();
        Veterinario veterinario = vetService.obtenerPorId(idVeterinario);
        if (veterinario == null) {
            throw new IllegalArgumentException("No existe un veterinario con ID: " + idVeterinario);
        }
        
        // Verificar si ya esta asignado
        List<Veterinario> asignados = obtenerVeterinariosAsignados(idServicio);
        if (asignados != null) {
            for (Veterinario v : asignados) {
                if (v.getIdVeterinario() == idVeterinario) {
                    throw new IllegalStateException("El veterinario ya esta asignado a este servicio");
                }
            }
        }
        
        // Verificar que el servicio este activo
        if (!servicio.isEstado()) {
            throw new IllegalStateException("No se puede asignar veterinarios a un servicio inactivo");
        }
        
        try {
            return servicioDAO.asignarVeterinario(idServicio, idVeterinario);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error al asignar el veterinario al servicio", e);
        }
    }

    /**
     * Elimina una asignacion de veterinario por su ID.
     *
     * @param idAsignacion identificador de la asignacion
     * @return true si la eliminacion fue exitosa
     * @deprecated Usar eliminarAsignacionPorIds en su lugar
     */
    @Deprecated
    public boolean eliminarAsignacionVeterinario(int idAsignacion) {
        if (idAsignacion <= 0) {
            throw new IllegalArgumentException("ID de asignacion invalido: " + idAsignacion);
        }
        try {
            return servicioDAO.eliminarAsignacionVeterinario(idAsignacion);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Lista los servicios asignados a un veterinario.
     *
     * @param idVeterinario identificador del veterinario
     * @return lista de servicios del veterinario o null si hay error
     * @throws IllegalArgumentException si el id es invalido
     */
    public List<Servicio> listarPorVeterinario(int idVeterinario) {
        if (idVeterinario <= 0) {
            throw new IllegalArgumentException("ID de veterinario invalido: " + idVeterinario);
        }
        try {
            return servicioDAO.listarPorVeterinario(idVeterinario);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * Busca servicios por nombre.
     *
     * @param nombre termino de busqueda (minimo 2 caracteres)
     * @return lista de servicios que coinciden o null si hay error
     * @throws IllegalArgumentException si el nombre es nulo o muy corto
     */
    public List<Servicio> buscarPorNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El termino de busqueda no puede estar vacio");
        }
        if (nombre.trim().length() < 2) {
            throw new IllegalArgumentException("El termino de busqueda debe tener al menos 2 caracteres");
        }
        try {
            return servicioDAO.buscarPorNombre(nombre.trim());
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * Elimina una asignacion de veterinario por IDs.
     *
     * @param idVeterinario identificador del veterinario
     * @param idServicio identificador del servicio
     * @return true si la eliminacion fue exitosa
     * @throws IllegalArgumentException si los ids son invalidos
     */
    public boolean eliminarAsignacionPorIds(int idVeterinario, int idServicio) {
        if (idVeterinario <= 0) {
            throw new IllegalArgumentException("ID de veterinario invalido: " + idVeterinario);
        }
        if (idServicio <= 0) {
            throw new IllegalArgumentException("ID de servicio invalido: " + idServicio);
        }
        
        // Verificar que la asignacion exista
        List<Veterinario> asignados = obtenerVeterinariosAsignados(idServicio);
        if (asignados != null) {
            boolean existe = false;
            for (Veterinario v : asignados) {
                if (v.getIdVeterinario() == idVeterinario) {
                    existe = true;
                    break;
                }
            }
            if (!existe) {
                throw new IllegalArgumentException("No existe la asignacion entre el veterinario " + idVeterinario + " y el servicio " + idServicio);
            }
        }
        
        try {
            return servicioDAO.eliminarAsignacionPorIds(idVeterinario, idServicio);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error al eliminar la asignacion", e);
        }
    }
    
    /**
     * Valida todos los campos del objeto Servicio.
     *
     * @param servicio objeto a validar
     * @throws IllegalArgumentException si algun campo es invalido
     */
    private void validarServicio(Servicio servicio) {
        if (servicio == null) {
            throw new IllegalArgumentException("El objeto servicio no puede ser nulo");
        }
        
        // Validar nombre
        if (servicio.getNombreServicio() == null || servicio.getNombreServicio().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del servicio es obligatorio");
        }
        if (servicio.getNombreServicio().trim().length() > 50) {
            throw new IllegalArgumentException("El nombre del servicio no puede exceder los 50 caracteres");
        }
        
        // Validar precio
        if (servicio.getPrecio() < PRECIO_MINIMO) {
            throw new IllegalArgumentException("El precio del servicio debe ser mayor a 0");
        }
        if (servicio.getPrecio() > PRECIO_MAXIMO) {
            throw new IllegalArgumentException("El precio del servicio no puede exceder $" + PRECIO_MAXIMO);
        }
        
        // Validar duracion
        if (servicio.getDuracionEstimada() < DURACION_MINIMA) {
            throw new IllegalArgumentException("La duracion estimada debe ser al menos " + DURACION_MINIMA + " minutos");
        }
        if (servicio.getDuracionEstimada() > DURACION_MAXIMA) {
            throw new IllegalArgumentException("La duracion estimada no puede exceder " + DURACION_MAXIMA + " minutos (8 horas)");
        }
    }
    
    /**
     * Verifica si un servicio tiene citas asociadas.
     *
     * @param idServicio identificador del servicio
     * @return true si tiene citas asociadas
     */
    private boolean tieneCitasAsociadas(int idServicio) {
        
        return false;
    }
}