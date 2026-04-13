package com.mycompany.veterinaria.grupo4.view;

import com.mycompany.veterinaria.grupo4.model.entity.Recordatorio;
import com.mycompany.veterinaria.grupo4.service.RecordatorioService;
import javax.swing.*;

public class frmEditarRecordatorio extends JDialog {
    private RecordatorioService recordatorioService = new RecordatorioService();
    private int idRecordatorio;
    
    private JTextArea txtMensaje;
    private JCheckBox chkLeido;
    private JButton btnAceptar, btnCancelar;
    
    public frmEditarRecordatorio(int id, String mensaje, boolean leido) {
        this.idRecordatorio = id;
        setTitle("Editar Recordatorio");
        setModal(true);
        setSize(400, 300);
        setLocationRelativeTo(null);
        initComponents();
        
        txtMensaje.setText(mensaje);
        chkLeido.setSelected(leido);
    }
    
    private void initComponents() {
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        mainPanel.add(new JLabel("Mensaje:"));
        txtMensaje = new JTextArea(8, 40);
        mainPanel.add(new JScrollPane(txtMensaje));
        
        chkLeido = new JCheckBox("Marcar como leído");
        mainPanel.add(chkLeido);
        
        add(mainPanel);
        
        JPanel buttonPanel = new JPanel();
        btnAceptar = new JButton("Guardar");
        btnCancelar = new JButton("Cancelar");
        buttonPanel.add(btnAceptar);
        buttonPanel.add(btnCancelar);
        add(buttonPanel);
        
        btnAceptar.addActionListener(e -> guardar());
        btnCancelar.addActionListener(e -> dispose());
    }
    
    private void guardar() {
        Recordatorio r = new Recordatorio();
        r.setIdRecordatorio(idRecordatorio);
        r.setMensaje(txtMensaje.getText());
        r.setLeido(chkLeido.isSelected());
        
        if (recordatorioService.actualizar(r)) {
            JOptionPane.showMessageDialog(this, "Recordatorio actualizado", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        }
    }
}