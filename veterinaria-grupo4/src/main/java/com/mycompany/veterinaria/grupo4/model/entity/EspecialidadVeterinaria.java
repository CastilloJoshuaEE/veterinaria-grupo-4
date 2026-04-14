package com.mycompany.veterinaria.grupo4.model.entity;

public class EspecialidadVeterinaria {
    private int idEspecialidad;
    private String nombreEspecialidad;

    public EspecialidadVeterinaria() {}

    public EspecialidadVeterinaria(int idEspecialidad, String nombreEspecialidad) {
        this.idEspecialidad = idEspecialidad;
        this.nombreEspecialidad = nombreEspecialidad;
    }

    public int getIdEspecialidad() { return idEspecialidad; }
    public void setIdEspecialidad(int idEspecialidad) { this.idEspecialidad = idEspecialidad; }
    public String getNombreEspecialidad() { return nombreEspecialidad; }
    public void setNombreEspecialidad(String nombreEspecialidad) { this.nombreEspecialidad = nombreEspecialidad; }
}