package com.mycompany.veterinaria.grupo4.view.mascota;

import com.mycompany.veterinaria.grupo4.model.entity.Cliente;
import com.mycompany.veterinaria.grupo4.model.entity.Mascota;
import com.mycompany.veterinaria.grupo4.view.swing.Button;
import com.mycompany.veterinaria.grupo4.view.swing.CalendarioCita;
import com.mycompany.veterinaria.grupo4.view.swing.MyTextField;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.awt.geom.RoundRectangle2D;
import java.io.File;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JWindow;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * Formulario modal de registro y edición de {@link Mascota}.
 * <p>
 * Incluye carga de foto, búsqueda de cliente por cédula y campos
 * organizados en dos columnas. La lógica reside en {@link CtrlMascotas}.
 *
 * @author juan
 */
public class FormRegistroMascota extends JDialog {

    private static final SimpleDateFormat SDF       = new SimpleDateFormat("dd/MM/yyyy");
    private static final int              FOTO_SIZE  = 108;
    private static final Dimension        FIELD_SIZE = new Dimension(0, 36);
    private boolean seleccionandoFoto = false;
    // ─── Foto ─────────────────────────────────────────────────────────────────
    private JLabel  lblFoto;
    private JPanel  panelFoto;
    private byte[]  fotoBytes;
    private String  fotoNombreArchivo;

    // ─── Cliente ──────────────────────────────────────────────────────────────
    private MyTextField txtCedula;
    private Button      btnBuscarCliente;
    private JTextField  txtNombreCliente;
    private Cliente     clienteSeleccionado;

    // ─── Datos de la mascota ──────────────────────────────────────────────────
    private MyTextField    txtNombre;
    private MyTextField    txtEspecie;
    private MyTextField    txtRaza;
    private JComboBox<String> cmbSexo;
    private JTextField     txtFechaNacimiento;
    private JButton        btnCalendario;
    private Date           fechaNacimiento;
    private MyTextField    txtPeso;
    private MyTextField    txtColor;
    private JWindow        calendarPopup;

    // ─── Acciones ─────────────────────────────────────────────────────────────

    private Button btnAccion;
    private Button btnCancelar;

    // ─── Modo ─────────────────────────────────────────────────────────────────
    private boolean modoEdicion = false;
    private Mascota mascotaActual;

    // ─── Constructores ────────────────────────────────────────────────────────

    /** Constructor para modo alta. */
    public FormRegistroMascota(Frame parent) {
        super(parent, true);
        init("Registrar Mascota", "Registrar");
    }

    /**
     * Constructor para modo edición.
     * El controlador debe llamar a {@link #setClienteSeleccionado(Cliente)}
     * después de la construcción.
     *
     * @param parent   ventana padre
     * @param mascota  mascota a editar
     * @param cliente  cliente propietario
     */
    public FormRegistroMascota(Frame parent, Mascota mascota, Cliente cliente) {
        super(parent, true);
        this.modoEdicion   = true;
        this.mascotaActual = mascota;
        init("Editar Mascota", "Actualizar");
        rellenarCampos(mascota, cliente);
    }

    // ─── Construcción UI ──────────────────────────────────────────────────────

    private void init(String titulo, String labelBoton) {
        setUndecorated(true);
        setBackground(new Color(0, 0, 0, 0));
        setSize(480, 620);
        setLocationRelativeTo(getParent());

        addWindowFocusListener(new WindowFocusListener() {
            @Override public void windowGainedFocus(WindowEvent e) {}
            @Override public void windowLostFocus(WindowEvent e) {
                if (seleccionandoFoto) return;      // ← no cerrar mientras el explorador está abierto
                if (calendarPopup != null && e.getOppositeWindow() == calendarPopup) return;
                if (calendarPopup != null) { calendarPopup.dispose(); calendarPopup = null; }
                dispose();
            }
        });

        JPanel root = buildRoot();
        root.add(buildHeader(titulo),  BorderLayout.NORTH);
        root.add(buildCuerpo(),        BorderLayout.CENTER);
        root.add(buildFooter(labelBoton), BorderLayout.SOUTH);
        setContentPane(root);
    }

