package com.mycompany.veterinaria.grupo4.view;

import com.mycompany.veterinaria.grupo4.model.entity.*;
import com.mycompany.veterinaria.grupo4.service.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.List;

public class frmHistorialMedico extends JFrame {
    private MascotaService mascotaService = new MascotaService();
    private HistorialService historialService = new HistorialService();
    private FichaMedicaService fichaMedicaService = new FichaMedicaService();
    private AtencionMedicaService atencionService = new AtencionMedicaService();
    private MedicamentoService medicamentoService = new MedicamentoService();
    private InstrumentoMedicoService instrumentoService = new InstrumentoMedicoService();
    
    private JTextField txtNombreMascota, txtEspecie, txtRaza, txtSexo, txtFechaNacimiento;
    private JTextField txtNombreCliente, txtCedulaCliente, txtTelefonoCliente;
    private JTextArea txtAlergias, txtEnfermedadesCronicas, txtObservacionesFicha;
    private JTable tblHistorial, tblFichaMedica, tblVacunas;
    private DefaultTableModel modelHistorial, modelFicha, modelVacunas;
    private JButton btnBuscarMascota, btnActualizarFicha;
    private JLabel lblFoto;
    private int idMascotaSeleccionada = 0;
    
    public frmHistorialMedico() {
        initComponents();
        setLocationRelativeTo(null);
    }
    
