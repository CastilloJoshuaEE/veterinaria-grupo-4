package com.mycompany.veterinaria.grupo4.view;

import com.mycompany.veterinaria.grupo4.model.entity.FichaMedica;
import com.mycompany.veterinaria.grupo4.service.FichaMedicaService;
import javax.swing.*;
import java.awt.*;

public class frmEditarFichaMedica extends JDialog {
    private FichaMedicaService fichaMedicaService = new FichaMedicaService();
    private int idMascota;
    
    private JTextArea txtAlergias, txtEnfermedadesCronicas, txtObservaciones;
    private JButton btnGuardar, btnCancelar;
    
    public frmEditarFichaMedica(int idMascota) {
        this.idMascota = idMascota;
        setTitle("Editar Ficha Médica");
        setModal(true);
        setSize(500, 400);
        setLocationRelativeTo(null);
        initComponents();
        cargarDatos();
    }
    
    private void initComponents() {
        setLayout(new BorderLayout());
        
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.BOTH;
        
        int row = 0;
        gbc.gridx = 0; gbc.gridy = row;
        mainPanel.add(new JLabel("Alergias:"), gbc);
        gbc.gridx = 1;
        txtAlergias = new JTextArea(3, 30);
        mainPanel.add(new JScrollPane(txtAlergias), gbc);
        
        row++;
        gbc.gridx = 0; gbc.gridy = row;
        mainPanel.add(new JLabel("Enfermedades Crónicas:"), gbc);
        gbc.gridx = 1;
        txtEnfermedadesCronicas = new JTextArea(3, 30);
        mainPanel.add(new JScrollPane(txtEnfermedadesCronicas), gbc);
        
        row++;
        gbc.gridx = 0; gbc.gridy = row;
        mainPanel.add(new JLabel("Observaciones:"), gbc);
        gbc.gridx = 1;
        txtObservaciones = new JTextArea(3, 30);
        mainPanel.add(new JScrollPane(txtObservaciones), gbc);
        
        add(mainPanel, BorderLayout.CENTER);
        
        JPanel buttonPanel = new JPanel(new FlowLayout());
        btnGuardar = new JButton("Guardar");
        btnCancelar = new JButton("Cancelar");
        buttonPanel.add(btnGuardar);
        buttonPanel.add(btnCancelar);
        add(buttonPanel, BorderLayout.SOUTH);
        
        btnGuardar.addActionListener(e -> guardar());
        btnCancelar.addActionListener(e -> dispose());
    }
    
    private void cargarDatos() {
        FichaMedica ficha = fichaMedicaService.obtenerPorMascota(idMascota);
        if (ficha != null) {
            txtAlergias.setText(ficha.getAlergias());
            txtEnfermedadesCronicas.setText(ficha.getEnfermedadesCronicas());
            txtObservaciones.setText(ficha.getObservaciones());
        }
    }
    
    private void guardar() {
        boolean resultado = fichaMedicaService.actualizar(
            idMascota,
            txtAlergias.getText(),
            txtEnfermedadesCronicas.getText(),
            txtObservaciones.getText()
        );
        
        if (resultado) {
            JOptionPane.showMessageDialog(this, "Ficha médica actualizada", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Error al actualizar", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}