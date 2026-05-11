package com.mycompany.veterinaria.grupo4.view.factura;

import com.mycompany.veterinaria.grupo4.model.entity.Cliente;
import com.mycompany.veterinaria.grupo4.service.ClienteService;
import javax.swing.*;
import java.awt.*;
import java.util.List;

public class frmSeleccionarCedula extends JDialog {
    private JComboBox<String> cmbCedulas;
    private JTextField txtNombre, txtTelefono, txtEmail;
    private JButton btnAceptar, btnCancelar;
    private String cedulaSeleccionada;
    private boolean confirmed = false;
    
    private ClienteService clienteService;
    
    public frmSeleccionarCedula(Window parent) {
        super(parent, "Seleccionar Cliente", ModalityType.APPLICATION_MODAL);
        clienteService = new ClienteService();
        setSize(500, 250);
        setLocationRelativeTo(parent);
        initComponents();
        cargarCedulas();
    }
    
    private void initComponents() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        gbc.gridx = 0; gbc.gridy = 0;
        add(new JLabel("Cédula del cliente:"), gbc);
        gbc.gridx = 1;
        cmbCedulas = new JComboBox<>();
        cmbCedulas.setPreferredSize(new Dimension(250, 25));
        cmbCedulas.addActionListener(e -> cargarDatosCliente());
        add(cmbCedulas, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1;
        add(new JLabel("Nombre completo:"), gbc);
        gbc.gridx = 1;
        txtNombre = new JTextField(20);
        txtNombre.setEditable(false);
        add(txtNombre, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2;
        add(new JLabel("Teléfono:"), gbc);
        gbc.gridx = 1;
        txtTelefono = new JTextField(15);
        txtTelefono.setEditable(false);
        add(txtTelefono, gbc);
        
        gbc.gridx = 0; gbc.gridy = 3;
        add(new JLabel("Email:"), gbc);
        gbc.gridx = 1;
        txtEmail = new JTextField(20);
        txtEmail.setEditable(false);
        add(txtEmail, gbc);
        
        JPanel buttonPanel = new JPanel(new FlowLayout());
        btnAceptar = new JButton("Aceptar");
        btnCancelar = new JButton("Cancelar");
        buttonPanel.add(btnAceptar);
        buttonPanel.add(btnCancelar);
        
        gbc.gridx = 0; gbc.gridy = 4;
        gbc.gridwidth = 2;
        add(buttonPanel, gbc);
        
        btnAceptar.addActionListener(e -> aceptar());
        btnCancelar.addActionListener(e -> cancelar());
    }
    
    private void cargarCedulas() {
        List<Cliente> clientes = clienteService.listarTodos();
        if (clientes != null) {
            for (Cliente c : clientes) {
                cmbCedulas.addItem(c.getCedula());
            }
        }
    }
    
    private void cargarDatosCliente() {
        String cedula = (String) cmbCedulas.getSelectedItem();
        if (cedula != null) {
            Cliente cliente = clienteService.obtenerPorCedula(cedula);
            if (cliente != null) {
                txtNombre.setText(cliente.getNombre() + " " + cliente.getApellido());
                txtTelefono.setText(cliente.getTelefono());
                txtEmail.setText(cliente.getCorreoElectronico());
            }
        }
    }
    
    private void aceptar() {
        cedulaSeleccionada = (String) cmbCedulas.getSelectedItem();
        if (cedulaSeleccionada != null) {
            confirmed = true;
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Debe seleccionar una cédula válida", 
                "Advertencia", JOptionPane.WARNING_MESSAGE);
        }
    }
    
    private void cancelar() {
        confirmed = false;
        dispose();
    }
    
    public boolean isConfirmed() { return confirmed; }
    public String getCedulaSeleccionada() { return cedulaSeleccionada; }
}