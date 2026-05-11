package com.mycompany.veterinaria.grupo4.view.factura;

import com.mycompany.veterinaria.grupo4.model.entity.Factura;
import com.mycompany.veterinaria.grupo4.service.FacturaService;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;

public class frmDetalleFactura extends JDialog {
    private FacturaService facturaService;
    private int idFactura;
    
    private JTextField txtNumeroFactura, txtFecha, txtSubtotal, txtIVA, txtTotal, txtEstado, txtMetodoPago;
    private JTextField txtCliente, txtCedula, txtTelefono, txtDireccion, txtEmail;
    private JTable tblServicios, tblMascotas, tblMedicamentos, tblInstrumentos, tblVacunas;
    private DefaultTableModel modelServicios, modelMascotas, modelMedicamentos, modelInstrumentos, modelVacunas;
    
    public frmDetalleFactura(Window parent, int idFactura) {
        super(parent, "Detalle de Factura #" + idFactura, ModalityType.APPLICATION_MODAL);
        this.idFactura = idFactura;
        this.facturaService = new FacturaService();
        setSize(1100, 750);
        setLocationRelativeTo(parent);
        initComponents();
        cargarDetalleFactura();
    }
    
    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JPanel clientePanel = new JPanel(new GridBagLayout());
        clientePanel.setBorder(BorderFactory.createTitledBorder("Datos del Cliente"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        addField(clientePanel, gbc, "Cliente:", txtCliente = createReadOnlyTextField(), 0, 0, 1, 1);
        addField(clientePanel, gbc, "Cédula:", txtCedula = createReadOnlyTextField(), 2, 0, 1, 1);
        addField(clientePanel, gbc, "Teléfono:", txtTelefono = createReadOnlyTextField(), 0, 1, 1, 1);
        addField(clientePanel, gbc, "Email:", txtEmail = createReadOnlyTextField(), 2, 1, 1, 1);
        addField(clientePanel, gbc, "Dirección:", txtDireccion = createReadOnlyTextField(), 0, 2, 4, 1);
        
        mainPanel.add(clientePanel, BorderLayout.NORTH);
        
        JPanel facturaPanel = new JPanel(new GridBagLayout());
        facturaPanel.setBorder(BorderFactory.createTitledBorder("Datos de Factura"));
        GridBagConstraints gbcF = new GridBagConstraints();
        gbcF.insets = new Insets(5, 5, 5, 5);
        gbcF.fill = GridBagConstraints.HORIZONTAL;
        
        addField(facturaPanel, gbcF, "N° Factura:", txtNumeroFactura = createReadOnlyTextField(), 0, 0, 1, 1);
        addField(facturaPanel, gbcF, "Fecha:", txtFecha = createReadOnlyTextField(), 2, 0, 1, 1);
        addField(facturaPanel, gbcF, "Subtotal:", txtSubtotal = createReadOnlyTextField(), 0, 1, 1, 1);
        addField(facturaPanel, gbcF, "IVA:", txtIVA = createReadOnlyTextField(), 2, 1, 1, 1);
        addField(facturaPanel, gbcF, "Total:", txtTotal = createReadOnlyTextField(), 0, 2, 1, 1);
        addField(facturaPanel, gbcF, "Estado:", txtEstado = createReadOnlyTextField(), 2, 2, 1, 1);
        addField(facturaPanel, gbcF, "Método Pago:", txtMetodoPago = createReadOnlyTextField(), 0, 3, 4, 1);
        
        mainPanel.add(facturaPanel, BorderLayout.CENTER);
        
        JTabbedPane tabbedPane = new JTabbedPane();
        
        modelServicios = new DefaultTableModel(new String[]{"Servicio", "Descripción", "Precio Unitario", "Total", "Veterinario"}, 0);
        tblServicios = new JTable(modelServicios);
        tabbedPane.addTab("Servicios", new JScrollPane(tblServicios));
        
        modelMascotas = new DefaultTableModel(new String[]{"Mascota", "Especie", "Raza", "Diagnóstico", "Tratamiento"}, 0);
        tblMascotas = new JTable(modelMascotas);
        tabbedPane.addTab("Mascotas", new JScrollPane(tblMascotas));
        
        modelMedicamentos = new DefaultTableModel(new String[]{"Medicamento", "Dosis", "Frecuencia", "Duración", "Precio"}, 0);
        tblMedicamentos = new JTable(modelMedicamentos);
        tabbedPane.addTab("Medicamentos", new JScrollPane(tblMedicamentos));
        
        modelInstrumentos = new DefaultTableModel(new String[]{"Instrumento", "Costo Uso"}, 0);
        tblInstrumentos = new JTable(modelInstrumentos);
        tabbedPane.addTab("Instrumentos", new JScrollPane(tblInstrumentos));
        
        modelVacunas = new DefaultTableModel(new String[]{"Vacuna", "Fecha Aplicación", "Próxima Aplicación", "Descripción"}, 0);
        tblVacunas = new JTable(modelVacunas);
        tabbedPane.addTab("Vacunas", new JScrollPane(tblVacunas));
        
        mainPanel.add(tabbedPane, BorderLayout.SOUTH);
        add(mainPanel, BorderLayout.CENTER);
        
        JPanel buttonPanel = new JPanel();
        JButton btnCerrar = new JButton("Cerrar");
        btnCerrar.addActionListener(e -> dispose());
        buttonPanel.add(btnCerrar);
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    private void addField(JPanel panel, GridBagConstraints gbc, String label, JTextField field, 
                          int gridx, int gridy, int gridwidth, int gridheight) {
        gbc.gridx = gridx;
        gbc.gridy = gridy;
        gbc.gridwidth = 1;
        panel.add(new JLabel(label), gbc);
        gbc.gridx = gridx + 1;
        gbc.gridwidth = gridwidth;
        panel.add(field, gbc);
    }
    
    private JTextField createReadOnlyTextField() {
        JTextField field = new JTextField(15);
        field.setEditable(false);
        return field;
    }
    
    private void cargarDetalleFactura() {
        Factura factura = facturaService.obtenerDetalle(idFactura);
        if (factura != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            txtNumeroFactura.setText(String.valueOf(factura.getIdFactura()));
            txtFecha.setText(sdf.format(factura.getFecha()));
            txtSubtotal.setText(String.format("$%.2f", factura.getSubtotal()));
            txtIVA.setText(String.format("$%.2f", factura.getIva()));
            txtTotal.setText(String.format("$%.2f", factura.getTotal()));
            txtEstado.setText(factura.getEstado());
            txtMetodoPago.setText(factura.getMetodoPago() != null ? factura.getMetodoPago() : "EFECTIVO");
            
            if (factura.getCliente() != null) {
                txtCliente.setText(factura.getCliente().getNombre() + " " + factura.getCliente().getApellido());
                txtCedula.setText(factura.getCliente().getCedula());
                txtTelefono.setText(factura.getCliente().getTelefono());
                txtEmail.setText(factura.getCliente().getCorreoElectronico());
                txtDireccion.setText(factura.getCliente().getDireccion() != null ? factura.getCliente().getDireccion() : "");
            }
            
            if (factura.getServicios() != null) {
                for (Object[] servicio : factura.getServicios()) {
                    modelServicios.addRow(servicio);
                }
            }
            
            if (factura.getMascotas() != null) {
                for (Object[] mascota : factura.getMascotas()) {
                    modelMascotas.addRow(mascota);
                }
            }
            
            if (factura.getMedicamentos() != null) {
                for (Object[] medicamento : factura.getMedicamentos()) {
                    modelMedicamentos.addRow(medicamento);
                }
            }
            
            if (factura.getInstrumentos() != null) {
                for (Object[] instrumento : factura.getInstrumentos()) {
                    modelInstrumentos.addRow(instrumento);
                }
            }
            
            if (factura.getVacunas() != null) {
                SimpleDateFormat sdfDate = new SimpleDateFormat("dd/MM/yyyy");
                for (Object[] vacuna : factura.getVacunas()) {
                    modelVacunas.addRow(vacuna);
                }
            }
        }
    }
}