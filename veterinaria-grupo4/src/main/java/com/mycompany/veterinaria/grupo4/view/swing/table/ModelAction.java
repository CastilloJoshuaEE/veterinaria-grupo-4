/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.veterinaria.grupo4.view.swing.table;

/**
 *
 * @author Juan 
 */
public class ModelAction {
    
    private final Runnable onEdit;
    private final Runnable onView;
    private final Runnable onDelete;
    private final Runnable onMedicalRecord;  // Nueva acción para ficha médica
    private final Runnable onHistorial;
    // Constructor para 3 acciones (para las otras tablas)
    public ModelAction(Runnable onEdit, Runnable onView, Runnable onDelete) {
        this(onEdit, onView, onDelete, null, null);
    }
    // Constructor para 4 acciones (mascotas)
    public ModelAction(Runnable onEdit, Runnable onView, Runnable onDelete, Runnable onMedicalRecord) {
        this(onEdit, onView, onDelete, onMedicalRecord, null);
    }
    // Constructor con 5 acciones (para mascotas)
    public ModelAction(Runnable onEdit, Runnable onView, Runnable onDelete, Runnable onMedicalRecord, Runnable onHistorial) {
        this.onEdit   = onEdit;
        this.onView   = onView;
        this.onDelete = onDelete;
        this.onMedicalRecord = onMedicalRecord;
        this.onHistorial = onHistorial;
        
    }
    
    public Runnable getOnEdit()   { return onEdit;   }
    public Runnable getOnView()   { return onView;   }
    public Runnable getOnDelete() { return onDelete; }
    public Runnable getOnMedicalRecord() { return onMedicalRecord; }
    public Runnable getOnHistorial() { return onHistorial; }
}