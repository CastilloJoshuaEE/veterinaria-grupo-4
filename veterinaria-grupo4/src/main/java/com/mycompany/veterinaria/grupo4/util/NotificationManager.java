package com.mycompany.veterinaria.grupo4.util;

import com.mycompany.veterinaria.grupo4.model.entity.Recordatorio;
import com.mycompany.veterinaria.grupo4.service.RecordatorioService;
import com.mycompany.veterinaria.grupo4.view.notificacion.FrmNotificacion;
import javax.swing.*;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Gestor central de notificaciones del sistema.
 * <p>
 * Implementa un patron Singleton para manejar las notificaciones push
 * a los usuarios del sistema. Verifica periodicamente (cada 30 segundos)
 * los recordatorios pendientes y muestra ventanas modales con la informacion
 * correspondiente. Se integra con el RecordatorioService para obtener
 * los datos de la base de datos.
 * </p>
 * 
 * <p><b>Fecha de inicio del proyecto:</b> 15/04/2026</p>
 * 
 * @author CHILAN CHILAN DANNY ANDRES – MODULO: AGENDAMIENTO DE CITA
 * @version 1.0
 * @since 1.0
 */
public class NotificationManager {
    private static NotificationManager instance;
    private Timer timer;
    private RecordatorioService service;
    private int currentUserId;
    private boolean isRunning = false;

    /**
     * Constructor privado para el patron Singleton.
     * Inicializa el servicio de recordatorios.
     */
    private NotificationManager() {
        service = new RecordatorioService();
    }

    /**
     * Obtiene la instancia unica del gestor de notificaciones.
     *
     * @return instancia del NotificationManager
     */
    public static synchronized NotificationManager getInstance() {
        if (instance == null) {
            instance = new NotificationManager();
        }
        return instance;
    }

    /**
     * Inicia el gestor de notificaciones para un usuario especifico.
     * Programa un temporizador que verifica notificaciones cada 30 segundos.
     *
     * @param userId identificador del usuario logueado
     */
    public void start(int userId) {
        if (userId <= 0) {
            System.err.println("No se puede iniciar NotificationManager: ID de usuario invalido (" + userId + ")");
            return;
        }
        
        if (isRunning && this.currentUserId == userId) {
            System.out.println("NotificationManager ya esta corriendo para usuario ID: " + userId);
            return;
        }
        
        if (isRunning) {
            System.out.println("Cambiando usuario de NotificationManager de " + this.currentUserId + " a " + userId);
            stop();
        }
        
        this.currentUserId = userId;
        timer = new Timer(true);
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                checkNotifications();
            }
        }, 0, 30000);
        isRunning = true;
        System.out.println("NotificationManager iniciado para usuario ID: " + userId);
    }

    /**
     * Verifica las notificaciones pendientes del usuario actual.
     * Si hay recordatorios pendientes, los muestra y actualiza el contador.
     */
    private void checkNotifications() {
        if (currentUserId <= 0 || !isRunning) {
            return;
        }
        
        try {
            List<Recordatorio> pendientes = service.obtenerPendientes(currentUserId);
            
            if (pendientes != null && !pendientes.isEmpty()) {
                System.out.println("Se encontraron " + pendientes.size() + " notificaciones pendientes para usuario " + currentUserId);
                
                for (Recordatorio rec : pendientes) {
                    SwingUtilities.invokeLater(() -> {
                        FrmNotificacion notif = new FrmNotificacion(null, rec);
                        notif.setVisible(true);
                    });
                    service.incrementarContador(rec.getIdRecordatorio());
                }
            }
        } catch (Exception e) {
            System.err.println("Error al verificar notificaciones: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Detiene el gestor de notificaciones.
     * Cancela el temporizador y limpia el estado.
     */
    public void stop() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        isRunning = false;
        currentUserId = 0;
        System.out.println("NotificationManager detenido");
    }
    
    /**
     * Verifica si el gestor de notificaciones esta en ejecucion.
     *
     * @return true si esta corriendo, false en caso contrario
     */
    public boolean isRunning() {
        return isRunning;
    }
    
    /**
     * Obtiene el ID del usuario actual.
     *
     * @return ID del usuario o 0 si no hay usuario activo
     */
    public int getCurrentUserId() {
        return currentUserId;
    }
    
    /**
     * Reinicia el gestor de notificaciones con el mismo usuario.
     */
    public void restart() {
        if (currentUserId > 0) {
            stop();
            start(currentUserId);
        }
    }
}