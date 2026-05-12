package com.mycompany.veterinaria.grupo4.view.factura;

import com.mycompany.veterinaria.grupo4.model.entity.Factura;
import com.mycompany.veterinaria.grupo4.service.FacturaService;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.List;
import javax.swing.border.TitledBorder;

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
        // Crear modelo no editable
        modelFacturas = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Todas las celdas no editables
            }
        };
        tblFacturas = new JTable(modelFacturas);
        tblFacturas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblFacturas.getSelectionModel().addListSelectionListener(e -> {
            btnDetalleFactura.setEnabled(tblFacturas.getSelectedRow() >= 0);
        });
        
        // Estilo de la tabla
        tblFacturas.setFont(new Font("Arial", Font.PLAIN, 12));
        tblFacturas.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        tblFacturas.getTableHeader().setBackground(new Color(230, 140, 30));
        tblFacturas.getTableHeader().setForeground(Color.WHITE);
        tblFacturas.setRowHeight(30);
        tblFacturas.setIntercellSpacing(new Dimension(10, 5));
        
        JScrollPane scrollPane = new JScrollPane(tblFacturas);
        scrollPane.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createEtchedBorder(),
            "Listado de Facturas",
            TitledBorder.LEFT,
            TitledBorder.TOP,
            new Font("Arial", Font.BOLD, 12),
            new Color(230, 140, 30)
        ));
        add(scrollPane, BorderLayout.CENTER);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        btnDetalleFactura = new JButton("Ver Detalle");
        btnDetalleFactura.setEnabled(false);
        btnDetalleFactura.setBackground(new Color(230, 140, 30));
        btnDetalleFactura.setForeground(Color.WHITE);
        btnDetalleFactura.setFocusPainted(false);
        btnDetalleFactura.setFont(new Font("Arial", Font.BOLD, 12));
        btnDetalleFactura.setPreferredSize(new Dimension(120, 35));
        
        btnCerrar = new JButton("Cerrar");
        btnCerrar.setBackground(new Color(108, 117, 125));
        btnCerrar.setForeground(Color.WHITE);
        btnCerrar.setFocusPainted(false);
        btnCerrar.setFont(new Font("Arial", Font.BOLD, 12));
        btnCerrar.setPreferredSize(new Dimension(100, 35));
        
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
                    String.format("$ %.2f", f.getSubtotal()),
                    String.format("$ %.2f", f.getIva()),
                    String.format("$ %.2f", f.getTotal()),
                    f.getEstado(),
                    f.getMetodoPago() != null ? f.getMetodoPago() : "EFECTIVO"
                });
            }
        } else {
            modelFacturas.addRow(new Object[]{"-", "-", "-", "-", "-", "No hay facturas", "-"});
        }
        
        // Ajustar anchos de columna
        tblFacturas.getColumnModel().getColumn(0).setPreferredWidth(80);
        tblFacturas.getColumnModel().getColumn(1).setPreferredWidth(150);
        tblFacturas.getColumnModel().getColumn(2).setPreferredWidth(100);
        tblFacturas.getColumnModel().getColumn(3).setPreferredWidth(80);
        tblFacturas.getColumnModel().getColumn(4).setPreferredWidth(100);
        tblFacturas.getColumnModel().getColumn(5).setPreferredWidth(100);
        tblFacturas.getColumnModel().getColumn(6).setPreferredWidth(120);
    }
    
    private void verDetalle() {
        int row = tblFacturas.getSelectedRow();
        if (row >= 0) {
            Object idObj = modelFacturas.getValueAt(row, 0);
            int idFactura;
            if (idObj instanceof Integer) {
                idFactura = (Integer) idObj;
            } else if (idObj instanceof String) {
                idFactura = Integer.parseInt((String) idObj);
            } else {
                return;
            }
            frmDetalleFactura detalle = new frmDetalleFactura(this, idFactura);
            detalle.setVisible(true);
        }
    }
}