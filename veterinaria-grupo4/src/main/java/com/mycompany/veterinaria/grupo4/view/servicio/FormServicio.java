package com.mycompany.veterinaria.grupo4.view.servicio;

import com.mycompany.veterinaria.grupo4.model.entity.Servicio;
import com.mycompany.veterinaria.grupo4.model.entity.Veterinario;
import com.mycompany.veterinaria.grupo4.view.swing.Button;
import com.mycompany.veterinaria.grupo4.view.swing.MyTextField;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.RenderingHints;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.awt.geom.RoundRectangle2D;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;

public class FormServicio extends JDialog {

    private static final Dimension FIELD_SIZE = new Dimension(0, 36);
    private JPanel panelSeccionVeterinarios;

    // Campos del servicio
    private JTextField txtId;
    private MyTextField txtNombre;
    private MyTextField txtDescripcion;
    private MyTextField txtPrecio;
    private MyTextField txtDuracion;
    private JCheckBox chkEstado;

    // Tablas de veterinarios
    private JTable tblVeterinariosAsignados;
    private JTable tblVeterinariosDisponibles;
    private DefaultTableModel modelAsignados;
    private DefaultTableModel modelDisponibles;

    // Botones
    private Button btnAsignar;
    private Button btnQuitar;
    private Button btnAccion;
    private Button btnCancelar;
    private Button btnAbrirVeterinario;

    private boolean modoEdicion = false;
    private Servicio servicioActual;
    private int idServicioSeleccionado = 0;

    public FormServicio(Frame parent) {
        super(parent, true);
        init("Registrar Servicio", "Registrar");
    }

    public FormServicio(Frame parent, Servicio servicio) {
        super(parent, true);
        this.modoEdicion = true;
        this.servicioActual = servicio;
        this.idServicioSeleccionado = servicio.getIdServicio();
        init("Editar Servicio", "Actualizar");
        cargarDatosServicio();
    }

    private void init(String titulo, String labelBoton) {
        setUndecorated(true);
        setBackground(new Color(0, 0, 0, 0));
        setSize(800, 650);
        setLocationRelativeTo(getParent());

        JPanel root = buildRoot();
        root.add(buildHeader(titulo), BorderLayout.NORTH);
        root.add(buildCuerpo(), BorderLayout.CENTER);
        root.add(buildFooter(labelBoton), BorderLayout.SOUTH);
        setContentPane(root);
        
        configurarValidaciones();
    }

