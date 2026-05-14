package com.mycompany.veterinaria.grupo4.view.factura;

import com.mycompany.veterinaria.grupo4.model.entity.Factura;
import com.mycompany.veterinaria.grupo4.service.FacturaService;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import javax.swing.border.TitledBorder;

public class frmDetalleFactura extends JDialog {
    private FacturaService facturaService;
    private int idFactura;
    
    private JTextField txtNumeroFactura, txtFecha, txtSubtotal, txtIVA, txtTotal, txtEstado, txtMetodoPago;
    private JTextField txtCliente, txtCedula, txtTelefono, txtDireccion, txtEmail;
    private JTable tblServicios, tblMascotas, tblMedicamentos, tblInstrumentos, tblVacunas;
    private DefaultTableModel modelServicios, modelMascotas, modelMedicamentos, modelInstrumentos, modelVacunas;
    
    public frmDetalleFactura(Window parent, int idFactura) {
        super(parent, "Detalle de Factura #" + idFactura, ModalityType.APPLICATION_MODAL);
        this.idFactura = idFactura;
        this.facturaService = new FacturaService();
        setSize(1200, 750);
        setLocationRelativeTo(parent);
        initComponents();
        cargarDetalleFactura();
    }
    
    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Panel superior con cliente y factura en dos columnas
        JPanel topPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        
        // Panel de cliente
        JPanel clientePanel = createInfoPanel("Datos del Cliente");
        clientePanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        txtCliente = createReadOnlyTextField();
        txtCedula = createReadOnlyTextField();
        txtTelefono = createReadOnlyTextField();
        txtEmail = createReadOnlyTextField();
        txtDireccion = createReadOnlyTextField();
        
        addField(clientePanel, gbc, "Cliente:", txtCliente, 0, 0, 1, 1);
        addField(clientePanel, gbc, "Cédula:", txtCedula, 2, 0, 1, 1);
        addField(clientePanel, gbc, "Teléfono:", txtTelefono, 0, 1, 1, 1);
        addField(clientePanel, gbc, "Email:", txtEmail, 2, 1, 1, 1);
        addField(clientePanel, gbc, "Dirección:", txtDireccion, 0, 2, 4, 1);
        
        // Panel de factura
        JPanel facturaPanel = createInfoPanel("Datos de Factura");
        facturaPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbcF = new GridBagConstraints();
        gbcF.insets = new Insets(5, 5, 5, 5);
        gbcF.fill = GridBagConstraints.HORIZONTAL;
        
        txtNumeroFactura = createReadOnlyTextField();
        txtFecha = createReadOnlyTextField();
        txtSubtotal = createReadOnlyTextField();
        txtIVA = createReadOnlyTextField();
        txtTotal = createReadOnlyTextField();
        txtEstado = createReadOnlyTextField();
        txtMetodoPago = createReadOnlyTextField();
        
        addField(facturaPanel, gbcF, "N° Factura:", txtNumeroFactura, 0, 0, 1, 1);
        addField(facturaPanel, gbcF, "Fecha:", txtFecha, 2, 0, 1, 1);
        addField(facturaPanel, gbcF, "Subtotal:", txtSubtotal, 0, 1, 1, 1);
        addField(facturaPanel, gbcF, "IVA (12%):", txtIVA, 2, 1, 1, 1);
        addField(facturaPanel, gbcF, "Total:", txtTotal, 0, 2, 1, 1);
        addField(facturaPanel, gbcF, "Estado:", txtEstado, 2, 2, 1, 1);
        addField(facturaPanel, gbcF, "Método Pago:", txtMetodoPago, 0, 3, 4, 1);
        
        topPanel.add(clientePanel);
        topPanel.add(facturaPanel);
        mainPanel.add(topPanel, BorderLayout.NORTH);
        
