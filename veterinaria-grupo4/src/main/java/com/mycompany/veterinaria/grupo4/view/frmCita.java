//package com.mycompany.veterinaria.grupo4.view;
//
//import com.mycompany.veterinaria.grupo4.model.entity.Cliente;
//import com.mycompany.veterinaria.grupo4.model.entity.Mascota;
//import com.mycompany.veterinaria.grupo4.model.entity.Servicio;
//import com.mycompany.veterinaria.grupo4.model.entity.Cita;
//import com.mycompany.veterinaria.grupo4.service.ClienteService;
//import com.mycompany.veterinaria.grupo4.service.MascotaService;
//import com.mycompany.veterinaria.grupo4.service.ServicioService;
//import com.mycompany.veterinaria.grupo4.service.CitaService;
//import org.springframework.web.client.RestTemplate;
//
//import javax.swing.*;
//import javax.swing.table.DefaultTableModel;
//import java.awt.*;
//import java.text.SimpleDateFormat;
//import java.util.Date;
//import java.util.List;
//
//public class frmCita extends JFrame {
//    private RestTemplate restTemplate = new RestTemplate();
//    private String apiBaseUrl = "http://localhost:8080/api";
//    
//    private ClienteService clienteService = new ClienteService();
//    private MascotaService mascotaService = new MascotaService();
//    private ServicioService servicioService = new ServicioService();
//    private CitaService citaService = new CitaService();
//    
//    private JComboBox<Cliente> cmbCliente;
//    private JComboBox<Servicio> cmbServicio;
//    private JComboBox<String> cmbEstado;
//    private JComboBox<String> cmbTipoVacuna;
//    private JTextField txtCedula, txtNombreCliente, txtApellidoCliente;
//    private JTextField txtObservaciones;
//    private JSpinner spnFechaCita, spnHoraCita;
//    private JTable tblCitas, tblMascotasDisponibles, tblMascotasAsignadas;
//    private DefaultTableModel modelCitas, modelMascotasDisponibles, modelMascotasAsignadas;
//    private JButton btnAgendar, btnActualizar, btnCancelar, btnNuevo, btnBuscar, btnLimpiar;
//    private JButton btnAsignar, btnQuitar, btnCalendario;
//    
//    private int idCitaSeleccionada = 0;
//    private int idClienteSeleccionado = 0;
//    private String nombreUsuario;
//    private int idUsuario;
//    
//    public frmCita(String nombreUsuario, int idUsuario) {
//        this.nombreUsuario = nombreUsuario;
//        this.idUsuario = idUsuario;
//        initComponents();
//        cargarDatosIniciales();
//        setLocationRelativeTo(null);
//    }
//    
//    private void initComponents() {
//        setTitle("Gestión de Citas Veterinarias");
//        setSize(1100, 750);
//        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
//        setLayout(new BorderLayout());
//        
//        // Panel principal
//        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
//        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
//        
//        // Panel de datos de la cita
//        JPanel datosPanel = new JPanel(new GridBagLayout());
//        datosPanel.setBorder(BorderFactory.createTitledBorder("Datos de la Cita"));
//        GridBagConstraints gbc = new GridBagConstraints();
//        gbc.insets = new Insets(5, 5, 5, 5);
//        gbc.fill = GridBagConstraints.HORIZONTAL;
//        
//        int row = 0;
//        gbc.gridx = 0; gbc.gridy = row;
//        datosPanel.add(new JLabel("Cliente:"), gbc);
//        gbc.gridx = 1;
//        cmbCliente = new JComboBox<>();
//        cmbCliente.setPreferredSize(new Dimension(200, 25));
//        datosPanel.add(cmbCliente, gbc);
//        
//        gbc.gridx = 2;
//        datosPanel.add(new JLabel("Cédula:"), gbc);
//        gbc.gridx = 3;
//        txtCedula = new JTextField(15);
//        txtCedula.setEditable(false);
//        datosPanel.add(txtCedula, gbc);
//        
//        row++;
//        gbc.gridx = 0; gbc.gridy = row;
//        datosPanel.add(new JLabel("Nombre:"), gbc);
//        gbc.gridx = 1;
//        txtNombreCliente = new JTextField(15);
//        txtNombreCliente.setEditable(false);
//        datosPanel.add(txtNombreCliente, gbc);
//        
//        gbc.gridx = 2;
//        datosPanel.add(new JLabel("Apellido:"), gbc);
//        gbc.gridx = 3;
//        txtApellidoCliente = new JTextField(15);
//        txtApellidoCliente.setEditable(false);
//        datosPanel.add(txtApellidoCliente, gbc);
//        
//        row++;
//        gbc.gridx = 0; gbc.gridy = row;
//        datosPanel.add(new JLabel("Servicio:"), gbc);
//        gbc.gridx = 1;
//        cmbServicio = new JComboBox<>();
//        cmbServicio.setPreferredSize(new Dimension(200, 25));
//        datosPanel.add(cmbServicio, gbc);
//        
//        gbc.gridx = 2;
//        datosPanel.add(new JLabel("Tipo Vacuna:"), gbc);
//        gbc.gridx = 3;
//        cmbTipoVacuna = new JComboBox<>(new String[]{"Antirrábica", "Polivalente", "Leucemia Felina", "Moquillo", "Parvovirus"});
//        cmbTipoVacuna.setVisible(false);
//        datosPanel.add(cmbTipoVacuna, gbc);
//        
//        row++;
//        gbc.gridx = 0; gbc.gridy = row;
//        datosPanel.add(new JLabel("Fecha:"), gbc);
//        gbc.gridx = 1;
//        spnFechaCita = new JSpinner(new SpinnerDateModel());
//        JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(spnFechaCita, "dd/MM/yyyy");
//        spnFechaCita.setEditor(dateEditor);
//        datosPanel.add(spnFechaCita, gbc);
//        
//        gbc.gridx = 2;
//        datosPanel.add(new JLabel("Hora:"), gbc);
//        gbc.gridx = 3;
//        spnHoraCita = new JSpinner(new SpinnerDateModel());
//        JSpinner.DateEditor timeEditor = new JSpinner.DateEditor(spnHoraCita, "HH:mm");
//        spnHoraCita.setEditor(timeEditor);
//        datosPanel.add(spnHoraCita, gbc);
//        
//        row++;
//        gbc.gridx = 0; gbc.gridy = row;
//        datosPanel.add(new JLabel("Estado:"), gbc);
//        gbc.gridx = 1;
//        cmbEstado = new JComboBox<>(new String[]{"PENDIENTE", "COMPLETADA", "CANCELADA"});
//        datosPanel.add(cmbEstado, gbc);
//        
//        gbc.gridx = 2;
//        gbc.gridwidth = 2;
//        datosPanel.add(new JLabel("Observaciones:"), gbc);
//        
//        row++;
//        gbc.gridx = 0; gbc.gridy = row;
//        gbc.gridwidth = 4;
//        txtObservaciones = new JTextField(40);
//        datosPanel.add(txtObservaciones, gbc);
//        
//        mainPanel.add(datosPanel, BorderLayout.NORTH);
//        
//        // Panel de asignación de mascotas
//        JPanel mascotasPanel = new JPanel(new GridLayout(1, 2, 10, 10));
//        mascotasPanel.setBorder(BorderFactory.createTitledBorder("Asignación de Mascotas"));
//        
//        // Mascotas disponibles
//        JPanel disponiblesPanel = new JPanel(new BorderLayout());
//        disponiblesPanel.add(new JLabel("Mascotas del Cliente:"), BorderLayout.NORTH);
//        modelMascotasDisponibles = new DefaultTableModel(new String[]{"ID", "Nombre", "Especie", "Raza"}, 0);
//        tblMascotasDisponibles = new JTable(modelMascotasDisponibles);
//        tblMascotasDisponibles.getColumnModel().getColumn(0).setMinWidth(0);
//        tblMascotasDisponibles.getColumnModel().getColumn(0).setMaxWidth(0);
//        disponiblesPanel.add(new JScrollPane(tblMascotasDisponibles), BorderLayout.CENTER);
//        
//        // Botones de asignación
//        JPanel botonesAsignacion = new JPanel(new GridLayout(2, 1, 5, 5));
//        btnAsignar = new JButton("→ Asignar →");
//        btnQuitar = new JButton("← Quitar ←");
//        botonesAsignacion.add(btnAsignar);
//        botonesAsignacion.add(btnQuitar);
//        
//        // Mascotas asignadas
//        JPanel asignadasPanel = new JPanel(new BorderLayout());
//        asignadasPanel.add(new JLabel("Mascotas Asignadas a la Cita:"), BorderLayout.NORTH);
//        modelMascotasAsignadas = new DefaultTableModel(new String[]{"ID", "Nombre", "Especie", "Raza"}, 0);
//        tblMascotasAsignadas = new JTable(modelMascotasAsignadas);
//        tblMascotasAsignadas.getColumnModel().getColumn(0).setMinWidth(0);
//        tblMascotasAsignadas.getColumnModel().getColumn(0).setMaxWidth(0);
//        asignadasPanel.add(new JScrollPane(tblMascotasAsignadas), BorderLayout.CENTER);
//        
//        mascotasPanel.add(disponiblesPanel);
//        mascotasPanel.add(botonesAsignacion);
//        mascotasPanel.add(asignadasPanel);
//        
//        mainPanel.add(mascotasPanel, BorderLayout.CENTER);
//        
//        // Panel de botones
//        JPanel botonesPanel = new JPanel(new FlowLayout());
//        btnAgendar = new JButton("Agendar Cita");
//        btnActualizar = new JButton("Actualizar");
//        btnCancelar = new JButton("Cancelar Cita");
//        btnNuevo = new JButton("Nuevo");
//        btnCalendario = new JButton("Ver Calendario");
//        btnBuscar = new JButton("Buscar");
//        btnLimpiar = new JButton("Limpiar Filtros");
//        
//        botonesPanel.add(btnAgendar);
//        botonesPanel.add(btnActualizar);
//        botonesPanel.add(btnCancelar);
//        botonesPanel.add(btnNuevo);
//        botonesPanel.add(btnCalendario);
//        botonesPanel.add(btnBuscar);
//        botonesPanel.add(btnLimpiar);
//        
//        mainPanel.add(botonesPanel, BorderLayout.SOUTH);
//        
//        // Tabla de citas
//        JPanel citasPanel = new JPanel(new BorderLayout());
//        citasPanel.setBorder(BorderFactory.createTitledBorder("Listado de Citas"));
//        modelCitas = new DefaultTableModel(new String[]{"ID", "Cliente", "Mascota", "Servicio", "Fecha", "Estado"}, 0);
//        tblCitas = new JTable(modelCitas);
//        citasPanel.add(new JScrollPane(tblCitas), BorderLayout.CENTER);
//        
//        mainPanel.add(citasPanel, BorderLayout.SOUTH);
//        
//        add(mainPanel);
//        
//        // Eventos
//        cmbCliente.addActionListener(e -> cargarMascotasCliente());
//        cmbServicio.addActionListener(e -> {
//            Servicio s = (Servicio) cmbServicio.getSelectedItem();
//            if (s != null && "VACUNA".equalsIgnoreCase(s.getNombreServicio())) {
//                cmbTipoVacuna.setVisible(true);
//            } else {
//                cmbTipoVacuna.setVisible(false);
//            }
//        });
//        
//        btnAsignar.addActionListener(e -> asignarMascota());
//        btnQuitar.addActionListener(e -> quitarMascota());
//        btnAgendar.addActionListener(e -> agendarCita());
//        btnActualizar.addActionListener(e -> actualizarCita());
//        btnCancelar.addActionListener(e -> cancelarCita());
//        btnNuevo.addActionListener(e -> limpiarFormulario());
//        btnCalendario.addActionListener(e -> abrirCalendario());
//        btnBuscar.addActionListener(e -> buscarCitas());
//        btnLimpiar.addActionListener(e -> cargarCitas());
//        tblCitas.getSelectionModel().addListSelectionListener(e -> cargarCitaSeleccionada());
//    }
//    
//    private void cargarDatosIniciales() {
//        cargarClientes();
//        cargarServicios();
//        cargarCitas();
//    }
//    
//    private void cargarClientes() {
//        List<Cliente> clientes = clienteService.listarTodos();
//        cmbCliente.removeAllItems();
//        if (clientes != null) {
//            for (Cliente c : clientes) {
//                cmbCliente.addItem(c);
//            }
//        }
//    }
//    
//    private void cargarServicios() {
//        List<Servicio> servicios = servicioService.listarActivos();
//        cmbServicio.removeAllItems();
//        if (servicios != null) {
//            for (Servicio s : servicios) {
//                cmbServicio.addItem(s);
//            }
//        }
//    }
//    
//    private void cargarMascotasCliente() {
//        Cliente cliente = (Cliente) cmbCliente.getSelectedItem();
//        if (cliente != null) {
//            idClienteSeleccionado = cliente.getIdCliente();
//            txtCedula.setText(cliente.getCedula());
//            txtNombreCliente.setText(cliente.getNombre());
//            txtApellidoCliente.setText(cliente.getApellido());
//            
//            List<Mascota> mascotas = mascotaService.listarPorCliente(idClienteSeleccionado);
//            modelMascotasDisponibles.setRowCount(0);
//            if (mascotas != null) {
//                for (Mascota m : mascotas) {
//                    boolean yaAsignada = false;
//                    for (int i = 0; i < modelMascotasAsignadas.getRowCount(); i++) {
//                        if ((int) modelMascotasAsignadas.getValueAt(i, 0) == m.getIdMascota()) {
//                            yaAsignada = true;
//                            break;
//                        }
//                    }
//                    if (!yaAsignada) {
//                        modelMascotasDisponibles.addRow(new Object[]{
//                            m.getIdMascota(), m.getNombre(), m.getEspecie(), m.getRaza()
//                        });
//                    }
//                }
//            }
//        }
//    }
//    
//    private void asignarMascota() {
//        int row = tblMascotasDisponibles.getSelectedRow();
//        if (row >= 0) {
//            Object[] fila = {
//                modelMascotasDisponibles.getValueAt(row, 0),
//                modelMascotasDisponibles.getValueAt(row, 1),
//                modelMascotasDisponibles.getValueAt(row, 2),
//                modelMascotasDisponibles.getValueAt(row, 3)
//            };
//            modelMascotasAsignadas.addRow(fila);
//            modelMascotasDisponibles.removeRow(row);
//        }
//    }
//    
//    private void quitarMascota() {
//        int row = tblMascotasAsignadas.getSelectedRow();
//        if (row >= 0) {
//            Object[] fila = {
//                modelMascotasAsignadas.getValueAt(row, 0),
//                modelMascotasAsignadas.getValueAt(row, 1),
//                modelMascotasAsignadas.getValueAt(row, 2),
//                modelMascotasAsignadas.getValueAt(row, 3)
//            };
//            modelMascotasDisponibles.addRow(fila);
//            modelMascotasAsignadas.removeRow(row);
//        }
//    }
//    
//    private void agendarCita() {
//        if (cmbCliente.getSelectedItem() == null) {
//            JOptionPane.showMessageDialog(this, "Seleccione un cliente", "Error", JOptionPane.ERROR_MESSAGE);
//            return;
//        }
//        if (cmbServicio.getSelectedItem() == null) {
//            JOptionPane.showMessageDialog(this, "Seleccione un servicio", "Error", JOptionPane.ERROR_MESSAGE);
//            return;
//        }
//        if (modelMascotasAsignadas.getRowCount() == 0) {
//            JOptionPane.showMessageDialog(this, "Asigne al menos una mascota", "Error", JOptionPane.ERROR_MESSAGE);
//            return;
//        }
//        
//        try {
//            Cita cita = new Cita();
//            cita.setIdCliente(((Cliente) cmbCliente.getSelectedItem()).getIdCliente());
//            cita.setIdServicio(((Servicio) cmbServicio.getSelectedItem()).getIdServicio());
//            
//            Date fecha = (Date) spnFechaCita.getValue();
//            Date hora = (Date) spnHoraCita.getValue();
//            java.util.Calendar calFecha = java.util.Calendar.getInstance();
//            java.util.Calendar calHora = java.util.Calendar.getInstance();
//            calFecha.setTime(fecha);
//            calHora.setTime(hora);
//            calFecha.set(java.util.Calendar.HOUR_OF_DAY, calHora.get(java.util.Calendar.HOUR_OF_DAY));
//            calFecha.set(java.util.Calendar.MINUTE, calHora.get(java.util.Calendar.MINUTE));
//            cita.setFechaHora(calFecha.getTime());
//            
//            String observaciones = txtObservaciones.getText();
//            if (cmbTipoVacuna.isVisible() && cmbTipoVacuna.getSelectedItem() != null) {
//                observaciones = "Tipo de vacuna: " + cmbTipoVacuna.getSelectedItem() + ". " + observaciones;
//            }
//            cita.setObservaciones(observaciones);
//            cita.setEstado("PENDIENTE");
//            
//            StringBuilder idsMascotas = new StringBuilder();
//            for (int i = 0; i < modelMascotasAsignadas.getRowCount(); i++) {
//                if (i > 0) idsMascotas.append(",");
//                idsMascotas.append(modelMascotasAsignadas.getValueAt(i, 0));
//            }
//            
//            int idCita = citaService.agendar(cita, idsMascotas.toString());
//            if (idCita > 0) {
//                JOptionPane.showMessageDialog(this, "Cita agendada correctamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
//                limpiarFormulario();
//                cargarCitas();
//            } else {
//                JOptionPane.showMessageDialog(this, "Error al agendar cita", "Error", JOptionPane.ERROR_MESSAGE);
//            }
//        } catch (Exception ex) {
//            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
//        }
//    }
//    
//    private void actualizarCita() {
//        if (idCitaSeleccionada == 0) {
//            JOptionPane.showMessageDialog(this, "Seleccione una cita para actualizar", "Error", JOptionPane.ERROR_MESSAGE);
//            return;
//        }
//        
//        try {
//            Cita cita = new Cita();
//            cita.setIdCita(idCitaSeleccionada);
//            cita.setIdCliente(((Cliente) cmbCliente.getSelectedItem()).getIdCliente());
//            cita.setIdServicio(((Servicio) cmbServicio.getSelectedItem()).getIdServicio());
//            
//            Date fecha = (Date) spnFechaCita.getValue();
//            Date hora = (Date) spnHoraCita.getValue();
//            java.util.Calendar calFecha = java.util.Calendar.getInstance();
//            java.util.Calendar calHora = java.util.Calendar.getInstance();
//            calFecha.setTime(fecha);
//            calHora.setTime(hora);
//            calFecha.set(java.util.Calendar.HOUR_OF_DAY, calHora.get(java.util.Calendar.HOUR_OF_DAY));
//            calFecha.set(java.util.Calendar.MINUTE, calHora.get(java.util.Calendar.MINUTE));
//            cita.setFechaHora(calFecha.getTime());
//            
//            cita.setObservaciones(txtObservaciones.getText());
//            cita.setEstado((String) cmbEstado.getSelectedItem());
//            
//            StringBuilder idsMascotas = new StringBuilder();
//            for (int i = 0; i < modelMascotasAsignadas.getRowCount(); i++) {
//                if (i > 0) idsMascotas.append(",");
//                idsMascotas.append(modelMascotasAsignadas.getValueAt(i, 0));
//            }
//            
//            if (citaService.actualizar(cita, idsMascotas.toString())) {
//                JOptionPane.showMessageDialog(this, "Cita actualizada correctamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
//                limpiarFormulario();
//                cargarCitas();
//            }
//        } catch (Exception ex) {
//            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
//        }
//    }
//    
//    private void cancelarCita() {
//        if (idCitaSeleccionada == 0) {
//            JOptionPane.showMessageDialog(this, "Seleccione una cita para cancelar", "Error", JOptionPane.ERROR_MESSAGE);
//            return;
//        }
//        
//        String motivo = JOptionPane.showInputDialog(this, "Ingrese el motivo de cancelación:");
//        if (motivo != null && !motivo.trim().isEmpty()) {
//            if (citaService.cancelar(idCitaSeleccionada, motivo)) {
//                JOptionPane.showMessageDialog(this, "Cita cancelada correctamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
//                limpiarFormulario();
//                cargarCitas();
//            }
//        }
//    }
//    
//    private void cargarCitas() {
//        modelCitas.setRowCount(0);
//        List<Cita> citas = citaService.listarTodas();
//        if (citas != null) {
//            for (Cita c : citas) {
//                modelCitas.addRow(new Object[]{
//                    c.getIdCita(),
//                    "ID: " + c.getIdCliente(),
//                    "-",
//                    "ID: " + c.getIdServicio(),
//                    new SimpleDateFormat("dd/MM/yyyy HH:mm").format(c.getFechaHora()),
//                    c.getEstado()
//                });
//            }
//        }
//    }
//    
//    private void buscarCitas() {
//        modelCitas.setRowCount(0);
//        Date fecha = (Date) spnFechaCita.getValue();
//        List<Cita> citas = citaService.listarPorFecha(fecha);
//        if (citas != null) {
//            for (Cita c : citas) {
//                modelCitas.addRow(new Object[]{
//                    c.getIdCita(),
//                    "ID: " + c.getIdCliente(),
//                    "-",
//                    "ID: " + c.getIdServicio(),
//                    new SimpleDateFormat("dd/MM/yyyy HH:mm").format(c.getFechaHora()),
//                    c.getEstado()
//                });
//            }
//        }
//    }
//    
//    private void cargarCitaSeleccionada() {
//        int row = tblCitas.getSelectedRow();
//        if (row >= 0) {
//            idCitaSeleccionada = (int) modelCitas.getValueAt(row, 0);
//            // Aquí cargarías los detalles completos de la cita seleccionada
//        }
//    }
//    
//    private void limpiarFormulario() {
//        idCitaSeleccionada = 0;
//        cmbCliente.setSelectedIndex(-1);
//        cmbServicio.setSelectedIndex(-1);
//        cmbEstado.setSelectedIndex(0);
//        spnFechaCita.setValue(new Date());
//        spnHoraCita.setValue(new Date());
//        txtObservaciones.setText("");
//        txtCedula.setText("");
//        txtNombreCliente.setText("");
//        txtApellidoCliente.setText("");
//        modelMascotasDisponibles.setRowCount(0);
//        modelMascotasAsignadas.setRowCount(0);
//        cmbTipoVacuna.setVisible(false);
//    }
//    
//    private void abrirCalendario() {
//        frmCalendarioCitasMedicas calendario = new frmCalendarioCitasMedicas(nombreUsuario, idUsuario);
//        calendario.setVisible(true);
//    }
//}