package com.mycompany.veterinaria.grupo4.view;

import com.mycompany.veterinaria.grupo4.model.entity.InstrumentoMedico;
import com.mycompany.veterinaria.grupo4.service.InstrumentoMedicoService;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class frmBuscarInstrumento extends JDialog {
    private InstrumentoMedicoService instrumentoService = new InstrumentoMedicoService();
    private InstrumentoMedico instrumentoSeleccionado;
    private boolean confirmed = false;
    
    private JTable tblInstrumentos;
    private DefaultTableModel model;
    private JButton btnSeleccionar, btnCancelar;
    
    public frmBuscarInstrumento() {
        setTitle("Buscar Instrumento Médico");
        setModal(true);
        setSize(500, 400);
        setLocationRelativeTo(null);
        initComponents();
        cargarInstrumentos();
    }
    
    private void initComponents() {
        setLayout(new BorderLayout());
        
        // Tabla de instrumentos
        model = new DefaultTableModel(new String[]{"ID", "Nombre", "Descripción", "Costo Uso"}, 0);
        tblInstrumentos = new JTable(model);
        tblInstrumentos.getColumnModel().getColumn(0).setMinWidth(0);
        tblInstrumentos.getColumnModel().getColumn(0).setMaxWidth(0);
        tblInstrumentos.getColumnModel().getColumn(2).setPreferredWidth(200);
        
        add(new JScrollPane(tblInstrumentos), BorderLayout.CENTER);
        
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
    
    private void cargarInstrumentos() {
        model.setRowCount(0);
        List<InstrumentoMedico> instrumentos = instrumentoService.listarDisponibles();
        if (instrumentos != null) {
            for (InstrumentoMedico i : instrumentos) {
                model.addRow(new Object[]{
                    i.getIdInstrumento(),
                    i.getNombre(),
                    i.getDescripcion(),
                    String.format("$%.2f", i.getCostoUso())
                });
            }
        }
    }
    
    private void seleccionar() {
        int row = tblInstrumentos.getSelectedRow();
        if (row >= 0) {
            int id = (int) model.getValueAt(row, 0);
            instrumentoSeleccionado = instrumentoService.obtenerPorId(id);
            confirmed = true;
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Seleccione un instrumento", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void cancelar() {
        confirmed = false;
        dispose();
    }
    
    public boolean isConfirmed() { return confirmed; }
    public InstrumentoMedico getInstrumento() { return instrumentoSeleccionado; }
}