package com.mycompany.veterinaria.grupo4.view.personalVeterinario;

import com.mycompany.veterinaria.grupo4.model.entity.EspecialidadVeterinaria;
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
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

/**
 * Formulario modal de registro y edición de {@link Veterinario}.
 * <p>
 * Incluye selección de especialidad, asignación dinámica de servicios
 * y los campos de datos del médico. La lógica reside en {@link CtrlVeterinario}.
 *
 * @author juan
 */
public class FormVeterinario extends JDialog {

    private static final Dimension FIELD_SIZE = new Dimension(0, 36);

    // ─── Datos personales ─────────────────────────────────────────────────────

    private MyTextField txtCedula;
    private MyTextField txtNombre;
    private MyTextField txtApellido;
    private MyTextField txtTelefono;
    private MyTextField txtCorreo;
    private JComboBox<EspecialidadVeterinaria> cmbEspecialidad;
    private MyTextField txtPagoMensual;
    private MyTextField txtDireccion;

    // ─── Servicios ────────────────────────────────────────────────────────────

    private JComboBox<Servicio> cmbServiciosDisponibles;
    private Button              btnAgregarServicio;
    private JPanel              panelServiciosAsignados;

    // ─── Acciones ─────────────────────────────────────────────────────────────

    private Button btnAccion;
    private Button btnCancelar;

    // ─── Modo ─────────────────────────────────────────────────────────────────

    private boolean    modoEdicion      = false;
    private Veterinario veterinarioActual;

    // ─── Constructores ────────────────────────────────────────────────────────

    /** Constructor para modo alta. */
    public FormVeterinario(Frame parent) {
        super(parent, true);
        init("Registrar Veterinario", "Registrar");
    }

    /**
     * Constructor para modo edición.
     * El controlador rellena los campos y carga los servicios asignados
     * después de la construcción.
     *
     * @param parent      ventana padre
     * @param veterinario veterinario a editar
     */
    public FormVeterinario(Frame parent, Veterinario veterinario) {
        super(parent, true);
        this.modoEdicion       = true;
        this.veterinarioActual = veterinario;
        init("Editar Veterinario", "Actualizar");
    }

    // ─── Construcción UI ──────────────────────────────────────────────────────

