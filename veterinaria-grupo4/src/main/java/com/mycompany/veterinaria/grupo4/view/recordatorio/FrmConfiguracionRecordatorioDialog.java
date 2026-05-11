/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.veterinaria.grupo4.view.recordatorio;

import com.mycompany.veterinaria.grupo4.config.RecordatorioConfig;
import com.mycompany.veterinaria.grupo4.controller.CtrlRecordatorio;
import javax.swing.*;
import java.awt.*;

/**
 * Diálogo para editar una configuración de recordatorio existente
 * 
 * @author Usuario
 */
public class FrmConfiguracionRecordatorioDialog extends JDialog {
    
    private RecordatorioConfig config;
    private JTextArea txtMensaje;
    private JCheckBox chkActivo;
    private JLabel lblTipo;
    private JLabel lblAnticipacion;
    private CtrlRecordatorio controller;
    private boolean ok = false;
    
    public FrmConfiguracionRecordatorioDialog(Window parent, RecordatorioConfig config) {
        super(parent, "Editar Configuración de Recordatorio", ModalityType.APPLICATION_MODAL);
        this.config = config;
        this.controller = new CtrlRecordatorio();
        setSize(500, 350);
        setLocationRelativeTo(parent);
        initUI();
        cargarDatos();
    }
    
    private void initUI() {
        setLayout(new BorderLayout(10, 10));
        
        // Panel central con los campos
        JPanel pnlCentro = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Tipo (solo lectura)
        gbc.gridx = 0; gbc.gridy = 0;
        pnlCentro.add(new JLabel("Tipo de Recordatorio:"), gbc);
        gbc.gridx = 1;
        lblTipo = new JLabel();
        lblTipo.setFont(new Font("Arial", Font.BOLD, 12));
        pnlCentro.add(lblTipo, gbc);
        
        // Anticipación (solo lectura)
        gbc.gridx = 0; gbc.gridy = 1;
        pnlCentro.add(new JLabel("Anticipación:"), gbc);
        gbc.gridx = 1;
        lblAnticipacion = new JLabel();
        lblAnticipacion.setFont(new Font("Arial", Font.BOLD, 12));
        pnlCentro.add(lblAnticipacion, gbc);
        
        // Mensaje (editable)
        gbc.gridx = 0; gbc.gridy = 2;
        pnlCentro.add(new JLabel("Mensaje:"), gbc);
        gbc.gridx = 1;
        txtMensaje = new JTextArea(5, 30);
        txtMensaje.setLineWrap(true);
        txtMensaje.setWrapStyleWord(true);
        JScrollPane spMensaje = new JScrollPane(txtMensaje);
        spMensaje.setPreferredSize(new Dimension(300, 100));
        pnlCentro.add(spMensaje, gbc);
        
        // Activo (checkbox)
        gbc.gridx = 0; gbc.gridy = 3;
        pnlCentro.add(new JLabel("Activo:"), gbc);
        gbc.gridx = 1;
        chkActivo = new JCheckBox("Configuración activa");
        pnlCentro.add(chkActivo, gbc);
        
        // Panel de botones
        JPanel pnlBotones = new JPanel(new FlowLayout());
        JButton btnGuardar = new JButton("Guardar Cambios");
        JButton btnCancelar = new JButton("Cancelar");
        btnGuardar.setBackground(new Color(76, 175, 80));
        btnGuardar.setForeground(Color.WHITE);
        btnGuardar.setPreferredSize(new Dimension(120, 35));
        btnCancelar.setPreferredSize(new Dimension(100, 35));
        pnlBotones.add(btnGuardar);
        pnlBotones.add(btnCancelar);
        
        add(pnlCentro, BorderLayout.CENTER);
        add(pnlBotones, BorderLayout.SOUTH);
        
        // Eventos
        btnGuardar.addActionListener(e -> guardar());
        btnCancelar.addActionListener(e -> dispose());
    }
    
    private void cargarDatos() {
        if (config != null) {
            // Formatear el tipo para mostrar más amigable
            String tipoMostrar = "";
            switch (config.getTipoRecordatorio()) {
                case "CITA":
                    tipoMostrar = "Cita Médica";
                    break;
                case "VACUNA":
                    tipoMostrar = "Vacunación";
                    break;
                default:
                    tipoMostrar = config.getTipoRecordatorio();
                    break;
            }
            
            // Formatear anticipación para mostrar más amigable
            String anticipacionMostrar = "";
            switch (config.getAnticipacion()) {
                case "15_DIAS":
                    anticipacionMostrar = "15 días antes";
                    break;
                case "7_DIAS":
                    anticipacionMostrar = "7 días antes";
                    break;
                case "1_MES":
                    anticipacionMostrar = "1 mes antes";
                    break;
                case "12_HORAS":
                    anticipacionMostrar = "12 horas antes";
                    break;
                case "10_HORAS":
                    anticipacionMostrar = "10 horas antes";
                    break;
                case "5_MINUTOS":
                    anticipacionMostrar = "5 minutos antes";
                    break;
                case "30_SEGUNDOS":
                    anticipacionMostrar = "30 segundos antes";
                    break;
                default:
                    anticipacionMostrar = config.getAnticipacion();
                    break;
            }
            
            lblTipo.setText(tipoMostrar);
            lblAnticipacion.setText(anticipacionMostrar);
            txtMensaje.setText(config.getMensaje());
            chkActivo.setSelected(config.isActivo());
        }
    }
    
    private void guardar() {
        if (txtMensaje.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "El mensaje no puede estar vacío", 
                "Error de validación", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Actualizar la configuración
        config.setMensaje(txtMensaje.getText().trim());
        config.setActivo(chkActivo.isSelected());
        
        // Guardar en el servidor
        if (controller.actualizarConfiguracion(config)) {
            JOptionPane.showMessageDialog(this, 
                "Configuración actualizada correctamente", 
                "Éxito", 
                JOptionPane.INFORMATION_MESSAGE);
            ok = true;
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, 
                "Error al guardar la configuración", 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public boolean showDialog() {
        setVisible(true);
        return ok;
    }
    
    public RecordatorioConfig getConfiguracion() {
        return config;
    }
}