    private JPanel buildRoot() {
        JPanel root = new JPanel(new BorderLayout(0, 8)) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                for (int i = 8; i >= 1; i--) {
                    g2.setColor(new Color(0, 0, 0, i * 6));
                    g2.fill(new RoundRectangle2D.Float(i, i,
                        getWidth() - i * 2, getHeight() - i * 2, 20, 20));
                }
                g2.setColor(UIManager.getColor("Panel.background"));
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 20, 20));
                g2.setColor(new Color(230, 140, 30, 80));
                g2.setStroke(new BasicStroke(1.5f));
                g2.draw(new RoundRectangle2D.Float(1, 1, getWidth() - 2, getHeight() - 2, 20, 20));
                g2.dispose();
            }
        };
        root.setOpaque(false);
        root.setBorder(new EmptyBorder(20, 28, 20, 28));
        return root;
    }

    private JPanel buildHeader(String titulo) {
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        header.setBorder(new EmptyBorder(0, 0, 8, 0));

        JLabel lbl = new JLabel(titulo);
        lbl.setFont(new Font("SansSerif", Font.BOLD, 18));
        lbl.setForeground(UIManager.getColor("Label.foreground"));

        JButton btnX = new JButton("✕");
        btnX.setFocusPainted(false);
        btnX.setBorderPainted(false);
        btnX.setContentAreaFilled(false);
        btnX.setForeground(new Color(150, 150, 150));
        btnX.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnX.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnX.addActionListener(e -> dispose());

        header.add(lbl, BorderLayout.WEST);
        header.add(btnX, BorderLayout.EAST);
        return header;
    }

    private JPanel buildCuerpo() {
    JPanel panel = new JPanel();
    panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
    panel.setOpaque(false);

    // ID (oculto)
    txtId = new JTextField();
    txtId.setVisible(false);
    panel.add(txtId);

    // Nombre
    txtNombre = field("Nombre del servicio *");
    txtNombre.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
    panel.add(wrap("Nombre", txtNombre));
    panel.add(Box.createVerticalStrut(8));

    // Descripción
    txtDescripcion = field("Descripción");
    txtDescripcion.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
    panel.add(wrap("Descripción", txtDescripcion));
    panel.add(Box.createVerticalStrut(8));

    // Precio y Duración
    txtPrecio = field("0.00");
    txtDuracion = field("30");
    panel.add(wrapDos("Precio ($)", txtPrecio, "Duración (min)", txtDuracion));
    panel.add(Box.createVerticalStrut(8));

    // Estado
    chkEstado = new JCheckBox("Activo", true);
    chkEstado.setOpaque(false);
    panel.add(wrap("Estado", chkEstado));
    panel.add(Box.createVerticalStrut(12));

    // Sección de veterinarios - guardar referencia
    panelSeccionVeterinarios = buildSeccionVeterinarios();
    panel.add(panelSeccionVeterinarios);

    return panel;
}

    private JPanel buildSeccionVeterinarios() {
        JPanel section = new JPanel();
        section.setLayout(new BoxLayout(section, BoxLayout.Y_AXIS));
        section.setOpaque(false);

        JLabel titulo = new JLabel("Veterinarios Asignados");
        titulo.setFont(new Font("SansSerif", Font.BOLD, 12));
        titulo.setForeground(new Color(230, 140, 30));
        titulo.setAlignmentX(Component.LEFT_ALIGNMENT);
        section.add(titulo);
        section.add(Box.createVerticalStrut(6));

        // Panel de botones de asignación
        JPanel botonesPanel = new JPanel(new GridLayout(1, 3, 10, 0));
        botonesPanel.setOpaque(false);
        botonesPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        
        btnAsignar = new Button();
        btnAsignar.setText("→ Asignar →");
        btnAsignar.setBackground(new Color(230, 140, 30));
        btnAsignar.setForeground(Color.WHITE);
        
        btnQuitar = new Button();
        btnQuitar.setText("← Quitar ←");
        btnQuitar.setBackground(new Color(200, 60, 60));
        btnQuitar.setForeground(Color.WHITE);
        
        btnAbrirVeterinario = new Button();
        btnAbrirVeterinario.setText("+ Veterinario");
        btnAbrirVeterinario.setBackground(new Color(100, 100, 100));
        btnAbrirVeterinario.setForeground(Color.WHITE);
        
        botonesPanel.add(btnAsignar);
        botonesPanel.add(btnQuitar);
        botonesPanel.add(btnAbrirVeterinario);
        section.add(botonesPanel);
        section.add(Box.createVerticalStrut(6));

        // Panel de tablas
        JPanel tablasPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        tablasPanel.setOpaque(false);
        tablasPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 300));

        // Veterinarios Asignados
        JPanel asignadosPanel = new JPanel(new BorderLayout());
        asignadosPanel.setOpaque(false);
        asignadosPanel.add(new JLabel("Asignados:"), BorderLayout.NORTH);
        
        modelAsignados = new DefaultTableModel(new String[]{"ID", "Nombre", "Especialidad", "Fecha Asignación"}, 0) {
            @Override public boolean isCellEditable(int row, int col) { return false; }
        };
        tblVeterinariosAsignados = new JTable(modelAsignados);
        tblVeterinariosAsignados.getColumnModel().getColumn(0).setMinWidth(0);
        tblVeterinariosAsignados.getColumnModel().getColumn(0).setMaxWidth(0);
        asignadosPanel.add(new JScrollPane(tblVeterinariosAsignados), BorderLayout.CENTER);

        // Veterinarios Disponibles
        JPanel disponiblesPanel = new JPanel(new BorderLayout());
        disponiblesPanel.setOpaque(false);
        disponiblesPanel.add(new JLabel("Disponibles:"), BorderLayout.NORTH);
        
        modelDisponibles = new DefaultTableModel(new String[]{"ID", "Nombre", "Especialidad"}, 0) {
            @Override public boolean isCellEditable(int row, int col) { return false; }
        };
        tblVeterinariosDisponibles = new JTable(modelDisponibles);
        tblVeterinariosDisponibles.getColumnModel().getColumn(0).setMinWidth(0);
        tblVeterinariosDisponibles.getColumnModel().getColumn(0).setMaxWidth(0);
        disponiblesPanel.add(new JScrollPane(tblVeterinariosDisponibles), BorderLayout.CENTER);

        tablasPanel.add(asignadosPanel);
        tablasPanel.add(disponiblesPanel);
        section.add(tablasPanel);

        return section;
    }

    private JPanel buildFooter(String labelBoton) {
        JPanel footer = new JPanel(new GridLayout(1, 2, 10, 0));
        footer.setOpaque(false);
        footer.setBorder(new EmptyBorder(10, 0, 0, 0));

        btnCancelar = new Button();
        btnCancelar.setText("Cancelar");
        btnCancelar.putClientProperty("JButton.buttonType", "borderless");
        btnCancelar.addActionListener(e -> dispose());

        btnAccion = new Button();
        btnAccion.setText(labelBoton);
        btnAccion.setBackground(new Color(230, 140, 30));
        btnAccion.setForeground(Color.WHITE);

        footer.add(btnCancelar);
        footer.add(btnAccion);
        return footer;
    }

    private void configurarValidaciones() {
        // Validar solo números para precio
        JTextField precioField = txtPrecio;
        precioField.getDocument().addDocumentListener(new DocumentListener() {
            @Override public void insertUpdate(DocumentEvent e) { validarPrecio(); }
            @Override public void removeUpdate(DocumentEvent e) { validarPrecio(); }
            @Override public void changedUpdate(DocumentEvent e) { validarPrecio(); }
            
            private void validarPrecio() {
                String texto = precioField.getText();
                if (!texto.isEmpty()) {
                    try {
                        NumberFormat.getInstance().parse(texto);
                        precioField.setForeground(UIManager.getColor("TextField.foreground"));
                    } catch (ParseException ex) {
                        precioField.setForeground(Color.RED);
                    }
                } else {
                    precioField.setForeground(UIManager.getColor("TextField.foreground"));
                }
            }
        });

        // Validar solo números para duración
        JTextField duracionField = txtDuracion;
        duracionField.getDocument().addDocumentListener(new DocumentListener() {
            @Override public void insertUpdate(DocumentEvent e) { validarDuracion(); }
            @Override public void removeUpdate(DocumentEvent e) { validarDuracion(); }
            @Override public void changedUpdate(DocumentEvent e) { validarDuracion(); }
            
            private void validarDuracion() {
                String texto = duracionField.getText();
                if (!texto.isEmpty()) {
                    try {
                        Integer.parseInt(texto);
                        duracionField.setForeground(UIManager.getColor("TextField.foreground"));
                    } catch (NumberFormatException ex) {
                        duracionField.setForeground(Color.RED);
                    }
                } else {
                    duracionField.setForeground(UIManager.getColor("TextField.foreground"));
                }
            }
        });
    }

    private void cargarDatosServicio() {
        if (servicioActual != null) {
            txtId.setText(String.valueOf(servicioActual.getIdServicio()));
            txtNombre.setText(servicioActual.getNombreServicio());
            txtDescripcion.setText(servicioActual.getDescripcion());
            txtPrecio.setText(String.valueOf(servicioActual.getPrecio()));
            txtDuracion.setText(String.valueOf(servicioActual.getDuracionEstimada()));
            chkEstado.setSelected(servicioActual.isEstado());
        }
    }

    public void cargarVeterinariosAsignados(List<Veterinario> veterinarios) {
    modelAsignados.setRowCount(0);
    if (veterinarios != null && !veterinarios.isEmpty()) {
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy");
        for (Veterinario v : veterinarios) {
            String especialidad = "—";
            if (v.getEspecialidad() != null && v.getEspecialidad().getNombreEspecialidad() != null) {
                especialidad = v.getEspecialidad().getNombreEspecialidad();
            }
            modelAsignados.addRow(new Object[]{
                v.getIdVeterinario(),
                v.getNombre() + " " + v.getApellido(),
                especialidad,
                sdf.format(new java.util.Date())  // Fecha actual temporal
            });
        }
    }
}


    public void cargarVeterinariosDisponibles(List<Veterinario> veterinarios) {
        modelDisponibles.setRowCount(0);
        if (veterinarios != null && !veterinarios.isEmpty()) {
            for (Veterinario v : veterinarios) {
                String especialidad = "—";
                if (v.getEspecialidad() != null) {
                    especialidad = v.getEspecialidad().getNombreEspecialidad();
                }
                modelDisponibles.addRow(new Object[]{
                    v.getIdVeterinario(),
                    v.getNombre() + " " + v.getApellido(),
                    especialidad
                });
            }
        }
    }

    private MyTextField field(String hint) {
        MyTextField f = new MyTextField();
        f.setHint(hint);
        f.setPreferredSize(FIELD_SIZE);
        return f;
    }

    private JPanel wrap(String label, JComponent comp) {
        JPanel p = new JPanel(new BorderLayout(0, 4));
        p.setOpaque(false);
        p.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("SansSerif", Font.PLAIN, 12));
        lbl.setForeground(new Color(120, 120, 120));
        p.add(lbl, BorderLayout.NORTH);
        p.add(comp, BorderLayout.CENTER);
        return p;
    }

    private JPanel wrapDos(String lbl1, JComponent c1, String lbl2, JComponent c2) {
        JPanel p = new JPanel(new GridLayout(1, 2, 10, 0));
        p.setOpaque(false);
        p.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
        p.add(wrap(lbl1, c1));
        p.add(wrap(lbl2, c2));
        return p;
    }
    

