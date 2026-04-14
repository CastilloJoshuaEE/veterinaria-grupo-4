package com.mycompany.veterinaria.grupo4.view;

import com.mycompany.veterinaria.grupo4.model.entity.Recordatorio;
import com.mycompany.veterinaria.grupo4.service.RecordatorioService;
import javax.swing.*;
import java.awt.*;

public class frmNuevoRecordatorio extends JDialog {
    private RecordatorioService recordatorioService = new RecordatorioService();
    
    private JComboBox<String> cmbTipo, cmbAnticipacion;
    private JComboBox<?> cmbCita, cmbVacuna, cmbCorreoUsuario;
    private JTextArea txtMensaje;
    private JButton btnAceptar, btnCancelar, btnGenerarMensaje;
    
    public frmNuevoRecordatorio() {
        setTitle("Nuevo Recordatorio");
        setModal(true);
        setSize(500, 400);
        setLocationRelativeTo(null);
        initComponents();
    }
    
    private void initComponents() {
        setLayout(new BorderLayout());
        
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        int row = 0;
        gbc.gridx = 0; gbc.gridy = row;
        mainPanel.add(new JLabel("Tipo:"), gbc);
        gbc.gridx = 1;
        cmbTipo = new JComboBox<>(new String[]{"CITA", "VACUNA"});
        mainPanel.add(cmbTipo, gbc);
        
        row++;
        gbc.gridx = 0; gbc.gridy = row;
        mainPanel.add(new JLabel("Usuario:"), gbc);
        gbc.gridx = 1;
        cmbCorreoUsuario = new JComboBox<>();
        mainPanel.add(cmbCorreoUsuario, gbc);
        
        row++;
        gbc.gridx = 0; gbc.gridy = row;
        mainPanel.add(new JLabel("Cita:"), gbc);
        gbc.gridx = 1;
        cmbCita = new JComboBox<>();
        mainPanel.add(cmbCita, gbc);
        
        row++;
        gbc.gridx = 0; gbc.gridy = row;
        mainPanel.add(new JLabel("Vacuna:"), gbc);
        gbc.gridx = 1;
        cmbVacuna = new JComboBox<>();
        mainPanel.add(cmbVacuna, gbc);
        
        row++;
        gbc.gridx = 0; gbc.gridy = row;
        mainPanel.add(new JLabel("Anticipación:"), gbc);
        gbc.gridx = 1;
        cmbAnticipacion = new JComboBox<>(new String[]{"15_DIAS", "7_DIAS", "1_MES", "12_HORAS", "10_HORAS", "5_MINUTOS", "30_SEGUNDOS"});
        mainPanel.add(cmbAnticipacion, gbc);
        
        row++;
        gbc.gridx = 0; gbc.gridy = row;
        mainPanel.add(new JLabel("Mensaje:"), gbc);
        gbc.gridx = 1;
        txtMensaje = new JTextArea(5, 30);
        mainPanel.add(new JScrollPane(txtMensaje), gbc);
        
        row++;
        gbc.gridx = 0; gbc.gridy = row;
        gbc.gridwidth = 2;
        btnGenerarMensaje = new JButton("Generar Mensaje Automático");
        mainPanel.add(btnGenerarMensaje, gbc);
        
        add(mainPanel, BorderLayout.CENTER);
        
        JPanel buttonPanel = new JPanel(new FlowLayout());
        btnAceptar = new JButton("Aceptar");
        btnCancelar = new JButton("Cancelar");
        buttonPanel.add(btnAceptar);
        buttonPanel.add(btnCancelar);
        add(buttonPanel, BorderLayout.SOUTH);
        
        // Eventos
        cmbTipo.addActionListener(e -> {
            boolean esCita = "CITA".equals(cmbTipo.getSelectedItem());
            cmbCita.setEnabled(esCita);
            cmbVacuna.setEnabled(!esCita);
        });
        
        btnGenerarMensaje.addActionListener(e -> generarMensaje());
        btnAceptar.addActionListener(e -> guardar());
        btnCancelar.addActionListener(e -> dispose());
        
        cmbCita.setEnabled(true);
        cmbVacuna.setEnabled(false);
    }
    
    private void generarMensaje() {
        StringBuilder msg = new StringBuilder();
        msg.append("Recordatorio de ").append(cmbTipo.getSelectedItem()).append("\n");
        msg.append("Estimado cliente,\n");
        msg.append("Le recordamos que tiene un(a) ").append(cmbTipo.getSelectedItem().toString().toLowerCase());
        msg.append(" programado(a) para dentro de ").append(cmbAnticipacion.getSelectedItem()).append(".\n");
        msg.append("Por favor, no olvide asistir.\n");
        msg.append("Atentamente,\nVida Animal S.A.");
        txtMensaje.setText(msg.toString());
    }
    
    private void guardar() {
        Recordatorio r = new Recordatorio();
        r.setTipo((String) cmbTipo.getSelectedItem());
        r.setMensaje(txtMensaje.getText());
        
        String anticipacion = (String) cmbAnticipacion.getSelectedItem();
        int id = recordatorioService.registrar(r, anticipacion);
        
        if (id > 0) {
            JOptionPane.showMessageDialog(this, "Recordatorio registrado", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        }
    }
}