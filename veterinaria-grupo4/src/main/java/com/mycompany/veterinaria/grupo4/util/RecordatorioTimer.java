/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.veterinaria.grupo4.util;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * Temporizador para recordatorios que ejecuta una tarea de forma periódica.
 * Versión Java equivalente al RecordatorioTimer de C#.
 * 
 * @author Usuario
 */
public class RecordatorioTimer implements AutoCloseable {
    
    private final ScheduledExecutorService scheduler;
    private final Runnable callback;
    private final int intervaloMilisegundos;
    private ScheduledFuture<?> futureTask;
    private boolean isRunning;
    
    /**
     * Constructor del temporizador.
     * 
     * @param callback La tarea a ejecutar periódicamente (debe ser un Runnable)
     * @param intervaloMilisegundos Intervalo en milisegundos entre ejecuciones
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
     * @param callback La tarea a ejecutar periódicamente
     */
    public RecordatorioTimer(Runnable callback) {
        this(callback, 30000);
    }
    
    /**
     * Inicia el temporizador. La tarea se ejecutará inmediatamente y luego
     * cada 'intervaloMilisegundos' milisegundos.
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
     * Detiene el temporizador. La tarea no se ejecutará más hasta que se llame a start() nuevamente.
     */
    public void stop() {
        if (futureTask != null && !futureTask.isCancelled()) {
            futureTask.cancel(false);
        }
        isRunning = false;
    }
    
    /**
     * Verifica si el temporizador está en ejecución.
     * 
     * @return true si está en ejecución, false en caso contrario
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
     * Cambia el intervalo de ejecución (requiere reiniciar el temporizador).
     * 
     * @param nuevoIntervaloMilisegundos Nuevo intervalo en milisegundos
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