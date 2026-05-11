package com.mycompany.veterinaria.grupo4.view.recordatorio;

import com.mycompany.veterinaria.grupo4.model.entity.Recordatorio;
import javax.swing.*;
import java.awt.*;

public class FrmEditarRecordatorioDialog extends JDialog {
    private Recordatorio recordatorio;
    private JTextArea txtMensaje;
    private JCheckBox chkLeido;
    private boolean ok = false;

    public FrmEditarRecordatorioDialog(Window parent, Recordatorio r) {
        super(parent, "Editar Recordatorio", ModalityType.APPLICATION_MODAL);
        this.recordatorio = r;
        setSize(450, 250);
        setLocationRelativeTo(parent);
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout(10, 10));
        
        JPanel pnlCentro = new JPanel(new GridLayout(0, 1, 5, 5));
        pnlCentro.add(new JLabel("Mensaje:"));
        txtMensaje = new JTextArea(recordatorio.getMensaje(), 5, 30);
        txtMensaje.setLineWrap(true);
        txtMensaje.setWrapStyleWord(true);
        pnlCentro.add(new JScrollPane(txtMensaje));
        
        chkLeido = new JCheckBox("Marcar como Leído", recordatorio.isLeido());
        pnlCentro.add(chkLeido);
        
        JPanel pnlBotones = new JPanel();
        JButton btnGuardar = new JButton("Guardar Cambios");
        JButton btnCancelar = new JButton("Cancelar");
        pnlBotones.add(btnGuardar);
        pnlBotones.add(btnCancelar);
        
        add(pnlCentro, BorderLayout.CENTER);
        add(pnlBotones, BorderLayout.SOUTH);
        
        btnGuardar.addActionListener(e -> {
            recordatorio.setMensaje(txtMensaje.getText());
            recordatorio.setLeido(chkLeido.isSelected());
            ok = true;
            dispose();
        });
        btnCancelar.addActionListener(e -> dispose());
    }
    
    public boolean showDialog() {
        setVisible(true);
        return ok;
    }
    
    public Recordatorio getRecordatorio() { return recordatorio; }
}