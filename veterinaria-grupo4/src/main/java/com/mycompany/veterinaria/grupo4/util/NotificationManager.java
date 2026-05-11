package com.mycompany.veterinaria.grupo4.util;

import com.mycompany.veterinaria.grupo4.model.entity.Recordatorio;
import com.mycompany.veterinaria.grupo4.service.RecordatorioService;
import com.mycompany.veterinaria.grupo4.view.notificacion.FrmNotificacion;
import javax.swing.*;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class NotificationManager {
    private static NotificationManager instance;
    private Timer timer;
    private RecordatorioService service;
    private int currentUserId;
    private boolean isRunning = false;

    private NotificationManager() {
        service = new RecordatorioService();
    }

    public static synchronized NotificationManager getInstance() {
        if (instance == null) {
            instance = new NotificationManager();
        }
        return instance;
    }

    //  Inicia el manager con un usuario específico
    public void start(int userId) {
        // Validar que no se inicie con usuario inválido
        if (userId <= 0) {
            System.err.println(" No se puede iniciar NotificationManager: ID de usuario inválido (" + userId + ")");
            return;
        }
        
        // Si ya está corriendo con el mismo usuario, no hacer nada
        if (isRunning && this.currentUserId == userId) {
            System.out.println(" NotificationManager ya está corriendo para usuario ID: " + userId);
            return;
        }
        
        // Si está corriendo con otro usuario, detenerlo primero
        if (isRunning) {
            System.out.println(" Cambiando usuario de NotificationManager de " + this.currentUserId + " a " + userId);
            stop();
        }
        
        this.currentUserId = userId;
        timer = new Timer(true);
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                checkNotifications();
            }
        }, 0, 30000); // 30 segundos
        isRunning = true;
        System.out.println(" NotificationManager iniciado para usuario ID: " + userId);
    }

    //  Verificar solo notificaciones del usuario actual
    private void checkNotifications() {
        if (currentUserId <= 0 || !isRunning) {
            return;
        }
        
        try {
            List<Recordatorio> pendientes = service.obtenerPendientes(currentUserId);
            
            if (pendientes != null && !pendientes.isEmpty()) {
                System.out.println(" Se encontraron " + pendientes.size() + " notificaciones pendientes para usuario " + currentUserId);
                
                for (Recordatorio rec : pendientes) {
                    SwingUtilities.invokeLater(() -> {
                        FrmNotificacion notif = new FrmNotificacion(null, rec);
                        notif.setVisible(true);
                    });
                    service.incrementarContador(rec.getIdRecordatorio());
                }
            }
        } catch (Exception e) {
            System.err.println(" Error al verificar notificaciones: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void stop() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        isRunning = false;
        currentUserId = 0;
        System.out.println("NotificationManager detenido");
    }
    
    public boolean isRunning() {
        return isRunning;
    }
    
    public int getCurrentUserId() {
        return currentUserId;
    }
    
    public void restart() {
        if (currentUserId > 0) {
            stop();
            start(currentUserId);
        }
    }
}