    private void init(String titulo, String labelBoton) {
        setUndecorated(true);
        setBackground(new Color(0, 0, 0, 0));
        setSize(460, 650);
        setLocationRelativeTo(getParent());

        JPanel root = buildRoot();
        root.add(buildHeader(titulo),     BorderLayout.NORTH);
        root.add(buildCuerpo(),           BorderLayout.CENTER);
        root.add(buildFooter(labelBoton), BorderLayout.SOUTH);
        setContentPane(root);
        configurarValidaciones();
    }
    /**
     * Configura las validaciones en tiempo real para los campos del formulario.
     */
    public void configurarValidaciones() {
        final boolean[] mensajeMostrado = {false};
        
        // --- Cédula: solo números y máximo 10 dígitos ---
        txtCedula.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyTyped(java.awt.event.KeyEvent e) {
                char c = e.getKeyChar();
                String textoActual = txtCedula.getText();
                
                if (c == '\b' || c == '\u007F') return;
                
                if (!Character.isDigit(c)) {
                    e.consume();
                    mostrarMensajeValidacion("La cédula solo puede contener números", mensajeMostrado);
                    return;
                }
                
                if (textoActual.length() >= 10) {
                    e.consume();
                    mostrarMensajeValidacion("La cédula debe tener exactamente 10 dígitos", mensajeMostrado);
                }
            }
        });
        
        // --- Teléfono: solo números y máximo 10 dígitos ---
        txtTelefono.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyTyped(java.awt.event.KeyEvent e) {
                char c = e.getKeyChar();
                String textoActual = txtTelefono.getText();
                
                if (c == '\b' || c == '\u007F') return;
                
                if (!Character.isDigit(c)) {
                    e.consume();
                    mostrarMensajeValidacion("El teléfono solo puede contener números", mensajeMostrado);
                    return;
                }
                
                if (textoActual.length() >= 10) {
                    e.consume();
                    mostrarMensajeValidacion("El teléfono debe tener exactamente 10 dígitos", mensajeMostrado);
                }
            }
        });
        
        // --- Pago Mensual: números, punto y coma ---
        txtPagoMensual.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyTyped(java.awt.event.KeyEvent e) {
                char c = e.getKeyChar();
                String textoActual = txtPagoMensual.getText();
                
                if (c == '\b' || c == '\u007F') return;
                
                if (Character.isDigit(c) || c == '.' || c == ',') {
                    // Verificar que no haya más de un separador decimal
                    if ((c == '.' || c == ',') && (textoActual.contains(".") || textoActual.contains(","))) {
                        e.consume();
                        mostrarMensajeValidacion("El pago mensual solo puede tener un separador decimal", mensajeMostrado);
                    }
                } else {
                    e.consume();
                    mostrarMensajeValidacion("El pago mensual solo puede contener números", mensajeMostrado);
                }
            }
        });
        
        // --- Nombre: solo letras, espacios, tildes y ñ ---
        txtNombre.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyTyped(java.awt.event.KeyEvent e) {
                char c = e.getKeyChar();
                if (c == '\b' || c == '\u007F') return;
                
                if (!Character.isLetter(c) && c != ' ' && 
                    c != 'á' && c != 'é' && c != 'í' && c != 'ó' && c != 'ú' && 
                    c != 'Á' && c != 'É' && c != 'Í' && c != 'Ó' && c != 'Ú' && c != 'ñ' && c != 'Ñ') {
                    e.consume();
                    mostrarMensajeValidacion("El nombre solo puede contener letras y espacios", mensajeMostrado);
                }
            }
        });
        
        // --- Apellido: solo letras, espacios, tildes y ñ ---
        txtApellido.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyTyped(java.awt.event.KeyEvent e) {
                char c = e.getKeyChar();
                if (c == '\b' || c == '\u007F') return;
                
                if (!Character.isLetter(c) && c != ' ' && 
                    c != 'á' && c != 'é' && c != 'í' && c != 'ó' && c != 'ú' && 
                    c != 'Á' && c != 'É' && c != 'Í' && c != 'Ó' && c != 'Ú' && c != 'ñ' && c != 'Ñ') {
                    e.consume();
                    mostrarMensajeValidacion("El apellido solo puede contener letras y espacios", mensajeMostrado);
                }
            }
        });
        
        // --- Dirección: validación básica (solo espacios, letras y números) ---
        txtDireccion.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyTyped(java.awt.event.KeyEvent e) {
                char c = e.getKeyChar();
                if (c == '\b' || c == '\u007F') return;
                
                // Permitir letras (incluyendo tildes y ñ), números, espacios y algunos caracteres especiales comunes
                if (!Character.isLetterOrDigit(c) && c != ' ' && c != '.' && c != ',' && c != '-' && c != '#' && 
                    c != 'á' && c != 'é' && c != 'í' && c != 'ó' && c != 'ú' && 
                    c != 'Á' && c != 'É' && c != 'Í' && c != 'Ó' && c != 'Ú' && c != 'ñ' && c != 'Ñ') {
                    e.consume();
                    mostrarMensajeValidacion("La dirección solo puede contener letras, números, espacios y algunos signos básicos (. , - #)", mensajeMostrado);
                }
            }
        });
    }
    
    /**
     * Muestra un mensaje de validación sin saturar al usuario con múltiples popups.
     */
    private void mostrarMensajeValidacion(String mensaje, boolean[] flag) {
        if (!flag[0]) {
            flag[0] = true;
            JOptionPane.showMessageDialog(this, mensaje, "Validación", JOptionPane.WARNING_MESSAGE);
            new Thread(() -> {
                try { Thread.sleep(1500); } catch (InterruptedException ex) {}
                flag[0] = false;
            }).start();
        }
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

        header.add(lbl,  BorderLayout.WEST);
        header.add(btnX, BorderLayout.EAST);
        return header;
    }

    private JPanel buildCuerpo() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);

        // Cédula (fila completa)
        txtCedula = field("Cédula / RUC");
        txtCedula.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        panel.add(wrap("Cédula", txtCedula));
        panel.add(Box.createVerticalStrut(8));

        // Nombre | Apellido
        txtNombre   = field("Nombre");
        txtApellido = field("Apellido");
        panel.add(wrapDos("Nombre", txtNombre, "Apellido", txtApellido));
        panel.add(Box.createVerticalStrut(8));

        // Teléfono | Correo
        txtTelefono = field("Teléfono");
        txtCorreo   = field("Correo electrónico");
        panel.add(wrapDos("Teléfono", txtTelefono, "Correo", txtCorreo));
        panel.add(Box.createVerticalStrut(8));

        // Especialidad (fila completa)
        cmbEspecialidad = new JComboBox<>();
        cmbEspecialidad.setSelectedIndex(-1);
        cmbEspecialidad.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        cmbEspecialidad.setRenderer(nombreRenderer(v ->
            v instanceof EspecialidadVeterinaria e ? e.getNombreEspecialidad() : ""));
        panel.add(wrap("Especialidad", cmbEspecialidad));
        panel.add(Box.createVerticalStrut(8));

        // Pago mensual | Dirección
        txtPagoMensual = field("460.00");
        txtDireccion   = field("Dirección");
        panel.add(wrapDos("Pago mensual ($)", txtPagoMensual, "Dirección", txtDireccion));
        panel.add(Box.createVerticalStrut(12));

        // Sección servicios
        panel.add(buildSeccionServicios());

        return panel;
    }

    private JPanel buildSeccionServicios() {
        JPanel section = new JPanel();
        section.setLayout(new BoxLayout(section, BoxLayout.Y_AXIS));
        section.setOpaque(false);

        // Título de sección
        JLabel titulo = new JLabel("Servicios que puede realizar");
        titulo.setFont(new Font("SansSerif", Font.BOLD, 12));
        titulo.setForeground(new Color(230, 140, 30));
        titulo.setAlignmentX(Component.LEFT_ALIGNMENT);
        section.add(titulo);
        section.add(Box.createVerticalStrut(6));

        // Combo de disponibles + botón agregar
        JPanel filaAgregar = new JPanel(new BorderLayout(6, 0));
        filaAgregar.setOpaque(false);
        filaAgregar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        filaAgregar.setAlignmentX(Component.LEFT_ALIGNMENT);

        cmbServiciosDisponibles = new JComboBox<>();
        cmbServiciosDisponibles.setSelectedIndex(-1);
        cmbServiciosDisponibles.setRenderer(nombreRenderer(v ->
            v instanceof Servicio s ? s.getNombreServicio() : ""));

        btnAgregarServicio = new Button();
        btnAgregarServicio.setText("+ Agregar");
        btnAgregarServicio.setBackground(new Color(230, 140, 30));
        btnAgregarServicio.setForeground(Color.WHITE);
        btnAgregarServicio.setPreferredSize(new Dimension(95, 36));

        filaAgregar.add(cmbServiciosDisponibles, BorderLayout.CENTER);
        filaAgregar.add(btnAgregarServicio,       BorderLayout.EAST);
        section.add(filaAgregar);
        section.add(Box.createVerticalStrut(6));

        // Panel de servicios asignados dentro de un scroll
        panelServiciosAsignados = new JPanel();
        panelServiciosAsignados.setLayout(new BoxLayout(panelServiciosAsignados, BoxLayout.Y_AXIS));
        panelServiciosAsignados.setOpaque(false);

        JScrollPane scroll = new JScrollPane(panelServiciosAsignados);
        scroll.setPreferredSize(new Dimension(0, 115));
        scroll.setMaximumSize(new Dimension(Integer.MAX_VALUE, 115));
        scroll.setAlignmentX(Component.LEFT_ALIGNMENT);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(230, 140, 30, 70), 1));
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.getViewport().setBackground(new Color(0, 0, 0, 0));
        section.add(scroll);

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

        footer.add(btnCancelar);
        footer.add(btnAccion);
        return footer;
    }

    // ─── API pública (llamada desde el controlador) ───────────────────────────

    /**
     * Puebla el combo de especialidades.
     *
     * @param especialidades lista de especialidades disponibles
     */
    public void cargarEspecialidades(List<EspecialidadVeterinaria> especialidades) {
        cmbEspecialidad.removeAllItems();
        especialidades.forEach(cmbEspecialidad::addItem);
        cmbEspecialidad.setSelectedIndex(-1);
    }

    /**
     * Puebla el combo de servicios disponibles para asignar.
     *
     * @param servicios lista de servicios activos
     */
    public void cargarServiciosDisponibles(List<Servicio> servicios) {
        cmbServiciosDisponibles.removeAllItems();
        servicios.forEach(cmbServiciosDisponibles::addItem);
        cmbServiciosDisponibles.setSelectedIndex(-1);
    }

    /**
     * Agrega una fila al panel de servicios asignados.
     * El botón ✕ ejecuta {@code onRemove}, que el controlador define.
     *
     * @param nombre   nombre del servicio a mostrar
     * @param onRemove acción a ejecutar al pulsar ✕
     */
    public void agregarServicioAsignado(String nombre, Runnable onRemove) {
        JPanel row = new JPanel(new BorderLayout(8, 0));
        row.setOpaque(false);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        row.setBorder(new EmptyBorder(2, 8, 2, 6));

        JLabel lbl = new JLabel("• " + nombre);
        lbl.setFont(new Font("SansSerif", Font.PLAIN, 12));

        JButton btnX = new JButton("✕");
        btnX.setFocusPainted(false);
        btnX.setBorderPainted(false);
        btnX.setContentAreaFilled(false);
        btnX.setForeground(new Color(200, 60, 60));
        btnX.setFont(new Font("SansSerif", Font.BOLD, 11));
        btnX.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnX.addActionListener(e -> onRemove.run());

        row.add(lbl,  BorderLayout.CENTER);
        row.add(btnX, BorderLayout.EAST);

        panelServiciosAsignados.add(row);
        panelServiciosAsignados.revalidate();
        panelServiciosAsignados.repaint();
    }

    /** Elimina todas las filas del panel de servicios asignados. */
    public void limpiarServiciosAsignados() {
        panelServiciosAsignados.removeAll();
        panelServiciosAsignados.revalidate();
        panelServiciosAsignados.repaint();
    }

    /**
     * Preselecciona la especialidad cuyo ID coincida con el del veterinario en edición.
     *
     * @param idEspecialidad identificador de la especialidad a seleccionar
     */
    public void preseleccionarEspecialidad(int idEspecialidad) {
        for (int i = 0; i < cmbEspecialidad.getItemCount(); i++) {
            if (cmbEspecialidad.getItemAt(i).getIdEspecialidad() == idEspecialidad) {
                cmbEspecialidad.setSelectedIndex(i);
                return;
            }
        }
    }

    /**
     * Rellena los campos de texto con los datos del veterinario en modo edición.
     *
     * @param v veterinario cuyos datos se muestran
     */
    public void rellenarCampos(Veterinario v) {
        txtCedula.setText(v.getCedula());
        txtNombre.setText(v.getNombre());
        txtApellido.setText(v.getApellido());
        txtTelefono.setText(v.getTelefono());
        if (v.getCorreoElectronico() != null) txtCorreo.setText(v.getCorreoElectronico());
        if (v.getDireccion()         != null) txtDireccion.setText(v.getDireccion());
        txtPagoMensual.setText(String.valueOf(v.getPagoMensual()));
    }

    // ─── Helpers UI privados ──────────────────────────────────────────────────

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
        p.add(lbl,  BorderLayout.NORTH);
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

    // ─── Getters ──────────────────────────────────────────────────────────────

    public Button            getBtnAccion()              { return btnAccion;              }
    public Button            getBtnCancelar()            { return btnCancelar;            }
    public Button            getBtnAgregarServicio()     { return btnAgregarServicio;     }
    public MyTextField       getTxtCedula()              { return txtCedula;              }
    public MyTextField       getTxtNombre()              { return txtNombre;              }
    public MyTextField       getTxtApellido()            { return txtApellido;            }
    public MyTextField       getTxtTelefono()            { return txtTelefono;            }
    public MyTextField       getTxtCorreo()              { return txtCorreo;              }
    public MyTextField       getTxtPagoMensual()         { return txtPagoMensual;         }
    public MyTextField       getTxtDireccion()           { return txtDireccion;           }
    public JComboBox<EspecialidadVeterinaria> getCmbEspecialidad() { return cmbEspecialidad; }
    public JComboBox<Servicio> getCmbServiciosDisponibles() { return cmbServiciosDisponibles; }
    public boolean           isModoEdicion()             { return modoEdicion;            }
    public Veterinario       getVeterinarioActual()      { return veterinarioActual;      }
}