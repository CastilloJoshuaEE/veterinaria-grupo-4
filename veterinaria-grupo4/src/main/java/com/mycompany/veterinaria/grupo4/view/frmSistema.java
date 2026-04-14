package com.mycompany.veterinaria.grupo4.view;

import com.mycompany.veterinaria.grupo4.view.frmCliente;
import com.mycompany.veterinaria.grupo4.view.frmMascota;
import com.mycompany.veterinaria.grupo4.view.frmCita;
import com.mycompany.veterinaria.grupo4.view.frmVeterinario;
import com.mycompany.veterinaria.grupo4.view.frmServicio;
import com.mycompany.veterinaria.grupo4.view.frmAtencionMedica;
import com.mycompany.veterinaria.grupo4.view.frmHistorialMedico;
import com.mycompany.veterinaria.grupo4.view.frmFactura;
import com.mycompany.veterinaria.grupo4.view.frmSeleccionarCedula;
import com.mycompany.veterinaria.grupo4.view.frmReporte;
import com.mycompany.veterinaria.grupo4.view.frmRegistrarme;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class frmSistema extends JFrame {
    private String nombreUsuario;
    private int idUsuario;
    private boolean isDarkMode = false;

    private JMenuBar menuBar;
    private JPanel panelControles;
    private JPanel panelButtons;
    private JPanel panelSearch;
    private JPanel panelLogo;
    private JLabel lblTitulo;
    private JButton btnClientes, btnMascotas, btnCitas, btnFacturas;
    private JTextField searchBox;
    private JButton btnBuscar;

    public frmSistema(String nombreUsuario, int idUsuario) {
        this.nombreUsuario = nombreUsuario;
        this.idUsuario = idUsuario;
        setTitle("Sistema de Gestión Veterinaria");
        setSize(1200, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        initComponents();
    }

    private void initComponents() {
        // Menu Bar
        menuBar = new JMenuBar();

        JMenu archivoMenu = new JMenu("Archivo");
        JMenuItem homeItem = new JMenuItem("HOME");
        JMenuItem cerrarSesionItem = new JMenuItem("CERRAR SESIÓN");
        JMenuItem registrarUsuarioItem = new JMenuItem("REGISTRAR USUARIO");
        archivoMenu.add(homeItem);
        archivoMenu.add(cerrarSesionItem);
        archivoMenu.add(registrarUsuarioItem);

        JMenu mascotasMenu = new JMenu("Mascotas");
        JMenuItem listarMascotasItem = new JMenuItem("MASCOTAS");
        JMenuItem citasItem = new JMenuItem("CITAS");
        JMenuItem serviciosItem = new JMenuItem("SERVICIOS");
        JMenuItem atencionMedicaItem = new JMenuItem("ATENCION MEDICA");
        JMenuItem historialItem = new JMenuItem("Historial Médico");
        mascotasMenu.add(listarMascotasItem);
        mascotasMenu.add(citasItem);
        mascotasMenu.add(serviciosItem);
        mascotasMenu.add(atencionMedicaItem);
        mascotasMenu.add(historialItem);

        JMenu personasMenu = new JMenu("Personas");
        JMenuItem clientesItem = new JMenuItem("Clientes");
        JMenuItem veterinariosItem = new JMenuItem("Veterinarios");
        personasMenu.add(clientesItem);
        personasMenu.add(veterinariosItem);

        JMenu facturacionMenu = new JMenu("Facturación");
        JMenuItem facturasItem = new JMenuItem("Facturas");
        facturacionMenu.add(facturasItem);

        JMenu opcionesMenu = new JMenu("Opciones");
        JMenuItem modoClaroItem = new JMenuItem("MODO CLARO");
        JMenuItem modoOscuroItem = new JMenuItem("MODO OSCURO");
        opcionesMenu.add(modoClaroItem);
        opcionesMenu.add(modoOscuroItem);

        JMenu reportesMenu = new JMenu("Reportes");
        JMenuItem reporteItem = new JMenuItem("Reporte");
        reportesMenu.add(reporteItem);

        menuBar.add(archivoMenu);
        menuBar.add(mascotasMenu);
        menuBar.add(personasMenu);
        menuBar.add(facturacionMenu);
        menuBar.add(opcionesMenu);
        menuBar.add(reportesMenu);
        setJMenuBar(menuBar);

        // Panel de controles (HOME)
        panelControles = new JPanel(new BorderLayout());
        panelControles.setBackground(Color.WHITE);

        lblTitulo = new JLabel("Sistema de Gestión Veterinaria", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 24));
        panelControles.add(lblTitulo, BorderLayout.NORTH);

        panelLogo = new JPanel();
        panelLogo.setOpaque(false);
        try {
            URL imgUrl = getClass().getResource("/images/logo.png");
            if (imgUrl != null) {
                ImageIcon logoIcon = new ImageIcon(imgUrl);
                Image scaledImage = logoIcon.getImage().getScaledInstance(150, 80, Image.SCALE_SMOOTH);
                JLabel logoLabel = new JLabel(new ImageIcon(scaledImage));
                panelLogo.add(logoLabel);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        panelControles.add(panelLogo, BorderLayout.CENTER);

        panelButtons = new JPanel(new GridLayout(2, 2, 20, 20));
        panelButtons.setBorder(BorderFactory.createEmptyBorder(50, 100, 50, 100));
        panelButtons.setOpaque(false);
        btnClientes = new JButton("Clientes");
        btnMascotas = new JButton("Mascotas");
        btnCitas = new JButton("Citas");
        btnFacturas = new JButton("Facturas");
        Font buttonFont = new Font("Arial", Font.BOLD, 18);
        for (JButton btn : new JButton[]{btnClientes, btnMascotas, btnCitas, btnFacturas}) {
            btn.setFont(buttonFont);
            btn.setPreferredSize(new Dimension(200, 100));
        }
        panelButtons.add(btnClientes);
        panelButtons.add(btnMascotas);
        panelButtons.add(btnCitas);
        panelButtons.add(btnFacturas);
        panelControles.add(panelButtons, BorderLayout.SOUTH);

        panelSearch = new JPanel(new FlowLayout());
        panelSearch.setOpaque(false);
        searchBox = new JTextField(30);
        btnBuscar = new JButton("Buscar");
        panelSearch.add(searchBox);
        panelSearch.add(btnBuscar);
        panelControles.add(panelSearch, BorderLayout.NORTH);

        add(panelControles);

        // Event Listeners
        btnClientes.addActionListener(e -> abrirFormulario("frmCliente"));
        btnMascotas.addActionListener(e -> abrirFormulario("frmMascota"));
        btnCitas.addActionListener(e -> abrirFormulario("frmCita"));
        btnFacturas.addActionListener(e -> seleccionarClienteYMostrarFactura());

        clientesItem.addActionListener(e -> abrirFormulario("frmCliente"));
        listarMascotasItem.addActionListener(e -> abrirFormulario("frmMascota"));
        citasItem.addActionListener(e -> abrirFormulario("frmCita"));
        facturasItem.addActionListener(e -> seleccionarClienteYMostrarFactura());
        veterinariosItem.addActionListener(e -> abrirFormulario("frmVeterinario"));
        serviciosItem.addActionListener(e -> abrirFormulario("frmServicio"));
        atencionMedicaItem.addActionListener(e -> abrirFormulario("frmAtencionMedica"));
        historialItem.addActionListener(e -> abrirFormulario("frmHistorialMedico"));
        reporteItem.addActionListener(e -> abrirFormulario("frmReporte"));
        registrarUsuarioItem.addActionListener(e -> abrirFormulario("frmRegistrarme"));

        modoClaroItem.addActionListener(e -> aplicarTema(false));
        modoOscuroItem.addActionListener(e -> aplicarTema(true));
        homeItem.addActionListener(e -> mostrarHome());
        cerrarSesionItem.addActionListener(e -> cerrarSesion());

        btnBuscar.addActionListener(e -> buscar());
    }
    
    private void abrirFormulario(String formName) {
        JFrame form = null;
        switch (formName.toLowerCase()) {
            case "frmcliente":
                form = new frmCliente(isDarkMode);
                break;
            case "frmmascota":
                form = new frmMascota();
                break;
            case "frmcita":
                form = new frmCita(nombreUsuario, idUsuario);
                break;
            case "frmveterinario":
                form = new frmVeterinario(isDarkMode);
                break;
            case "frmservicio":
                form = new frmServicio();
                break;
            case "frmatencionmedica":
                form = new frmAtencionMedica(nombreUsuario, idUsuario);
                break;
            case "frmhistorialmedico":
                form = new frmHistorialMedico();
                break;
            case "frmreporte":
                form = new frmReporte(idUsuario);
                break;
            case "frmregistrarme":
                form = new frmRegistrarme();
                break;
        }
        if (form != null) {
            panelControles.setVisible(false);
            form.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            form.addWindowListener(new java.awt.event.WindowAdapter() {
                public void windowClosed(java.awt.event.WindowEvent e) {
                    mostrarHome();
                }
            });
            form.setVisible(true);
        }
    }
    
    private void seleccionarClienteYMostrarFactura() {
        frmSeleccionarCedula dialog = new frmSeleccionarCedula();
        dialog.setVisible(true);
        if (dialog.isConfirmed()) {
            String cedula = dialog.getCedulaSeleccionada();
            if (cedula != null && !cedula.isEmpty()) {
                panelControles.setVisible(false);
                frmFactura facturaForm = new frmFactura(cedula);
                facturaForm.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                facturaForm.addWindowListener(new java.awt.event.WindowAdapter() {
                    public void windowClosed(java.awt.event.WindowEvent e) {
                        mostrarHome();
                    }
                });
                facturaForm.setVisible(true);
            }
        }
    }
    
    private void mostrarHome() {
        for (Window window : Window.getWindows()) {
            if (window instanceof JFrame && window != this && window.isVisible()) {
                window.dispose();
            }
        }
        panelControles.setVisible(true);
    }
    
    private void cerrarSesion() {
        int confirm = JOptionPane.showConfirmDialog(this, "¿Estás seguro de que deseas cerrar la sesión?", 
            "Cerrar Sesión", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            dispose();
            frmLogin login = new frmLogin();
            login.setVisible(true);
        }
    }
    
    private void buscar() {
        String texto = searchBox.getText().toLowerCase();
        switch (texto) {
            case "clientes":
                abrirFormulario("frmCliente");
                break;
            case "mascotas":
                abrirFormulario("frmMascota");
                break;
            case "citas":
                abrirFormulario("frmCita");
                break;
            case "facturas":
                seleccionarClienteYMostrarFactura();
                break;
            default:
                JOptionPane.showMessageDialog(this, "No se encontró su búsqueda. Intente con: clientes, mascotas, citas, facturas", 
                    "Búsqueda sin resultados", JOptionPane.INFORMATION_MESSAGE);
                break;
        }
        searchBox.setText("");
    }
    
    private void aplicarTema(boolean darkMode) {
        isDarkMode = darkMode;
        Color bgColor = darkMode ? Color.DARK_GRAY : Color.WHITE;
        Color fgColor = darkMode ? Color.WHITE : Color.BLACK;
        getContentPane().setBackground(bgColor);
        panelControles.setBackground(bgColor);
        lblTitulo.setForeground(fgColor);
        for (JButton btn : new JButton[]{btnClientes, btnMascotas, btnCitas, btnFacturas}) {
            btn.setBackground(darkMode ? Color.GRAY : new Color(240, 240, 240));
            btn.setForeground(fgColor);
        }
    }
}