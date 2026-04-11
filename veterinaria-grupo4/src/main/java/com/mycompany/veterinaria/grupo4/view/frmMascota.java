/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.veterinaria.grupo4.view;

import com.mycompany.veterinaria.grupo4.model.entity.Cliente;
import com.mycompany.veterinaria.grupo4.model.entity.Mascota;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class frmMascota extends JFrame {
    private RestTemplate restTemplate = new RestTemplate();
    private String apiBaseUrl = "http://localhost:8080/api";

    private JComboBox<Cliente> cmbClientes;
    private JTextField txtId, txtNombre, txtEspecie, txtRaza, txtPeso, txtColor, txtFoto;
    private JComboBox<String> cmbSexo;
    private JButton btnNuevo, btnGrabar, btnActualizar, btnEliminar, btnSubirFoto, btnGuardarFicha, btnRecargarTabla;
    private JTable tblMascotas;
    private DefaultTableModel tableModel;
    private JTextArea txtAlergias, txtEnfermedades, txtObservaciones;
    private JLabel lblFoto;
    private ImageIcon fotoIcon;

    private boolean esNuevo = false;
    private int idMascotaSeleccionada = 0;

    public frmMascota() {
        setTitle("Gestión de Mascotas");
        setSize(900, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        initComponents();
        cargarClientes();
        cargarMascotas();
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        JPanel mainPanel = new JPanel(new BorderLayout());

        // Panel superior - Datos del Cliente
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.add(new JLabel("Cliente:"));
        cmbClientes = new JComboBox<>();
        cmbClientes.setPreferredSize(new Dimension(200, 25));
        topPanel.add(cmbClientes);
        mainPanel.add(topPanel, BorderLayout.NORTH);

        // Panel central - Formulario de mascota
        JPanel formPanel = new JPanel(new GridBagLayout());
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
        formPanel.add(new JLabel("Nombre:"), gbc);
        gbc.gridx = 3;
        txtNombre = new JTextField(15);
        formPanel.add(txtNombre, gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row;
        formPanel.add(new JLabel("Especie:"), gbc);
        gbc.gridx = 1;
        txtEspecie = new JTextField(15);
        formPanel.add(txtEspecie, gbc);

        gbc.gridx = 2;
        formPanel.add(new JLabel("Raza:"), gbc);
        gbc.gridx = 3;
        txtRaza = new JTextField(15);
        formPanel.add(txtRaza, gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row;
        formPanel.add(new JLabel("Sexo:"), gbc);
        gbc.gridx = 1;
        cmbSexo = new JComboBox<>(new String[]{"Macho", "Hembra"});
        formPanel.add(cmbSexo, gbc);

        gbc.gridx = 2;
        formPanel.add(new JLabel("Peso (kg):"), gbc);
        gbc.gridx = 3;
        txtPeso = new JTextField(10);
        formPanel.add(txtPeso, gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row;
        formPanel.add(new JLabel("Color:"), gbc);
        gbc.gridx = 1;
        txtColor = new JTextField(15);
        formPanel.add(txtColor, gbc);

        gbc.gridx = 2;
        formPanel.add(new JLabel("Foto:"), gbc);
        gbc.gridx = 3;
        JPanel fotoPanel = new JPanel(new FlowLayout());
        txtFoto = new JTextField(15);
        txtFoto.setEditable(false);
        btnSubirFoto = new JButton("Subir Foto");
        fotoPanel.add(txtFoto);
        fotoPanel.add(btnSubirFoto);
        formPanel.add(fotoPanel, gbc);

        // Botones de acciones
        row++;
        JPanel buttonPanel = new JPanel(new FlowLayout());
        btnNuevo = new JButton("Nuevo");
        btnGrabar = new JButton("Grabar");
        btnActualizar = new JButton("Actualizar");
        btnEliminar = new JButton("Eliminar");
        btnGuardarFicha = new JButton("Guardar Ficha Médica");
        btnRecargarTabla = new JButton("Recargar");
        buttonPanel.add(btnNuevo);
        buttonPanel.add(btnGrabar);
        buttonPanel.add(btnActualizar);
        buttonPanel.add(btnEliminar);
        buttonPanel.add(btnGuardarFicha);
        buttonPanel.add(btnRecargarTabla);
        formPanel.add(buttonPanel, gbc);

        mainPanel.add(formPanel, BorderLayout.CENTER);

        // Tabla de mascotas
        tableModel = new DefaultTableModel(new String[]{"ID", "Nombre", "Especie", "Raza", "Sexo", "Peso", "Color"}, 0);
        tblMascotas = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(tblMascotas);
        scrollPane.setPreferredSize(new Dimension(800, 200));
        mainPanel.add(scrollPane, BorderLayout.SOUTH);

        // Panel de ficha médica
        JPanel fichaPanel = new JPanel(new GridBagLayout());
        fichaPanel.setBorder(BorderFactory.createTitledBorder("Ficha Médica"));
        GridBagConstraints gbcFicha = new GridBagConstraints();
        gbcFicha.insets = new Insets(5, 5, 5, 5);
        gbcFicha.fill = GridBagConstraints.BOTH;

        gbcFicha.gridx = 0; gbcFicha.gridy = 0;
        fichaPanel.add(new JLabel("Alergias:"), gbcFicha);
        gbcFicha.gridx = 1; gbcFicha.gridwidth = 3;
        txtAlergias = new JTextArea(3, 30);
        fichaPanel.add(new JScrollPane(txtAlergias), gbcFicha);

        gbcFicha.gridx = 0; gbcFicha.gridy = 1; gbcFicha.gridwidth = 1;
        fichaPanel.add(new JLabel("Enfermedades Crónicas:"), gbcFicha);
        gbcFicha.gridx = 1; gbcFicha.gridwidth = 3;
        txtEnfermedades = new JTextArea(3, 30);
        fichaPanel.add(new JScrollPane(txtEnfermedades), gbcFicha);

        gbcFicha.gridx = 0; gbcFicha.gridy = 2;
        fichaPanel.add(new JLabel("Observaciones:"), gbcFicha);
        gbcFicha.gridx = 1; gbcFicha.gridwidth = 3;
        txtObservaciones = new JTextArea(3, 30);
        fichaPanel.add(new JScrollPane(txtObservaciones), gbcFicha);

        mainPanel.add(fichaPanel, BorderLayout.EAST);

        add(mainPanel);

        // Eventos
        btnNuevo.addActionListener(e -> nuevo());
        btnGrabar.addActionListener(e -> grabar());
        btnActualizar.addActionListener(e -> actualizar());
        btnEliminar.addActionListener(e -> eliminar());
        btnSubirFoto.addActionListener(e -> subirFoto());
        btnGuardarFicha.addActionListener(e -> guardarFichaMedica());
        btnRecargarTabla.addActionListener(e -> cargarMascotas());
        cmbClientes.addActionListener(e -> cargarMascotas());
        tblMascotas.getSelectionModel().addListSelectionListener(e -> cargarMascotaSeleccionada());

        // Estado inicial
        habilitarBotones(false);
        btnNuevo.setEnabled(true);
    }

    private void cargarClientes() {
        try {
            ResponseEntity<Cliente[]> response = restTemplate.getForEntity(apiBaseUrl + "/cliente/listar", Cliente[].class);
            Cliente[] clientes = response.getBody();
            if (clientes != null) {
                cmbClientes.removeAllItems();
                for (Cliente c : clientes) {
                    cmbClientes.addItem(c);
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar clientes: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cargarMascotas() {
        Cliente clienteSeleccionado = (Cliente) cmbClientes.getSelectedItem();
        if (clienteSeleccionado == null) return;

        try {
            ResponseEntity<Mascota[]> response = restTemplate.getForEntity(apiBaseUrl + "/mascota/listar/" + clienteSeleccionado.getIdCliente(), Mascota[].class);
            Mascota[] mascotas = response.getBody();
            tableModel.setRowCount(0);
            if (mascotas != null) {
                for (Mascota m : mascotas) {
                    tableModel.addRow(new Object[]{
                        m.getIdMascota(),
                        m.getNombre(),
                        m.getEspecie(),
                        m.getRaza(),
                        m.getSexo() == 'M' ? "Macho" : "Hembra",
                        m.getPeso(),
                        m.getColor()
                    });
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar mascotas: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cargarMascotaSeleccionada() {
        int row = tblMascotas.getSelectedRow();
        if (row >= 0) {
            idMascotaSeleccionada = (int) tableModel.getValueAt(row, 0);
            try {
                ResponseEntity<Mascota> response = restTemplate.getForEntity(apiBaseUrl + "/mascota/" + idMascotaSeleccionada, Mascota.class);
                Mascota m = response.getBody();
                if (m != null) {
                    txtId.setText(String.valueOf(m.getIdMascota()));
                    txtNombre.setText(m.getNombre());
                    txtEspecie.setText(m.getEspecie());
                    txtRaza.setText(m.getRaza());
                    cmbSexo.setSelectedItem(m.getSexo() == 'M' ? "Macho" : "Hembra");
                    txtPeso.setText(m.getPeso() != null ? String.valueOf(m.getPeso()) : "");
                    txtColor.setText(m.getColor());
                    // Cargar foto
                    try {
                        ResponseEntity<byte[]> fotoResponse = restTemplate.getForEntity(apiBaseUrl + "/mascota/foto/" + idMascotaSeleccionada, byte[].class);
                        if (fotoResponse.getBody() != null) {
                            fotoIcon = new ImageIcon(fotoResponse.getBody());
                        }
                    } catch (Exception ex) {}
                    // Cargar ficha médica
                    cargarFichaMedica();
                }
                habilitarBotones(true);
                esNuevo = false;
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error al cargar mascota: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void nuevo() {
        limpiarFormulario();
        esNuevo = true;
        habilitarBotones(true);
        btnGrabar.setEnabled(true);
        btnActualizar.setEnabled(false);
    }

    private void grabar() {
        if (!validarDatos()) return;

        Cliente cliente = (Cliente) cmbClientes.getSelectedItem();
        if (cliente == null) {
            JOptionPane.showMessageDialog(this, "Seleccione un cliente", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            body.add("idCliente", cliente.getIdCliente());
            body.add("nombre", txtNombre.getText());
            body.add("especie", txtEspecie.getText());
            body.add("raza", txtRaza.getText());
            body.add("sexo", cmbSexo.getSelectedItem().toString().equals("Macho") ? "M" : "H");
            body.add("peso", txtPeso.getText().isEmpty() ? null : Double.parseDouble(txtPeso.getText()));
            body.add("color", txtColor.getText());

            if (txtFoto.getText() != null && !txtFoto.getText().isEmpty()) {
                File file = new File(txtFoto.getText());
                byte[] fileBytes = Files.readAllBytes(file.toPath());
                body.add("foto", new ByteArrayResource(fileBytes) {
                    @Override
                    public String getFilename() {
                        return file.getName();
                    }
                });
            }

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);
            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

            if (esNuevo) {
                Integer id = restTemplate.postForObject(apiBaseUrl + "/mascota/crear", requestEntity, Integer.class);
                if (id != null && id > 0) {
                    JOptionPane.showMessageDialog(this, "Mascota registrada correctamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    cargarMascotas();
                    limpiarFormulario();
                }
            } else {
                body.add("idMascota", idMascotaSeleccionada);
                restTemplate.put(apiBaseUrl + "/mascota/actualizar", requestEntity);
                JOptionPane.showMessageDialog(this, "Mascota actualizada correctamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                cargarMascotas();
            }
            habilitarBotones(false);
            btnNuevo.setEnabled(true);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al guardar: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void actualizar() {
        grabar();
    }

    private void eliminar() {
        if (idMascotaSeleccionada <= 0) {
            JOptionPane.showMessageDialog(this, "Seleccione una mascota", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "¿Está seguro de eliminar esta mascota?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                restTemplate.delete(apiBaseUrl + "/mascota/eliminar/" + idMascotaSeleccionada);
                JOptionPane.showMessageDialog(this, "Mascota eliminada correctamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                cargarMascotas();
                limpiarFormulario();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error al eliminar: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void subirFoto() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Imágenes", "jpg", "jpeg", "png", "bmp"));
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            txtFoto.setText(fileChooser.getSelectedFile().getAbsolutePath());
        }
    }

    private void guardarFichaMedica() {
        if (idMascotaSeleccionada <= 0) {
            JOptionPane.showMessageDialog(this, "Seleccione una mascota", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            body.add("idMascota", idMascotaSeleccionada);
            body.add("alergias", txtAlergias.getText());
            body.add("enfermedadesCronicas", txtEnfermedades.getText());
            body.add("observaciones", txtObservaciones.getText());

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

            restTemplate.postForObject(apiBaseUrl + "/mascota/ficha/actualizar", requestEntity, Boolean.class);
            JOptionPane.showMessageDialog(this, "Ficha médica guardada correctamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al guardar ficha médica: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cargarFichaMedica() {
        // Implementar llamada a API para obtener ficha médica
    }

    private void limpiarFormulario() {
        txtId.setText("");
        txtNombre.setText("");
        txtEspecie.setText("");
        txtRaza.setText("");
        cmbSexo.setSelectedIndex(0);
        txtPeso.setText("");
        txtColor.setText("");
        txtFoto.setText("");
        txtAlergias.setText("");
        txtEnfermedades.setText("");
        txtObservaciones.setText("");
        idMascotaSeleccionada = 0;
    }

    private boolean validarDatos() {
        if (txtNombre.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese el nombre", "Validación", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        if (txtEspecie.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese la especie", "Validación", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        return true;
    }

    private void habilitarBotones(boolean habilitar) {
        btnGrabar.setEnabled(habilitar);
        btnActualizar.setEnabled(habilitar);
        btnEliminar.setEnabled(habilitar);
        btnGuardarFicha.setEnabled(habilitar);
    }
}