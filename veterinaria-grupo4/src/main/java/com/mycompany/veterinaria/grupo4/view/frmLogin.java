package com.mycompany.veterinaria.grupo4.view;

import com.mycompany.veterinaria.grupo4.api.dto.LoginRequest;
import com.mycompany.veterinaria.grupo4.model.entity.Usuario;
import org.springframework.web.client.RestTemplate;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class frmLogin extends JFrame {
    private JTextField userField;
    private JPasswordField passField;
    private JCheckBox showPassCheck;
    private JButton loginButton;
    private JLabel registerLabel;
    private JButton closeButton;
    
    private RestTemplate restTemplate = new RestTemplate();
    private String apiBaseUrl = "http://localhost:8080/api/auth";
    
    public frmLogin() {
        initComponents();
        setLocationRelativeTo(null);
    }
    
    private void initComponents() {
        setSize(350, 350);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setUndecorated(true);
        getContentPane().setBackground(Color.WHITE);
        setLayout(null);
        
        // Botón cerrar
        closeButton = new JButton("X");
        closeButton.setBounds(320, 5, 25, 25);
        closeButton.setFocusPainted(false);
        closeButton.setBorderPainted(false);
        closeButton.setContentAreaFilled(false);
        closeButton.setFont(new Font("Arial", Font.BOLD, 14));
        closeButton.addActionListener(e -> System.exit(0));
        add(closeButton);
        
        // Logo
        JLabel logoLabel = new JLabel();
        URL imgUrl = getClass().getResource("/images/user.png");
        if (imgUrl != null) {
            ImageIcon icon = new ImageIcon(imgUrl);
            Image scaledImage = icon.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH);
            logoLabel.setIcon(new ImageIcon(scaledImage));
        }
        logoLabel.setBounds(135, 30, 80, 80);
        add(logoLabel);
        
        // Campo usuario
        userField = new JTextField("Ingrese su usuario");
        userField.setBounds(50, 130, 250, 30);
        userField.setForeground(Color.GRAY);
        userField.setHorizontalAlignment(JTextField.CENTER);
        userField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                if (userField.getText().equals("Ingrese su usuario")) {
                    userField.setText("");
                    userField.setForeground(Color.BLACK);
                }
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                if (userField.getText().isEmpty()) {
                    userField.setText("Ingrese su usuario");
                    userField.setForeground(Color.GRAY);
                }
            }
        });
        add(userField);
        
        // Campo contraseña
        passField = new JPasswordField("Ingrese su contraseña");
        passField.setBounds(50, 175, 200, 30);
        passField.setForeground(Color.GRAY);
        passField.setHorizontalAlignment(JTextField.CENTER);
        passField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                if (new String(passField.getPassword()).equals("Ingrese su contraseña")) {
                    passField.setText("");
                    passField.setForeground(Color.BLACK);
                    passField.setEchoChar('*');
                }
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                if (passField.getPassword().length == 0) {
                    passField.setText("Ingrese su contraseña");
                    passField.setForeground(Color.GRAY);
                    passField.setEchoChar((char)0);
                }
            }
        });
        add(passField);
        
        // Checkbox mostrar contraseña
        showPassCheck = new JCheckBox("Mostrar");
        showPassCheck.setBounds(255, 175, 80, 30);
        showPassCheck.setBackground(Color.WHITE);
        showPassCheck.addActionListener(e -> {
            if (showPassCheck.isSelected()) {
                passField.setEchoChar((char)0);
            } else {
                passField.setEchoChar('*');
            }
        });
        add(showPassCheck);
        
        // Botón ingresar
        loginButton = new JButton("Ingresar");
        loginButton.setBounds(50, 220, 100, 30);
        loginButton.setBackground(new Color(0, 150, 200));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFocusPainted(false);
        loginButton.addActionListener(e -> login());
        add(loginButton);
        
        // Label registrarse
        registerLabel = new JLabel("Registrate");
        registerLabel.setBounds(180, 225, 80, 20);
        registerLabel.setForeground(Color.BLUE);
        registerLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        registerLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                frmRegistrarme registro = new frmRegistrarme();
                registro.setVisible(true);
            }
        });
        add(registerLabel);
    }
    
    private void login() {
        String usuario = userField.getText().trim();
        String password = new String(passField.getPassword()).trim();
        
        if (usuario.equals("Ingrese su usuario") || usuario.isEmpty() ||
            password.equals("Ingrese su contraseña") || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Complete todos los campos", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        try {
            LoginRequest request = new LoginRequest();
            request.setUsuario(usuario);
            request.setPassword(password);
            
            Usuario user = restTemplate.postForObject(apiBaseUrl + "/login", request, Usuario.class);
            
            if (user != null && user.getIdUsuario() > 0) {
                JOptionPane.showMessageDialog(this, "Login exitoso", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                frmSistema sistema = new frmSistema(user.getNombreUsuario(), user.getIdUsuario());
                sistema.setVisible(true);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Usuario o contraseña incorrectos", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            // Si el servidor no está corriendo, mostrar mensaje
            JOptionPane.showMessageDialog(this, "Error de conexión. Asegúrese que el servidor esté corriendo.\n" + ex.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}