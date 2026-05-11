package com.mycompany.veterinaria.grupo4.view.recordatorio;

import com.mycompany.veterinaria.grupo4.config.RecordatorioConfig;
import com.mycompany.veterinaria.grupo4.controller.CtrlRecordatorio;
import com.mycompany.veterinaria.grupo4.model.entity.Recordatorio;
import com.mycompany.veterinaria.grupo4.util.NotificationManager;
import com.mycompany.veterinaria.grupo4.util.SessionManager;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class PnlRecordatorioReporte extends JPanel {

    private JTable tblRecordatorios;
    private JTable tblConfiguraciones;
    private DefaultTableModel modelRecordatorios;
    private DefaultTableModel modelConfiguraciones;
    private JSpinner spFechaInicio;
    private JSpinner spFechaFin;
    private JButton btnCargar, btnNuevo, btnEditar, btnEliminar, btnActualizarConfig, btnNuevaConfiguracion;
    private CtrlRecordatorio controller;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    
    // Guardar el último usuario para detectar cambios
    private int lastUserId = -1;

    public PnlRecordatorioReporte() {
        controller = new CtrlRecordatorio();
        initComponents();
        cargarConfiguraciones();
        
        // No iniciamos NotificationManager aquí, lo haremos cuando el panel se muestre
    }
    
    // Método para refrescar cuando el panel se muestra
    @Override
    public void addNotify() {
        super.addNotify();
        // Verificar si el usuario cambió
        int currentUserId = SessionManager.getInstance().getCurrentUserId();
        
        if (currentUserId != lastUserId && currentUserId > 0) {
            // Usuario cambió, actualizar
            lastUserId = currentUserId;
            
            // Detener NotificationManager anterior (si existe)
            NotificationManager.getInstance().stop();
            
            // Iniciar para el nuevo usuario
            NotificationManager.getInstance().start(currentUserId);
            System.out.println(" PnlRecordatorioReporte: NotificationManager reiniciado para usuario ID: " + currentUserId);
            
            // Recargar reportes
            cargarReportesPorDefecto();
        } else if (currentUserId > 0 && lastUserId == -1) {
            // Primera vez
            lastUserId = currentUserId;
            NotificationManager.getInstance().start(currentUserId);
            cargarReportesPorDefecto();
        }
    }
    
    // Limpiar cuando el panel se oculta
    @Override
    public void removeNotify() {
        // No detenemos NotificationManager aquí porque otros paneles lo usan
        super.removeNotify();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Panel de Filtros
        JPanel pnlFiltros = new JPanel(new FlowLayout(FlowLayout.LEFT));
        
        SpinnerDateModel modelInicio = new SpinnerDateModel();
        SpinnerDateModel modelFin = new SpinnerDateModel();
        spFechaInicio = new JSpinner(modelInicio);
        spFechaFin = new JSpinner(modelFin);
        JSpinner.DateEditor editorInicio = new JSpinner.DateEditor(spFechaInicio, "dd/MM/yyyy");
        JSpinner.DateEditor editorFin = new JSpinner.DateEditor(spFechaFin, "dd/MM/yyyy");
        spFechaInicio.setEditor(editorInicio);
        spFechaFin.setEditor(editorFin);
        spFechaInicio.setPreferredSize(new Dimension(120, 25));
        spFechaFin.setPreferredSize(new Dimension(120, 25));
        
        btnCargar = new JButton("Cargar Reporte");
        
        pnlFiltros.add(new JLabel("Fecha Inicio:"));
        pnlFiltros.add(spFechaInicio);
        pnlFiltros.add(new JLabel("Fecha Fin:"));
        pnlFiltros.add(spFechaFin);
        pnlFiltros.add(btnCargar);

        // Tabla de Recordatorios
        modelRecordatorios = new DefaultTableModel(new String[]{"ID", "Tipo", "Mensaje", "Fecha Envío", "Leído", "Veces Mostrado", "ID Cita", "ID Vacuna"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { 
                // Solo permitir editar el mensaje (columna 2) y leído (columna 4)
                return column == 2 || column == 4;
            }
        };
        tblRecordatorios = new JTable(modelRecordatorios);
        JScrollPane spRecordatorios = new JScrollPane(tblRecordatorios);
        
        JPanel pnlBotonesAccion = new JPanel(new FlowLayout());
        btnNuevo = new JButton("Nuevo Recordatorio");
        btnEditar = new JButton("Editar Recordatorio");
        btnEliminar = new JButton("Eliminar Recordatorio");
        pnlBotonesAccion.add(btnNuevo);
        pnlBotonesAccion.add(btnEditar);
        pnlBotonesAccion.add(btnEliminar);

        // Tabla de Configuraciones
        modelConfiguraciones = new DefaultTableModel(new String[]{"ID", "Tipo", "Anticipación", "Mensaje", "Activo"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return column == 3 || column == 4; }
        };
        tblConfiguraciones = new JTable(modelConfiguraciones);
        JScrollPane spConfiguraciones = new JScrollPane(tblConfiguraciones);
        
        JPanel pnlBotonesConfig = new JPanel(new FlowLayout());
        btnActualizarConfig = new JButton("Actualizar Configuración Seleccionada");
        btnNuevaConfiguracion = new JButton("Nueva Configuración");
        pnlBotonesConfig.add(btnActualizarConfig);
        pnlBotonesConfig.add(btnNuevaConfiguracion);
        
        JPanel pnlConfigTab = new JPanel(new BorderLayout());
        pnlConfigTab.add(spConfiguraciones, BorderLayout.CENTER);
        pnlConfigTab.add(pnlBotonesConfig, BorderLayout.SOUTH);

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Reporte de Recordatorios", createReportPanel(spRecordatorios, pnlBotonesAccion));
        tabbedPane.addTab("Configuraciones de Recordatorios", pnlConfigTab);

        add(pnlFiltros, BorderLayout.NORTH);
        add(tabbedPane, BorderLayout.CENTER);

        // Listeners
        btnCargar.addActionListener(e -> cargarReportes());
        btnNuevo.addActionListener(e -> nuevoRecordatorio());
        btnEditar.addActionListener(e -> editarRecordatorio());
        btnEliminar.addActionListener(e -> eliminarRecordatorio());
        btnActualizarConfig.addActionListener(e -> actualizarConfiguracion());
        btnNuevaConfiguracion.addActionListener(e -> nuevaConfiguracion());
    }
private void nuevoRecordatorio() {
        FrmNuevoRecordatorioDialog dialog = new FrmNuevoRecordatorioDialog(SwingUtilities.getWindowAncestor(this));
        if (dialog.showDialog()) {
            Recordatorio nuevo = dialog.getRecordatorio();
            String anticipacion = dialog.getAnticipacion();
            if (nuevo != null && controller.registrarRecordatorio(nuevo, anticipacion) > 0) {
                JOptionPane.showMessageDialog(this, "Recordatorio registrado con éxito.");
                cargarReportes();
            } else {
                JOptionPane.showMessageDialog(this, "Error al registrar el recordatorio.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
private void editarRecordatorio() {
    int selectedRow = tblRecordatorios.getSelectedRow();
    if (selectedRow == -1) {
        JOptionPane.showMessageDialog(this, "Seleccione un recordatorio para editar.");
        return;
    }
    
    int idRecordatorio = (int) modelRecordatorios.getValueAt(selectedRow, 0);
    String mensaje = (String) modelRecordatorios.getValueAt(selectedRow, 2);
    boolean leido = "Sí".equals(modelRecordatorios.getValueAt(selectedRow, 4));
    
    // Obtenemos los IDs separados
    String idCitaStr = (String) modelRecordatorios.getValueAt(selectedRow, 6);
    String idVacunaStr = (String) modelRecordatorios.getValueAt(selectedRow, 7);
    
    Recordatorio r = new Recordatorio();
    r.setIdRecordatorio(idRecordatorio);
    r.setMensaje(mensaje);
    r.setLeido(leido);
    
    // Asignamos los IDs si existen
    if (idCitaStr != null && !idCitaStr.isEmpty()) {
        r.setIdCita(Integer.parseInt(idCitaStr));
    }
    if (idVacunaStr != null && !idVacunaStr.isEmpty()) {
        r.setIdVacuna(Integer.parseInt(idVacunaStr));
    }
    
    FrmEditarRecordatorioDialog dialog = new FrmEditarRecordatorioDialog(SwingUtilities.getWindowAncestor(this), r);
    if (dialog.showDialog()) {
        if (controller.actualizarRecordatorio(dialog.getRecordatorio())) {
            JOptionPane.showMessageDialog(this, "Recordatorio actualizado.");
            cargarReportes();
        }
    }
}
private void eliminarRecordatorio() {
        int selectedRow = tblRecordatorios.getSelectedRow();
        if (selectedRow == -1) return;
        int idRecordatorio = (int) modelRecordatorios.getValueAt(selectedRow, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "¿Eliminar este recordatorio?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION && controller.eliminarRecordatorio(idRecordatorio)) {
            JOptionPane.showMessageDialog(this, "Recordatorio eliminado.");
            cargarReportes();
        }
    }

    private JPanel createReportPanel(JScrollPane spTable, JPanel pnlButtons) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(spTable, BorderLayout.CENTER);
        panel.add(pnlButtons, BorderLayout.SOUTH);
        return panel;
    }

    private void cargarReportesPorDefecto() {
        Date now = new Date();
        Date thirtyDaysAgo = new Date(now.getTime() - 30L * 24 * 3600 * 1000);
        spFechaInicio.setValue(thirtyDaysAgo);
        spFechaFin.setValue(now);
        cargarReportes();
    }
    private void cargarReportes() {
    modelRecordatorios.setRowCount(0);
    Date inicio = (Date) spFechaInicio.getValue();
    Date fin = (Date) spFechaFin.getValue();
    if (inicio == null || fin == null) return;

    try {
        List<Recordatorio> recordatorios = controller.obtenerRecordatorios(inicio, fin);
        
        // Validación crítica: si es null, usar lista vacía
        if (recordatorios == null) {
            System.out.println("No se pudieron cargar los recordatorios (lista null)");
            JOptionPane.showMessageDialog(this, 
                "No se pudieron cargar los recordatorios. Verifique la conexión con el servidor.",
                "Error de conexión", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        for (Recordatorio r : recordatorios) {
            String leidoStr = r.isLeido() ? "Sí" : "No";
            String idCitaStr = (r.getIdCita() != null && r.getIdCita() > 0) ? String.valueOf(r.getIdCita()) : "";
            String idVacunaStr = (r.getIdVacuna() != null && r.getIdVacuna() > 0) ? String.valueOf(r.getIdVacuna()) : "";
            
            modelRecordatorios.addRow(new Object[]{
                r.getIdRecordatorio(), 
                r.getTipo(), 
                r.getMensaje(),
                new SimpleDateFormat("dd/MM/yyyy HH:mm").format(r.getFechaEnvio()),
                leidoStr, 
                r.getContadorMostrado(), 
                idCitaStr,
                idVacunaStr
            });
        }
    } catch (Exception e) {
        System.err.println("Error en cargarReportes: " + e.getMessage());
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, 
            "Error al cargar reportes: " + e.getMessage(),
            "Error", JOptionPane.ERROR_MESSAGE);
    }
}
    private void cargarConfiguraciones() {
        modelConfiguraciones.setRowCount(0);
        List<RecordatorioConfig> configs = controller.obtenerConfiguraciones();
        if (configs != null) {
            for (RecordatorioConfig c : configs) {
                modelConfiguraciones.addRow(new Object[]{
                    c.getIdConfig(), 
                    c.getTipoRecordatorio(), 
                    c.getAnticipacion(), 
                    c.getMensaje(), 
                    c.isActivo()
                });
            }
        }
    }

    private void actualizarConfiguracion() {
        int selectedRow = tblConfiguraciones.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione una configuración para actualizar");
            return;
        }

        try {
            int idConfig = (int) modelConfiguraciones.getValueAt(selectedRow, 0);
            String tipo = (String) modelConfiguraciones.getValueAt(selectedRow, 1);
            String anticipacion = (String) modelConfiguraciones.getValueAt(selectedRow, 2);
            String mensaje = (String) modelConfiguraciones.getValueAt(selectedRow, 3);
            
            boolean activo;
            Object activoObj = modelConfiguraciones.getValueAt(selectedRow, 4);
            if (activoObj instanceof Boolean) {
                activo = (Boolean) activoObj;
            } else if (activoObj instanceof String) {
                activo = "true".equalsIgnoreCase((String) activoObj) || "sí".equalsIgnoreCase((String) activoObj) || "1".equals(activoObj);
            } else {
                activo = false;
            }

            RecordatorioConfig config = new RecordatorioConfig();
            config.setIdConfig(idConfig);
            config.setTipoRecordatorio(tipo);
            config.setAnticipacion(anticipacion);
            config.setMensaje(mensaje);
            config.setActivo(activo);

            if (controller.actualizarConfiguracion(config)) {
                JOptionPane.showMessageDialog(this, "Configuración actualizada.");
                cargarConfiguraciones();
            } else {
                JOptionPane.showMessageDialog(this, "Error al actualizar configuración.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void nuevaConfiguracion() {
        FrmNuevaConfiguracionRecordatorioDialog dialog = new FrmNuevaConfiguracionRecordatorioDialog(SwingUtilities.getWindowAncestor(this));
        if (dialog.showDialog()) {
            JOptionPane.showMessageDialog(this, "Configuración creada exitosamente.");
            cargarConfiguraciones();
        }
    }
}