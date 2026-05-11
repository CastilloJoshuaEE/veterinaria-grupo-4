package com.mycompany.veterinaria.grupo4.view.historial;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;

public class FormFichaMedica extends JDialog {
    
    private JTextArea txtAlergias;
    private JTextArea txtEnfermedades;
    private JTextArea txtObservaciones;
    private JButton btnGuardar;
    private JButton btnCancelar;
    private boolean guardado = false;
    
    public FormFichaMedica(Frame parent, String titulo, 
                           String alergias, String enfermedades, String observaciones) {
        super(parent, titulo, true);
        initComponents();
        cargarDatos(alergias, enfermedades, observaciones);
        setSize(580, 520);
        setLocationRelativeTo(parent);
        setResizable(false);
    }
    
    private void initComponents() {
        // Panel principal con borde redondeado y sombra
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        mainPanel.setBackground(new Color(248, 249, 250));
        
        // Panel de título con icono
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setOpaque(false);
        titlePanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        
        JLabel lblTitle = new JLabel(getTitle());
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitle.setForeground(new Color(44, 62, 80));
        
        titlePanel.add(lblTitle, BorderLayout.CENTER);
        
        mainPanel.add(titlePanel, BorderLayout.NORTH);
        
        // Panel del formulario
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setOpaque(false);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        formPanel.setBackground(Color.WHITE);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        
        // Alergias - con icono
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.gridwidth = 2;
        JLabel lblAlergiasTitle = new JLabel("️ ALERGIAS");
        lblAlergiasTitle.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblAlergiasTitle.setForeground(new Color(220, 53, 69));
        formPanel.add(lblAlergiasTitle, gbc);
        
        gbc.gridy = 1;
        txtAlergias = createStyledTextArea(4);
        JScrollPane scrollAlergias = createStyledScrollPane(txtAlergias);
        formPanel.add(scrollAlergias, gbc);
        
        // Separador
        gbc.gridy = 2;
        formPanel.add(createSeparator(), gbc);
        
        // Enfermedades Crónicas
        gbc.gridy = 3;
        JLabel lblEnfermedadesTitle = new JLabel(" ENFERMEDADES CRÓNICAS");
        lblEnfermedadesTitle.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblEnfermedadesTitle.setForeground(new Color(255, 193, 7));
        formPanel.add(lblEnfermedadesTitle, gbc);
        
        gbc.gridy = 4;
        txtEnfermedades = createStyledTextArea(4);
        JScrollPane scrollEnfermedades = createStyledScrollPane(txtEnfermedades);
        formPanel.add(scrollEnfermedades, gbc);
        
        // Separador
        gbc.gridy = 5;
        formPanel.add(createSeparator(), gbc);
        
        // Observaciones
        gbc.gridy = 6;
        JLabel lblObservacionesTitle = new JLabel(" OBSERVACIONES");
        lblObservacionesTitle.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblObservacionesTitle.setForeground(new Color(40, 167, 69));
        formPanel.add(lblObservacionesTitle, gbc);
        
        gbc.gridy = 7;
        txtObservaciones = createStyledTextArea(5);
        JScrollPane scrollObservaciones = createStyledScrollPane(txtObservaciones);
        formPanel.add(scrollObservaciones, gbc);
        
        // Panel con información de ayuda
        gbc.gridy = 8;
        JPanel helpPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        helpPanel.setOpaque(false);
        JLabel lblHelp = new JLabel("Complete los campos que correspondan. Deje en blanco si no aplica.");
        lblHelp.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        lblHelp.setForeground(new Color(108, 117, 125));
        helpPanel.add(lblHelp);
        formPanel.add(helpPanel, gbc);
        
        mainPanel.add(formPanel, BorderLayout.CENTER);
        
        // Panel de botones
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        
        btnGuardar = createStyledButton("Guardar", new Color(40, 167, 69));
        
        btnCancelar = createStyledButton("Cancelar", new Color(108, 117, 125));
        
        buttonPanel.add(btnGuardar);
        buttonPanel.add(btnCancelar);
        
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        // Eventos
        btnGuardar.addActionListener(e -> {
            guardado = true;
            dispose();
        });
        
        btnCancelar.addActionListener(e -> dispose());
        
        // Aplicar tema general
        getContentPane().setBackground(new Color(248, 249, 250));
        setContentPane(mainPanel);
    }
    
