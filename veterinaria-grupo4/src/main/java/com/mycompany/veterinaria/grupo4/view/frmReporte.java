package com.mycompany.veterinaria.grupo4.view;

import com.mycompany.veterinaria.grupo4.model.entity.Recordatorio;
import com.mycompany.veterinaria.grupo4.service.RecordatorioService;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class frmReporte extends JFrame {
    private RecordatorioService recordatorioService = new RecordatorioService();
    private int idUsuarioActual;
    
    private JTable tblRecordatorios, tblConfiguraciones;
    private DefaultTableModel modelRecordatorios, modelConfiguraciones;
    private JButton btnCargar, btnGenerar, btnNuevo, btnEditar, btnEliminar;
    private JTextField txtFiltroInicio, txtFiltroFin;
    
    public frmReporte(int idUsuario) {
        this.idUsuarioActual = idUsuario;
        initComponents();
        cargarRecordatorios();
        cargarConfiguraciones();
        setLocationRelativeTo(null);
    }
    
    private void initComponents() {
        setTitle("Gestión de Recordatorios");
        setSize(1100, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        
        JTabbedPane tabbedPane = new JTabbedPane();
        
        // Panel de recordatorios
        JPanel recordatoriosPanel = new JPanel(new BorderLayout(10, 10));
        recordatoriosPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Panel de filtros
        JPanel filtrosPanel = new JPanel(new FlowLayout());
        filtrosPanel.add(new JLabel("Fecha Inicio:"));
        txtFiltroInicio = new JTextField(new SimpleDateFormat("yyyy-MM-dd").format(new Date()), 12);
        filtrosPanel.add(txtFiltroInicio);
        filtrosPanel.add(new JLabel("Fecha Fin:"));
        txtFiltroFin = new JTextField(new SimpleDateFormat("yyyy-MM-dd").format(new Date()), 12);
        filtrosPanel.add(txtFiltroFin);
        
        btnCargar = new JButton("Cargar");
        btnGenerar = new JButton("Generar Recordatorios");
        filtrosPanel.add(btnCargar);
        filtrosPanel.add(btnGenerar);
        
        recordatoriosPanel.add(filtrosPanel, BorderLayout.NORTH);
        
        // Tabla de recordatorios
        modelRecordatorios = new DefaultTableModel(new String[]{"ID", "Tipo", "Mensaje", "Fecha Envío", "Leído", "Veces Mostrado"}, 0);
        tblRecordatorios = new JTable(modelRecordatorios);
        recordatoriosPanel.add(new JScrollPane(tblRecordatorios), BorderLayout.CENTER);
        
        // Panel de botones para recordatorios
        JPanel botonesRecordatorios = new JPanel(new FlowLayout());
        btnNuevo = new JButton("Nuevo Recordatorio");
        btnEditar = new JButton("Editar");
        btnEliminar = new JButton("Eliminar");
        botonesRecordatorios.add(btnNuevo);
        botonesRecordatorios.add(btnEditar);
        botonesRecordatorios.add(btnEliminar);
        recordatoriosPanel.add(botonesRecordatorios, BorderLayout.SOUTH);
        
        tabbedPane.addTab("Recordatorios", recordatoriosPanel);
        
        // Panel de configuraciones
        JPanel configuracionesPanel = new JPanel(new BorderLayout());
        configuracionesPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        modelConfiguraciones = new DefaultTableModel(new String[]{"ID", "Tipo", "Anticipación", "Mensaje", "Activo"}, 0);
        tblConfiguraciones = new JTable(modelConfiguraciones);
        configuracionesPanel.add(new JScrollPane(tblConfiguraciones), BorderLayout.CENTER);
        
        tabbedPane.addTab("Configuraciones", configuracionesPanel);
        
        add(tabbedPane, BorderLayout.CENTER);
        
        // Eventos
        btnCargar.addActionListener(e -> cargarRecordatorios());
        btnGenerar.addActionListener(e -> generarRecordatorios());
        btnNuevo.addActionListener(e -> nuevoRecordatorio());
        btnEditar.addActionListener(e -> editarRecordatorio());
        btnEliminar.addActionListener(e -> eliminarRecordatorio());
    }
    
    private void cargarRecordatorios() {
        modelRecordatorios.setRowCount(0);
        try {
            Date inicio = new java.text.SimpleDateFormat("yyyy-MM-dd").parse(txtFiltroInicio.getText());
            Date fin = new java.text.SimpleDateFormat("yyyy-MM-dd").parse(txtFiltroFin.getText());
            
            List<Recordatorio> lista = recordatorioService.listarTodos(inicio, fin);
            if (lista != null) {
                for (Recordatorio r : lista) {
                    modelRecordatorios.addRow(new Object[]{
                        r.getIdRecordatorio(),
                        r.getTipo(),
                        r.getMensaje(),
                        new SimpleDateFormat("dd/MM/yyyy HH:mm").format(r.getFechaEnvio()),
                        r.isLeido() ? "Sí" : "No",
                        r.getContadorMostrado()
                    });
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void cargarConfiguraciones() {
        // Cargar configuraciones desde la base de datos
        modelConfiguraciones.setRowCount(0);
        // Aquí cargarías las configuraciones desde un servicio
    }
    
    private void generarRecordatorios() {
        recordatorioService.generarRecordatorios(idUsuarioActual);
        JOptionPane.showMessageDialog(this, "Recordatorios generados", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        cargarRecordatorios();
    }
    
    private void nuevoRecordatorio() {
        frmNuevoRecordatorio nuevo = new frmNuevoRecordatorio();
        nuevo.setVisible(true);
        cargarRecordatorios();
    }
    
    private void editarRecordatorio() {
        int row = tblRecordatorios.getSelectedRow();
        if (row >= 0) {
            int id = (int) modelRecordatorios.getValueAt(row, 0);
            frmEditarRecordatorio editar = new frmEditarRecordatorio(id, 
                (String) modelRecordatorios.getValueAt(row, 2),
                "Sí".equals(modelRecordatorios.getValueAt(row, 4)));
            editar.setVisible(true);
            cargarRecordatorios();
        }
    }
    
    private void eliminarRecordatorio() {
        int row = tblRecordatorios.getSelectedRow();
        if (row >= 0) {
            int confirm = JOptionPane.showConfirmDialog(this, "¿Eliminar este recordatorio?", 
                "Confirmar", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                int id = (int) modelRecordatorios.getValueAt(row, 0);
                if (recordatorioService.eliminar(id)) {
                    cargarRecordatorios();
                }
            }
        }
    }
}