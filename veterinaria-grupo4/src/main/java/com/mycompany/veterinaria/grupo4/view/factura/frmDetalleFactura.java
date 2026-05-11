package com.mycompany.veterinaria.grupo4.view;

import com.mycompany.veterinaria.grupo4.model.entity.Factura;
import com.mycompany.veterinaria.grupo4.service.FacturaService;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;

public class frmDetalleFactura extends JDialog {
    private FacturaService facturaService = new FacturaService();
    private int idFactura;
    
    private JTextField txtNumeroFactura, txtFecha, txtSubtotal, txtIVA, txtTotal, txtEstado, txtMetodoPago;
    private JTextField txtCliente, txtCedula, txtTelefono, txtDireccion, txtEmail;
    private JTable tblServicios, tblMascotas, tblMedicamentos, tblInstrumentos, tblVacunas;
    private DefaultTableModel modelServicios, modelMascotas, modelMedicamentos, modelInstrumentos, modelVacunas;
    private JButton btnCerrar;
    
    public frmDetalleFactura(int idFactura) {
        this.idFactura = idFactura;
        setTitle("Detalle de Factura #" + idFactura);
        setModal(true);
        setSize(1100, 750);
        setLocationRelativeTo(null);
        initComponents();
        cargarDetalleFactura();
    }
    
    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Panel de datos del cliente
        JPanel clientePanel = new JPanel(new GridBagLayout());
        clientePanel.setBorder(BorderFactory.createTitledBorder("Datos del Cliente"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        gbc.gridx = 0; gbc.gridy = 0;
        clientePanel.add(new JLabel("Cliente:"), gbc);
        gbc.gridx = 1;
        txtCliente = new JTextField(20);
        txtCliente.setEditable(false);
        clientePanel.add(txtCliente, gbc);
        
        gbc.gridx = 2;
        clientePanel.add(new JLabel("Cédula:"), gbc);
        gbc.gridx = 3;
        txtCedula = new JTextField(15);
        txtCedula.setEditable(false);
        clientePanel.add(txtCedula, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1;
        clientePanel.add(new JLabel("Teléfono:"), gbc);
        gbc.gridx = 1;
        txtTelefono = new JTextField(15);
        txtTelefono.setEditable(false);
        clientePanel.add(txtTelefono, gbc);
        
        gbc.gridx = 2;
        clientePanel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 3;
        txtEmail = new JTextField(20);
        txtEmail.setEditable(false);
        clientePanel.add(txtEmail, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2;
        gbc.gridwidth = 4;
        clientePanel.add(new JLabel("Dirección:"), gbc);
        gbc.gridy = 3;
        txtDireccion = new JTextField(40);
        txtDireccion.setEditable(false);
        clientePanel.add(txtDireccion, gbc);
        
        mainPanel.add(clientePanel, BorderLayout.NORTH);
        
        // Panel de datos de factura
        JPanel facturaPanel = new JPanel(new GridBagLayout());
        facturaPanel.setBorder(BorderFactory.createTitledBorder("Datos de Factura"));
        GridBagConstraints gbcF = new GridBagConstraints();
        gbcF.insets = new Insets(5, 5, 5, 5);
        gbcF.fill = GridBagConstraints.HORIZONTAL;
        
        gbcF.gridx = 0; gbcF.gridy = 0;
        facturaPanel.add(new JLabel("N° Factura:"), gbcF);
        gbcF.gridx = 1;
        txtNumeroFactura = new JTextField(10);
        txtNumeroFactura.setEditable(false);
        facturaPanel.add(txtNumeroFactura, gbcF);
        
        gbcF.gridx = 2;
        facturaPanel.add(new JLabel("Fecha:"), gbcF);
        gbcF.gridx = 3;
        txtFecha = new JTextField(20);
        txtFecha.setEditable(false);
        facturaPanel.add(txtFecha, gbcF);
        
        gbcF.gridx = 0; gbcF.gridy = 1;
        facturaPanel.add(new JLabel("Subtotal:"), gbcF);
        gbcF.gridx = 1;
        txtSubtotal = new JTextField(10);
        txtSubtotal.setEditable(false);
        facturaPanel.add(txtSubtotal, gbcF);
        
        gbcF.gridx = 2;
        facturaPanel.add(new JLabel("IVA:"), gbcF);
        gbcF.gridx = 3;
        txtIVA = new JTextField(10);
        txtIVA.setEditable(false);
        facturaPanel.add(txtIVA, gbcF);
        
        gbcF.gridx = 0; gbcF.gridy = 2;
        facturaPanel.add(new JLabel("Total:"), gbcF);
        gbcF.gridx = 1;
        txtTotal = new JTextField(10);
        txtTotal.setEditable(false);
        facturaPanel.add(txtTotal, gbcF);
        
        gbcF.gridx = 2;
        facturaPanel.add(new JLabel("Estado:"), gbcF);
        gbcF.gridx = 3;
        txtEstado = new JTextField(15);
        txtEstado.setEditable(false);
        facturaPanel.add(txtEstado, gbcF);
        
        gbcF.gridx = 0; gbcF.gridy = 3;
        facturaPanel.add(new JLabel("Método Pago:"), gbcF);
        gbcF.gridx = 1;
        gbcF.gridwidth = 3;
        txtMetodoPago = new JTextField(30);
        txtMetodoPago.setEditable(false);
        facturaPanel.add(txtMetodoPago, gbcF);
        
        mainPanel.add(facturaPanel, BorderLayout.CENTER);
        
        // TabbedPane para detalles
        JTabbedPane tabbedPane = new JTabbedPane();
        
        // Servicios
        modelServicios = new DefaultTableModel(new String[]{"Servicio", "Descripción", "Precio Unitario", "Total", "Veterinario"}, 0);
        tblServicios = new JTable(modelServicios);
        tabbedPane.addTab("Servicios", new JScrollPane(tblServicios));
        
        // Mascotas
        modelMascotas = new DefaultTableModel(new String[]{"Mascota", "Especie", "Raza", "Diagnóstico", "Tratamiento"}, 0);
        tblMascotas = new JTable(modelMascotas);
        tabbedPane.addTab("Mascotas", new JScrollPane(tblMascotas));
        
        // Medicamentos
        modelMedicamentos = new DefaultTableModel(new String[]{"Medicamento", "Dosis", "Frecuencia", "Duración", "Precio"}, 0);
        tblMedicamentos = new JTable(modelMedicamentos);
        tabbedPane.addTab("Medicamentos", new JScrollPane(tblMedicamentos));
        
        // Instrumentos
        modelInstrumentos = new DefaultTableModel(new String[]{"Instrumento", "Costo Uso"}, 0);
        tblInstrumentos = new JTable(modelInstrumentos);
        tabbedPane.addTab("Instrumentos", new JScrollPane(tblInstrumentos));
        
        // Vacunas
        modelVacunas = new DefaultTableModel(new String[]{"Vacuna", "Fecha Aplicación", "Próxima Aplicación", "Descripción"}, 0);
        tblVacunas = new JTable(modelVacunas);
        tabbedPane.addTab("Vacunas", new JScrollPane(tblVacunas));
        
        mainPanel.add(tabbedPane, BorderLayout.SOUTH);
        
        add(mainPanel, BorderLayout.CENTER);
        
        // Botón cerrar
        JPanel buttonPanel = new JPanel();
        btnCerrar = new JButton("Cerrar");
        btnCerrar.addActionListener(e -> dispose());
        buttonPanel.add(btnCerrar);
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    private void cargarDetalleFactura() {
        Factura factura = facturaService.obtenerDetalle(idFactura);
        if (factura != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            txtNumeroFactura.setText(String.valueOf(factura.getIdFactura()));
            txtFecha.setText(sdf.format(factura.getFecha()));
            txtSubtotal.setText(String.format("$%.2f", factura.getSubtotal()));
            txtIVA.setText(String.format("$%.2f", factura.getIva()));
            txtTotal.setText(String.format("$%.2f", factura.getTotal()));
            txtEstado.setText(factura.getEstado());
            txtMetodoPago.setText(factura.getMetodoPago() != null ? factura.getMetodoPago() : "EFECTIVO");
            
            // Datos del cliente
            if (factura.getCliente() != null) {
                txtCliente.setText(factura.getCliente().getNombre() + " " + factura.getCliente().getApellido());
                txtCedula.setText(factura.getCliente().getCedula());
                txtTelefono.setText(factura.getCliente().getTelefono());
                txtEmail.setText(factura.getCliente().getCorreoElectronico());
                txtDireccion.setText(factura.getCliente().getDireccion());
            }
            
            // Cargar detalles
            cargarServicios(factura);
            cargarMascotas(factura);
            cargarMedicamentos(factura);
            cargarInstrumentos(factura);
            cargarVacunas(factura);
        }
    }
    
    private void cargarServicios(Factura factura) {
        if (factura.getServicios() != null) {
            for (Object[] servicio : factura.getServicios()) {
                modelServicios.addRow(servicio);
            }
        }
    }
    
    private void cargarMascotas(Factura factura) {
        if (factura.getMascotas() != null) {
            for (Object[] mascota : factura.getMascotas()) {
                modelMascotas.addRow(mascota);
            }
        }
    }
    
    private void cargarMedicamentos(Factura factura) {
        if (factura.getMedicamentos() != null) {
            for (Object[] medicamento : factura.getMedicamentos()) {
                modelMedicamentos.addRow(medicamento);
            }
        }
    }
    
    private void cargarInstrumentos(Factura factura) {
        if (factura.getInstrumentos() != null) {
            for (Object[] instrumento : factura.getInstrumentos()) {
                modelInstrumentos.addRow(instrumento);
            }
        }
    }
    
    private void cargarVacunas(Factura factura) {
        if (factura.getVacunas() != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            for (Object[] vacuna : factura.getVacunas()) {
                modelVacunas.addRow(vacuna);
            }
        }
    }
}