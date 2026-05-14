/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.veterinaria.grupo4.view.notificacion;

import com.mycompany.veterinaria.grupo4.model.entity.Recordatorio;
import com.mycompany.veterinaria.grupo4.service.RecordatorioService;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FrmNotificacion extends JDialog {
    private Recordatorio recordatorio;
    private RecordatorioService service;
    private Timer autoCloseTimer;

    public FrmNotificacion(Frame parent, Recordatorio recordatorio) {
        super(parent, "Recordatorio", true);
        this.recordatorio = recordatorio;
        this.service = new RecordatorioService();
        initUI();
    }

    private void initUI() {
        setSize(450, 200);
        setLocationRelativeTo(getParent());
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        
        // Color de fondo según tipo
        if ("CITA".equals(recordatorio.getTipo())) {
            getContentPane().setBackground(new Color(173, 216, 230)); // Azul claro
        } else {
            getContentPane().setBackground(new Color(144, 238, 144)); // Verde claro
        }
        
        setLayout(new BorderLayout(10, 10));
        
        JTextArea txtMensaje = new JTextArea(recordatorio.getMensaje());
        txtMensaje.setWrapStyleWord(true);
        txtMensaje.setLineWrap(true);
        txtMensaje.setOpaque(false);
        txtMensaje.setEditable(false);
        txtMensaje.setFont(new Font("Arial", Font.PLAIN, 14));
        
        JPanel pnlDetalles = new JPanel(new GridLayout(0, 1));
        pnlDetalles.setOpaque(false);
        if (recordatorio.getIdCita() != null) {
            pnlDetalles.add(new JLabel("Servicio: Consulta Veterinaria"));
            pnlDetalles.add(new JLabel("Veterinario: Dr. Ejemplo"));
        } else if (recordatorio.getIdVacuna() != null) {
            pnlDetalles.add(new JLabel("Vacuna: Antirrábica"));
            pnlDetalles.add(new JLabel("Próxima dosis: " + new SimpleDateFormat("dd/MM/yyyy").format(new Date())));
        }
        
        JButton btnAceptar = new JButton("Aceptar (Marcar como Leído)");
        JButton btnCerrar = new JButton("Cerrar");
        
        JPanel pnlBotones = new JPanel();
        pnlBotones.setOpaque(false);
        pnlBotones.add(btnAceptar);
        pnlBotones.add(btnCerrar);
        
        add(new JScrollPane(txtMensaje), BorderLayout.CENTER);
        add(pnlDetalles, BorderLayout.NORTH);
        add(pnlBotones, BorderLayout.SOUTH);
        
        btnAceptar.addActionListener(e -> {
            if (service.marcarComoLeido(recordatorio.getIdRecordatorio())) {
                JOptionPane.showMessageDialog(this, "Recordatorio marcado como leído.");
                dispose();
            }
        });
        
        btnCerrar.addActionListener(e -> dispose());
        
        // Auto-cierre después de 10 segundos
        autoCloseTimer = new Timer(10000, (ActionEvent e) -> {
            autoCloseTimer.stop();
            dispose();
        });
        autoCloseTimer.start();
    }
    
    @Override
    public void dispose() {
        if (autoCloseTimer != null) autoCloseTimer.stop();
        super.dispose();
    }
}