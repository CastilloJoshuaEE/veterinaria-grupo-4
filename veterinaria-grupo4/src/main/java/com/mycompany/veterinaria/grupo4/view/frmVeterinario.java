package com.mycompany.veterinaria.grupo4.view;

import com.mycompany.veterinaria.grupo4.model.entity.Veterinario;
import com.mycompany.veterinaria.grupo4.service.VeterinarioService;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class frmVeterinario extends JFrame {
    private VeterinarioService veterinarioService = new VeterinarioService();
    
    private JTextField txtId, txtCedula, txtNombre, txtApellido, txtTelefono, txtEmail, txtDireccion, txtPagoMensual;
    private JComboBox<String> cmbEspecialidad;
    private JTable tblVeterinarios;
    private DefaultTableModel model;
    private JButton btnNuevo, btnGrabar, btnEliminar, btnBuscar, btnLimpiar;
    private JTextField txtFiltro;
    private JRadioButton rbNombre, rbEspecialidad, rbCedula;
    private boolean isDarkMode = false;
    
    public frmVeterinario(boolean darkMode) {
        this.isDarkMode = darkMode;
        initComponents();
        cargarVeterinarios();
        cargarEspecialidades();
        setLocationRelativeTo(null);
    }
    
    private void initComponents() {
        setTitle("Gestión de Veterinarios");
        setSize(950, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Panel de datos personales
        JPanel datosPanel = new JPanel(new GridBagLayout());
        datosPanel.setBorder(BorderFactory.createTitledBorder("Datos Personales"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        int row = 0;
        gbc.gridx = 0; gbc.gridy = row;
        datosPanel.add(new JLabel("ID:"), gbc);
        gbc.gridx = 1;
        txtId = new JTextField(10);
        txtId.setEditable(false);
        datosPanel.add(txtId, gbc);
        
        gbc.gridx = 2;
        datosPanel.add(new JLabel("Cédula:"), gbc);
        gbc.gridx = 3;
        txtCedula = new JTextField(15);
        datosPanel.add(txtCedula, gbc);
        
        row++;
        gbc.gridx = 0; gbc.gridy = row;
        datosPanel.add(new JLabel("Nombre:"), gbc);
        gbc.gridx = 1;
        txtNombre = new JTextField(15);
        datosPanel.add(txtNombre, gbc);
        
        gbc.gridx = 2;
        datosPanel.add(new JLabel("Apellido:"), gbc);
        gbc.gridx = 3;
        txtApellido = new JTextField(15);
        datosPanel.add(txtApellido, gbc);
        
        row++;
        gbc.gridx = 0; gbc.gridy = row;
        datosPanel.add(new JLabel("Teléfono:"), gbc);
        gbc.gridx = 1;
        txtTelefono = new JTextField(15);
        datosPanel.add(txtTelefono, gbc);
        
        gbc.gridx = 2;
        datosPanel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 3;
        txtEmail = new JTextField(15);
        datosPanel.add(txtEmail, gbc);
        
        row++;
        gbc.gridx = 0; gbc.gridy = row;
        datosPanel.add(new JLabel("Dirección:"), gbc);
        gbc.gridx = 1;
        txtDireccion = new JTextField(20);
        datosPanel.add(txtDireccion, gbc);
        
        gbc.gridx = 2;
        datosPanel.add(new JLabel("Pago Mensual:"), gbc);
        gbc.gridx = 3;
        txtPagoMensual = new JTextField(10);
        datosPanel.add(txtPagoMensual, gbc);
        
        row++;
        gbc.gridx = 0; gbc.gridy = row;
        datosPanel.add(new JLabel("Especialidad:"), gbc);
        gbc.gridx = 1;
        cmbEspecialidad = new JComboBox<>();
        cmbEspecialidad.setPreferredSize(new Dimension(150, 25));
        datosPanel.add(cmbEspecialidad, gbc);
        
        mainPanel.add(datosPanel, BorderLayout.NORTH);
        
        // Panel de botones de acción
        JPanel accionesPanel = new JPanel(new FlowLayout());
        btnNuevo = new JButton("Nuevo");
        btnGrabar = new JButton("Grabar");
        btnEliminar = new JButton("Eliminar");
        accionesPanel.add(btnNuevo);
        accionesPanel.add(btnGrabar);
        accionesPanel.add(btnEliminar);
        mainPanel.add(accionesPanel, BorderLayout.CENTER);
        
        // Panel de búsqueda
        JPanel busquedaPanel = new JPanel(new FlowLayout());
        busquedaPanel.setBorder(BorderFactory.createTitledBorder("Búsqueda"));
        rbNombre = new JRadioButton("Nombre", true);
        rbEspecialidad = new JRadioButton("Especialidad");
        rbCedula = new JRadioButton("Cédula");
        ButtonGroup bg = new ButtonGroup();
        bg.add(rbNombre);
        bg.add(rbEspecialidad);
        bg.add(rbCedula);
        
        txtFiltro = new JTextField(20);
        btnBuscar = new JButton("Buscar");
        btnLimpiar = new JButton("Limpiar");
        
        busquedaPanel.add(rbNombre);
        busquedaPanel.add(rbEspecialidad);
        busquedaPanel.add(rbCedula);
        busquedaPanel.add(txtFiltro);
        busquedaPanel.add(btnBuscar);
        busquedaPanel.add(btnLimpiar);
        mainPanel.add(busquedaPanel, BorderLayout.SOUTH);
        
        // Tabla de veterinarios
        JPanel tablaPanel = new JPanel(new BorderLayout());
        tablaPanel.setBorder(BorderFactory.createTitledBorder("Listado de Veterinarios"));
        model = new DefaultTableModel(new String[]{"ID", "Cédula", "Nombre", "Apellido", "Teléfono", "Especialidad", "Pago Mensual"}, 0);
        tblVeterinarios = new JTable(model);
        tblVeterinarios.getColumnModel().getColumn(0).setMinWidth(0);
        tblVeterinarios.getColumnModel().getColumn(0).setMaxWidth(0);
        tablaPanel.add(new JScrollPane(tblVeterinarios), BorderLayout.CENTER);
        mainPanel.add(tablaPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
        
        // Eventos
        btnNuevo.addActionListener(e -> nuevo());
        btnGrabar.addActionListener(e -> grabar());
        btnEliminar.addActionListener(e -> eliminar());
        btnBuscar.addActionListener(e -> buscar());
        btnLimpiar.addActionListener(e -> cargarVeterinarios());
        tblVeterinarios.getSelectionModel().addListSelectionListener(e -> cargarSeleccionado());
        
        aplicarTema(isDarkMode);
    }
    
    private void cargarEspecialidades() {
        cmbEspecialidad.addItem("Medicina General");
        cmbEspecialidad.addItem("Cirugía Veterinaria");
        cmbEspecialidad.addItem("Dermatología");
        cmbEspecialidad.addItem("Cardiología");
        cmbEspecialidad.addItem("Odontología");
        cmbEspecialidad.addItem("Oftalmología");
        cmbEspecialidad.addItem("Neurología");
        cmbEspecialidad.addItem("Urgencias");
        cmbEspecialidad.addItem("Nutrición");
        cmbEspecialidad.addItem("Vacunación y Desparasitación");
    }
    
    private void cargarVeterinarios() {
        model.setRowCount(0);
        List<Veterinario> lista = veterinarioService.listarTodos();
        if (lista != null) {
            for (Veterinario v : lista) {
                model.addRow(new Object[]{
                    v.getIdVeterinario(),
                    v.getCedula(),
                    v.getNombre(),
                    v.getApellido(),
                    v.getTelefono(),
                    obtenerNombreEspecialidad(v.getIdEspecialidad()),
                    v.getPagoMensual()
                });
            }
        }
    }
    
    private String obtenerNombreEspecialidad(int id) {
        String[] especialidades = {"Medicina General", "Cirugía Veterinaria", "Dermatología", 
            "Cardiología", "Odontología", "Oftalmología", "Neurología", "Urgencias", 
            "Nutrición", "Vacunación y Desparasitación"};
        if (id >= 1 && id <= especialidades.length) {
            return especialidades[id - 1];
        }
        return "";
    }
    
    private void nuevo() {
        limpiar();
        btnGrabar.setEnabled(true);
        btnEliminar.setEnabled(false);
        txtCedula.requestFocus();
    }
    
    private void grabar() {
        if (txtCedula.getText().trim().isEmpty() || txtNombre.getText().trim().isEmpty() ||
            txtTelefono.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Complete los campos obligatorios", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        Veterinario v = new Veterinario();
        if (!txtId.getText().isEmpty()) {
            v.setIdVeterinario(Integer.parseInt(txtId.getText()));
        }
        v.setCedula(txtCedula.getText().trim());
        v.setNombre(txtNombre.getText().trim());
        v.setApellido(txtApellido.getText().trim());
        v.setTelefono(txtTelefono.getText().trim());
        v.setCorreoElectronico(txtEmail.getText().trim());
        v.setDireccion(txtDireccion.getText().trim());
        v.setIdEspecialidad(cmbEspecialidad.getSelectedIndex() + 1);
        try {
            v.setPagoMensual(Double.parseDouble(txtPagoMensual.getText().trim()));
        } catch (NumberFormatException e) {
            v.setPagoMensual(460.00);
        }
        
        boolean resultado;
        if (txtId.getText().isEmpty()) {
            resultado = veterinarioService.crear(v);
        } else {
            resultado = veterinarioService.actualizar(v);
        }
        
        if (resultado) {
            JOptionPane.showMessageDialog(this, "Veterinario guardado correctamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            cargarVeterinarios();
            limpiar();
        } else {
            JOptionPane.showMessageDialog(this, "Error al guardar", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void eliminar() {
        if (txtId.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Seleccione un veterinario", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this, "¿Está seguro de eliminar este veterinario?", 
            "Confirmar", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            int id = Integer.parseInt(txtId.getText());
            if (veterinarioService.eliminar(id)) {
                JOptionPane.showMessageDialog(this, "Veterinario eliminado", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                cargarVeterinarios();
                limpiar();
            } else {
                JOptionPane.showMessageDialog(this, "No se puede eliminar - tiene registros asociados", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void buscar() {
        String filtro = txtFiltro.getText().trim();
        if (filtro.isEmpty()) {
            cargarVeterinarios();
            return;
        }
        
        model.setRowCount(0);
        List<Veterinario> lista = null;
        
        if (rbNombre.isSelected()) {
            lista = veterinarioService.buscarPorNombre(filtro);
        } else if (rbCedula.isSelected()) {
            Veterinario v = veterinarioService.obtenerPorCedula(filtro);
            if (v != null) {
                lista = java.util.Arrays.asList(v);
            }
        }
        
        if (lista != null) {
            for (Veterinario v : lista) {
                model.addRow(new Object[]{
                    v.getIdVeterinario(),
                    v.getCedula(),
                    v.getNombre(),
                    v.getApellido(),
                    v.getTelefono(),
                    obtenerNombreEspecialidad(v.getIdEspecialidad()),
                    v.getPagoMensual()
                });
            }
        }
    }
    
    private void cargarSeleccionado() {
        int row = tblVeterinarios.getSelectedRow();
        if (row >= 0) {
            txtId.setText(String.valueOf(model.getValueAt(row, 0)));
            txtCedula.setText(String.valueOf(model.getValueAt(row, 1)));
            txtNombre.setText(String.valueOf(model.getValueAt(row, 2)));
            txtApellido.setText(String.valueOf(model.getValueAt(row, 3)));
            txtTelefono.setText(String.valueOf(model.getValueAt(row, 4)));
            txtPagoMensual.setText(String.valueOf(model.getValueAt(row, 6)));
            
            btnGrabar.setEnabled(true);
            btnEliminar.setEnabled(true);
        }
    }
    
    private void limpiar() {
        txtId.setText("");
        txtCedula.setText("");
        txtNombre.setText("");
        txtApellido.setText("");
        txtTelefono.setText("");
        txtEmail.setText("");
        txtDireccion.setText("");
        txtPagoMensual.setText("460.00");
        cmbEspecialidad.setSelectedIndex(0);
        btnGrabar.setEnabled(false);
        btnEliminar.setEnabled(false);
    }
    
    private void aplicarTema(boolean darkMode) {
        Color bg = darkMode ? Color.DARK_GRAY : Color.WHITE;
        Color fg = darkMode ? Color.WHITE : Color.BLACK;
        getContentPane().setBackground(bg);
        // Aplicar a más componentes según necesidad
    }
}