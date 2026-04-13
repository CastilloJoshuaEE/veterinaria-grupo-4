package com.mycompany.veterinaria.grupo4.view;

import com.mycompany.veterinaria.grupo4.model.entity.Medicamento;
import com.mycompany.veterinaria.grupo4.service.MedicamentoService;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class frmBuscarMedicamento extends JDialog {
    private MedicamentoService medicamentoService = new MedicamentoService();
    private Medicamento medicamentoSeleccionado;
    private String dosis, frecuencia, duracion;
    private boolean confirmed = false;
    
    private JTable tblMedicamentos;
    private DefaultTableModel model;
    private JTextField txtDosis, txtFrecuencia, txtDuracion;
    private JButton btnSeleccionar, btnCancelar;
    
    public frmBuscarMedicamento() {
        setTitle("Buscar Medicamento");
        setModal(true);
        setSize(700, 500);
        setLocationRelativeTo(null);
        initComponents();
        cargarMedicamentos();
    }
    
    private void initComponents() {
        setLayout(new BorderLayout());
        
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Tabla de medicamentos
        model = new DefaultTableModel(new String[]{"ID", "Nombre", "Descripción", "Precio"}, 0);
        tblMedicamentos = new JTable(model);
        tblMedicamentos.getColumnModel().getColumn(0).setMinWidth(0);
        tblMedicamentos.getColumnModel().getColumn(0).setMaxWidth(0);
        tblMedicamentos.getColumnModel().getColumn(2).setPreferredWidth(250);
        
        mainPanel.add(new JScrollPane(tblMedicamentos), BorderLayout.CENTER);
        
        // Panel de detalles de receta
        JPanel recetaPanel = new JPanel(new GridBagLayout());
        recetaPanel.setBorder(BorderFactory.createTitledBorder("Detalles de la Receta"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        int row = 0;
        gbc.gridx = 0; gbc.gridy = row;
        recetaPanel.add(new JLabel("Dosis (unidades):"), gbc);
        gbc.gridx = 1;
        txtDosis = new JTextField(10);
        recetaPanel.add(txtDosis, gbc);
        
        row++;
        gbc.gridx = 0; gbc.gridy = row;
        recetaPanel.add(new JLabel("Frecuencia (veces/día):"), gbc);
        gbc.gridx = 1;
        txtFrecuencia = new JTextField(10);
        recetaPanel.add(txtFrecuencia, gbc);
        
        row++;
        gbc.gridx = 0; gbc.gridy = row;
        recetaPanel.add(new JLabel("Duración (días):"), gbc);
        gbc.gridx = 1;
        txtDuracion = new JTextField(10);
        recetaPanel.add(txtDuracion, gbc);
        
        mainPanel.add(recetaPanel, BorderLayout.SOUTH);
        
        add(mainPanel, BorderLayout.CENTER);
        
        // Botones
        JPanel buttonPanel = new JPanel(new FlowLayout());
        btnSeleccionar = new JButton("Seleccionar");
        btnCancelar = new JButton("Cancelar");
        buttonPanel.add(btnSeleccionar);
        buttonPanel.add(btnCancelar);
        add(buttonPanel, BorderLayout.SOUTH);
        
        // Eventos - solo permitir números
        txtDosis.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent e) {
                char c = e.getKeyChar();
                if (!Character.isDigit(c) && c != '\b') {
                    e.consume();
                }
            }
        });
        txtFrecuencia.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent e) {
                char c = e.getKeyChar();
                if (!Character.isDigit(c) && c != '\b') {
                    e.consume();
                }
            }
        });
        txtDuracion.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent e) {
                char c = e.getKeyChar();
                if (!Character.isDigit(c) && c != '\b') {
                    e.consume();
                }
            }
        });
        
        btnSeleccionar.addActionListener(e -> seleccionar());
        btnCancelar.addActionListener(e -> cancelar());
    }
    
    private void cargarMedicamentos() {
        model.setRowCount(0);
        List<Medicamento> medicamentos = medicamentoService.listarDisponibles();
        if (medicamentos != null) {
            for (Medicamento m : medicamentos) {
                model.addRow(new Object[]{
                    m.getIdMedicamento(),
                    m.getNombre(),
                    m.getDescripcion(),
                    String.format("$%.2f", m.getPrecio())
                });
            }
        }
    }
    
    private void seleccionar() {
        int row = tblMedicamentos.getSelectedRow();
        if (row >= 0) {
            dosis = txtDosis.getText().trim();
            frecuencia = txtFrecuencia.getText().trim();
            duracion = txtDuracion.getText().trim();
            
            if (dosis.isEmpty() || frecuencia.isEmpty() || duracion.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Complete los detalles de la receta", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            int id = (int) model.getValueAt(row, 0);
            medicamentoSeleccionado = medicamentoService.obtenerPorId(id);
            medicamentoSeleccionado.setDosis(dosis);
            medicamentoSeleccionado.setFrecuencia(frecuencia);
            medicamentoSeleccionado.setDuracion(duracion);
            confirmed = true;
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Seleccione un medicamento", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void cancelar() {
        confirmed = false;
        dispose();
    }
    
    public boolean isConfirmed() { return confirmed; }
    public Medicamento getMedicamento() { return medicamentoSeleccionado; }
    public String getDosis() { return dosis; }
    public String getFrecuencia() { return frecuencia; }
    public String getDuracion() { return duracion; }
}