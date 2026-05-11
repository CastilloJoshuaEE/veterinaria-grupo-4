package com.mycompany.veterinaria.grupo4.view;

import com.mycompany.veterinaria.grupo4.model.entity.Factura;
import com.mycompany.veterinaria.grupo4.service.FacturaService;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.List;

public class frmFactura extends JFrame {
    private FacturaService facturaService = new FacturaService();
    private String cedulaCliente;
    
    private JTable tblFacturas;
    private DefaultTableModel modelFacturas;
    private JButton btnDetalleFactura, btnCerrar;
    private JLabel lblTitulo;
    
    public frmFactura(String cedulaCliente) {
        this.cedulaCliente = cedulaCliente;
        setTitle("Facturas del Cliente: " + cedulaCliente);
        setSize(900, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        initComponents();
        cargarFacturas();
    }
    
    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        
        // Panel superior con título
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 5, 10));
        
        lblTitulo = new JLabel("Facturas del Cliente: " + cedulaCliente, SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 16));
        topPanel.add(lblTitulo, BorderLayout.CENTER);
        
        add(topPanel, BorderLayout.NORTH);
        
        // Tabla de facturas
        String[] columnas = {"ID Factura", "Fecha", "Subtotal", "IVA", "Total", "Estado", "Método Pago"};
        modelFacturas = new DefaultTableModel(columnas, 0);
        tblFacturas = new JTable(modelFacturas);
        tblFacturas.getSelectionModel().addListSelectionListener(e -> {
            btnDetalleFactura.setEnabled(tblFacturas.getSelectedRow() >= 0);
        });
        
        JScrollPane scrollPane = new JScrollPane(tblFacturas);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Listado de Facturas"));
        add(scrollPane, BorderLayout.CENTER);
        
        // Panel de botones
        JPanel buttonPanel = new JPanel(new FlowLayout());
        btnDetalleFactura = new JButton("Ver Detalle");
        btnDetalleFactura.setEnabled(false);
        btnCerrar = new JButton("Cerrar");
        
        buttonPanel.add(btnDetalleFactura);
        buttonPanel.add(btnCerrar);
        add(buttonPanel, BorderLayout.SOUTH);
        
        // Eventos
        btnDetalleFactura.addActionListener(e -> verDetalle());
        btnCerrar.addActionListener(e -> dispose());
    }
    
    private void cargarFacturas() {
        modelFacturas.setRowCount(0);
        List<Factura> facturas = facturaService.listarPorCedulaCliente(cedulaCliente);
        
        if (facturas != null && !facturas.isEmpty()) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            for (Factura f : facturas) {
                modelFacturas.addRow(new Object[]{
                    f.getIdFactura(),
                    sdf.format(f.getFecha()),
                    String.format("$%.2f", f.getSubtotal()),
                    String.format("$%.2f", f.getIva()),
                    String.format("$%.2f", f.getTotal()),
                    f.getEstado(),
                    f.getMetodoPago() != null ? f.getMetodoPago() : "EFECTIVO"
                });
            }
        } else {
            modelFacturas.addRow(new Object[]{"-", "-", "-", "-", "-", "No hay facturas", "-"});
        }
    }
    
    private void verDetalle() {
        int row = tblFacturas.getSelectedRow();
        if (row >= 0) {
            int idFactura = (int) modelFacturas.getValueAt(row, 0);
            frmDetalleFactura detalle = new frmDetalleFactura(idFactura);
            detalle.setVisible(true);
        }
    }
}