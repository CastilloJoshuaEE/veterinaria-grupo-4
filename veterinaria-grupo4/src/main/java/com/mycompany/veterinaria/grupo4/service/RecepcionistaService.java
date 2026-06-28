package com.mycompany.veterinaria.grupo4.service;

import com.mycompany.veterinaria.grupo4.model.entity.Recepcionista;
import com.mycompany.veterinaria.grupo4.model.dao.IRcepcionistaDAO;
import com.mycompany.veterinaria.grupo4.model.impl.RecepcionistaDAOImpl;
import org.springframework.stereotype.Service;

/**
 * Servicio para la gestión de recepcionistas.
 * 
 * @author juan
 */
@Service
public class RecepcionistaService {
    
    private IRcepcionistaDAO recepcionistaDAO = new RecepcionistaDAOImpl();

    /**
     * Registra un nuevo recepcionista en el sistema.
     * 
     * @param registro Datos del recepcionista a registrar
     * @return true si el registro fue exitoso
     */
    public boolean registrarRecepcionista(Recepcionista registro) {
        // Validaciones
        if (registro == null) {
            throw new IllegalArgumentException("Los datos del recepcionista no pueden ser nulos");
        }
        
        if (registro.getCedula() == null || registro.getCedula().trim().isEmpty()) {
            throw new IllegalArgumentException("La cédula es obligatoria");
        }
        
        if (registro.getNombre() == null || registro.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre es obligatorio");
        }
        
        if (registro.getApellido() == null || registro.getApellido().trim().isEmpty()) {
            throw new IllegalArgumentException("El apellido es obligatorio");
        }
        
        if (registro.getTelefono() == null || registro.getTelefono().trim().isEmpty()) {
            throw new IllegalArgumentException("El teléfono es obligatorio");
        }
        
        if (registro.getEmail() == null || registro.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("El correo electrónico es obligatorio");
        }
        
        if (registro.getContrasena() == null || registro.getContrasena().trim().isEmpty()) {
            throw new IllegalArgumentException("La contraseña es obligatoria");
        }
        
        if (registro.getContrasena().length() < 6) {
            throw new IllegalArgumentException("La contraseña debe tener al menos 6 caracteres");
        }
        
        try {
            return recepcionistaDAO.registrarRecepcionista(registro);
        } catch (Exception e) {
            throw new RuntimeException("Error al registrar recepcionista: " + e.getMessage(), e);
        }
    }
}