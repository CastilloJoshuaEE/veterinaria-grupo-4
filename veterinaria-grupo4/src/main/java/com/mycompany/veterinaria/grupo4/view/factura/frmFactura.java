package com.mycompany.veterinaria.grupo4.view.factura;

import com.mycompany.veterinaria.grupo4.model.entity.Factura;
import com.mycompany.veterinaria.grupo4.service.FacturaService;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.List;

public class frmFactura extends JDialog {
    private FacturaService facturaService;
    private String cedulaCliente;
    
    private JTable tblFacturas;
    private DefaultTableModel modelFacturas;
    private JButton btnDetalleFactura, btnCerrar;
    
    public frmFactura(Window parent, String cedulaCliente) {
        super(parent, "Facturas del Cliente: " + cedulaCliente, ModalityType.APPLICATION_MODAL);
        this.cedulaCliente = cedulaCliente;
        this.facturaService = new FacturaService();
        setSize(1000, 500);
        setLocationRelativeTo(parent);
        initComponents();
        cargarFacturas();
    }
    
    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        
        JPanel topPanel = new JPanel();
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 5, 10));
        JLabel lblTitulo = new JLabel("Facturas del Cliente: " + cedulaCliente, SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 16));
        topPanel.add(lblTitulo);
        add(topPanel, BorderLayout.NORTH);
        
        String[] columnas = {"ID Factura", "Fecha", "Subtotal", "IVA", "Total", "Estado", "Método Pago"};
        modelFacturas = new DefaultTableModel(columnas, 0);
        tblFacturas = new JTable(modelFacturas);
        tblFacturas.getSelectionModel().addListSelectionListener(e -> {
            btnDetalleFactura.setEnabled(tblFacturas.getSelectedRow() >= 0);
        });
        
        JScrollPane scrollPane = new JScrollPane(tblFacturas);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Listado de Facturas"));
        add(scrollPane, BorderLayout.CENTER);
        
        JPanel buttonPanel = new JPanel(new FlowLayout());
        btnDetalleFactura = new JButton("Ver Detalle");
        btnDetalleFactura.setEnabled(false);
        btnCerrar = new JButton("Cerrar");
        buttonPanel.add(btnDetalleFactura);
        buttonPanel.add(btnCerrar);
        add(buttonPanel, BorderLayout.SOUTH);
        
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
            frmDetalleFactura detalle = new frmDetalleFactura(this, idFactura);
            detalle.setVisible(true);
        }
    }
}