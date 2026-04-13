package com.mycompany.veterinaria.grupo4.view;

import com.mycompany.veterinaria.grupo4.model.entity.Servicio;
import com.mycompany.veterinaria.grupo4.model.entity.Veterinario;
import com.mycompany.veterinaria.grupo4.service.ServicioService;
import com.mycompany.veterinaria.grupo4.service.VeterinarioService;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class frmServicio extends JFrame {
    private ServicioService servicioService = new ServicioService();
    private VeterinarioService veterinarioService = new VeterinarioService();
    
    private JTextField txtId, txtNombre, txtDescripcion, txtPrecio, txtDuracion;
    private JCheckBox chkEstado;
    private JTable tblServicios, tblVeterinariosAsignados, tblVeterinariosDisponibles;
    private DefaultTableModel modelServicios, modelAsignados, modelDisponibles;
    private JButton btnNuevo, btnGrabar, btnEliminar, btnCancelar;
    private JButton btnAsignar, btnQuitar, btnRegistrarVeterinario;
    private boolean modoEdicion = false;
    
    public frmServicio() {
        initComponents();
        cargarServicios();
        setLocationRelativeTo(null);
    }
    
    private void initComponents() {
        setTitle("Gestión de Servicios");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Panel de datos del servicio
        JPanel datosPanel = new JPanel(new GridBagLayout());
        datosPanel.setBorder(BorderFactory.createTitledBorder("Datos del Servicio"));
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
        datosPanel.add(new JLabel("Nombre:"), gbc);
        gbc.gridx = 3;
        txtNombre = new JTextField(20);
        datosPanel.add(txtNombre, gbc);
        
        row++;
        gbc.gridx = 0; gbc.gridy = row;
        datosPanel.add(new JLabel("Descripción:"), gbc);
        gbc.gridx = 1;
        gbc.gridwidth = 3;
        txtDescripcion = new JTextField(40);
        datosPanel.add(txtDescripcion, gbc);
        gbc.gridwidth = 1;
        
        row++;
        gbc.gridx = 0; gbc.gridy = row;
        datosPanel.add(new JLabel("Precio:"), gbc);
        gbc.gridx = 1;
        txtPrecio = new JTextField(10);
        datosPanel.add(txtPrecio, gbc);
        
        gbc.gridx = 2;
        datosPanel.add(new JLabel("Duración (min):"), gbc);
        gbc.gridx = 3;
        txtDuracion = new JTextField(10);
        datosPanel.add(txtDuracion, gbc);
        
        row++;
        gbc.gridx = 0; gbc.gridy = row;
        datosPanel.add(new JLabel("Estado:"), gbc);
        gbc.gridx = 1;
        chkEstado = new JCheckBox("Activo", true);
        datosPanel.add(chkEstado, gbc);
        
        mainPanel.add(datosPanel, BorderLayout.NORTH);
        
        // Tabla de servicios
        JPanel tablaServiciosPanel = new JPanel(new BorderLayout());
        tablaServiciosPanel.setBorder(BorderFactory.createTitledBorder("Listado de Servicios"));
        modelServicios = new DefaultTableModel(new String[]{"ID", "Nombre", "Precio", "Duración", "Estado"}, 0);
        tblServicios = new JTable(modelServicios);
        tblServicios.getColumnModel().getColumn(0).setMinWidth(0);
        tblServicios.getColumnModel().getColumn(0).setMaxWidth(0);
        tablaServiciosPanel.add(new JScrollPane(tblServicios), BorderLayout.CENTER);
        mainPanel.add(tablaServiciosPanel, BorderLayout.CENTER);
        
        // Panel de asignación de veterinarios
        JPanel asignacionPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        asignacionPanel.setBorder(BorderFactory.createTitledBorder("Asignación de Veterinarios"));
        
        // Veterinarios asignados
        JPanel asignadosPanel = new JPanel(new BorderLayout());
        asignadosPanel.add(new JLabel("Veterinarios Asignados:"), BorderLayout.NORTH);
        modelAsignados = new DefaultTableModel(new String[]{"ID", "Nombre", "Especialidad"}, 0);
        tblVeterinariosAsignados = new JTable(modelAsignados);
        tblVeterinariosAsignados.getColumnModel().getColumn(0).setMinWidth(0);
        tblVeterinariosAsignados.getColumnModel().getColumn(0).setMaxWidth(0);
        asignadosPanel.add(new JScrollPane(tblVeterinariosAsignados), BorderLayout.CENTER);
        
        JPanel botonesAsignacion = new JPanel(new GridLayout(3, 1, 5, 5));
        btnAsignar = new JButton("→ Asignar →");
        btnQuitar = new JButton("← Quitar ←");
        btnRegistrarVeterinario = new JButton("Registrar Veterinario");
        botonesAsignacion.add(btnAsignar);
        botonesAsignacion.add(btnQuitar);
        botonesAsignacion.add(btnRegistrarVeterinario);
        
        // Veterinarios disponibles
        JPanel disponiblesPanel = new JPanel(new BorderLayout());
        disponiblesPanel.add(new JLabel("Veterinarios Disponibles:"), BorderLayout.NORTH);
        modelDisponibles = new DefaultTableModel(new String[]{"ID", "Nombre", "Especialidad"}, 0);
        tblVeterinariosDisponibles = new JTable(modelDisponibles);
        tblVeterinariosDisponibles.getColumnModel().getColumn(0).setMinWidth(0);
        tblVeterinariosDisponibles.getColumnModel().getColumn(0).setMaxWidth(0);
        disponiblesPanel.add(new JScrollPane(tblVeterinariosDisponibles), BorderLayout.CENTER);
        
        asignacionPanel.add(asignadosPanel);
        asignacionPanel.add(botonesAsignacion);
        asignacionPanel.add(disponiblesPanel);
        
        mainPanel.add(asignacionPanel, BorderLayout.SOUTH);
        
        // Panel de botones
        JPanel botonesPanel = new JPanel(new FlowLayout());
        btnNuevo = new JButton("Nuevo");
        btnGrabar = new JButton("Grabar");
        btnEliminar = new JButton("Eliminar");
        btnCancelar = new JButton("Cancelar");
        botonesPanel.add(btnNuevo);
        botonesPanel.add(btnGrabar);
        botonesPanel.add(btnEliminar);
        botonesPanel.add(btnCancelar);
        mainPanel.add(botonesPanel, BorderLayout.NORTH);
        
        add(mainPanel);
        
        // Eventos
        btnNuevo.addActionListener(e -> nuevo());
        btnGrabar.addActionListener(e -> grabar());
        btnEliminar.addActionListener(e -> eliminar());
        btnCancelar.addActionListener(e -> cancelar());
        btnAsignar.addActionListener(e -> asignarVeterinario());
        btnQuitar.addActionListener(e -> quitarVeterinario());
        btnRegistrarVeterinario.addActionListener(e -> new frmVeterinario(false).setVisible(true));
        tblServicios.getSelectionModel().addListSelectionListener(e -> cargarSeleccionado());
        
        estadoInicial();
    }
    
    private void cargarServicios() {
        modelServicios.setRowCount(0);
        List<Servicio> lista = servicioService.listarTodos();
        if (lista != null) {
            for (Servicio s : lista) {
                modelServicios.addRow(new Object[]{
                    s.getIdServicio(),
                    s.getNombreServicio(),
                    s.getPrecio(),
                    s.getDuracionEstimada(),
                    s.isEstado() ? "Activo" : "Inactivo"
                });
            }
        }
    }
    
    private void cargarVeterinariosAsignados(int idServicio) {
        modelAsignados.setRowCount(0);
        List<Veterinario> lista = servicioService.obtenerVeterinariosAsignados(idServicio);
        if (lista != null) {
            for (Veterinario v : lista) {
                modelAsignados.addRow(new Object[]{
                    v.getIdVeterinario(),
                    v.getNombre() + " " + v.getApellido(),
                    obtenerNombreEspecialidad(v.getIdEspecialidad())
                });
            }
        }
    }
    
    private void cargarVeterinariosDisponibles(int idServicio) {
        modelDisponibles.setRowCount(0);
        List<Veterinario> lista = servicioService.obtenerVeterinariosNoAsignados(idServicio);
        if (lista != null) {
            for (Veterinario v : lista) {
                modelDisponibles.addRow(new Object[]{
                    v.getIdVeterinario(),
                    v.getNombre() + " " + v.getApellido(),
                    obtenerNombreEspecialidad(v.getIdEspecialidad())
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
        modoEdicion = false;
        btnGrabar.setEnabled(true);
        btnEliminar.setEnabled(false);
        txtNombre.requestFocus();
    }
    
    private void grabar() {
        if (txtNombre.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese el nombre del servicio", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        try {
            double precio = Double.parseDouble(txtPrecio.getText().trim());
            int duracion = Integer.parseInt(txtDuracion.getText().trim());
            
            Servicio s = new Servicio();
            if (!txtId.getText().isEmpty()) {
                s.setIdServicio(Integer.parseInt(txtId.getText()));
            }
            s.setNombreServicio(txtNombre.getText().trim());
            s.setDescripcion(txtDescripcion.getText().trim());
            s.setPrecio(precio);
            s.setDuracionEstimada(duracion);
            s.setEstado(chkEstado.isSelected());
            
            boolean resultado;
            if (modoEdicion) {
                resultado = servicioService.actualizar(s);
            } else {
                resultado = servicioService.crear(s) > 0;
            }
            
            if (resultado) {
                JOptionPane.showMessageDialog(this, "Servicio guardado correctamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                cargarServicios();
                limpiar();
                estadoInicial();
            } else {
                JOptionPane.showMessageDialog(this, "Error al guardar", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Ingrese valores numéricos válidos", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void eliminar() {
        if (txtId.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Seleccione un servicio", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this, "¿Está seguro de eliminar este servicio?", 
            "Confirmar", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            int id = Integer.parseInt(txtId.getText());
            if (servicioService.eliminar(id)) {
                JOptionPane.showMessageDialog(this, "Servicio eliminado", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                cargarServicios();
                limpiar();
            } else {
                JOptionPane.showMessageDialog(this, "No se puede eliminar - tiene registros asociados", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void cancelar() {
        limpiar();
        estadoInicial();
    }
    
    private void cargarSeleccionado() {
        int row = tblServicios.getSelectedRow();
        if (row >= 0) {
            modoEdicion = true;
            int id = (int) modelServicios.getValueAt(row, 0);
            Servicio s = servicioService.obtenerPorId(id);
            if (s != null) {
                txtId.setText(String.valueOf(s.getIdServicio()));
                txtNombre.setText(s.getNombreServicio());
                txtDescripcion.setText(s.getDescripcion());
                txtPrecio.setText(String.valueOf(s.getPrecio()));
                txtDuracion.setText(String.valueOf(s.getDuracionEstimada()));
                chkEstado.setSelected(s.isEstado());
                
                cargarVeterinariosAsignados(id);
                cargarVeterinariosDisponibles(id);
                
                btnGrabar.setEnabled(true);
                btnEliminar.setEnabled(true);
            }
        }
    }
    
    private void asignarVeterinario() {
        int row = tblVeterinariosDisponibles.getSelectedRow();
        if (row >= 0 && !txtId.getText().isEmpty()) {
            int idVeterinario = (int) modelDisponibles.getValueAt(row, 0);
            int idServicio = Integer.parseInt(txtId.getText());
            
            if (servicioService.asignarVeterinario(idServicio, idVeterinario)) {
                cargarVeterinariosAsignados(idServicio);
                cargarVeterinariosDisponibles(idServicio);
            }
        }
    }
    
    private void quitarVeterinario() {
        int row = tblVeterinariosAsignados.getSelectedRow();
        if (row >= 0 && !txtId.getText().isEmpty()) {
            // Nota: Para quitar se necesita el ID de asignación
            JOptionPane.showMessageDialog(this, "Seleccione la asignación en la tabla de asignados", "Info", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    private void limpiar() {
        txtId.setText("");
        txtNombre.setText("");
        txtDescripcion.setText("");
        txtPrecio.setText("");
        txtDuracion.setText("");
        chkEstado.setSelected(true);
        modelAsignados.setRowCount(0);
        modelDisponibles.setRowCount(0);
    }
    
    private void estadoInicial() {
        btnGrabar.setEnabled(false);
        btnEliminar.setEnabled(false);
        modoEdicion = false;
    }
}