package com.mycompany.veterinaria.grupo4.view;

import com.mycompany.veterinaria.grupo4.model.entity.*;
import com.mycompany.veterinaria.grupo4.service.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class frmAtencionMedica extends JFrame {
    private ServicioService servicioService = new ServicioService();
    private VeterinarioService veterinarioService = new VeterinarioService();
    private CitaService citaService = new CitaService();
    private MascotaService mascotaService = new MascotaService();
    private AtencionMedicaService atencionService = new AtencionMedicaService();
    private MedicamentoService medicamentoService = new MedicamentoService();
    private InstrumentoMedicoService instrumentoService = new InstrumentoMedicoService();
    private FichaMedicaService fichaMedicaService = new FichaMedicaService();
    private HistorialService historialService = new HistorialService();
    private FacturaService facturaService = new FacturaService();
    
    private JComboBox<Servicio> cmbServicio;
    private JComboBox<Veterinario> cmbVeterinario;
    private JComboBox<Cita> cmbCita;
    private JComboBox<Cliente> cmbCliente;
    private JSpinner spnFecha;
    private JTextArea txtDiagnostico, txtTratamiento, txtObservaciones;
    private JTable tblMascotasPendientes, tblMedicamentos, tblInstrumentos;
    private DefaultTableModel modelMascotas, modelMedicamentos, modelInstrumentos;
    private JButton btnAgregarMedicamento, btnEliminarMedicamento;
    private JButton btnAgregarInstrumento, btnEliminarInstrumento;
    private JButton btnGuardar, btnNuevo, btnCancelar, btnHistorial;
    
    private int idCitaSeleccionada = 0;
    private int idMascotaSeleccionada = 0;
    private int idVeterinarioSeleccionado = 0;
    private String nombreUsuario;
    private int idUsuario;
    
    // Panel de vacunas (visible solo para servicio VACUNA)
    private JPanel pnlVacuna;
    private JComboBox<String> cmbVacuna;
    private JTextField txtNombreVacuna, txtDescripcionVacuna, txtPeriodoMeses;
    private JSpinner spnFechaAplicacion, spnFechaProxima;
    
    public frmAtencionMedica(String nombreUsuario, int idUsuario) {
        this.nombreUsuario = nombreUsuario;
        this.idUsuario = idUsuario;
        initComponents();
        cargarDatosIniciales();
        setLocationRelativeTo(null);
    }
    
    private void initComponents() {
        setTitle("Atención Médica");
        setSize(1200, 750);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Panel de selección
        JPanel seleccionPanel = new JPanel(new GridBagLayout());
        seleccionPanel.setBorder(BorderFactory.createTitledBorder("Selección de Servicio"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        int row = 0;
        gbc.gridx = 0; gbc.gridy = row;
        seleccionPanel.add(new JLabel("Servicio:"), gbc);
        gbc.gridx = 1;
        cmbServicio = new JComboBox<>();
        cmbServicio.setPreferredSize(new Dimension(200, 25));
        seleccionPanel.add(cmbServicio, gbc);
        
        gbc.gridx = 2;
        seleccionPanel.add(new JLabel("Veterinario:"), gbc);
        gbc.gridx = 3;
        cmbVeterinario = new JComboBox<>();
        cmbVeterinario.setPreferredSize(new Dimension(200, 25));
        seleccionPanel.add(cmbVeterinario, gbc);
        
        row++;
        gbc.gridx = 0; gbc.gridy = row;
        seleccionPanel.add(new JLabel("Cita:"), gbc);
        gbc.gridx = 1;
        cmbCita = new JComboBox<>();
        cmbCita.setPreferredSize(new Dimension(200, 25));
        seleccionPanel.add(cmbCita, gbc);
        
        gbc.gridx = 2;
        seleccionPanel.add(new JLabel("Cliente:"), gbc);
        gbc.gridx = 3;
        cmbCliente = new JComboBox<>();
        cmbCliente.setPreferredSize(new Dimension(200, 25));
        seleccionPanel.add(cmbCliente, gbc);
        
        row++;
        gbc.gridx = 0; gbc.gridy = row;
        seleccionPanel.add(new JLabel("Fecha:"), gbc);
        gbc.gridx = 1;
        spnFecha = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(spnFecha, "dd/MM/yyyy HH:mm");
        spnFecha.setEditor(dateEditor);
        seleccionPanel.add(spnFecha, gbc);
        
        gbc.gridx = 2;
        btnHistorial = new JButton("Ver Historial Médico");
        seleccionPanel.add(btnHistorial, gbc);
        
        mainPanel.add(seleccionPanel, BorderLayout.NORTH);
        
        // Panel de mascotas pendientes
        JPanel mascotasPanel = new JPanel(new BorderLayout());
        mascotasPanel.setBorder(BorderFactory.createTitledBorder("Mascotas Pendientes de la Cita"));
        modelMascotas = new DefaultTableModel(new String[]{"ID", "Nombre", "Especie", "Raza"}, 0);
        tblMascotasPendientes = new JTable(modelMascotas);
        tblMascotasPendientes.getColumnModel().getColumn(0).setMinWidth(0);
        tblMascotasPendientes.getColumnModel().getColumn(0).setMaxWidth(0);
        mascotasPanel.add(new JScrollPane(tblMascotasPendientes), BorderLayout.CENTER);
        mainPanel.add(mascotasPanel, BorderLayout.WEST);
        
        // Panel de diagnóstico y tratamiento
        JPanel diagnosticoPanel = new JPanel(new GridBagLayout());
        diagnosticoPanel.setBorder(BorderFactory.createTitledBorder("Diagnóstico y Tratamiento"));
        GridBagConstraints gbcD = new GridBagConstraints();
        gbcD.insets = new Insets(5, 5, 5, 5);
        gbcD.fill = GridBagConstraints.BOTH;
        
        gbcD.gridx = 0; gbcD.gridy = 0;
        diagnosticoPanel.add(new JLabel("Diagnóstico:"), gbcD);
        gbcD.gridx = 1;
        txtDiagnostico = new JTextArea(3, 30);
        diagnosticoPanel.add(new JScrollPane(txtDiagnostico), gbcD);
        
        gbcD.gridx = 0; gbcD.gridy = 1;
        diagnosticoPanel.add(new JLabel("Tratamiento:"), gbcD);
        gbcD.gridx = 1;
        txtTratamiento = new JTextArea(3, 30);
        diagnosticoPanel.add(new JScrollPane(txtTratamiento), gbcD);
        
        gbcD.gridx = 0; gbcD.gridy = 2;
        diagnosticoPanel.add(new JLabel("Observaciones:"), gbcD);
        gbcD.gridx = 1;
        txtObservaciones = new JTextArea(2, 30);
        diagnosticoPanel.add(new JScrollPane(txtObservaciones), gbcD);
        
        mainPanel.add(diagnosticoPanel, BorderLayout.CENTER);
        
        // Panel de medicamentos
        JPanel medicamentosPanel = new JPanel(new BorderLayout());
        medicamentosPanel.setBorder(BorderFactory.createTitledBorder("Medicamentos Recetados"));
        modelMedicamentos = new DefaultTableModel(new String[]{"ID", "Medicamento", "Dosis", "Frecuencia", "Duración"}, 0);
        tblMedicamentos = new JTable(modelMedicamentos);
        tblMedicamentos.getColumnModel().getColumn(0).setMinWidth(0);
        tblMedicamentos.getColumnModel().getColumn(0).setMaxWidth(0);
        medicamentosPanel.add(new JScrollPane(tblMedicamentos), BorderLayout.CENTER);
        
        JPanel medicamentosBotones = new JPanel(new FlowLayout());
        btnAgregarMedicamento = new JButton("Agregar Medicamento");
        btnEliminarMedicamento = new JButton("Eliminar Medicamento");
        medicamentosBotones.add(btnAgregarMedicamento);
        medicamentosBotones.add(btnEliminarMedicamento);
        medicamentosPanel.add(medicamentosBotones, BorderLayout.SOUTH);
        
        // Panel de instrumentos
        JPanel instrumentosPanel = new JPanel(new BorderLayout());
        instrumentosPanel.setBorder(BorderFactory.createTitledBorder("Instrumentos Usados"));
        modelInstrumentos = new DefaultTableModel(new String[]{"ID", "Instrumento", "Costo"}, 0);
        tblInstrumentos = new JTable(modelInstrumentos);
        tblInstrumentos.getColumnModel().getColumn(0).setMinWidth(0);
        tblInstrumentos.getColumnModel().getColumn(0).setMaxWidth(0);
        instrumentosPanel.add(new JScrollPane(tblInstrumentos), BorderLayout.CENTER);
        
        JPanel instrumentosBotones = new JPanel(new FlowLayout());
        btnAgregarInstrumento = new JButton("Agregar Instrumento");
        btnEliminarInstrumento = new JButton("Eliminar Instrumento");
        instrumentosBotones.add(btnAgregarInstrumento);
        instrumentosBotones.add(btnEliminarInstrumento);
        instrumentosPanel.add(instrumentosBotones, BorderLayout.SOUTH);
        
        // Panel de vacunas (oculto inicialmente)
        pnlVacuna = new JPanel(new GridBagLayout());
        pnlVacuna.setBorder(BorderFactory.createTitledBorder("Información de Vacuna"));
        pnlVacuna.setVisible(false);
        GridBagConstraints gbcV = new GridBagConstraints();
        gbcV.insets = new Insets(5, 5, 5, 5);
        gbcV.fill = GridBagConstraints.HORIZONTAL;
        
        int vRow = 0;
        gbcV.gridx = 0; gbcV.gridy = vRow;
        pnlVacuna.add(new JLabel("Vacuna:"), gbcV);
        gbcV.gridx = 1;
        cmbVacuna = new JComboBox<>();
        pnlVacuna.add(cmbVacuna, gbcV);
        
        vRow++;
        gbcV.gridx = 0; gbcV.gridy = vRow;
        pnlVacuna.add(new JLabel("Nombre:"), gbcV);
        gbcV.gridx = 1;
        txtNombreVacuna = new JTextField(20);
        txtNombreVacuna.setEditable(false);
        pnlVacuna.add(txtNombreVacuna, gbcV);
        
        vRow++;
        gbcV.gridx = 0; gbcV.gridy = vRow;
        pnlVacuna.add(new JLabel("Descripción:"), gbcV);
        gbcV.gridx = 1;
        txtDescripcionVacuna = new JTextField(30);
        txtDescripcionVacuna.setEditable(false);
        pnlVacuna.add(txtDescripcionVacuna, gbcV);
        
        vRow++;
        gbcV.gridx = 0; gbcV.gridy = vRow;
        pnlVacuna.add(new JLabel("Período (meses):"), gbcV);
        gbcV.gridx = 1;
        txtPeriodoMeses = new JTextField(5);
        txtPeriodoMeses.setEditable(false);
        pnlVacuna.add(txtPeriodoMeses, gbcV);
        
        vRow++;
        gbcV.gridx = 0; gbcV.gridy = vRow;
        pnlVacuna.add(new JLabel("Fecha Aplicación:"), gbcV);
        gbcV.gridx = 1;
        spnFechaAplicacion = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor fechaEditor = new JSpinner.DateEditor(spnFechaAplicacion, "dd/MM/yyyy");
        spnFechaAplicacion.setEditor(fechaEditor);
        pnlVacuna.add(spnFechaAplicacion, gbcV);
        
        vRow++;
        gbcV.gridx = 0; gbcV.gridy = vRow;
        pnlVacuna.add(new JLabel("Próxima Aplicación:"), gbcV);
        gbcV.gridx = 1;
        spnFechaProxima = new JSpinner(new SpinnerDateModel());
        spnFechaProxima.setEditor(new JSpinner.DateEditor(spnFechaProxima, "dd/MM/yyyy"));
        pnlVacuna.add(spnFechaProxima, gbcV);
        
        // Panel derecho combinado
        JPanel rightPanel = new JPanel(new GridLayout(3, 1, 10, 10));
        rightPanel.add(medicamentosPanel);
        rightPanel.add(instrumentosPanel);
        rightPanel.add(pnlVacuna);
        
        mainPanel.add(rightPanel, BorderLayout.EAST);
        
        // Panel de botones
        JPanel botonesPanel = new JPanel(new FlowLayout());
        btnGuardar = new JButton("Guardar Atención Médica");
        btnNuevo = new JButton("Nuevo");
        btnCancelar = new JButton("Cancelar");
        botonesPanel.add(btnGuardar);
        botonesPanel.add(btnNuevo);
        botonesPanel.add(btnCancelar);
        
        add(mainPanel, BorderLayout.CENTER);
        add(botonesPanel, BorderLayout.SOUTH);
        
        // Eventos
        cmbServicio.addActionListener(e -> cargarVeterinariosPorServicio());
        cmbVeterinario.addActionListener(e -> cargarCitas());
        cmbCita.addActionListener(e -> cargarMascotasPendientes());
        tblMascotasPendientes.getSelectionModel().addListSelectionListener(e -> seleccionarMascota());
        btnAgregarMedicamento.addActionListener(e -> agregarMedicamento());
        btnEliminarMedicamento.addActionListener(e -> eliminarMedicamento());
        btnAgregarInstrumento.addActionListener(e -> agregarInstrumento());
        btnEliminarInstrumento.addActionListener(e -> eliminarInstrumento());
        btnGuardar.addActionListener(e -> guardarAtencion());
        btnNuevo.addActionListener(e -> limpiarFormulario());
        btnCancelar.addActionListener(e -> dispose());
        btnHistorial.addActionListener(e -> verHistorial());
    }
    
    private void cargarDatosIniciales() {
        cargarServicios();
    }
    
    private void cargarServicios() {
        List<Servicio> servicios = servicioService.listarActivos();
        cmbServicio.removeAllItems();
        if (servicios != null) {
            for (Servicio s : servicios) {
                cmbServicio.addItem(s);
            }
        }
    }
    
    private void cargarVeterinariosPorServicio() {
        Servicio servicio = (Servicio) cmbServicio.getSelectedItem();
        if (servicio != null) {
            List<Veterinario> veterinarios = veterinarioService.obtenerPorServicio(servicio.getIdServicio());
            cmbVeterinario.removeAllItems();
            if (veterinarios != null) {
                for (Veterinario v : veterinarios) {
                    cmbVeterinario.addItem(v);
                }
            }
            
            // Mostrar panel de vacunas si el servicio es VACUNA
            boolean esVacuna = "VACUNA".equalsIgnoreCase(servicio.getNombreServicio());
            pnlVacuna.setVisible(esVacuna);
            if (esVacuna) {
                cargarVacunasDisponibles();
            }
        }
    }
    
    private void cargarVacunasDisponibles() {
        cmbVacuna.removeAllItems();
        cmbVacuna.addItem("Rabia");
        cmbVacuna.addItem("Vacuna Sétuple Canina");
        cmbVacuna.addItem("Vacuna Triple Felina");
        cmbVacuna.addItem("Leucemia Felina");
        cmbVacuna.addItem("Parvovirus");
        cmbVacuna.addItem("Moquillo");
        
        cmbVacuna.addActionListener(e -> {
            String seleccion = (String) cmbVacuna.getSelectedItem();
            if (seleccion != null) {
                txtNombreVacuna.setText(seleccion);
                txtPeriodoMeses.setText("12");
                spnFechaAplicacion.setValue(new Date());
                java.util.Calendar cal = java.util.Calendar.getInstance();
                cal.add(java.util.Calendar.MONTH, 12);
                spnFechaProxima.setValue(cal.getTime());
            }
        });
    }
    
    private void cargarCitas() {
        Servicio servicio = (Servicio) cmbServicio.getSelectedItem();
        Veterinario veterinario = (Veterinario) cmbVeterinario.getSelectedItem();
        
        if (servicio != null && veterinario != null) {
            List<Cita> citas = citaService.listarPorServicioYVeterinario(
                servicio.getIdServicio(), veterinario.getIdVeterinario(), "PENDIENTE");
            cmbCita.removeAllItems();
            if (citas != null) {
                for (Cita c : citas) {
                    cmbCita.addItem(c);
                }
            }
        }
    }
    
    private void cargarMascotasPendientes() {
        Cita cita = (Cita) cmbCita.getSelectedItem();
        if (cita != null) {
            idCitaSeleccionada = cita.getIdCita();
            modelMascotas.setRowCount(0);
            
            List<Mascota> mascotas = mascotaService.listarPorCliente(cita.getCliente().getIdCliente());
            if (mascotas != null) {
                for (Mascota m : mascotas) {
                    modelMascotas.addRow(new Object[]{
                        m.getIdMascota(), m.getNombre(), m.getEspecie(), m.getRaza()
                    });
                }
            }
            
            spnFecha.setValue(cita.getFechaHora());
        }
    }
    
    private void seleccionarMascota() {
        int row = tblMascotasPendientes.getSelectedRow();
        if (row >= 0) {
            idMascotaSeleccionada = (int) modelMascotas.getValueAt(row, 0);
        }
    }
    
    private void agregarMedicamento() {
        frmBuscarMedicamento frm = new frmBuscarMedicamento();
        frm.setVisible(true);
        if (frm.isConfirmed()) {
            modelMedicamentos.addRow(new Object[]{
                frm.getMedicamento().getIdMedicamento(),
                frm.getMedicamento().getNombre(),
                frm.getDosis(),
                frm.getFrecuencia(),
                frm.getDuracion()
            });
        }
    }
    
    private void eliminarMedicamento() {
        int row = tblMedicamentos.getSelectedRow();
        if (row >= 0) {
            modelMedicamentos.removeRow(row);
        }
    }
    
    private void agregarInstrumento() {
        frmBuscarInstrumento frm = new frmBuscarInstrumento();
        frm.setVisible(true);
        if (frm.isConfirmed()) {
            modelInstrumentos.addRow(new Object[]{
                frm.getInstrumento().getIdInstrumento(),
                frm.getInstrumento().getNombre(),
                frm.getInstrumento().getCostoUso()
            });
        }
    }
    
    private void eliminarInstrumento() {
        int row = tblInstrumentos.getSelectedRow();
        if (row >= 0) {
            modelInstrumentos.removeRow(row);
        }
    }
    
    private void guardarAtencion() {
        if (idCitaSeleccionada == 0 || idMascotaSeleccionada == 0) {
            JOptionPane.showMessageDialog(this, "Seleccione una cita y una mascota", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (txtDiagnostico.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese un diagnóstico", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        try {
            AtencionMedica atencion = new AtencionMedica();
            atencion.setIdCita(idCitaSeleccionada);
            atencion.setIdMascota(idMascotaSeleccionada);
            atencion.setIdVeterinario(((Veterinario) cmbVeterinario.getSelectedItem()).getIdVeterinario());
            atencion.setFecha((Date) spnFecha.getValue());
            atencion.setDiagnostico(txtDiagnostico.getText());
            atencion.setTratamiento(txtTratamiento.getText());
            atencion.setObservaciones(txtObservaciones.getText());
            
            int idAtencion = atencionService.guardar(atencion);
            
            if (idAtencion > 0) {
                // Guardar medicamentos
                for (int i = 0; i < modelMedicamentos.getRowCount(); i++) {
                    int idMed = (int) modelMedicamentos.getValueAt(i, 0);
                    String dosis = (String) modelMedicamentos.getValueAt(i, 2);
                    String frecuencia = (String) modelMedicamentos.getValueAt(i, 3);
                    String duracion = (String) modelMedicamentos.getValueAt(i, 4);
                    medicamentoService.recetar(idAtencion, idMed, dosis, frecuencia, duracion);
                }
                
                // Guardar instrumentos
                for (int i = 0; i < modelInstrumentos.getRowCount(); i++) {
                    int idInst = (int) modelInstrumentos.getValueAt(i, 0);
                    instrumentoService.registrarUso(idAtencion, idInst);
                }
                
                // Registrar en historial
                historialService.registrar(idMascotaSeleccionada, idCitaSeleccionada, idAtencion);
                
                // Generar factura
                frmMetodoPago frmPago = new frmMetodoPago(calcularTotal());
                frmPago.setVisible(true);
                if (frmPago.isConfirmed()) {
                    facturaService.generarFacturaAtencion(idAtencion, frmPago.getMetodoPago(),
                        frmPago.getCuentaOrigen(), frmPago.getCuentaDestino());
                }
                
                JOptionPane.showMessageDialog(this, "Atención médica guardada", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                limpiarFormulario();
                cargarMascotasPendientes();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private double calcularTotal() {
        double total = 0;
        Servicio servicio = (Servicio) cmbServicio.getSelectedItem();
        if (servicio != null) total += servicio.getPrecio();
        
        for (int i = 0; i < modelMedicamentos.getRowCount(); i++) {
            // total += precio del medicamento
        }
        
        for (int i = 0; i < modelInstrumentos.getRowCount(); i++) {
            total += (double) modelInstrumentos.getValueAt(i, 2);
        }
        
        return total * 1.12; // IVA 12%
    }
    
    private void limpiarFormulario() {
        idCitaSeleccionada = 0;
        idMascotaSeleccionada = 0;
        cmbServicio.setSelectedIndex(-1);
        cmbVeterinario.removeAllItems();
        cmbCita.removeAllItems();
        txtDiagnostico.setText("");
        txtTratamiento.setText("");
        txtObservaciones.setText("");
        modelMedicamentos.setRowCount(0);
        modelInstrumentos.setRowCount(0);
        modelMascotas.setRowCount(0);
        spnFecha.setValue(new Date());
        pnlVacuna.setVisible(false);
    }
    
    private void verHistorial() {
        if (idMascotaSeleccionada > 0) {
            frmHistorialMedico historial = new frmHistorialMedico();
            historial.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "Seleccione una mascota", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}