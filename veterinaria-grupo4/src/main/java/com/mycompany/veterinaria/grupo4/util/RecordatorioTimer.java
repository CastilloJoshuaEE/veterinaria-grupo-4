package com.mycompany.veterinaria.grupo4.util;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * Temporizador para recordatorios que ejecuta una tarea de forma periodica.
 * <p>
 * Proporciona una abstraccion sobre ScheduledExecutorService para gestionar
 * tareas programadas en intervalos regulares. Permite iniciar, detener,
 * reiniciar y cambiar el intervalo de ejecucion de la tarea asociada.
 * Implementa AutoCloseable para liberar recursos automaticamente.
 * </p>
 * 
 * <p><b>Fecha de inicio del proyecto:</b> 15/04/2026</p>
 * 
 * @author CHILAN CHILAN DANNY ANDRES – MODULO: AGENDAMIENTO DE CITA
 * @version 1.0
 * @since 1.0
 */
public class RecordatorioTimer implements AutoCloseable {
    
    private final ScheduledExecutorService scheduler;
    private final Runnable callback;
    private final int intervaloMilisegundos;
    private ScheduledFuture<?> futureTask;
    private boolean isRunning;
    
    /**
     * Constructor del temporizador con intervalo personalizado.
     *
     * @param callback tarea a ejecutar periodicamente
     * @param intervaloMilisegundos intervalo en milisegundos entre ejecuciones
     * @throws IllegalArgumentException si el callback es null
     */
    public RecordatorioTimer(Runnable callback, int intervaloMilisegundos) {
        if (callback == null) {
            throw new IllegalArgumentException("callback no puede ser null");
        }
        this.callback = callback;
        this.intervaloMilisegundos = intervaloMilisegundos;
        this.scheduler = Executors.newSingleThreadScheduledExecutor();
        this.isRunning = false;
    }
    
    /**
     * Constructor con intervalo por defecto de 30 segundos.
     *
     * @param callback tarea a ejecutar periodicamente
     */
    public RecordatorioTimer(Runnable callback) {
        this(callback, 30000);
    }
    
    /**
     * Inicia el temporizador. La tarea se ejecutara inmediatamente y luego
     * cada intervaloMilisegundos milisegundos.
     */
    public void start() {
        if (isRunning) {
            return;
        }
        
        futureTask = scheduler.scheduleAtFixedRate(() -> {
            try {
                callback.run();
            } catch (Exception ex) {
                System.err.println("Error en el temporizador de recordatorios: " + ex.getMessage());
                ex.printStackTrace();
            }
        }, 0, intervaloMilisegundos, TimeUnit.MILLISECONDS);
        
        isRunning = true;
    }
    
    /**
     * Detiene el temporizador. La tarea no se ejecutara mas hasta que se llame a start() nuevamente.
     */
    public void stop() {
        if (futureTask != null && !futureTask.isCancelled()) {
            futureTask.cancel(false);
        }
        isRunning = false;
    }
    
    /**
     * Verifica si el temporizador esta en ejecucion.
     *
     * @return true si esta en ejecucion, false en caso contrario
     */
    public boolean isRunning() {
        return isRunning && futureTask != null && !futureTask.isCancelled();
    }
    
    /**
     * Reinicia el temporizador (lo detiene y lo vuelve a iniciar).
     */
    public void restart() {
        stop();
        start();
    }
    
    /**
     * Cambia el intervalo de ejecucion (requiere reiniciar el temporizador).
     *
     * @param nuevoIntervaloMilisegundos nuevo intervalo en milisegundos
     * @throws IllegalArgumentException si el intervalo es menor o igual a 0
     */
    public void changeInterval(int nuevoIntervaloMilisegundos) {
        if (nuevoIntervaloMilisegundos <= 0) {
            throw new IllegalArgumentException("El intervalo debe ser mayor a 0");
        }
        restart();
    }
    
    /**
     * Libera los recursos del temporizador. Implementa AutoCloseable.
     */
    @Override
    public void close() {
        stop();
        scheduler.shutdown();
        try {
            if (!scheduler.awaitTermination(5, TimeUnit.SECONDS)) {
                scheduler.shutdownNow();
            }
        } catch (InterruptedException e) {
            scheduler.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}