    private JTextArea createStyledTextArea(int rows) {
        JTextArea textArea = new JTextArea(rows, 25);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        textArea.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        textArea.setBackground(new Color(255, 255, 255));
        return textArea;
    }
    
    private JScrollPane createStyledScrollPane(JTextArea textArea) {
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(0, 0, 0, 0)
        ));
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        return scrollPane;
    }
    
    private JSeparator createSeparator() {
        JSeparator separator = new JSeparator();
        separator.setForeground(new Color(200, 200, 200));
        separator.setPreferredSize(new Dimension(0, 10));
        return separator;
    }
    
    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Efecto hover
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor.darker());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor);
            }
        });
        
        return button;
    }
    
    private ImageIcon createIcon(String emoji) {
        return null;
    }
    
    private void cargarDatos(String alergias, String enfermedades, String observaciones) {
        txtAlergias.setText(alergias != null && !alergias.equals("null") ? alergias : "");
        txtEnfermedades.setText(enfermedades != null && !enfermedades.equals("null") ? enfermedades : "");
        txtObservaciones.setText(observaciones != null && !observaciones.equals("null") ? observaciones : "");
        
        // Agregar placeholders visuales si están vacíos
        if (txtAlergias.getText().isEmpty()) {
            txtAlergias.setForeground(Color.GRAY);
            txtAlergias.setText("Ej: Polen, medicamentos, alimentos...");
            txtAlergias.addFocusListener(new java.awt.event.FocusAdapter() {
                public void focusGained(java.awt.event.FocusEvent evt) {
                    if (txtAlergias.getText().equals("Ej: Polen, medicamentos, alimentos...")) {
                        txtAlergias.setText("");
                        txtAlergias.setForeground(Color.BLACK);
                    }
                }
                public void focusLost(java.awt.event.FocusEvent evt) {
                    if (txtAlergias.getText().isEmpty()) {
                        txtAlergias.setForeground(Color.GRAY);
                        txtAlergias.setText("Ej: Polen, medicamentos, alimentos...");
                    }
                }
            });
        }
        
        if (txtEnfermedades.getText().isEmpty()) {
            txtEnfermedades.setForeground(Color.GRAY);
            txtEnfermedades.setText("Ej: Diabetes, artritis, cardiopatías...");
            txtEnfermedades.addFocusListener(new java.awt.event.FocusAdapter() {
                public void focusGained(java.awt.event.FocusEvent evt) {
                    if (txtEnfermedades.getText().equals("Ej: Diabetes, artritis, cardiopatías...")) {
                        txtEnfermedades.setText("");
                        txtEnfermedades.setForeground(Color.BLACK);
                    }
                }
                public void focusLost(java.awt.event.FocusEvent evt) {
                    if (txtEnfermedades.getText().isEmpty()) {
                        txtEnfermedades.setForeground(Color.GRAY);
                        txtEnfermedades.setText("Ej: Diabetes, artritis, cardiopatías...");
                    }
                }
            });
        }
        
        if (txtObservaciones.getText().isEmpty()) {
            txtObservaciones.setForeground(Color.GRAY);
            txtObservaciones.setText("Información adicional relevante para el tratamiento...");
            txtObservaciones.addFocusListener(new java.awt.event.FocusAdapter() {
                public void focusGained(java.awt.event.FocusEvent evt) {
                    if (txtObservaciones.getText().equals("Información adicional relevante para el tratamiento...")) {
                        txtObservaciones.setText("");
                        txtObservaciones.setForeground(Color.BLACK);
                    }
                }
                public void focusLost(java.awt.event.FocusEvent evt) {
                    if (txtObservaciones.getText().isEmpty()) {
                        txtObservaciones.setForeground(Color.GRAY);
                        txtObservaciones.setText("Información adicional relevante para el tratamiento...");
                    }
                }
            });
        }
    }
    
    public boolean isGuardado() { return guardado; }
    public String getAlergias() { 
        String text = txtAlergias.getText();
        if (text.equals("Ej: Polen, medicamentos, alimentos...")) return "";
        return text;
    }
    public String getEnfermedades() { 
        String text = txtEnfermedades.getText();
        if (text.equals("Ej: Diabetes, artritis, cardiopatías...")) return "";
        return text;
    }
    public String getObservaciones() { 
        String text = txtObservaciones.getText();
        if (text.equals("Información adicional relevante para el tratamiento...")) return "";
        return text;
    }
}