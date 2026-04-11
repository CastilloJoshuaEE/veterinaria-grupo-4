/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.veterinaria.grupo4.view;

import com.mycompany.veterinaria.grupo4.model.entity.Cliente;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class frmCliente extends JFrame {
    private RestTemplate restTemplate = new RestTemplate();
    private String apiBaseUrl = "http://localhost:8080/api/cliente";
    
    private JTable tblClientes;
    private DefaultTableModel tableModel;
    private JTextField txtId, txtCedula, txtNombre, txtApellido, txtTelefono, txtEmail, txtDireccion;
    private JButton btnNuevo, btnGrabar, btnEliminar, btnBuscar, btnLimpiar;
    private JComboBox<String> cmbCedulas;
    private JTextField txtFiltroNombre, txtFiltroMascota;
    
    private boolean esNuevo = false;
    
    public frmCliente(boolean darkMode) {
        setTitle("Gestión de Clientes");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        initComponents();
        cargarClientes();
        cargarCedulas();
    }
    
    private void initComponents() {
        setLayout(new BorderLayout());
        
        // Panel principal
        JPanel mainPanel = new JPanel(new BorderLayout());
        
        // Panel de formulario
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Datos del Cliente"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        int row = 0;
        gbc.gridx = 0; gbc.gridy = row;
        formPanel.add(new JLabel("ID:"), gbc);
        gbc.gridx = 1;
        txtId = new JTextField(10);
        txtId.setEditable(false);
        formPanel.add(txtId, gbc);
        
        gbc.gridx = 2;
        formPanel.add(new JLabel("Cédula:"), gbc);
        gbc.gridx = 3;
        txtCedula = new JTextField(15);
        formPanel.add(txtCedula, gbc);
        
        row++;
        gbc.gridx = 0; gbc.gridy = row;
        formPanel.add(new JLabel("Nombre:"), gbc);
        gbc.gridx = 1;
        txtNombre = new JTextField(15);
        formPanel.add(txtNombre, gbc);
        
        gbc.gridx = 2;
        formPanel.add(new JLabel("Apellido:"), gbc);
        gbc.gridx = 3;
        txtApellido = new JTextField(15);
        formPanel.add(txtApellido, gbc);
        
        row++;
        gbc.gridx = 0; gbc.gridy = row;
        formPanel.add(new JLabel("Teléfono:"), gbc);
        gbc.gridx = 1;
        txtTelefono = new JTextField(15);
        formPanel.add(txtTelefono, gbc);
        
        gbc.gridx = 2;
        formPanel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 3;
        txtEmail = new JTextField(15);
        formPanel.add(txtEmail, gbc);
        
        row++;
        gbc.gridx = 0; gbc.gridy = row;
        formPanel.add(new JLabel("Dirección:"), gbc);
        gbc.gridx = 1;
        gbc.gridwidth = 3;
        txtDireccion = new JTextField(30);
        formPanel.add(txtDireccion, gbc);
        gbc.gridwidth = 1;
        
        // Panel de botones
        JPanel buttonPanel = new JPanel(new FlowLayout());
        btnNuevo = new JButton("Nuevo");
        btnGrabar = new JButton("Grabar");
        btnEliminar = new JButton("Eliminar");
        buttonPanel.add(btnNuevo);
        buttonPanel.add(btnGrabar);
        buttonPanel.add(btnEliminar);
        
        row++;
        gbc.gridx = 0; gbc.gridy = row;
        gbc.gridwidth = 4;
        formPanel.add(buttonPanel, gbc);
        
        mainPanel.add(formPanel, BorderLayout.NORTH);
        
        // Tabla de clientes
        tableModel = new DefaultTableModel(new String[]{"ID", "Cédula", "Nombre", "Apellido", "Teléfono", "Email"}, 0);
        tblClientes = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(tblClientes);
        scrollPane.setPreferredSize(new Dimension(900, 250));
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        
        // Panel de búsqueda
        JPanel searchPanel = new JPanel(new FlowLayout());
        searchPanel.setBorder(BorderFactory.createTitledBorder("Búsqueda"));
        searchPanel.add(new JLabel("Cédula:"));
        cmbCedulas = new JComboBox<>();
        cmbCedulas.setPreferredSize(new Dimension(150, 25));
        searchPanel.add(cmbCedulas);
        
        searchPanel.add(new JLabel("Nombre cliente:"));
        txtFiltroNombre = new JTextField(15);
        searchPanel.add(txtFiltroNombre);
        
        searchPanel.add(new JLabel("Nombre mascota:"));
        txtFiltroMascota = new JTextField(15);
        searchPanel.add(txtFiltroMascota);
        
        btnBuscar = new JButton("Buscar");
        btnLimpiar = new JButton("Limpiar");
        searchPanel.add(btnBuscar);
        searchPanel.add(btnLimpiar);
        
        mainPanel.add(searchPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
        
        // Eventos
        btnNuevo.addActionListener(e -> nuevo());
        btnGrabar.addActionListener(e -> grabar());
        btnEliminar.addActionListener(e -> eliminar());
        btnBuscar.addActionListener(e -> buscar());
        btnLimpiar.addActionListener(e -> limpiarBusqueda());
        cmbCedulas.addActionListener(e -> buscarPorCedula());
        tblClientes.getSelectionModel().addListSelectionListener(e -> cargarClienteSeleccionado());
        
        // Estado inicial
        habilitarBotones(false);
        btnNuevo.setEnabled(true);
    }
    
    private void cargarClientes() {
        try {
            ResponseEntity<List<Cliente>> response = restTemplate.exchange(
                apiBaseUrl + "/listar",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Cliente>>() {}
            );
            List<Cliente> clientes = response.getBody();
            tableModel.setRowCount(0);
            if (clientes != null) {
                for (Cliente c : clientes) {
                    tableModel.addRow(new Object[]{
                        c.getIdCliente(),
                        c.getCedula(),
                        c.getNombre(),
                        c.getApellido(),
                        c.getTelefono(),
                        c.getCorreoElectronico()
                    });
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar clientes: " + e.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void cargarCedulas() {
        try {
            ResponseEntity<List<String>> response = restTemplate.exchange(
                apiBaseUrl + "/cedulas",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<String>>() {}
            );
            List<String> cedulas = response.getBody();
            cmbCedulas.removeAllItems();
            if (cedulas != null) {
                for (String cedula : cedulas) {
                    cmbCedulas.addItem(cedula);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void buscarPorCedula() {
        String cedula = (String) cmbCedulas.getSelectedItem();
        if (cedula != null && !cedula.isEmpty()) {
            try {
                ResponseEntity<Cliente> response = restTemplate.getForEntity(
                    apiBaseUrl + "/cedula/" + cedula, Cliente.class);
                Cliente cliente = response.getBody();
                if (cliente != null) {
                    tableModel.setRowCount(0);
                    tableModel.addRow(new Object[]{
                        cliente.getIdCliente(),
                        cliente.getCedula(),
                        cliente.getNombre(),
                        cliente.getApellido(),
                        cliente.getTelefono(),
                        cliente.getCorreoElectronico()
                    });
                    cargarClienteEnFormulario(cliente);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    private void buscar() {
        String nombreCliente = txtFiltroNombre.getText().trim();
        String nombreMascota = txtFiltroMascota.getText().trim();
        
        if (!nombreMascota.isEmpty()) {
            // Búsqueda por mascota - requeriría endpoint adicional
            JOptionPane.showMessageDialog(this, "Búsqueda por mascota requiere endpoint adicional", 
                "Info", JOptionPane.INFORMATION_MESSAGE);
        } else if (!nombreCliente.isEmpty()) {
            try {
                ResponseEntity<List<Cliente>> response = restTemplate.exchange(
                    apiBaseUrl + "/buscar/nombre?nombre=" + nombreCliente,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<Cliente>>() {}
                );
                List<Cliente> clientes = response.getBody();
                tableModel.setRowCount(0);
                if (clientes != null) {
                    for (Cliente c : clientes) {
                        tableModel.addRow(new Object[]{
                            c.getIdCliente(),
                            c.getCedula(),
                            c.getNombre(),
                            c.getApellido(),
                            c.getTelefono(),
                            c.getCorreoElectronico()
                        });
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            cargarClientes();
        }
    }
    
    private void limpiarBusqueda() {
        txtFiltroNombre.setText("");
        txtFiltroMascota.setText("");
        cargarClientes();
        limpiarFormulario();
    }
    
    private void cargarClienteSeleccionado() {
        int row = tblClientes.getSelectedRow();
        if (row >= 0) {
            Cliente c = new Cliente();
            c.setIdCliente((int) tableModel.getValueAt(row, 0));
            c.setCedula((String) tableModel.getValueAt(row, 1));
            c.setNombre((String) tableModel.getValueAt(row, 2));
            c.setApellido((String) tableModel.getValueAt(row, 3));
            c.setTelefono((String) tableModel.getValueAt(row, 4));
            c.setCorreoElectronico((String) tableModel.getValueAt(row, 5));
            cargarClienteEnFormulario(c);
            habilitarBotones(true);
            esNuevo = false;
        }
    }
    
    private void cargarClienteEnFormulario(Cliente c) {
        txtId.setText(String.valueOf(c.getIdCliente()));
        txtCedula.setText(c.getCedula());
        txtNombre.setText(c.getNombre());
        txtApellido.setText(c.getApellido());
        txtTelefono.setText(c.getTelefono());
        txtEmail.setText(c.getCorreoElectronico());
        txtDireccion.setText(c.getDireccion());
    }
    
    private void nuevo() {
        limpiarFormulario();
        esNuevo = true;
        habilitarBotones(true);
        btnGrabar.setEnabled(true);
        btnEliminar.setEnabled(false);
        txtCedula.requestFocus();
    }
    
    private void grabar() {
        if (!validarDatos()) return;
        
        Cliente cliente = new Cliente();
        if (!esNuevo && !txtId.getText().isEmpty()) {
            cliente.setIdCliente(Integer.parseInt(txtId.getText()));
        }
        cliente.setCedula(txtCedula.getText().trim());
        cliente.setNombre(txtNombre.getText().trim());
        cliente.setApellido(txtApellido.getText().trim());
        cliente.setTelefono(txtTelefono.getText().trim());
        cliente.setCorreoElectronico(txtEmail.getText().trim());
        cliente.setDireccion(txtDireccion.getText().trim());
        
        try {
            if (esNuevo) {
                Boolean resultado = restTemplate.postForObject(apiBaseUrl + "/crear", cliente, Boolean.class);
                if (resultado != null && resultado) {
                    JOptionPane.showMessageDialog(this, "Cliente registrado correctamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                }
            } else {
                restTemplate.put(apiBaseUrl + "/actualizar", cliente);
                JOptionPane.showMessageDialog(this, "Cliente actualizado correctamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            }
            cargarClientes();
            cargarCedulas();
            limpiarFormulario();
            habilitarBotones(false);
            btnNuevo.setEnabled(true);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al guardar: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void eliminar() {
        if (txtId.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Seleccione un cliente", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this, "¿Está seguro de eliminar este cliente?", 
            "Confirmar", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                int id = Integer.parseInt(txtId.getText());
                restTemplate.delete(apiBaseUrl + "/eliminar/" + id);
                JOptionPane.showMessageDialog(this, "Cliente eliminado correctamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                cargarClientes();
                cargarCedulas();
                limpiarFormulario();
                habilitarBotones(false);
                btnNuevo.setEnabled(true);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error al eliminar: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void limpiarFormulario() {
        txtId.setText("");
        txtCedula.setText("");
        txtNombre.setText("");
        txtApellido.setText("");
        txtTelefono.setText("");
        txtEmail.setText("");
        txtDireccion.setText("");
    }
    
    private boolean validarDatos() {
        if (txtCedula.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese la cédula", "Validación", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        if (txtCedula.getText().length() != 10) {
            JOptionPane.showMessageDialog(this, "La cédula debe tener 10 dígitos", "Validación", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        if (txtNombre.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese el nombre", "Validación", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        if (txtApellido.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese el apellido", "Validación", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        if (txtTelefono.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese el teléfono", "Validación", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        if (txtTelefono.getText().length() != 10) {
            JOptionPane.showMessageDialog(this, "El teléfono debe tener 10 dígitos", "Validación", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        return true;
    }
    
    private void habilitarBotones(boolean habilitar) {
        btnGrabar.setEnabled(habilitar);
        btnEliminar.setEnabled(habilitar);
    }
}