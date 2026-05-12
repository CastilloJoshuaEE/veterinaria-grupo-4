package com.mycompany.veterinaria.grupo4.model.entity;

/**
 * Entidad que representa una especialidad veterinaria.
 * <p>
 * Define las areas de especializacion de los veterinarios, como cardiologia,
 * dermatologia, cirugia, entre otras. Permite clasificar a los profesionales
 * segun su area de expertise.
 * </p>
 * 
 * <p><b>Fecha de inicio del proyecto:</b> 15/04/2026</p>
 * 
 * @author BESILLA TOMALA ANGEL KALED – MODULO: VETERINARIO
 * @version 1.0
 * @since 1.0
 */
public class EspecialidadVeterinaria {
    private int idEspecialidad;
    private String nombreEspecialidad;

    /**
     * Constructor por defecto.
     */
    public EspecialidadVeterinaria() {}

    /**
     * Constructor con los datos de la especialidad.
     * 
     * @param idEspecialidad identificador de la especialidad
     * @param nombreEspecialidad nombre de la especialidad
     */
    public EspecialidadVeterinaria(int idEspecialidad, String nombreEspecialidad) {
        this.idEspecialidad = idEspecialidad;
        this.nombreEspecialidad = nombreEspecialidad;
    }

    public int getIdEspecialidad() { return idEspecialidad; }
    public void setIdEspecialidad(int idEspecialidad) { this.idEspecialidad = idEspecialidad; }
    public String getNombreEspecialidad() { return nombreEspecialidad; }
    public void setNombreEspecialidad(String nombreEspecialidad) { this.nombreEspecialidad = nombreEspecialidad; }
}