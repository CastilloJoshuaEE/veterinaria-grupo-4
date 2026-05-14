package com.mycompany.veterinaria.grupo4.util;

import com.mycompany.veterinaria.grupo4.model.entity.Usuario;

/**
 * Gestiona la sesion del usuario actual en la aplicacion.
 * <p>
 * Implementa un patron Singleton para mantener la informacion del usuario
 * logueado durante toda la sesion de trabajo. Proporciona metodos para
 * iniciar y cerrar sesion, asi como para consultar los datos del usuario
 * activo como ID, nombre, rol y estado de autenticacion.
 * </p>
 * 
 * <p><b>Fecha de inicio del proyecto:</b> 15/04/2026</p>
 * 
 * @author ROBLES MORALES JUAN ANDRES – MODULO: ATENCION VETERINARIA
 * @version 1.0
 * @since 1.0
 */
public class SessionManager {
    private static SessionManager instance;
    private Usuario currentUser;
    private boolean isLoggedIn;

    /**
     * Constructor privado para el patron Singleton.
     * Inicializa la sesion como no logueada.
     */
    private SessionManager() {
        this.isLoggedIn = false;
        this.currentUser = null;
    }

    /**
     * Obtiene la instancia unica del gestor de sesion.
     *
     * @return instancia del SessionManager
     */
    public static synchronized SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }

    /**
     * Inicia una nueva sesion con el usuario proporcionado.
     * Activa el NotificationManager para el usuario.
     *
     * @param user usuario que inicia sesion
     */
    public void login(Usuario user) {
        if (user != null) {
            this.currentUser = user;
            this.isLoggedIn = true;
            System.out.println("Usuario logueado: " + user.getNombreUsuario() + " (ID: " + user.getIdUsuario() + ")");
            NotificationManager.getInstance().start(user.getIdUsuario());
        }
    }

    /**
     * Cierra la sesion actual.
     * Detiene el NotificationManager y limpia los datos del usuario.
     */
    public void logout() {
        NotificationManager.getInstance().stop();
        this.currentUser = null;
        this.isLoggedIn = false;
        System.out.println("Sesion cerrada");
    }

    /**
     * Obtiene el usuario actualmente logueado.
     *
     * @return usuario actual o null si no hay sesion activa
     */
    public Usuario getCurrentUser() {
        return currentUser;
    }

    /**
     * Obtiene el ID del usuario actual.
     *
     * @return ID del usuario o -1 si no hay sesion activa
     */
    public int getCurrentUserId() {
        return (currentUser != null) ? currentUser.getIdUsuario() : -1;
    }

    /**
     * Obtiene el nombre de usuario del usuario actual.
     *
     * @return nombre de usuario o null si no hay sesion activa
     */
    public String getCurrentUserName() {
        return (currentUser != null) ? currentUser.getNombreUsuario() : null;
    }

    /**
     * Obtiene el rol del usuario actual.
     *
     * @return rol del usuario o null si no hay sesion activa
     */
    public String getCurrentUserRole() {
        return (currentUser != null) ? currentUser.getRol() : null;
    }

    /**
     * Verifica si hay una sesion activa.
     *
     * @return true si hay usuario logueado, false en caso contrario
     */
    public boolean isLoggedIn() {
        return isLoggedIn;
    }
    
    /**
     * Verifica si el usuario actual tiene un rol especifico.
     *
     * @param role nombre del rol a verificar
     * @return true si el usuario tiene el rol y esta logueado
     */
    public boolean hasRole(String role) {
        return isLoggedIn && currentUser != null && currentUser.getRol().equals(role);
    }
}