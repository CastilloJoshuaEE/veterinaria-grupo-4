package com.mycompany.veterinaria.grupo4.view.historial;

import com.formdev.flatlaf.FlatClientProperties;
import com.mycompany.veterinaria.grupo4.view.swing.Button;
import com.mycompany.veterinaria.grupo4.view.swing.MyTextField;
import com.mycompany.veterinaria.grupo4.view.swing.table.Table;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class PnlHistorialMedico extends JPanel {
    
    private Table tblAtenciones;
    private Table tblVacunas;
    private MyTextField txtBuscarMascota;
    private Button btnBuscarMascota;
    private Button btnActualizarFicha;
    private JLabel lblNombreMascota;
    private JLabel lblEspecie;
    private JLabel lblRaza;
    private JLabel lblSexo;
    private JLabel lblFechaNacimiento;
    private JLabel lblNombreCliente;
    private JLabel lblCedulaCliente;
    private JLabel lblTelefonoCliente;
    private JLabel lblTotalAtenciones;
    private JLabel lblServiciosUnicos;
    private JLabel lblFotoMascota;
    private JLabel lblSinFoto;
    private JPanel panelInfoMascota;
    private JPanel panelInfoCliente;
    private JTabbedPane tabPane;
    private JScrollPane scrollAtenciones;
    private JScrollPane scrollVacunas;
    
    private int idMascotaSeleccionada = 0;
    
    public PnlHistorialMedico() {
        initComponents();
        setOpaque(true);
        putClientProperty(FlatClientProperties.STYLE, "background:$Panel.background");
        setBorder(new EmptyBorder(10, 10, 10, 10));
    }
    
    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        
        // Panel superior con búsqueda y resumen
        JPanel topPanel = createTopPanel();
        add(topPanel, BorderLayout.NORTH);
        
        // Panel central con tabs
        tabPane = new JTabbedPane();
        tabPane.addTab("Historial de Atenciones", createAtencionesPanel());
        tabPane.addTab("Vacunas Aplicadas", createVacunasPanel());
        add(tabPane, BorderLayout.CENTER);
        
        // Panel inferior con resumen
        JPanel bottomPanel = createBottomPanel();
        add(bottomPanel, BorderLayout.SOUTH);
    }
    
    private JPanel createTopPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setOpaque(false);
        
        // Panel de búsqueda
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        searchPanel.setOpaque(false);
        
        txtBuscarMascota = new MyTextField();
        txtBuscarMascota.setHint("Buscar mascota por nombre o cédula del dueño");
        txtBuscarMascota.setPreferredSize(new Dimension(300, 38));
        
        btnBuscarMascota = new Button();
        btnBuscarMascota.setText("Buscar Mascota");
        btnBuscarMascota.setBackground(new Color(230, 140, 30));
        btnBuscarMascota.setForeground(Color.WHITE);
        
        searchPanel.add(txtBuscarMascota);
        searchPanel.add(btnBuscarMascota);
        
        panel.add(searchPanel, BorderLayout.NORTH);
        
        // Panel de información de mascota y dueño
        JPanel infoPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        infoPanel.setOpaque(false);
        
        // Información de la mascota
        panelInfoMascota = new JPanel(new GridBagLayout());
        panelInfoMascota.setBorder(BorderFactory.createTitledBorder("Datos de la Mascota"));
        panelInfoMascota.setOpaque(false);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.anchor = GridBagConstraints.WEST;
        
        // Foto
        lblFotoMascota = new JLabel();
        lblFotoMascota.setPreferredSize(new Dimension(80, 80));
        lblFotoMascota.setHorizontalAlignment(SwingConstants.CENTER);
        lblFotoMascota.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        
        lblSinFoto = new JLabel("Sin foto");
        lblSinFoto.setFont(new Font("SansSerif", Font.ITALIC, 10));
        lblSinFoto.setHorizontalAlignment(SwingConstants.CENTER);
        
        JPanel fotoPanel = new JPanel(new BorderLayout());
        fotoPanel.setOpaque(false);
        fotoPanel.add(lblFotoMascota, BorderLayout.CENTER);
        fotoPanel.add(lblSinFoto, BorderLayout.SOUTH);
        
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.gridheight = 4;
        panelInfoMascota.add(fotoPanel, gbc);
        gbc.gridheight = 1;
        
        gbc.gridx = 1; gbc.gridy = 0;
        panelInfoMascota.add(new JLabel("Nombre:"), gbc);
        gbc.gridx = 2;
        lblNombreMascota = new JLabel("-");
        lblNombreMascota.setFont(new Font("SansSerif", Font.BOLD, 12));
        panelInfoMascota.add(lblNombreMascota, gbc);
        
        gbc.gridx = 1; gbc.gridy = 1;
        panelInfoMascota.add(new JLabel("Especie/Raza:"), gbc);
        gbc.gridx = 2;
        lblEspecie = new JLabel("-");
        panelInfoMascota.add(lblEspecie, gbc);
        
        gbc.gridx = 1; gbc.gridy = 2;
        panelInfoMascota.add(new JLabel("Sexo:"), gbc);
        gbc.gridx = 2;
        lblSexo = new JLabel("-");
        panelInfoMascota.add(lblSexo, gbc);
        
        gbc.gridx = 1; gbc.gridy = 3;
        panelInfoMascota.add(new JLabel("Fecha Nac.:"), gbc);
        gbc.gridx = 2;
        lblFechaNacimiento = new JLabel("-");
        panelInfoMascota.add(lblFechaNacimiento, gbc);
        
        // Información del dueño
        panelInfoCliente = new JPanel(new GridBagLayout());
        panelInfoCliente.setBorder(BorderFactory.createTitledBorder("Datos del Propietario"));
        panelInfoCliente.setOpaque(false);
        
        GridBagConstraints gbc2 = new GridBagConstraints();
        gbc2.insets = new Insets(5, 10, 5, 10);
        gbc2.anchor = GridBagConstraints.WEST;
        
        gbc2.gridx = 0; gbc2.gridy = 0;
        panelInfoCliente.add(new JLabel("Dueño:"), gbc2);
        gbc2.gridx = 1;
        lblNombreCliente = new JLabel("-");
        panelInfoCliente.add(lblNombreCliente, gbc2);
        
        gbc2.gridx = 0; gbc2.gridy = 1;
        panelInfoCliente.add(new JLabel("Cédula:"), gbc2);
        gbc2.gridx = 1;
        lblCedulaCliente = new JLabel("-");
        panelInfoCliente.add(lblCedulaCliente, gbc2);
        
        gbc2.gridx = 0; gbc2.gridy = 2;
        panelInfoCliente.add(new JLabel("Teléfono:"), gbc2);
        gbc2.gridx = 1;
        lblTelefonoCliente = new JLabel("-");
        panelInfoCliente.add(lblTelefonoCliente, gbc2);
        
        gbc2.gridx = 0; gbc2.gridy = 3;
        btnActualizarFicha = new Button();
        btnActualizarFicha.setText("Actualizar Ficha Médica");
        btnActualizarFicha.setBackground(new Color(230, 140, 30));
        btnActualizarFicha.setForeground(Color.WHITE);
        btnActualizarFicha.setEnabled(false);
        panelInfoCliente.add(btnActualizarFicha, gbc2);
        
        infoPanel.add(panelInfoMascota);
        infoPanel.add(panelInfoCliente);
        
        panel.add(infoPanel, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createAtencionesPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        
        tblAtenciones = new Table();
        tblAtenciones.setModel(new javax.swing.table.DefaultTableModel(
            new Object[][]{},
            new String[]{"Fecha", "Servicio", "Veterinario", "Instrumentos", "Medicamentos", "Diagnóstico", "Tratamiento"}
        ));
        
        scrollAtenciones = new JScrollPane(tblAtenciones);
        tblAtenciones.fixTable(scrollAtenciones);
        panel.add(scrollAtenciones, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createVacunasPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        
        tblVacunas = new Table();
        tblVacunas.setModel(new javax.swing.table.DefaultTableModel(
            new Object[][]{},
            new String[]{"Vacuna", "Descripción", "Fecha Aplicación", "Próxima Dosis"}
        ));
        
        scrollVacunas = new JScrollPane(tblVacunas);
        tblVacunas.fixTable(scrollVacunas);
        panel.add(scrollVacunas, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createBottomPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 5));
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.LIGHT_GRAY));
        
        lblTotalAtenciones = new JLabel("Total atenciones: 0");
        lblTotalAtenciones.setFont(new Font("SansSerif", Font.BOLD, 12));
        
        lblServiciosUnicos = new JLabel("Servicios únicos: 0");
        lblServiciosUnicos.setFont(new Font("SansSerif", Font.BOLD, 12));
        
        panel.add(lblTotalAtenciones);
        panel.add(lblServiciosUnicos);
        
        return panel;
    }
    
    // Getters
    public Table getTblAtenciones() { return tblAtenciones; }
    public Table getTblVacunas() { return tblVacunas; }
    public MyTextField getTxtBuscarMascota() { return txtBuscarMascota; }
    public Button getBtnBuscarMascota() { return btnBuscarMascota; }
    public Button getBtnActualizarFicha() { return btnActualizarFicha; }
    public JLabel getLblNombreMascota() { return lblNombreMascota; }
    public JLabel getLblEspecie() { return lblEspecie; }
    public JLabel getLblRaza() { return lblRaza; }
    public JLabel getLblSexo() { return lblSexo; }
    public JLabel getLblFechaNacimiento() { return lblFechaNacimiento; }
    public JLabel getLblNombreCliente() { return lblNombreCliente; }
    public JLabel getLblCedulaCliente() { return lblCedulaCliente; }
    public JLabel getLblTelefonoCliente() { return lblTelefonoCliente; }
    public JLabel getLblTotalAtenciones() { return lblTotalAtenciones; }
    public JLabel getLblServiciosUnicos() { return lblServiciosUnicos; }
    public JLabel getLblFotoMascota() { return lblFotoMascota; }
    public JLabel getLblSinFoto() { return lblSinFoto; }
    public int getIdMascotaSeleccionada() { return idMascotaSeleccionada; }
    public void setIdMascotaSeleccionada(int id) { this.idMascotaSeleccionada = id; }
}