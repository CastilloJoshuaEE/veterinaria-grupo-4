package com.mycompany.veterinaria.grupo4.model.dao;

import com.mycompany.veterinaria.grupo4.api.dto.FichaMedicaDTO;
import com.mycompany.veterinaria.grupo4.model.entity.Mascota;
import java.sql.SQLException;
import java.util.List;

/**
 * Interfaz DAO para la gestion de mascotas.
 * <p>
 * Define las operaciones de acceso a datos para la entidad Mascota,
 * incluyendo consultas por cliente, ID, busqueda por termino,
 * asi como insercion, actualizacion, eliminacion y gestion de fotos
 * y fichas medicas.
 * </p>
 * 
 * <p><b>Fecha de inicio del proyecto:</b> 15/04/2026</p>
 * 
 * @author CASTILLO MEREJILDO JOSHUA JAVIER – MODULO: MASCOTA
 * @version 1.0
 * @since 1.0
 */
public interface IMascotaDAO {
    
    /**
     * Obtiene las mascotas asociadas a un cliente.
     * 
     * @param idCliente identificador del cliente
     * @return lista de mascotas del cliente
     * @throws SQLException si ocurre un error en la base de datos
     */
    List<Mascota> obtenerPorCliente(int idCliente) throws SQLException;
    
    /**
     * Obtiene una mascota por su identificador.
     * 
     * @param idMascota identificador de la mascota
     * @return objeto Mascota encontrado
     * @throws SQLException si ocurre un error en la base de datos
     */
    Mascota obtenerPorId(int idMascota) throws SQLException;
    
    /**
     * Inserta una nueva mascota en la base de datos.
     * 
     * @param mascota objeto Mascota a insertar
     * @return ID generado para la mascota
     * @throws SQLException si ocurre un error en la base de datos
     */
    int insertar(Mascota mascota) throws SQLException;
    
    /**
     * Actualiza los datos de una mascota existente.
     * 
     * @param mascota objeto Mascota con datos actualizados
     * @return true si la actualizacion fue exitosa
     * @throws SQLException si ocurre un error en la base de datos
     */
    boolean actualizar(Mascota mascota) throws SQLException;
    
    /**
     * Elimina una mascota de la base de datos.
     * 
     * @param idMascota identificador de la mascota a eliminar
     * @return true si la eliminacion fue exitosa
     * @throws SQLException si ocurre un error en la base de datos
     */
    boolean eliminar(int idMascota) throws SQLException;
    
    /**
     * Obtiene la foto de una mascota.
     * 
     * @param idMascota identificador de la mascota
     * @return arreglo de bytes con la imagen
     * @throws SQLException si ocurre un error en la base de datos
     */
    byte[] obtenerFoto(int idMascota) throws SQLException;
    
    /**
     * Lista todas las mascotas registradas.
     * 
     * @return lista de todas las mascotas
     * @throws SQLException si ocurre un error en la base de datos
     */
    List<Mascota> listarTodo() throws SQLException;
    
    /**
     * Busca mascotas por termino (nombre o cedula del dueño).
     * 
     * @param termino termino de busqueda
     * @return lista de mascotas que coinciden
     * @throws SQLException si ocurre un error en la base de datos
     */
    List<Mascota> buscarMascotas(String termino) throws SQLException;
    
    /**
     * Actualiza la ficha medica de una mascota.
     * 
     * @param idMascota identificador de la mascota
     * @param alergias alergias de la mascota
     * @param enfermedadesCronicas enfermedades cronicas
     * @param observaciones observaciones adicionales
     * @return true si la actualizacion fue exitosa
     * @throws SQLException si ocurre un error en la base de datos
     */
    boolean actualizarFichaMedica(int idMascota, String alergias, String enfermedadesCronicas, String observaciones) throws SQLException;
    
    /**
     * Obtiene la ficha medica de una mascota como DTO.
     * 
     * @param idMascota identificador de la mascota
     * @return objeto FichaMedicaDTO con los datos
     * @throws SQLException si ocurre un error en la base de datos
     */
    FichaMedicaDTO obtenerFichaMedicaDTO(int idMascota) throws SQLException;
}