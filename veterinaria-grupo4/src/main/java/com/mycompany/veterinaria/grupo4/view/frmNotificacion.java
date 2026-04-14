package com.mycompany.veterinaria.grupo4.view;

import com.mycompany.veterinaria.grupo4.model.entity.Recordatorio;
import com.mycompany.veterinaria.grupo4.service.RecordatorioService;
import javax.swing.*;
import java.awt.*;

public class frmNotificacion extends JDialog {
    private RecordatorioService recordatorioService = new RecordatorioService();
    private Recordatorio recordatorio;
    private Timer autoCloseTimer;
    
    private JTextArea txtMensaje, txtDetalles;
    private JButton btnAceptar;
    
    public frmNotificacion(Recordatorio recordatorio) {
        this.recordatorio = recordatorio;
        setTitle("Recordatorio de " + recordatorio.getTipo());
        setModal(true);
        setSize(500, 350);
        setLocationRelativeTo(null);
        initComponents();
        
        // Auto-cerrar después de 10 segundos
        autoCloseTimer = new Timer(10000, e -> {
            autoCloseTimer.stop();
            dispose();
        });
        autoCloseTimer.start();
    }
    
    private void initComponents() {
        setLayout(new BorderLayout());
        
        // Color según tipo
        if ("CITA".equals(recordatorio.getTipo())) {
            getContentPane().setBackground(new Color(173, 216, 230)); // Light Blue
        } else {
            getContentPane().setBackground(new Color(144, 238, 144)); // Light Green
        }
        
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setOpaque(false);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        // Mensaje principal
        txtMensaje = new JTextArea();
        txtMensaje.setText(recordatorio.getMensaje());
        txtMensaje.setFont(new Font("Segoe UI", Font.BOLD, 12));
        txtMensaje.setEditable(false);
        txtMensaje.setOpaque(false);
        txtMensaje.setWrapStyleWord(true);
        txtMensaje.setLineWrap(true);
        mainPanel.add(txtMensaje, BorderLayout.NORTH);
        
        // Detalles
        txtDetalles = new JTextArea();
        txtDetalles.setText(obtenerDetalles());
        txtDetalles.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        txtDetalles.setEditable(false);
        txtDetalles.setOpaque(false);
        txtDetalles.setWrapStyleWord(true);
        txtDetalles.setLineWrap(true);
        mainPanel.add(new JScrollPane(txtDetalles), BorderLayout.CENTER);
        
        // Botón
        JPanel buttonPanel = new JPanel(new FlowLayout());
        btnAceptar = new JButton("OK, no notificar más");
        btnAceptar.addActionListener(e -> {
            autoCloseTimer.stop();
            recordatorioService.marcarComoLeido(recordatorio.getIdRecordatorio());
            dispose();
        });
        buttonPanel.add(btnAceptar);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
    }
    
    private String obtenerDetalles() {
        StringBuilder sb = new StringBuilder();
        sb.append("\n--- Detalles ---\n");
        
        if ("CITA".equals(recordatorio.getTipo())) {
            sb.append("Tipo: Cita Médica\n");
            if (recordatorio.getIdCita() != null) {
                sb.append("ID Cita: ").append(recordatorio.getIdCita()).append("\n");
            }
        } else {
            sb.append("Tipo: Vacunación\n");
            if (recordatorio.getIdVacuna() != null) {
                sb.append("ID Vacuna: ").append(recordatorio.getIdVacuna()).append("\n");
            }
        }
        
        sb.append("\nAtentamente,\nVida Animal S.A.");
        return sb.toString();
    }
}