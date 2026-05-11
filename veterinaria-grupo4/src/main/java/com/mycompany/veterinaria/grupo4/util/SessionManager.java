package com.mycompany.veterinaria.grupo4.util;

import com.mycompany.veterinaria.grupo4.model.entity.Usuario;

/**
 * Gestiona la sesión del usuario actual en la aplicación
 */
public class SessionManager {
    private static SessionManager instance;
    private Usuario currentUser;
    private boolean isLoggedIn;

    private SessionManager() {
        this.isLoggedIn = false;
        this.currentUser = null;
    }

    public static synchronized SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }

    public void login(Usuario user) {
        if (user != null) {
            this.currentUser = user;
            this.isLoggedIn = true;
            System.out.println(" Usuario logueado: " + user.getNombreUsuario() + " (ID: " + user.getIdUsuario() + ")");
        }
    }

    public void logout() {
        // Detener NotificationManager al cerrar sesión
        NotificationManager.getInstance().stop();
        
        this.currentUser = null;
        this.isLoggedIn = false;
        System.out.println(" Sesión cerrada");
    }

    public Usuario getCurrentUser() {
        return currentUser;
    }

    public int getCurrentUserId() {
        return (currentUser != null) ? currentUser.getIdUsuario() : -1;
    }

    public String getCurrentUserName() {
        return (currentUser != null) ? currentUser.getNombreUsuario() : null;
    }

    public String getCurrentUserRole() {
        return (currentUser != null) ? currentUser.getRol() : null;
    }

    public boolean isLoggedIn() {
        return isLoggedIn;
    }
    
    public boolean hasRole(String role) {
        return isLoggedIn && currentUser != null && currentUser.getRol().equals(role);
    }
}