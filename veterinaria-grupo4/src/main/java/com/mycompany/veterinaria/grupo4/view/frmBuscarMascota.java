package com.mycompany.veterinaria.grupo4.view;

import com.mycompany.veterinaria.grupo4.model.entity.Mascota;
import com.mycompany.veterinaria.grupo4.service.MascotaService;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class frmBuscarMascota extends JDialog {
    private MascotaService mascotaService = new MascotaService();
    private Mascota mascotaSeleccionada;
    private boolean confirmed = false;
    
    private JTable tblMascotas;
    private DefaultTableModel model;
    private JButton btnSeleccionar, btnCancelar;
    private JTextField txtFiltro;
    
    public frmBuscarMascota() {
        setTitle("Buscar Mascota");
        setModal(true);
        setSize(600, 450);
        setLocationRelativeTo(null);
        initComponents();
        cargarMascotas();
    }
    
    private void initComponents() {
        setLayout(new BorderLayout());
        
        // Panel de filtro
        JPanel filtroPanel = new JPanel(new FlowLayout());
        filtroPanel.add(new JLabel("Filtrar por nombre:"));
        txtFiltro = new JTextField(20);
        txtFiltro.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent e) {
                filtrarMascotas();
            }
        });
        filtroPanel.add(txtFiltro);
        add(filtroPanel, BorderLayout.NORTH);
        
        // Tabla de mascotas
        model = new DefaultTableModel(new String[]{"ID", "Nombre", "Especie", "Raza", "Cliente"}, 0);
        tblMascotas = new JTable(model);
        tblMascotas.getColumnModel().getColumn(0).setMinWidth(0);
        tblMascotas.getColumnModel().getColumn(0).setMaxWidth(0);
        
        add(new JScrollPane(tblMascotas), BorderLayout.CENTER);
        
        // Botones
        JPanel buttonPanel = new JPanel(new FlowLayout());
        btnSeleccionar = new JButton("Seleccionar");
        btnCancelar = new JButton("Cancelar");
        buttonPanel.add(btnSeleccionar);
        buttonPanel.add(btnCancelar);
        add(buttonPanel, BorderLayout.SOUTH);
        
        btnSeleccionar.addActionListener(e -> seleccionar());
        btnCancelar.addActionListener(e -> cancelar());
    }
    
    private void cargarMascotas() {
        model.setRowCount(0);
        List<Mascota> mascotas = mascotaService.listarPorCliente(0);
        if (mascotas != null) {
            for (Mascota m : mascotas) {
                model.addRow(new Object[]{
                    m.getIdMascota(),
                    m.getNombre(),
                    m.getEspecie(),
                    m.getRaza(),
                    "Cliente ID: " + m.getIdCliente()
                });
            }
        }
    }
    
    private void filtrarMascotas() {
        String filtro = txtFiltro.getText().toLowerCase();
        model.setRowCount(0);
        List<Mascota> mascotas = mascotaService.listarPorCliente(0);
        if (mascotas != null) {
            for (Mascota m : mascotas) {
                if (m.getNombre().toLowerCase().contains(filtro) ||
                    m.getEspecie().toLowerCase().contains(filtro)) {
                    model.addRow(new Object[]{
                        m.getIdMascota(),
                        m.getNombre(),
                        m.getEspecie(),
                        m.getRaza(),
                        "Cliente ID: " + m.getIdCliente()
                    });
                }
            }
        }
    }
    
    private void seleccionar() {
        int row = tblMascotas.getSelectedRow();
        if (row >= 0) {
            int id = (int) model.getValueAt(row, 0);
            mascotaSeleccionada = mascotaService.obtenerPorId(id);
            confirmed = true;
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Seleccione una mascota", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void cancelar() {
        confirmed = false;
        dispose();
    }
    
    public boolean isConfirmed() { return confirmed; }
    public Mascota getMascota() { return mascotaSeleccionada; }
}