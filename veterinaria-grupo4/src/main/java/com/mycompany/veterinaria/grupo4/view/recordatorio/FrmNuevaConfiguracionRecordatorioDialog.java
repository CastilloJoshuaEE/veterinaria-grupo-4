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
 * Diálogo para crear una nueva configuración de recordatorio
 * 
 * @author Usuario
 */
public class FrmNuevaConfiguracionRecordatorioDialog extends JDialog {
    
    private JComboBox<String> cmbTipo;
    private JComboBox<String> cmbAnticipacion;
    private JTextArea txtMensaje;
    private JCheckBox chkActivo;
    private CtrlRecordatorio controller;
    private RecordatorioConfig nuevaConfig;
    private boolean ok = false;
    
    public FrmNuevaConfiguracionRecordatorioDialog(Window parent) {
        super(parent, "Nueva Configuración de Recordatorio", ModalityType.APPLICATION_MODAL);
        this.controller = new CtrlRecordatorio();
        setSize(500, 450);
        setLocationRelativeTo(parent);
        initUI();
    }
    
    private void initUI() {
        setLayout(new BorderLayout(10, 10));
        
        // Panel central con los campos
        JPanel pnlCentro = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Tipo
        gbc.gridx = 0; gbc.gridy = 0;
        pnlCentro.add(new JLabel("Tipo de Recordatorio:*"), gbc);
        gbc.gridx = 1;
        cmbTipo = new JComboBox<>(new String[]{"CITA", "VACUNA"});
        cmbTipo.setPreferredSize(new Dimension(200, 25));
        pnlCentro.add(cmbTipo, gbc);
        
        // Anticipación
        gbc.gridx = 0; gbc.gridy = 1;
        pnlCentro.add(new JLabel("Anticipación:*"), gbc);
        gbc.gridx = 1;
        cmbAnticipacion = new JComboBox<>(new String[]{
            "15_DIAS", "7_DIAS", "1_MES", "12_HORAS", "10_HORAS", "5_MINUTOS", "30_SEGUNDOS"
        });
        cmbAnticipacion.setPreferredSize(new Dimension(200, 25));
        pnlCentro.add(cmbAnticipacion, gbc);
        
        // Mensaje
        gbc.gridx = 0; gbc.gridy = 2;
        pnlCentro.add(new JLabel("Mensaje:*"), gbc);
        gbc.gridx = 1;
        txtMensaje = new JTextArea(5, 30);
        txtMensaje.setLineWrap(true);
        txtMensaje.setWrapStyleWord(true);
        JScrollPane spMensaje = new JScrollPane(txtMensaje);
        spMensaje.setPreferredSize(new Dimension(300, 100));
        pnlCentro.add(spMensaje, gbc);
        
        // Activo
        gbc.gridx = 0; gbc.gridy = 3;
        pnlCentro.add(new JLabel("Estado:"), gbc);
        gbc.gridx = 1;
        chkActivo = new JCheckBox("Activar esta configuración");
        chkActivo.setSelected(true);
        pnlCentro.add(chkActivo, gbc);
        
        // Panel de botones
        JPanel pnlBotones = new JPanel(new FlowLayout());
        JButton btnGuardar = new JButton("Crear Configuración");
        JButton btnCancelar = new JButton("Cancelar");
        btnGuardar.setBackground(new Color(33, 150, 243));
        btnGuardar.setForeground(Color.WHITE);
        btnGuardar.setPreferredSize(new Dimension(140, 35));
        btnCancelar.setPreferredSize(new Dimension(100, 35));
        pnlBotones.add(btnGuardar);
        pnlBotones.add(btnCancelar);
        
        // Panel de ayuda
        JPanel pnlAyuda = new JPanel();
        JLabel lblAyuda = new JLabel("<html><small>Nota: Las configuraciones activas se usarán para generar recordatorios automáticos</small></html>");
        lblAyuda.setForeground(Color.GRAY);
        pnlAyuda.add(lblAyuda);
        
        add(pnlCentro, BorderLayout.CENTER);
        add(pnlBotones, BorderLayout.SOUTH);
        add(pnlAyuda, BorderLayout.NORTH);
        
        // Eventos
        btnGuardar.addActionListener(e -> crearConfiguracion());
        btnCancelar.addActionListener(e -> dispose());
        
        // Generar mensaje automático al seleccionar tipo y anticipación
        cmbTipo.addActionListener(e -> generarMensajeSugerido());
        cmbAnticipacion.addActionListener(e -> generarMensajeSugerido());
    }
    
    private void generarMensajeSugerido() {
        String tipo = (String) cmbTipo.getSelectedItem();
        String anticipacion = (String) cmbAnticipacion.getSelectedItem();
        
        if (tipo == null || anticipacion == null) return;
        
        String mensajeSugerido = "";
        String tiempo = "";
        
        switch (anticipacion) {
            case "15_DIAS": tiempo = "15 días"; break;
            case "7_DIAS": tiempo = "7 días"; break;
            case "1_MES": tiempo = "1 mes"; break;
            case "12_HORAS": tiempo = "12 horas"; break;
            case "10_HORAS": tiempo = "10 horas"; break;
            case "5_MINUTOS": tiempo = "5 minutos"; break;
            case "30_SEGUNDOS": tiempo = "30 segundos"; break;
        }
        
        if ("CITA".equals(tipo)) {
            mensajeSugerido = String.format(
                "Tiene una cita pendiente dentro de %s. Por favor, no falte.",
                tiempo
            );
        } else {
            mensajeSugerido = String.format(
                "Recordatorio: Vacunación próxima dentro de %s. Acuda a su veterinario de confianza.",
                tiempo
            );
        }
        
        if (txtMensaje.getText().trim().isEmpty()) {
            txtMensaje.setText(mensajeSugerido);
        }
    }
    
    private void crearConfiguracion() {
        // Validaciones
        if (cmbTipo.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Seleccione un tipo de recordatorio", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (cmbAnticipacion.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Seleccione una anticipación", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (txtMensaje.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "El mensaje no puede estar vacío", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Crear nueva configuración
        nuevaConfig = new RecordatorioConfig();
        nuevaConfig.setTipoRecordatorio((String) cmbTipo.getSelectedItem());
        nuevaConfig.setAnticipacion((String) cmbAnticipacion.getSelectedItem());
        nuevaConfig.setMensaje(txtMensaje.getText().trim());
        nuevaConfig.setActivo(chkActivo.isSelected());
        
        // Llamar al controlador para crear la configuración en el backend
        int resultado = controller.crearConfiguracion(nuevaConfig);
        
        if (resultado > 0) {
            JOptionPane.showMessageDialog(this, 
                "Configuración creada correctamente", 
                "Éxito", 
                JOptionPane.INFORMATION_MESSAGE);
            ok = true;
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, 
                "Error al crear la configuración. Verifique que no exista una configuración duplicada.", 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public boolean showDialog() {
        setVisible(true);
        return ok;
    }
    
    public RecordatorioConfig getConfiguracion() {
        return nuevaConfig;
    }
}