        // Panel de pestañas
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 12));
        
        // Servicios
        modelServicios = new NonEditableTableModel(new String[]{"Servicio", "Descripción", "Precio Unitario", "Total", "Veterinario"}, 0);
        tblServicios = new JTable(modelServicios);
        styleTable(tblServicios);
        tabbedPane.addTab(" Servicios", new JScrollPane(tblServicios));
        
        // Mascotas
        modelMascotas = new NonEditableTableModel(new String[]{"Mascota", "Especie", "Raza", "Diagnóstico", "Tratamiento"}, 0);
        tblMascotas = new JTable(modelMascotas);
        styleTable(tblMascotas);
        tabbedPane.addTab(" Mascotas", new JScrollPane(tblMascotas));
        
        // Medicamentos
        modelMedicamentos = new NonEditableTableModel(new String[]{"Medicamento", "Dosis", "Frecuencia", "Duración", "Precio"}, 0);
        tblMedicamentos = new JTable(modelMedicamentos);
        styleTable(tblMedicamentos);
        tabbedPane.addTab(" Medicamentos", new JScrollPane(tblMedicamentos));
        
        // Instrumentos
        modelInstrumentos = new NonEditableTableModel(new String[]{"Instrumento", "Costo Uso"}, 0);
        tblInstrumentos = new JTable(modelInstrumentos);
        styleTable(tblInstrumentos);
        tabbedPane.addTab(" Instrumentos", new JScrollPane(tblInstrumentos));
        
        // Vacunas
        modelVacunas = new NonEditableTableModel(new String[]{"Vacuna", "Descripción", "Fecha Aplicación", "Próxima Aplicación"}, 0);
        tblVacunas = new JTable(modelVacunas);
        styleTable(tblVacunas);
        tabbedPane.addTab(" Vacunas", new JScrollPane(tblVacunas));
        
        mainPanel.add(tabbedPane, BorderLayout.CENTER);
        add(mainPanel, BorderLayout.CENTER);
        
        // Botón cerrar
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JButton btnCerrar = new JButton("Cerrar");
        btnCerrar.setBackground(new Color(230, 140, 30));
        btnCerrar.setForeground(Color.WHITE);
        btnCerrar.setFocusPainted(false);
        btnCerrar.setPreferredSize(new Dimension(120, 40));
        btnCerrar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnCerrar.addActionListener(e -> dispose());
        buttonPanel.add(btnCerrar);
        add(buttonPanel, BorderLayout.SOUTH);
        
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
    }
    
    private class NonEditableTableModel extends DefaultTableModel {
        public NonEditableTableModel(Object[] columnNames, int rowCount) {
            super(columnNames, rowCount);
        }
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    }
    
    private JPanel createInfoPanel(String title) {
        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createEtchedBorder(), 
            title,
            TitledBorder.LEFT,
            TitledBorder.TOP,
            new Font("Segoe UI", Font.BOLD, 12),
            new Color(230, 140, 30)
        ));
        return panel;
    }
    
    private void styleTable(JTable table) {
        table.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        table.getTableHeader().setBackground(new Color(230, 140, 30));
        table.getTableHeader().setForeground(Color.WHITE);
        table.setRowHeight(30);
        table.setIntercellSpacing(new Dimension(10, 5));
        table.setSelectionBackground(new Color(230, 140, 30, 100));
    }
    
    private void addField(JPanel panel, GridBagConstraints gbc, String label, JTextField field, 
                          int gridx, int gridy, int gridwidth, int gridheight) {
        gbc.gridx = gridx;
        gbc.gridy = gridy;
        gbc.gridwidth = 1;
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 12));
        panel.add(lbl, gbc);
        
        gbc.gridx = gridx + 1;
        gbc.gridwidth = gridwidth;
        field.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        field.setBackground(new Color(248, 249, 250));
        field.setPreferredSize(new Dimension(180, 30));
        panel.add(field, gbc);
    }
    
    private JTextField createReadOnlyTextField() {
        JTextField field = new JTextField();
        field.setEditable(false);
        return field;
    }
    
    private void cargarDetalleFactura() {
        try {
            Factura factura = facturaService.obtenerDetalle(idFactura);
            if (factura != null) {
                // Datos de factura
                txtNumeroFactura.setText(String.valueOf(factura.getIdFactura()));
                txtFecha.setText(factura.getFecha() != null ? new SimpleDateFormat("dd/MM/yyyy HH:mm").format(factura.getFecha()) : "-");
                txtSubtotal.setText(factura.getSubtotal() > 0 ? String.format("$ %.2f", factura.getSubtotal()) : "$ 0.00");
                txtIVA.setText(factura.getIva() > 0 ? String.format("$ %.2f", factura.getIva()) : "$ 0.00");
                txtTotal.setText(factura.getTotal() > 0 ? String.format("$ %.2f", factura.getTotal()) : "$ 0.00");
                txtEstado.setText(factura.getEstado());
                txtMetodoPago.setText(factura.getMetodoPago() != null ? factura.getMetodoPago() : "EFECTIVO");
                
                // Datos del cliente
                if (factura.getCliente() != null) {
                    txtCliente.setText(factura.getCliente().getNombre() + " " + factura.getCliente().getApellido());
                    txtCedula.setText(factura.getCliente().getCedula());
                    txtTelefono.setText(factura.getCliente().getTelefono());
                    txtEmail.setText(factura.getCliente().getCorreoElectronico() != null ? factura.getCliente().getCorreoElectronico() : "-");
                    txtDireccion.setText(factura.getCliente().getDireccion() != null ? factura.getCliente().getDireccion() : "-");
                }
                
                // Limpiar tablas
                modelServicios.setRowCount(0);
                modelMascotas.setRowCount(0);
                modelMedicamentos.setRowCount(0);
                modelInstrumentos.setRowCount(0);
                modelVacunas.setRowCount(0);
                
                // Cargar servicios (solo uno)
                if (factura.getServicios() != null && !factura.getServicios().isEmpty()) {
                    for (Object[] servicio : factura.getServicios()) {
                        if (servicio != null && servicio.length >= 5) {
                            modelServicios.addRow(servicio);
                        }
                    }
                }
                
                // Cargar mascotas
                if (factura.getMascotas() != null && !factura.getMascotas().isEmpty()) {
                    for (Object[] mascota : factura.getMascotas()) {
                        if (mascota != null && mascota.length >= 5 && mascota[0] != null && !mascota[0].toString().equals("-")) {
                            modelMascotas.addRow(mascota);
                        }
                    }
                }
                
                // Cargar medicamentos
                if (factura.getMedicamentos() != null && !factura.getMedicamentos().isEmpty()) {
                    for (Object[] medicamento : factura.getMedicamentos()) {
                        if (medicamento != null && medicamento.length >= 5 && medicamento[0] != null && !medicamento[0].toString().equals("-")) {
                            modelMedicamentos.addRow(medicamento);
                        }
                    }
                }
                
                // Cargar instrumentos
                if (factura.getInstrumentos() != null && !factura.getInstrumentos().isEmpty()) {
                    for (Object[] instrumento : factura.getInstrumentos()) {
                        if (instrumento != null && instrumento.length >= 2 && instrumento[0] != null && !instrumento[0].toString().equals("-")) {
                            modelInstrumentos.addRow(instrumento);
                        }
                    }
                }
                
                // Cargar vacunas
                if (factura.getVacunas() != null && !factura.getVacunas().isEmpty()) {
                    for (Object[] vacuna : factura.getVacunas()) {
                        if (vacuna != null && vacuna.length >= 4 && vacuna[0] != null && !vacuna[0].toString().equals("-")) {
                            modelVacunas.addRow(vacuna);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, 
                "Error al cargar detalle de factura: " + e.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}