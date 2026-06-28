package com.mycompany.veterinaria.grupo4.view.cita;

import com.mycompany.veterinaria.grupo4.model.entity.Cita;
import com.mycompany.veterinaria.grupo4.model.entity.Cliente;
import com.mycompany.veterinaria.grupo4.model.entity.Mascota;
import com.mycompany.veterinaria.grupo4.model.entity.Servicio;
import com.mycompany.veterinaria.grupo4.model.entity.Veterinario;
import com.mycompany.veterinaria.grupo4.view.swing.Button;
import com.mycompany.veterinaria.grupo4.view.swing.CalendarioCita;
import com.mycompany.veterinaria.grupo4.view.swing.MyTextField;
import com.mycompany.veterinaria.grupo4.view.swing.scrollbar.ScrollBarCustom;
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
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.awt.geom.RoundRectangle2D;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.JWindow;
import javax.swing.SpinnerDateModel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

/**
 * Formulario modal de registro y edición de {@link Cita}.
 * <p>
 * Layout de columna única con campos: cliente (búsqueda por cédula),
 * servicio, veterinario, mascota, fecha, hora y observaciones.
 * La lógica de negocio y las llamadas REST residen en {@link CtrlCitas}.
 *
 * @author juan
 */
public class FormRegistroCita extends JDialog {
 
    private static final SimpleDateFormat SDF = new SimpleDateFormat("dd/MM/yyyy");
 
    // ─── Búsqueda de cliente ──────────────────────────────────────────────────
 
    private MyTextField txtCedula;
    private Button      btnBuscarCliente;
    /** Solo lectura; se rellena tras la búsqueda por cédula. */
    private JTextField  txtNombreCliente;
    private Cliente     clienteSeleccionado;
 
    // ─── Combos ───────────────────────────────────────────────────────────────
 
    private JComboBox<Servicio>     cmbServicio;
    private JComboBox<Veterinario>  cmbVeterinario;
    private JComboBox<Mascota>      cmbMascota;
 
    // ─── Fecha / Hora ─────────────────────────────────────────────────────────
 
    private JTextField txtFecha;
    private JButton    btnCalendario;
    private JSpinner   spnHora;
    private Date       fechaSeleccionada;
    private JWindow    calendarPopup;
 
    // ─── Observaciones ────────────────────────────────────────────────────────
 
    private MyTextField txtObservaciones;
 
    // ─── Acciones ─────────────────────────────────────────────────────────────
 
    private Button btnAccion;
    private Button btnCancelar;
 
    // ─── Modo ─────────────────────────────────────────────────────────────────
 
    private boolean modoEdicion = false;
    private Cita    citaActual;
 
    // ─── Constructores ────────────────────────────────────────────────────────
 
    /**
     * Constructor para modo alta.
     *
     * @param parent    ventana padre
     * @param servicios lista de servicios disponibles para el combo
     */
    public FormRegistroCita(Frame parent, List<Servicio> servicios) {
        super(parent, true);
        init("Agendar Cita", "Registrar", servicios);
    }
 
    /**
     * Constructor para modo edición.
     * Las mascotas y el veterinario se preseleccionan desde el controlador
     * después de la construcción.
     *
     * @param parent    ventana padre
     * @param cita      cita a editar
     * @param cliente   cliente asociado a la cita
     * @param servicios lista de servicios disponibles para el combo
     */
    public FormRegistroCita(Frame parent, Cita cita,
                             Cliente cliente, List<Servicio> servicios) {
        super(parent, true);
        this.modoEdicion = true;
        this.citaActual  = cita;
        init("Editar Cita", "Actualizar", servicios);
        rellenarCampos(cita, cliente);
    }
 
    // ─── Construcción UI ──────────────────────────────────────────────────────
 
