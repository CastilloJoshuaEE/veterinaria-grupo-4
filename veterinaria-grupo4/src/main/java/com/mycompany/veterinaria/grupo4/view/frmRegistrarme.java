/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.veterinaria.grupo4.view;

import com.mycompany.veterinaria.grupo4.model.entity.Usuario;
import org.springframework.web.client.RestTemplate;

import javax.swing.*;
import java.awt.*;

public class frmRegistrarme extends JFrame {
    private RestTemplate restTemplate = new RestTemplate();
    private String apiBaseUrl = "http://localhost:8080/api/auth";

    private JTextField txtUsuario, txtCorreo;
    private JPasswordField txtPassword, txtConfirmPassword;
    private JButton btnRegistrar, btnCancelar;

    public frmRegistrarme() {
        setTitle("Registro de Usuario");
        setSize(400, 350);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        int row = 0;
        gbc.gridx = 0; gbc.gridy = row;
        formPanel.add(new JLabel("Usuario:"), gbc);
        gbc.gridx = 1;
        txtUsuario = new JTextField(15);
        formPanel.add(txtUsuario, gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row;
        formPanel.add(new JLabel("Correo Electrónico:"), gbc);
        gbc.gridx = 1;
        txtCorreo = new JTextField(15);
        formPanel.add(txtCorreo, gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row;
        formPanel.add(new JLabel("Contraseña:"), gbc);
        gbc.gridx = 1;
        txtPassword = new JPasswordField(15);
        formPanel.add(txtPassword, gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row;
        formPanel.add(new JLabel("Confirmar Contraseña:"), gbc);
        gbc.gridx = 1;
        txtConfirmPassword = new JPasswordField(15);
        formPanel.add(txtConfirmPassword, gbc);

        add(formPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        btnRegistrar = new JButton("Registrarse");
        btnCancelar = new JButton("Cancelar");
        buttonPanel.add(btnRegistrar);
        buttonPanel.add(btnCancelar);
        add(buttonPanel, BorderLayout.SOUTH);

        btnRegistrar.addActionListener(e -> registrar());
        btnCancelar.addActionListener(e -> dispose());
    }

    private void registrar() {
        String usuario = txtUsuario.getText().trim();
        String correo = txtCorreo.getText().trim();
        String password = new String(txtPassword.getPassword());
        String confirmPassword = new String(txtConfirmPassword.getPassword());

        if (usuario.isEmpty() || correo.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Complete todos los campos", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!password.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(this, "Las contraseñas no coinciden", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (password.length() < 6) {
            JOptionPane.showMessageDialog(this, "La contraseña debe tener al menos 6 caracteres", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            // Verificar si el usuario ya existe
            Boolean existe = restTemplate.getForObject(apiBaseUrl + "/existe/" + usuario, Boolean.class);
            if (existe != null && existe) {
                JOptionPane.showMessageDialog(this, "El nombre de usuario ya existe", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Usuario nuevoUsuario = new Usuario();
            nuevoUsuario.setNombreUsuario(usuario);
            nuevoUsuario.setContrasena(password);
            nuevoUsuario.setCorreoElectronico(correo);
            nuevoUsuario.setRol("ADMINISTRADOR");

            Integer id = restTemplate.postForObject(apiBaseUrl + "/registrar", nuevoUsuario, Integer.class);
            if (id != null && id > 0) {
                JOptionPane.showMessageDialog(this, "Usuario registrado exitosamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Error al registrar usuario", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error de conexión: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}