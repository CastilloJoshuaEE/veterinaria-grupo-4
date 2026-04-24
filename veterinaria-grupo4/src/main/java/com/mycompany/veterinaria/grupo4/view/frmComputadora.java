package com.mycompany.veterinaria.grupo4.view;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class frmComputadora extends JFrame {
    
    private JLabel lblMensajeInicio;
    private Timer timer;
    private int segundos = 3;
    
    public frmComputadora() {
        initComponents();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setUndecorated(true);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        
        // Mostrar mensaje de inicio
        mostrarMensajeInicio();
        
        // Timer para mostrar el botón de la aplicación después de 3 segundos
        timer = new Timer(1000, e -> {
            segundos--;
            if (lblMensajeInicio != null) {
                lblMensajeInicio.setText("Iniciando aplicación... " + segundos + "s");
            }
            if (segundos <= 0) {
                timer.stop();
                if (lblMensajeInicio != null) {
                    lblMensajeInicio.setVisible(false);
                }
                mostrarPantallaPrincipal();
            }
        });
        timer.start();
    }
    
    private void mostrarMensajeInicio() {
        JPanel glassPane = (JPanel) getGlassPane();
        glassPane.setLayout(new GridBagLayout());
        glassPane.setVisible(true);
        
        lblMensajeInicio = new JLabel("Iniciando aplicación... " + segundos + "s");
        lblMensajeInicio.setFont(new Font("Arial", Font.BOLD, 24));
        lblMensajeInicio.setForeground(Color.WHITE);
        lblMensajeInicio.setBackground(new Color(0, 0, 0, 180));
        lblMensajeInicio.setOpaque(true);
        
        glassPane.add(lblMensajeInicio);
    }
    
    private void initComponents() {
        setLayout(new BorderLayout());
        
        // Panel central para el logo
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setOpaque(false);
        
        JButton btnAppIcon = new JButton();
        URL iconUrl = getClass().getResource("/images/app_icon.png");
        if (iconUrl != null) {
            ImageIcon icon = new ImageIcon(iconUrl);
            Image scaledImage = icon.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH);
            btnAppIcon.setIcon(new ImageIcon(scaledImage));
        }
        btnAppIcon.setBorderPainted(false);
        btnAppIcon.setContentAreaFilled(false);
        btnAppIcon.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnAppIcon.addActionListener(e -> abrirLogin());
        
        JLabel lblNombreApp = new JLabel("App Vida Animal S.A.");
        lblNombreApp.setFont(new Font("Arial", Font.BOLD, 14));
        lblNombreApp.setForeground(Color.WHITE);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        centerPanel.add(btnAppIcon, gbc);
        gbc.gridy = 1;
        centerPanel.add(lblNombreApp, gbc);
        
        add(centerPanel, BorderLayout.CENTER);
        
        // Botón apagar
        JButton btnApagar = new JButton("Apagar computadora");
        btnApagar.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this, "¿Estás seguro de que quieres apagar la computadora?", 
                "Apagar", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (confirm == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        });
        
        JPanel southPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        southPanel.setOpaque(false);
        southPanel.add(btnApagar);
        add(southPanel, BorderLayout.SOUTH);
    }
    private void mostrarPantallaPrincipal() {
        getGlassPane().setVisible(false);
        
        // Panel principal con gradiente
        JPanel mainPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                GradientPaint gradient = new GradientPaint(0, 0, new Color(0, 102, 204), 
                                                            getWidth(), getHeight(), new Color(0, 51, 102));
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        
        // Panel central para el logo
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setOpaque(false);
        
        JButton btnAppIcon = new JButton();
        btnAppIcon.setPreferredSize(new Dimension(120, 120));
        btnAppIcon.setBorderPainted(false);
        btnAppIcon.setContentAreaFilled(false);
        btnAppIcon.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Intentar cargar imagen del logo
        ImageIcon icon = null;
        try {
            java.net.URL iconUrl = getClass().getResource("/images/app_icon.png");
            if (iconUrl != null) {
                icon = new ImageIcon(iconUrl);
                Image scaledImage = icon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
                btnAppIcon.setIcon(new ImageIcon(scaledImage));
            } else {
                // Texto alternativo si no hay imagen
                btnAppIcon.setText("🐾");
                btnAppIcon.setFont(new Font("Segoe UI", Font.PLAIN, 60));
                btnAppIcon.setForeground(Color.WHITE);
            }
        } catch (Exception e) {
            // Texto alternativo en caso de error
            btnAppIcon.setText("🐾");
            btnAppIcon.setFont(new Font("Segoe UI", Font.PLAIN, 60));
            btnAppIcon.setForeground(Color.WHITE);
        }
        
        btnAppIcon.addActionListener(e -> abrirLogin());
        
        JLabel lblNombreApp = new JLabel("App Vida Animal S.A.");
        lblNombreApp.setFont(new Font("Arial", Font.BOLD, 18));
        lblNombreApp.setForeground(Color.WHITE);
        
        JLabel lblBienvenida = new JLabel("Bienvenido al Sistema Veterinario");
        lblBienvenida.setFont(new Font("Arial", Font.PLAIN, 14));
        lblBienvenida.setForeground(new Color(255, 255, 255, 200));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 10, 10, 10);
        centerPanel.add(btnAppIcon, gbc);
        gbc.gridy = 1;
        centerPanel.add(lblNombreApp, gbc);
        gbc.gridy = 2;
        centerPanel.add(lblBienvenida, gbc);
        
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        
        // Botón apagar
        JButton btnApagar = new JButton("Apagar computadora");
        btnApagar.setBackground(new Color(200, 70, 70));
        btnApagar.setForeground(Color.WHITE);
        btnApagar.setFocusPainted(false);
        btnApagar.setFont(new Font("Arial", Font.BOLD, 12));
        btnApagar.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this, 
                "¿Estás seguro de que quieres apagar la computadora?", 
                "Apagar", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (confirm == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        });
        
        JPanel southPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        southPanel.setOpaque(false);
        southPanel.add(btnApagar);
        mainPanel.add(southPanel, BorderLayout.SOUTH);
        
        setContentPane(mainPanel);
        revalidate();
        repaint();
    }
    
    private void abrirLogin() {
        frmLogin login = new frmLogin();
        login.setVisible(true);
        this.dispose();
    }
}