    private JPanel buildRoot() {
        JPanel root = new JPanel(new BorderLayout(0, 10)) {
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

        header.add(lbl,  BorderLayout.WEST);
        header.add(btnX, BorderLayout.EAST);
        return header;
    }
    private JPanel buildCuerpo() {
        // Panel principal con BorderLayout que contendrá el scroll
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setOpaque(false);

        // Panel interior con BoxLayout Y_AXIS que contendrá todos los campos
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setOpaque(false);

        // Foto
        contentPanel.add(buildPanelFoto());
        contentPanel.add(Box.createVerticalStrut(12));

        // Cédula + botón buscar
        contentPanel.add(buildFilaCedula());
        contentPanel.add(Box.createVerticalStrut(8));

        // Nombre cliente (solo lectura)
        txtNombreCliente = new JTextField();
        txtNombreCliente.setEditable(false);
        txtNombreCliente.setFont(new Font("SansSerif", Font.ITALIC, 13));
        txtNombreCliente.setForeground(new Color(80, 160, 80));
        txtNombreCliente.setBorder(BorderFactory.createEmptyBorder(0, 4, 0, 4));
        txtNombreCliente.setPreferredSize(FIELD_SIZE);
        txtNombreCliente.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        contentPanel.add(wrap("Propietario", txtNombreCliente));
        contentPanel.add(Box.createVerticalStrut(8));

        // Nombre mascota (fila completa)
        txtNombre = new MyTextField();
        txtNombre.setHint("Nombre de la mascota");
        txtNombre.setPreferredSize(FIELD_SIZE);
        txtNombre.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        contentPanel.add(wrap("Nombre", txtNombre));
        contentPanel.add(Box.createVerticalStrut(8));

        // Especie | Raza
        txtEspecie = field("Especie");
        txtRaza    = field("Raza");
        contentPanel.add(wrapDos("Especie", txtEspecie, "Raza", txtRaza));
        contentPanel.add(Box.createVerticalStrut(8));

        // Sexo | Fecha de nacimiento
        cmbSexo = new JComboBox<>(new String[]{"Macho", "Hembra"});
        cmbSexo.setSelectedIndex(-1);
        cmbSexo.setPreferredSize(FIELD_SIZE);
        contentPanel.add(wrapDos("Sexo", cmbSexo, "Fecha de nacimiento", buildFilaFecha()));
        contentPanel.add(Box.createVerticalStrut(8));

        // Peso | Color
        txtPeso  = field("Ej: 4.5");
        txtColor = field("Color de pelaje");
        contentPanel.add(wrapDos("Peso (kg)", txtPeso, "Color", txtColor));

        // Espacio extra al final para mejor scroll
        contentPanel.add(Box.createVerticalStrut(20));

        // Crear JScrollPane con el contentPanel
        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        // Personalizar el scrollbar (opcional)
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        mainPanel.add(scrollPane, BorderLayout.CENTER);

        return mainPanel;
    }

    /** Panel de foto con borde punteado; clic abre el selector de archivo. */
    private JPanel buildPanelFoto() {
        JPanel outer = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        outer.setOpaque(false);
        outer.setAlignmentX(Component.CENTER_ALIGNMENT);

        panelFoto = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(UIManager.getColor("Panel.background"));
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 16, 16));
                g2.setColor(new Color(230, 140, 30, 120));
                float[] dash = {5f, 4f};
                g2.setStroke(new BasicStroke(1.5f, BasicStroke.CAP_ROUND,
                    BasicStroke.JOIN_ROUND, 0, dash, 0));
                g2.draw(new RoundRectangle2D.Float(1, 1, getWidth() - 2, getHeight() - 2, 16, 16));
                g2.dispose();
                super.paintComponent(g);
            }
        };
        panelFoto.setOpaque(false);
        panelFoto.setPreferredSize(new Dimension(FOTO_SIZE, FOTO_SIZE));
        panelFoto.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        panelFoto.setToolTipText("Clic para seleccionar foto");
        panelFoto.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override public void mouseClicked(java.awt.event.MouseEvent e) {
                seleccionarFoto();
            }
        });

        lblFoto = new JLabel();
        lblFoto.setHorizontalAlignment(SwingConstants.CENTER);
        lblFoto.setVerticalAlignment(SwingConstants.CENTER);
        panelFoto.add(lblFoto, BorderLayout.CENTER);
        setFotoIcon(null);

        JLabel hint = new JLabel("Clic para cargar foto");
        hint.setFont(new Font("SansSerif", Font.PLAIN, 10));
        hint.setForeground(new Color(150, 150, 150));
        hint.setHorizontalAlignment(SwingConstants.CENTER);

        JPanel col = new JPanel();
        col.setLayout(new BoxLayout(col, BoxLayout.Y_AXIS));
        col.setOpaque(false);
        panelFoto.setAlignmentX(Component.CENTER_ALIGNMENT);
        hint.setAlignmentX(Component.CENTER_ALIGNMENT);
        col.add(panelFoto);
        col.add(Box.createVerticalStrut(4));
        col.add(hint);

        outer.add(col);
        return outer;
    }

    /** Fila cédula + botón buscar. */
    private JPanel buildFilaCedula() {
        JPanel outer = new JPanel(new BorderLayout(0, 4));
        outer.setOpaque(false);
        outer.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));

        JLabel lbl = new JLabel("Cliente");
        lbl.setFont(new Font("SansSerif", Font.PLAIN, 12));
        lbl.setForeground(new Color(120, 120, 120));
        outer.add(lbl, BorderLayout.NORTH);

        JPanel fila = new JPanel(new BorderLayout(6, 0));
        fila.setOpaque(false);

        txtCedula = new MyTextField();
        txtCedula.setHint("Cédula / RUC");
        txtCedula.setPreferredSize(FIELD_SIZE);

        btnBuscarCliente = new Button();
        btnBuscarCliente.setText("Buscar");
        btnBuscarCliente.setBackground(new Color(230, 140, 30));
        btnBuscarCliente.setForeground(Color.WHITE);
        btnBuscarCliente.setPreferredSize(new Dimension(85, 36));

        fila.add(txtCedula,        BorderLayout.CENTER);
        fila.add(btnBuscarCliente, BorderLayout.EAST);
        outer.add(fila, BorderLayout.CENTER);
        return outer;
    }

    /** Campo de fecha con botón de calendario. Devuelve solo el campo compuesto. */
    private JPanel buildFilaFecha() {
        JPanel fila = new JPanel(new BorderLayout(4, 0));
        fila.setOpaque(false);

        txtFechaNacimiento = new JTextField();
        txtFechaNacimiento.setEditable(false);
        txtFechaNacimiento.setFont(new Font("SansSerif", Font.PLAIN, 13));
        txtFechaNacimiento.setPreferredSize(FIELD_SIZE);
        txtFechaNacimiento.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override public void mouseClicked(java.awt.event.MouseEvent e) {
                mostrarPopupCalendario();
            }
        });

        btnCalendario = new JButton("📅");
        btnCalendario.setFocusPainted(false);
        btnCalendario.setFont(new Font("SansSerif", Font.PLAIN, 15));
        btnCalendario.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnCalendario.setPreferredSize(new Dimension(36, 36));
        btnCalendario.addActionListener(e -> mostrarPopupCalendario());

        fila.add(txtFechaNacimiento, BorderLayout.CENTER);
        fila.add(btnCalendario,      BorderLayout.EAST);
        return fila;
    }

    private void mostrarPopupCalendario() {
        if (calendarPopup != null && calendarPopup.isVisible()) {
            calendarPopup.dispose();
            calendarPopup = null;
            return;
        }
        calendarPopup = new JWindow(this);

        CalendarioCita cal = new CalendarioCita();
        if (fechaNacimiento != null) cal.setFechaSeleccionada(fechaNacimiento);

        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(230, 140, 30, 160), 1),
            BorderFactory.createEmptyBorder(8, 8, 8, 8)
        ));
        wrapper.setBackground(UIManager.getColor("Panel.background"));
        wrapper.add(cal);
        calendarPopup.add(wrapper);
        calendarPopup.setSize(300, 280);

        Point loc = btnCalendario.getLocationOnScreen();
        calendarPopup.setLocation(
            loc.x + btnCalendario.getWidth() - calendarPopup.getWidth(),
            loc.y + btnCalendario.getHeight() + 2
        );
        calendarPopup.setVisible(true);

        cal.addPropertyChangeListener("fechaSeleccionada", evt -> {
            fechaNacimiento = cal.getFechaSeleccionada();
            txtFechaNacimiento.setText(SDF.format(fechaNacimiento));
            calendarPopup.dispose();
            calendarPopup = null;
        });

        calendarPopup.addWindowFocusListener(new WindowFocusListener() {
            @Override public void windowGainedFocus(WindowEvent e) {}
            @Override public void windowLostFocus(WindowEvent e) {
                if (e.getOppositeWindow() == FormRegistroMascota.this) return;
                calendarPopup.dispose();
                calendarPopup = null;
            }
        });
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

        footer.add(btnCancelar);
        footer.add(btnAccion);
        return footer;
    }

    /**
     * Rellena los campos con los datos de la mascota y su cliente.
     * Llamado solo desde el constructor de edición.
     *
     * @param m       mascota cuyos datos se muestran
     * @param cliente propietario de la mascota
     */
    private void rellenarCampos(Mascota m, Cliente cliente) {
        setClienteSeleccionado(cliente);
        if (cliente != null) txtCedula.setText(cliente.getCedula());

        txtNombre.setText(m.getNombre());
        txtEspecie.setText(m.getEspecie());
        if (m.getRaza()  != null) txtRaza.setText(m.getRaza());
        if (m.getColor() != null) txtColor.setText(m.getColor());
        if (m.getPeso()  != null) txtPeso.setText(String.valueOf(m.getPeso()));

        if (m.getSexo() != 0)
            cmbSexo.setSelectedItem(m.getSexo() == 'M' ? "Macho" : "Hembra");

        if (m.getFechaNacimiento() != null) {
            fechaNacimiento = m.getFechaNacimiento();
            txtFechaNacimiento.setText(SDF.format(fechaNacimiento));
        }

        setFotoIcon(m.getFoto());
    }

    // ─── API pública (llamada desde el controlador) ───────────────────────────

    /**
     * Actualiza el campo de nombre del cliente y limpia mascotas si es {@code null}.
     *
     * @param cliente cliente encontrado o {@code null} si no existe
     */
    public void setClienteSeleccionado(Cliente cliente) {
        this.clienteSeleccionado = cliente;
        if (cliente != null) {
            txtNombreCliente.setText(cliente.getNombre() + " " + cliente.getApellido());
            txtNombreCliente.setForeground(new Color(60, 150, 60));
        } else {
            txtNombreCliente.setText("Cliente no encontrado");
            txtNombreCliente.setForeground(new Color(200, 60, 60));
        }
    }

    /**
     * Carga y muestra la foto en el panel. Si {@code bytes} es null o vacío,
     * muestra el ícono por defecto.
     *
     * @param bytes bytes de la imagen, o {@code null}
     */
    public void setFotoIcon(byte[] bytes) {
        this.fotoBytes = bytes;
        ImageIcon icon;
        if (bytes != null && bytes.length > 0) {
            Image scaled = new ImageIcon(bytes)
                .getImage().getScaledInstance(FOTO_SIZE - 8, FOTO_SIZE - 8, Image.SCALE_SMOOTH);
            icon = new ImageIcon(scaled);
        } else {
            Image scaled = new ImageIcon(getClass().getResource("/icon/pet-paw.png"))
                .getImage().getScaledInstance(54, 54, Image.SCALE_SMOOTH);
            icon = new ImageIcon(scaled);
        }
        lblFoto.setIcon(icon);
    }

    // ─── Helpers UI privados ──────────────────────────────────────────────────

    /** Abre el selector de archivo y carga la imagen elegida. */
    private void seleccionarFoto() {
        seleccionandoFoto = true;
        JFileChooser fc = new JFileChooser();
        fc.setFileFilter(new FileNameExtensionFilter(
            "Imágenes (jpg, png, gif)", "jpg", "jpeg", "png", "gif"));
        int resultado = fc.showOpenDialog(this);
        seleccionandoFoto = false;          // se resetea siempre, abra o cancele
        if (resultado != JFileChooser.APPROVE_OPTION) return;
        try {
            File file         = fc.getSelectedFile();
            fotoBytes         = Files.readAllBytes(file.toPath());
            fotoNombreArchivo = file.getName();
            setFotoIcon(fotoBytes);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                "No se pudo cargar la imagen: " + ex.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /** Crea un {@link MyTextField} con hint y tamaño estándar. */
    private MyTextField field(String hint) {
        MyTextField f = new MyTextField();
        f.setHint(hint);
        f.setPreferredSize(FIELD_SIZE);
        return f;
    }

    /** Envuelve un componente con su etiqueta encima. */
    private JPanel wrap(String label, JComponent comp) {
        JPanel p = new JPanel(new BorderLayout(0, 4));
        p.setOpaque(false);
        p.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("SansSerif", Font.PLAIN, 12));
        lbl.setForeground(new Color(120, 120, 120));
        p.add(lbl,  BorderLayout.NORTH);
        p.add(comp, BorderLayout.CENTER);
        return p;
    }

    /** Coloca dos campos etiquetados en dos columnas iguales. */
    private JPanel wrapDos(String lbl1, JComponent c1, String lbl2, JComponent c2) {
        JPanel p = new JPanel(new GridLayout(1, 2, 10, 0));
        p.setOpaque(false);
        p.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
        p.add(wrap(lbl1, c1));
        p.add(wrap(lbl2, c2));
        return p;
    }

    // ─── Getters ──────────────────────────────────────────────────────────────

    public Button            getBtnAccion()           { return btnAccion;           }
    public Button            getBtnCancelar()         { return btnCancelar;         }
    public Button            getBtnBuscarCliente()    { return btnBuscarCliente;    }
    public MyTextField       getTxtCedula()           { return txtCedula;           }
    public JTextField        getTxtNombreCliente()    { return txtNombreCliente;    }
    public MyTextField       getTxtNombre()           { return txtNombre;           }
    public MyTextField       getTxtEspecie()          { return txtEspecie;          }
    public MyTextField       getTxtRaza()             { return txtRaza;             }
    public JComboBox<String> getCmbSexo()             { return cmbSexo;             }
    public JTextField        getTxtFechaNacimiento()  { return txtFechaNacimiento;  }
    public MyTextField       getTxtPeso()             { return txtPeso;             }
    public MyTextField       getTxtColor()            { return txtColor;            }
    public Cliente           getClienteSeleccionado() { return clienteSeleccionado; }
    public Date              getFechaNacimiento()     { return fechaNacimiento;     }
    public byte[]            getFotoBytes()           { return fotoBytes;           }
    public String            getFotoNombreArchivo()   { return fotoNombreArchivo;   }
    public boolean           isModoEdicion()          { return modoEdicion;         }
    public Mascota           getMascotaActual()       { return mascotaActual;       }
}