    private void init(String titulo, String labelBoton, List<Servicio> servicios) {
        setUndecorated(true);
        setBackground(new Color(0, 0, 0, 0));
        setSize(420, 530);
        setLocationRelativeTo(getParent());
 
        JScrollPane scroll = new JScrollPane(buildCuerpo(servicios));
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 5)); // Margen para el scroll
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.getVerticalScrollBar().setUnitIncrement(16); 
        scroll.setVerticalScrollBar(new ScrollBarCustom());
        
        JPanel root = buildRoot();
        root.add(buildHeader(titulo),     BorderLayout.NORTH);
        root.add(scroll,  BorderLayout.CENTER);
        root.add(buildFooter(labelBoton), BorderLayout.SOUTH);
        setContentPane(root);
    }
 
    private JPanel buildRoot() {
        JPanel root = new JPanel(new BorderLayout(0, 10)) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
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
 
    private JPanel buildCuerpo(List<Servicio> servicios) {
    JPanel panel = new JPanel();
    panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));  // Cambiar a BoxLayout
    panel.setOpaque(false);

    panel.add(buildFilaCedula());
    panel.add(Box.createVerticalStrut(8));
    
    txtNombreCliente = new JTextField();
    txtNombreCliente.setEditable(false);
    txtNombreCliente.setFont(new Font("SansSerif", Font.ITALIC, 13));
    txtNombreCliente.setForeground(new Color(80, 160, 80));
    txtNombreCliente.setBorder(BorderFactory.createEmptyBorder(0, 4, 0, 4));
    txtNombreCliente.setPreferredSize(new Dimension(0, 36));
    txtNombreCliente.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
    panel.add(wrap("Nombre del cliente", txtNombreCliente));
    panel.add(Box.createVerticalStrut(8));

    cmbServicio = new JComboBox<>(servicios.toArray(new Servicio[0]));
    cmbServicio.setSelectedIndex(-1);
    cmbServicio.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
    cmbServicio.setRenderer(nombreRenderer(v -> v instanceof Servicio s ? s.getNombreServicio() : ""));
    panel.add(wrap("Servicio", cmbServicio));
    panel.add(Box.createVerticalStrut(8));

    cmbVeterinario = new JComboBox<>();
    cmbVeterinario.setSelectedIndex(-1);
    cmbVeterinario.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
    cmbVeterinario.setRenderer(nombreRenderer(v -> v instanceof Veterinario vet ? vet.getNombre() + " " + vet.getApellido() : ""));
    panel.add(wrap("Veterinario", cmbVeterinario));
    panel.add(Box.createVerticalStrut(8));

    cmbMascota = new JComboBox<>();
    cmbMascota.setSelectedIndex(-1);
    cmbMascota.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
    cmbMascota.setRenderer(nombreRenderer(v -> v instanceof Mascota m ? m.getNombre() : ""));
    panel.add(wrap("Mascota", cmbMascota));
    panel.add(Box.createVerticalStrut(8));

    panel.add(buildSelectorFechaYHora());
    panel.add(Box.createVerticalStrut(8));

    // EL CAMPO OBSERVACIONES - Asegurar que se vea
    txtObservaciones = new MyTextField();
    txtObservaciones.setHint("Observaciones (opcional)");
    txtObservaciones.setPreferredSize(new Dimension(0, 60));  // Más alto
    txtObservaciones.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
    panel.add(wrap("Observaciones", txtObservaciones));

    return panel;
}
 
    private JPanel buildFilaCedula() {
        JPanel outer = new JPanel(new BorderLayout(0, 4));
        outer.setOpaque(false);
 
        JLabel lbl = new JLabel("Cliente");
        lbl.setFont(new Font("SansSerif", Font.PLAIN, 12));
        lbl.setForeground(new Color(120, 120, 120));
        outer.add(lbl, BorderLayout.NORTH);
 
        JPanel fila = new JPanel(new BorderLayout(6, 0));
        fila.setOpaque(false);
 
        txtCedula = new MyTextField();
        txtCedula.setHint("Cédula / RUC");
        txtCedula.setPreferredSize(new Dimension(0, 36));
 
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
 
    private JPanel buildSelectorFechaYHora() {
        JPanel outer = new JPanel(new BorderLayout(0, 4));
        outer.setOpaque(false);

        JLabel lbl = new JLabel("Fecha y Hora");
        lbl.setFont(new Font("SansSerif", Font.PLAIN, 12));
        lbl.setForeground(new Color(120, 120, 120));
        outer.add(lbl, BorderLayout.NORTH);

        JPanel fila = new JPanel(new BorderLayout(10, 0));
        fila.setOpaque(false);

        // ── 1. Fecha ──────────────────────────────────────────────────────────
        JPanel panelFecha = new JPanel(new BorderLayout(2, 0));
        panelFecha.setOpaque(false);

        txtFecha = new JTextField();
        txtFecha.setEditable(false);
        txtFecha.setFont(new Font("SansSerif", Font.PLAIN, 13));
        txtFecha.setPreferredSize(new Dimension(0, 36));
        fechaSeleccionada = new Date();
        txtFecha.setText(SDF.format(fechaSeleccionada));
        txtFecha.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override public void mouseClicked(java.awt.event.MouseEvent e) {
                mostrarPopupCalendario();
            }
        });

        btnCalendario = new JButton("📅");
        btnCalendario.setFocusPainted(false);
        btnCalendario.setFont(new Font("SansSerif", Font.PLAIN, 15));
        btnCalendario.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnCalendario.setPreferredSize(new Dimension(40, 36));
        btnCalendario.addActionListener(e -> mostrarPopupCalendario());

        panelFecha.add(txtFecha,      BorderLayout.CENTER);
        panelFecha.add(btnCalendario, BorderLayout.EAST);

        // ── 2. Hora — modelo que salta de 15 en 15 sin importar qué campo esté activo
        Calendar ahora = Calendar.getInstance();
        int m0 = ahora.get(Calendar.MINUTE);
        ahora.set(Calendar.MINUTE,      ((m0 + 7) / 15) * 15 % 60);
        ahora.set(Calendar.SECOND,      0);
        ahora.set(Calendar.MILLISECOND, 0);
        final Date valorInicial = ahora.getTime();

        SpinnerDateModel modeloHora = new SpinnerDateModel(
                valorInicial, null, null, Calendar.MINUTE) {

            private Date snap(Date d) {
                Calendar c = Calendar.getInstance();
                c.setTime(d);
                c.set(Calendar.MINUTE,      (c.get(Calendar.MINUTE) / 15) * 15);
                c.set(Calendar.SECOND,      0);
                c.set(Calendar.MILLISECOND, 0);
                return c.getTime();
            }

            @Override public Object getNextValue() {
                Calendar c = Calendar.getInstance();
                c.setTime(snap((Date) getValue()));
                c.add(Calendar.MINUTE, 15);
                return c.getTime();
            }

            @Override public Object getPreviousValue() {
                Calendar c = Calendar.getInstance();
                c.setTime(snap((Date) getValue()));
                c.add(Calendar.MINUTE, -15);
                return c.getTime();
            }
        };

        spnHora = new JSpinner(modeloHora);
        JSpinner.DateEditor editorHora = new JSpinner.DateEditor(spnHora, "HH:mm");
        spnHora.setEditor(editorHora);
        spnHora.setPreferredSize(new Dimension(85, 36));

        // Cada vez que el spinner gana foco, posicionar el cursor en "mm"
        // para que las flechas del teclado también muevan minutos
        editorHora.getTextField().addFocusListener(new java.awt.event.FocusAdapter() {
            @Override public void focusGained(java.awt.event.FocusEvent e) {
                SwingUtilities.invokeLater(() ->
                    editorHora.getTextField().setCaretPosition(3)
                );
            }
        });

        // ── 3. Ensamblar ──────────────────────────────────────────────────────
        fila.add(panelFecha, BorderLayout.CENTER);
        fila.add(spnHora,    BorderLayout.EAST);
        outer.add(fila, BorderLayout.CENTER);
        return outer;
    }
 
    private void mostrarPopupCalendario() {
        if (calendarPopup != null && calendarPopup.isVisible()) {
            calendarPopup.dispose();
            calendarPopup = null;
            return;
        }
 
        calendarPopup = new JWindow(FormRegistroCita.this);
 
        CalendarioCita cal = new CalendarioCita();
        if (fechaSeleccionada != null) cal.setFechaSeleccionada(fechaSeleccionada);
 
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
            fechaSeleccionada = cal.getFechaSeleccionada();
            txtFecha.setText(SDF.format(fechaSeleccionada));
            calendarPopup.dispose();
            calendarPopup = null;
        });
 
        calendarPopup.addWindowFocusListener(new WindowFocusListener() {
            @Override public void windowGainedFocus(WindowEvent e) {}
            @Override public void windowLostFocus(WindowEvent e) {
                if (e.getOppositeWindow() == FormRegistroCita.this) return;
                calendarPopup.dispose();
                calendarPopup = null;
            }
        });
    }
 
    private JPanel buildFooter(String labelBoton) {
        JPanel footer = new JPanel(new GridLayout(1, 2, 10, 0));
        footer.setOpaque(false);
        footer.setBorder(new EmptyBorder(8, 0, 0, 0));
 
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
 
    // ─── Precarga en modo edición ─────────────────────────────────────────────
 
    /**
     * Rellena los campos del formulario con los datos de una cita existente.
     * La mascota se preselecciona desde el controlador, no aquí, porque
     * el combo se llena después de esta llamada.
     *
     * @param c       cita cuyos datos se mostrarán
     * @param cliente cliente asociado
     */
    private void rellenarCampos(Cita c, Cliente cliente) {
        if (cliente != null) {
            txtCedula.setText(cliente.getCedula());
            setClienteSeleccionado(cliente);
        }
 
        if (c.getServicio() != null) {
            for (int i = 0; i < cmbServicio.getItemCount(); i++) {
                if (cmbServicio.getItemAt(i).getIdServicio() == c.getServicio().getIdServicio()) {
                    cmbServicio.setSelectedIndex(i);
                    break;
                }
            }
        }
 
        if (c.getVeterinario() != null) {
            for (int i = 0; i < cmbVeterinario.getItemCount(); i++) {
                if (cmbVeterinario.getItemAt(i).getIdVeterinario() == c.getVeterinario().getIdVeterinario()) {
                    cmbVeterinario.setSelectedIndex(i);
                    break;
                }
            }
        }
 
        if (c.getFechaHora() != null) {
            fechaSeleccionada = c.getFechaHora();
            txtFecha.setText(SDF.format(fechaSeleccionada));
            spnHora.setValue(c.getFechaHora());
        }
 
        if (c.getObservaciones() != null) {
            txtObservaciones.setText(c.getObservaciones());
        }
    }
 
    // ─── API pública (llamada desde el controlador) ───────────────────────────
 
    /**
     * Puebla el combo de mascotas con las del cliente activo.
     * Limpia la selección previa antes de cargar.
     *
     * @param mascotas lista de mascotas a mostrar
     */
    public void cargarMascotasDisponibles(List<Mascota> mascotas) {
        cmbMascota.removeAllItems();
        for (Mascota m : mascotas) {
            cmbMascota.addItem(m);
        }
        cmbMascota.setSelectedIndex(-1);
    }
 
    /**
     * Preselecciona en el combo la mascota cuyo ID coincida.
     * Debe llamarse después de {@link #cargarMascotasDisponibles}.
     *
     * @param idMascota identificador de la mascota a seleccionar
     */
    public void preseleccionarMascota(int idMascota) {
        for (int i = 0; i < cmbMascota.getItemCount(); i++) {
            if (cmbMascota.getItemAt(i).getIdMascota() == idMascota) {
                cmbMascota.setSelectedIndex(i);
                return;
            }
        }
    }
 
    /**
     * Asigna el cliente encontrado y actualiza el campo de nombre.
     * Si {@code cliente} es {@code null}, muestra un aviso de no encontrado
     * y limpia el combo de mascotas.
     *
     * @param cliente cliente encontrado, o {@code null} si no existe
     */
    public void setClienteSeleccionado(Cliente cliente) {
        this.clienteSeleccionado = cliente;
        if (cliente != null) {
            txtNombreCliente.setText(cliente.getNombre());
            txtNombreCliente.setForeground(new Color(60, 150, 60));
        } else {
            txtNombreCliente.setText("Cliente no encontrado");
            txtNombreCliente.setForeground(new Color(200, 60, 60));
            cmbMascota.removeAllItems();
        }
    }
 
    // ─── Helpers UI privados ──────────────────────────────────────────────────
 
    /**
     * Renderer genérico para combos: recibe una función que extrae el texto
     * a mostrar a partir del objeto del modelo.
     *
     * @param extractor función de conversión objeto → texto
     * @param <T>       tipo del elemento del combo
     * @return renderer configurado
     */
    @SuppressWarnings("unchecked")
    private <T> DefaultListCellRenderer nombreRenderer(
            java.util.function.Function<Object, String> extractor) {
        return new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value,
                    int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                setText(extractor.apply(value));
                return this;
            }
        };
    }
 
    private JPanel wrap(String label, JComponent comp) {
        JPanel p = new JPanel(new BorderLayout(0, 4));
        p.setOpaque(false);
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("SansSerif", Font.PLAIN, 12));
        lbl.setForeground(new Color(120, 120, 120));
        p.add(lbl,  BorderLayout.NORTH);
        p.add(comp, BorderLayout.CENTER);
        return p;
    }
 
    // ─── Getters ──────────────────────────────────────────────────────────────
 
    public Button                  getBtnAccion()           { return btnAccion;           }
    public Button                  getBtnCancelar()         { return btnCancelar;         }
    public Button                  getBtnBuscarCliente()    { return btnBuscarCliente;    }
    public MyTextField             getTxtCedula()           { return txtCedula;           }
    public JComboBox<Servicio>     getCmbServicio()         { return cmbServicio;         }
    public JComboBox<Veterinario>  getCmbVeterinario()      { return cmbVeterinario;      }
    public JComboBox<Mascota>      getCmbMascota()          { return cmbMascota;          }
    public JSpinner                getSpnHora()             { return spnHora;             }
    public MyTextField             getTxtObservaciones()    { return txtObservaciones;    }
    public Cliente                 getClienteSeleccionado() { return clienteSeleccionado; }
    public Date                    getFechaSeleccionada()   { return fechaSeleccionada;   }
    public boolean                 isModoEdicion()          { return modoEdicion;         }
    public Cita                    getCitaActual()          { return citaActual;          }
}