    private void initComponents() {
        setTitle("Historial Médico de Mascota");
        setSize(1100, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Panel superior - búsqueda
        JPanel busquedaPanel = new JPanel(new FlowLayout());
        btnBuscarMascota = new JButton("Buscar Mascota");
        busquedaPanel.add(btnBuscarMascota);
        mainPanel.add(busquedaPanel, BorderLayout.NORTH);
        
        // Panel de datos de mascota
        JPanel mascotaPanel = new JPanel(new GridBagLayout());
        mascotaPanel.setBorder(BorderFactory.createTitledBorder("Datos de la Mascota"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        int row = 0;
        gbc.gridx = 0; gbc.gridy = row;
        mascotaPanel.add(new JLabel("Nombre:"), gbc);
        gbc.gridx = 1;
        txtNombreMascota = new JTextField(15);
        txtNombreMascota.setEditable(false);
        mascotaPanel.add(txtNombreMascota, gbc);
        
        gbc.gridx = 2;
        mascotaPanel.add(new JLabel("Especie:"), gbc);
        gbc.gridx = 3;
        txtEspecie = new JTextField(15);
        txtEspecie.setEditable(false);
        mascotaPanel.add(txtEspecie, gbc);
        
        row++;
        gbc.gridx = 0; gbc.gridy = row;
        mascotaPanel.add(new JLabel("Raza:"), gbc);
        gbc.gridx = 1;
        txtRaza = new JTextField(15);
        txtRaza.setEditable(false);
        mascotaPanel.add(txtRaza, gbc);
        
        gbc.gridx = 2;
        mascotaPanel.add(new JLabel("Sexo:"), gbc);
        gbc.gridx = 3;
        txtSexo = new JTextField(10);
        txtSexo.setEditable(false);
        mascotaPanel.add(txtSexo, gbc);
        
        row++;
        gbc.gridx = 0; gbc.gridy = row;
        mascotaPanel.add(new JLabel("Fecha Nacimiento:"), gbc);
        gbc.gridx = 1;
        txtFechaNacimiento = new JTextField(15);
        txtFechaNacimiento.setEditable(false);
        mascotaPanel.add(txtFechaNacimiento, gbc);
        
        gbc.gridx = 2;
        lblFoto = new JLabel();
        lblFoto.setPreferredSize(new Dimension(100, 100));
        lblFoto.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        mascotaPanel.add(lblFoto, gbc);
        
        mainPanel.add(mascotaPanel, BorderLayout.NORTH);
        
        // Panel de datos del propietario
        JPanel propietarioPanel = new JPanel(new GridBagLayout());
        propietarioPanel.setBorder(BorderFactory.createTitledBorder("Datos del Propietario"));
        GridBagConstraints gbcP = new GridBagConstraints();
        gbcP.insets = new Insets(5, 5, 5, 5);
        gbcP.fill = GridBagConstraints.HORIZONTAL;
        
        int pRow = 0;
        gbcP.gridx = 0; gbcP.gridy = pRow;
        propietarioPanel.add(new JLabel("Nombre:"), gbcP);
        gbcP.gridx = 1;
        txtNombreCliente = new JTextField(20);
        txtNombreCliente.setEditable(false);
        propietarioPanel.add(txtNombreCliente, gbcP);
        
        gbcP.gridx = 2;
        propietarioPanel.add(new JLabel("Cédula:"), gbcP);
        gbcP.gridx = 3;
        txtCedulaCliente = new JTextField(15);
        txtCedulaCliente.setEditable(false);
        propietarioPanel.add(txtCedulaCliente, gbcP);
        
        pRow++;
        gbcP.gridx = 0; gbcP.gridy = pRow;
        propietarioPanel.add(new JLabel("Teléfono:"), gbcP);
        gbcP.gridx = 1;
        txtTelefonoCliente = new JTextField(15);
        txtTelefonoCliente.setEditable(false);
        propietarioPanel.add(txtTelefonoCliente, gbcP);
        
        mainPanel.add(propietarioPanel, BorderLayout.CENTER);
        
        // TabPanel para historial, ficha y vacunas
        JTabbedPane tabbedPane = new JTabbedPane();
        
        // Historial médico
        JPanel historialPanel = new JPanel(new BorderLayout());
        modelHistorial = new DefaultTableModel(new String[]{"Fecha", "Servicio", "Veterinario", "Diagnóstico", "Tratamiento"}, 0);
        tblHistorial = new JTable(modelHistorial);
        historialPanel.add(new JScrollPane(tblHistorial), BorderLayout.CENTER);
        tabbedPane.addTab("Historial Médico", historialPanel);
        
        // Ficha médica
        JPanel fichaPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbcF = new GridBagConstraints();
        gbcF.insets = new Insets(5, 5, 5, 5);
        gbcF.fill = GridBagConstraints.BOTH;
        
        gbcF.gridx = 0; gbcF.gridy = 0;
        fichaPanel.add(new JLabel("Alergias:"), gbcF);
        gbcF.gridx = 1;
        txtAlergias = new JTextArea(3, 30);
        txtAlergias.setEditable(false);
        fichaPanel.add(new JScrollPane(txtAlergias), gbcF);
        
        gbcF.gridx = 0; gbcF.gridy = 1;
        fichaPanel.add(new JLabel("Enfermedades Crónicas:"), gbcF);
        gbcF.gridx = 1;
        txtEnfermedadesCronicas = new JTextArea(3, 30);
        txtEnfermedadesCronicas.setEditable(false);
        fichaPanel.add(new JScrollPane(txtEnfermedadesCronicas), gbcF);
        
        gbcF.gridx = 0; gbcF.gridy = 2;
        fichaPanel.add(new JLabel("Observaciones:"), gbcF);
        gbcF.gridx = 1;
        txtObservacionesFicha = new JTextArea(3, 30);
        txtObservacionesFicha.setEditable(false);
        fichaPanel.add(new JScrollPane(txtObservacionesFicha), gbcF);
        
        gbcF.gridx = 0; gbcF.gridy = 3;
        gbcF.gridwidth = 2;
        btnActualizarFicha = new JButton("Actualizar Ficha Médica");
        fichaPanel.add(btnActualizarFicha, gbcF);
        
        tabbedPane.addTab("Ficha Médica", fichaPanel);
        
        // Vacunas
        JPanel vacunasPanel = new JPanel(new BorderLayout());
        modelVacunas = new DefaultTableModel(new String[]{"Vacuna", "Fecha Aplicación", "Próxima Aplicación", "Descripción"}, 0);
        tblVacunas = new JTable(modelVacunas);
        vacunasPanel.add(new JScrollPane(tblVacunas), BorderLayout.CENTER);
        tabbedPane.addTab("Vacunas", vacunasPanel);
        
        mainPanel.add(tabbedPane, BorderLayout.SOUTH);
        
        add(mainPanel);
        
        // Eventos
        btnBuscarMascota.addActionListener(e -> buscarMascota());
        btnActualizarFicha.addActionListener(e -> actualizarFicha());
    }
    
    private void buscarMascota() {
        frmBuscarMascota frm = new frmBuscarMascota();
        frm.setVisible(true);
        if (frm.isConfirmed()) {
            idMascotaSeleccionada = frm.getMascota().getIdMascota();
            cargarDatosMascota();
            cargarHistorial();
            cargarFichaMedica();
            cargarVacunas();
        }
    }
    
    private void cargarDatosMascota() {
        Mascota m = mascotaService.obtenerPorId(idMascotaSeleccionada);
        if (m != null) {
            txtNombreMascota.setText(m.getNombre());
            txtEspecie.setText(m.getEspecie());
            txtRaza.setText(m.getRaza());
            txtSexo.setText(m.getSexo() == 'M' ? "Macho" : "Hembra");
            if (m.getFechaNacimiento() != null) {
                txtFechaNacimiento.setText(new SimpleDateFormat("dd/MM/yyyy").format(m.getFechaNacimiento()));
            }
            
            Cliente cliente = null; // Obtener cliente por ID
            if (cliente != null) {
                txtNombreCliente.setText(cliente.getNombre() + " " + cliente.getApellido());
                txtCedulaCliente.setText(cliente.getCedula());
                txtTelefonoCliente.setText(cliente.getTelefono());
            }
            
            // Cargar foto
            byte[] foto = mascotaService.obtenerFoto(idMascotaSeleccionada);
            if (foto != null) {
                lblFoto.setIcon(new ImageIcon(new ImageIcon(foto).getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH)));
            }
        }
    }
    
    private void cargarHistorial() {
        modelHistorial.setRowCount(0);
        List<HistorialMedico> historial = historialService.obtenerPorMascota(idMascotaSeleccionada);
        if (historial != null) {
            for (HistorialMedico h : historial) {
                modelHistorial.addRow(new Object[]{
                    new SimpleDateFormat("dd/MM/yyyy HH:mm").format(h.getFecha()),
                    h.getNombreServicio(),
                    h.getNombreVeterinario(),
                    h.getDiagnostico(),
                    h.getTratamiento()
                });
            }
        }
    }
    
    private void cargarFichaMedica() {
        FichaMedica ficha = fichaMedicaService.obtenerPorMascota(idMascotaSeleccionada);
        if (ficha != null) {
            txtAlergias.setText(ficha.getAlergias());
            txtEnfermedadesCronicas.setText(ficha.getEnfermedadesCronicas());
            txtObservacionesFicha.setText(ficha.getObservaciones());
        }
    }
    
    private void cargarVacunas() {
        modelVacunas.setRowCount(0);
        // Aquí cargarías las vacunas de la mascota
    }
    
    private void actualizarFicha() {
        if (idMascotaSeleccionada > 0) {
            frmEditarFichaMedica frm = new frmEditarFichaMedica(idMascotaSeleccionada);
            frm.setVisible(true);
            cargarFichaMedica();
        }
    }
}