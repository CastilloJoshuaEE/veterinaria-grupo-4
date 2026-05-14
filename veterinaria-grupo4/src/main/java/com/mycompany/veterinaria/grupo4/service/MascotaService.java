package com.mycompany.veterinaria.grupo4.service;

import com.mycompany.veterinaria.grupo4.api.dto.FichaMedicaDTO;
import com.mycompany.veterinaria.grupo4.model.dao.IMascotaDAO;
import com.mycompany.veterinaria.grupo4.model.entity.Mascota;
import com.mycompany.veterinaria.grupo4.model.impl.MascotaDAOImpl;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

/**
 * Servicio para la gestion de mascotas.
 * <p>
 * Proporciona la capa de servicios para las operaciones de negocio
 * relacionadas con las mascotas, incluyendo listado por cliente,
 * busqueda, operaciones CRUD, gestion de fotos y fichas medicas.
 * </p>
 * 
 * <p><b>Fecha de inicio del proyecto:</b> 15/04/2026</p>
 * 
 * @author CASTILLO MEREJILDO JOSHUA JAVIER – MODULO: MASCOTA
 * @version 1.0
 * @since 1.0
 */
@Service
public class MascotaService {
    private IMascotaDAO mascotaDAO = new MascotaDAOImpl();

    /**
     * Lista las mascotas asociadas a un cliente.
     *
     * @param idCliente identificador del cliente
     * @return lista de mascotas del cliente o null si hay error
     */
    public List<Mascota> listarPorCliente(int idCliente) {
        try {
            return mascotaDAO.obtenerPorCliente(idCliente);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * Lista todas las mascotas registradas.
     *
     * @return lista de todas las mascotas o null si hay error
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
     * Busca mascotas por termino (nombre o cedula del dueño).
     *
     * @param termino termino de busqueda
     * @return lista de mascotas que coinciden o null si hay error
     */
    public List<Mascota> buscarMascotas(String termino) {
        try {
            if (termino == null || termino.trim().isEmpty()) {
                return mascotaDAO.listarTodo();
            }
            return mascotaDAO.buscarMascotas(termino);
        } catch (SQLException e) {
            System.err.println("Error en la busqueda de mascotas: " + e.getMessage());
            return null; 
        }
    }

    /**
     * Obtiene una mascota por su identificador.
     *
     * @param idMascota identificador de la mascota
     * @return objeto Mascota o null si no existe
     */
    public Mascota obtenerPorId(int idMascota) {
        try {
            return mascotaDAO.obtenerPorId(idMascota);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Crea una nueva mascota en el sistema.
     *
     * @param mascota objeto Mascota a insertar
     * @return ID de la mascota creada o -1 si hay error
     */
    public int crear(Mascota mascota) {

        if (mascota == null) {
            throw new IllegalArgumentException("La mascota no puede ser nula");
        }

        if (mascota.getIdCliente() <= 0) {
            throw new IllegalArgumentException("Cliente inválido");
        }

        if (mascota.getNombre() == null || mascota.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre es obligatorio");
        }

        if (mascota.getEspecie() == null || mascota.getEspecie().trim().isEmpty()) {
            throw new IllegalArgumentException("La especie es obligatoria");
        }

        if (mascota.getSexo() != 'M' && mascota.getSexo() != 'F') {
            throw new IllegalArgumentException("Sexo inválido");
        }

        try {
            return mascotaDAO.insertar(mascota);
        } catch (SQLException e) {
            throw new RuntimeException("Error al registrar mascota", e);
        }
    }

    /**
     * Actualiza los datos de una mascota existente.
     *
     * @param mascota objeto Mascota con datos actualizados
     * @return true si la actualizacion fue exitosa
     */
    public boolean actualizar(Mascota mascota) {
        try {

            if (mascota == null) {
                System.err.println("ERROR: Mascota es null");
                return false;
            }

            if (mascota.getIdMascota() <= 0) {
                System.err.println("ERROR: ID de mascota inválido");
                return false;
            }

            boolean resultado = mascotaDAO.actualizar(mascota);

            if (!resultado) {
                System.err.println("ERROR: No se actualizó la mascota. Posibles causas:");
                System.err.println("- ID inexistente en BD");
                System.err.println("- No hubo filas afectadas en UPDATE");
                System.err.println("- Datos no coinciden con registros existentes");
            }

            return resultado;

        } catch (SQLException e) {
            System.err.println("ERROR SQL al actualizar mascota ID=" + mascota.getIdMascota());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Elimina una mascota del sistema.
     *
     * @param idMascota identificador de la mascota a eliminar
     * @return true si la eliminacion fue exitosa
     */
    public boolean eliminar(int idMascota) {
        try {
            return mascotaDAO.eliminar(idMascota);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Obtiene la foto de una mascota.
     *
     * @param idMascota identificador de la mascota
     * @return arreglo de bytes con la imagen o null si no hay
     */
    public byte[] obtenerFoto(int idMascota) {
        try {
            return mascotaDAO.obtenerFoto(idMascota);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * Guarda o actualiza la ficha medica de una mascota.
     *
     * @param idMascota identificador de la mascota
     * @param alergias alergias de la mascota
     * @param enfermedadesCronicas enfermedades cronicas
     * @param observaciones observaciones adicionales
     * @return true si el guardado fue exitoso
     */
    public boolean guardarFichaMedica(int idMascota, String alergias, String enfermedadesCronicas, String observaciones) {
        try {
            return mascotaDAO.actualizarFichaMedica(idMascota, alergias, enfermedadesCronicas, observaciones);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Obtiene la ficha medica de una mascota.
     *
     * @param idMascota identificador de la mascota
     * @return objeto FichaMedicaDTO con los datos o null si hay error
     */
    public FichaMedicaDTO obtenerFichaMedica(int idMascota) {
        try {
            return mascotaDAO.obtenerFichaMedicaDTO(idMascota);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}