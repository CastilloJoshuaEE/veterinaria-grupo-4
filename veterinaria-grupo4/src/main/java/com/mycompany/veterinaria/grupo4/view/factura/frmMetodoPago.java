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
    private JLabel lblTotalMonto;
    
    public frmMetodoPago(Window parent, double totalAPagar) {
        super(parent, "Seleccionar Método de Pago", ModalityType.APPLICATION_MODAL);
        this.totalAPagar = totalAPagar;
        setSize(500, 350);
        setLocationRelativeTo(parent);
        initComponents();
    }
    
    private void initComponents() {
        setLayout(new BorderLayout());
        
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        mainPanel.setBackground(new Color(248, 249, 250));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Logo y título
        JLabel lblLogo = new JLabel("🐾", SwingConstants.CENTER);
        lblLogo.setFont(new Font("Segoe UI", Font.PLAIN, 40));
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        mainPanel.add(lblLogo, gbc);
        
        JLabel lblEmpresa = new JLabel("Vida Animal S.A.");
        lblEmpresa.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblEmpresa.setForeground(new Color(230, 140, 30));
        gbc.gridy = 1;
        mainPanel.add(lblEmpresa, gbc);
        
        JLabel lblDireccion = new JLabel("Av. América, Quito - Ecuador");
        lblDireccion.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblDireccion.setForeground(Color.GRAY);
        gbc.gridy = 2;
        mainPanel.add(lblDireccion, gbc);
        
        // Separador
        JSeparator separator = new JSeparator();
        gbc.gridy = 3;
        gbc.insets = new Insets(10, 8, 10, 8);
        mainPanel.add(separator, gbc);
        
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(8, 8, 8, 8);
        
        int row = 4;
        gbc.gridx = 0; gbc.gridy = row;
        JLabel lblTotalLabel = new JLabel("Total a Pagar:");
        lblTotalLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        mainPanel.add(lblTotalLabel, gbc);
        
        gbc.gridx = 1;
        lblTotalMonto = new JLabel(String.format("$ %.2f", totalAPagar));
        lblTotalMonto.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTotalMonto.setForeground(new Color(40, 167, 69));
        mainPanel.add(lblTotalMonto, gbc);
        
        row++;
        gbc.gridx = 0; gbc.gridy = row;
        JLabel lblMetodoLabel = new JLabel("Método de Pago:");
        lblMetodoLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        mainPanel.add(lblMetodoLabel, gbc);
        
        gbc.gridx = 1;
        cmbMetodoPago = new JComboBox<>(new String[]{"EFECTIVO", "TRANSFERENCIA BANCARIA"});
        cmbMetodoPago.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        cmbMetodoPago.setBackground(Color.WHITE);
        mainPanel.add(cmbMetodoPago, gbc);
        
        row++;
        gbc.gridx = 0; gbc.gridy = row;
        JLabel lblCuentaOrigenLabel = new JLabel("Cuenta Origen:");
        lblCuentaOrigenLabel.setEnabled(false);
        lblCuentaOrigenLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        mainPanel.add(lblCuentaOrigenLabel, gbc);
        
        gbc.gridx = 1;
        txtCuentaOrigen = new JTextField(15);
        txtCuentaOrigen.setEnabled(false);
        txtCuentaOrigen.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        txtCuentaOrigen.setPreferredSize(new Dimension(200, 30));
        mainPanel.add(txtCuentaOrigen, gbc);
        
        row++;
        gbc.gridx = 0; gbc.gridy = row;
        JLabel lblCuentaDestinoLabel = new JLabel("Cuenta Destino:");
        lblCuentaDestinoLabel.setEnabled(false);
        lblCuentaDestinoLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        mainPanel.add(lblCuentaDestinoLabel, gbc);
        
        gbc.gridx = 1;
        txtCuentaDestino = new JTextField("0123456789");
        txtCuentaDestino.setEnabled(false);
        txtCuentaDestino.setEditable(false);
        txtCuentaDestino.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        txtCuentaDestino.setPreferredSize(new Dimension(200, 30));
        txtCuentaDestino.setBackground(new Color(240, 240, 240));
        mainPanel.add(txtCuentaDestino, gbc);
        
        row++;
        gbc.gridx = 0; gbc.gridy = row;
        gbc.gridwidth = 2;
        JPanel infoPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        infoPanel.setOpaque(false);
        JLabel lblInfo = new JLabel("💡 Solo aceptamos transferencias del Banco Pichincha");
        lblInfo.setFont(new Font("Segoe UI", Font.ITALIC, 10));
        lblInfo.setForeground(new Color(108, 117, 125));
        infoPanel.add(lblInfo);
        mainPanel.add(infoPanel, gbc);
        
        add(mainPanel, BorderLayout.CENTER);
        
        // Panel de botones
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        buttonPanel.setBackground(new Color(248, 249, 250));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 15, 10));
        
        JButton btnAceptar = new JButton("Confirmar Pago");
        btnAceptar.setBackground(new Color(40, 167, 69));
        btnAceptar.setForeground(Color.WHITE);
        btnAceptar.setFocusPainted(false);
        btnAceptar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnAceptar.setPreferredSize(new Dimension(130, 35));
        
        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.setBackground(new Color(108, 117, 125));
        btnCancelar.setForeground(Color.WHITE);
        btnCancelar.setFocusPainted(false);
        btnCancelar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnCancelar.setPreferredSize(new Dimension(100, 35));
        
        buttonPanel.add(btnAceptar);
        buttonPanel.add(btnCancelar);
        add(buttonPanel, BorderLayout.SOUTH);
        
        // Eventos
        cmbMetodoPago.addActionListener(e -> {
            boolean esTransferencia = "TRANSFERENCIA BANCARIA".equals(cmbMetodoPago.getSelectedItem());
            txtCuentaOrigen.setEnabled(esTransferencia);
            lblCuentaOrigenLabel.setEnabled(esTransferencia);
            lblCuentaDestinoLabel.setEnabled(esTransferencia);
            if (!esTransferencia) {
                txtCuentaOrigen.setText("");
            }
        });
        
        btnAceptar.addActionListener(e -> aceptar());
        btnCancelar.addActionListener(e -> cancelar());
        
        getRootPane().setDefaultButton(btnAceptar);
    }
    
    private void aceptar() {
        metodoPago = (String) cmbMetodoPago.getSelectedItem();
        
        if ("TRANSFERENCIA BANCARIA".equals(metodoPago) && txtCuentaOrigen.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Debe ingresar el número de cuenta de origen", 
                "Campo requerido", 
                JOptionPane.WARNING_MESSAGE);
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