// Agrega este método para ocultar/mostrar la sección de veterinarios
public void ocultarSeccionVeterinarios() {
    if (panelSeccionVeterinarios != null) {
        panelSeccionVeterinarios.setVisible(false);
        // También puedes ajustar el tamaño de la ventana
        pack();
        setSize(800, 450); // Reducir altura cuando está oculto
    }
}
    // Getters
    public Button getBtnAccion() { return btnAccion; }
    public Button getBtnCancelar() { return btnCancelar; }
    public Button getBtnAsignar() { return btnAsignar; }
    public Button getBtnQuitar() { return btnQuitar; }
    public Button getBtnAbrirVeterinario() { return btnAbrirVeterinario; }
    public JTextField getTxtId() { return txtId; }
    public MyTextField getTxtNombre() { return txtNombre; }
    public MyTextField getTxtDescripcion() { return txtDescripcion; }
    public MyTextField getTxtPrecio() { return txtPrecio; }
    public MyTextField getTxtDuracion() { return txtDuracion; }
    public JCheckBox getChkEstado() { return chkEstado; }
    public JTable getTblVeterinariosAsignados() { return tblVeterinariosAsignados; }
    public JTable getTblVeterinariosDisponibles() { return tblVeterinariosDisponibles; }
    public boolean isModoEdicion() { return modoEdicion; }
    public Servicio getServicioActual() { return servicioActual; }
    public int getIdServicioSeleccionado() { return idServicioSeleccionado; }
}