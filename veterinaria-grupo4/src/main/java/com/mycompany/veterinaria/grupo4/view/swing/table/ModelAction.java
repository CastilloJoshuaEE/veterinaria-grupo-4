package com.mycompany.veterinaria.grupo4.view.swing.table;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Modelo de acciones para una celda de tabla.
 * Usa builder fluido: se agregan solo las acciones necesarias,
 * en el orden en que deben aparecer visualmente.
 *
 * @author Juan
 */
public class ModelAction {

    public enum Tipo {
        EDITAR,
        VER,
        ELIMINAR,
        ATENCION,
        HISTORIAL,
        ATENDER
    }

    private final Map<Tipo, Runnable> acciones = new LinkedHashMap<>();

    public ModelAction add(Tipo tipo, Runnable accion) {
        if (tipo != null && accion != null) {
            acciones.put(tipo, accion);
        }
        return this;
    }

    public Map<Tipo, Runnable> getAcciones() {
        return Collections.unmodifiableMap(acciones);
    }
}