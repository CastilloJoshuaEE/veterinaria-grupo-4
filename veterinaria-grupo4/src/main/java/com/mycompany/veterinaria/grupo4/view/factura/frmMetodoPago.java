package com.mycompany.veterinaria.grupo4.view.factura;

import javax.swing.*;
import java.awt.*;

public class frmMetodoPago extends JDialog {
    private double totalAPagar;
    private String metodoPago;
    private String cuentaOrigen;
    private String cuentaDestino;
    private boolean confirmed = false;
    
    private JComboBox<String> cmbMetodoPago;
    private JTextField txtCuentaOrigen, txtCuentaDestino;
    
    public frmMetodoPago(Window parent, double totalAPagar) {
        super(parent, "Seleccionar Método de Pago", ModalityType.APPLICATION_MODAL);
        this.totalAPagar = totalAPagar;
        setSize(450, 300);
        setLocationRelativeTo(parent);
        initComponents();
    }
    
    private void initComponents() {
        setLayout(new BorderLayout());
        
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        JLabel lblEmpresa = new JLabel("Vida Animal S.A.");
        lblEmpresa.setFont(new Font("Arial", Font.BOLD, 16));
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        mainPanel.add(lblEmpresa, gbc);
        
        JLabel lblDireccion = new JLabel("Av. America");
        gbc.gridy = 1;
        mainPanel.add(lblDireccion, gbc);
        
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.WEST;
        
        int row = 2;
        gbc.gridx = 0; gbc.gridy = row;
        mainPanel.add(new JLabel("Total a Pagar:"), gbc);
        gbc.gridx = 1;
        JLabel lblTotal = new JLabel(String.format("$%.2f", totalAPagar));
        lblTotal.setFont(new Font("Arial", Font.BOLD, 14));
        mainPanel.add(lblTotal, gbc);
        
        row++;
        gbc.gridx = 0; gbc.gridy = row;
        mainPanel.add(new JLabel("Método de Pago:"), gbc);
        gbc.gridx = 1;
        cmbMetodoPago = new JComboBox<>(new String[]{"EFECTIVO", "TRANSFERENCIA BANCARIA"});
        mainPanel.add(cmbMetodoPago, gbc);
        
        row++;
        gbc.gridx = 0; gbc.gridy = row;
        JLabel lblCuentaOrigen = new JLabel("Cuenta Origen:");
        lblCuentaOrigen.setEnabled(false);
        mainPanel.add(lblCuentaOrigen, gbc);
        gbc.gridx = 1;
        txtCuentaOrigen = new JTextField(15);
        txtCuentaOrigen.setEnabled(false);
        mainPanel.add(txtCuentaOrigen, gbc);
        
        row++;
        gbc.gridx = 0; gbc.gridy = row;
        JLabel lblCuentaDestino = new JLabel("Cuenta Destino:");
        lblCuentaDestino.setEnabled(false);
        mainPanel.add(lblCuentaDestino, gbc);
        gbc.gridx = 1;
        txtCuentaDestino = new JTextField("0123456789");
        txtCuentaDestino.setEnabled(false);
        txtCuentaDestino.setEditable(false);
        mainPanel.add(txtCuentaDestino, gbc);
        
        row++;
        gbc.gridx = 0; gbc.gridy = row;
        gbc.gridwidth = 2;
        JLabel lblBanco = new JLabel("Solo aceptamos transferencias hechas en Banco Pichincha");
        lblBanco.setFont(new Font("Arial", Font.ITALIC, 10));
        lblBanco.setForeground(Color.GRAY);
        mainPanel.add(lblBanco, gbc);
        
        add(mainPanel, BorderLayout.CENTER);
        
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton btnAceptar = new JButton("Aceptar");
        JButton btnCancelar = new JButton("Cancelar");
        buttonPanel.add(btnAceptar);
        buttonPanel.add(btnCancelar);
        add(buttonPanel, BorderLayout.SOUTH);
        
        cmbMetodoPago.addActionListener(e -> {
            boolean esTransferencia = "TRANSFERENCIA BANCARIA".equals(cmbMetodoPago.getSelectedItem());
            txtCuentaOrigen.setEnabled(esTransferencia);
            lblCuentaOrigen.setEnabled(esTransferencia);
            lblCuentaDestino.setEnabled(esTransferencia);
            txtCuentaDestino.setEnabled(esTransferencia);
        });
        
        btnAceptar.addActionListener(e -> aceptar());
        btnCancelar.addActionListener(e -> cancelar());
    }
    
    private void aceptar() {
        metodoPago = (String) cmbMetodoPago.getSelectedItem();
        
        if ("TRANSFERENCIA BANCARIA".equals(metodoPago) && txtCuentaOrigen.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese el número de cuenta de origen", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        cuentaOrigen = txtCuentaOrigen.getText().trim();
        cuentaDestino = txtCuentaDestino.getText().trim();
        confirmed = true;
        dispose();
    }
    
    private void cancelar() {
        confirmed = false;
        dispose();
    }
    
    public boolean isConfirmed() { return confirmed; }
    public String getMetodoPago() { return metodoPago; }
    public String getCuentaOrigen() { return cuentaOrigen; }
    public String getCuentaDestino() { return cuentaDestino; }
}