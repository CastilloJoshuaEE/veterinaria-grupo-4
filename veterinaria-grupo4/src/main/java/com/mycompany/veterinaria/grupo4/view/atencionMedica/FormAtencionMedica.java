
package com.mycompany.veterinaria.grupo4.view.atencionMedica;

import com.mycompany.veterinaria.grupo4.model.entity.Cita;
import com.mycompany.veterinaria.grupo4.model.entity.InstrumentoMedico;
import com.mycompany.veterinaria.grupo4.model.entity.Medicamento;
import com.mycompany.veterinaria.grupo4.view.swing.Button;
import com.mycompany.veterinaria.grupo4.view.swing.MyTextField;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.RenderingHints;
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
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

   /**
 * Formulario modal de registro de atención médica.
 * <p>
 * Organizado en dos pestañas: datos clínicos y prescripciones.
 * Toda la lógica reside en {@link CtrlAtencion}.
 *
 * @author juan
 */
public class FormAtencionMedica extends JDialog {
 
    private static final Color NARANJA = new Color(230, 140, 30);
 
    // ─── Contexto (solo lectura) ──────────────────────────────────────────────
    private final Cita cita;
 
    // ─── Tab 1 · Datos clínicos ───────────────────────────────────────────────
 
    private JTextArea  txtDiagnostico;
    private JTextArea  txtTratamiento;
    private MyTextField txtPeso;
    private JTextArea  txtObservaciones;
 
    // ─── Tab 2 · Medicamentos ─────────────────────────────────────────────────
 
    private JComboBox<Medicamento>      cmbMedicamentos;
    private MyTextField                  txtDosis;
    private MyTextField                  txtFrecuencia;
    private MyTextField                  txtDuracion;
    private Button                       btnRecetar;
    private JPanel                       panelMedicamentos;
 
    // ─── Tab 2 · Instrumentos ────────────────────────────────────────────────
 
    private JComboBox<InstrumentoMedico> cmbInstrumentos;
    private Button                       btnUsarInstrumento;
    private JPanel                       panelInstrumentos;
 
    // ─── Acciones ─────────────────────────────────────────────────────────────
 
    private Button btnGuardar;
    private Button btnCancelar;
 
    // ─── Constructor ─────────────────────────────────────────────────────────
 
    /**
     * @param parent ventana padre
     * @param cita   cita pendiente sobre la que se registra la atención
     */
    public FormAtencionMedica (Frame parent, Cita cita) {
        super(parent, true);
        this.cita = cita;
        init();
    }
 
    // ─── Construcción UI ─────────────────────────────────────────────────────
 
    private void init() {
        setUndecorated(true);
        setBackground(new Color(0, 0, 0, 0));
        setSize(600, 600);
        setLocationRelativeTo(getParent());
        // Sin windowFocusListener: el formulario de atención NO debe cerrarse
        // accidentalmente al interactuar con combos o diálogos internos.
 
        JPanel root = buildRoot();
        root.add(buildHeader(),  BorderLayout.NORTH);
        root.add(buildTabs(),    BorderLayout.CENTER);
        root.add(buildFooter(),  BorderLayout.SOUTH);
        setContentPane(root);
    }
 
    private JPanel buildRoot() {
        JPanel root = new JPanel(new BorderLayout(0, 8)) {
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
        root.setBorder(new EmptyBorder(18, 24, 18, 24));
        return root;
    }
 
    // ─── Cabecera ─────────────────────────────────────────────────────────────
 
    private JPanel buildHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        header.setBorder(new EmptyBorder(0, 0, 6, 0));
 
        // Título + botón cerrar
        JPanel top = new JPanel(new BorderLayout());
        top.setOpaque(false);
        JLabel titulo = new JLabel("Atención Médica");
        titulo.setFont(new Font("SansSerif", Font.BOLD, 18));
        titulo.setForeground(UIManager.getColor("Label.foreground"));
        JButton btnX = new JButton("✕");
        btnX.setFocusPainted(false); btnX.setBorderPainted(false);
        btnX.setContentAreaFilled(false);
        btnX.setForeground(new Color(150, 150, 150));
        btnX.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnX.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnX.addActionListener(e -> dispose());
        top.add(titulo, BorderLayout.WEST);
        top.add(btnX,   BorderLayout.EAST);
        header.add(top, BorderLayout.NORTH);
 
        // Contexto de la cita
        header.add(buildContexto(), BorderLayout.CENTER);
        return header;
    }
 
    /**
     * Panel de contexto con los datos de la cita, solo lectura.
     * Muestra mascota, dueño, veterinario, servicio y motivo.
     */
    private JPanel buildContexto() {
        JPanel ctx = new JPanel(new GridLayout(0, 2, 10, 2)) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(230, 140, 30, 18));
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 10, 10));
                g2.setColor(new Color(230, 140, 30, 60));
                g2.setStroke(new BasicStroke(1f));
                g2.draw(new RoundRectangle2D.Float(0, 0, getWidth() - 1, getHeight() - 1, 10, 10));
                g2.dispose();
            }
        };
        ctx.setOpaque(false);
        ctx.setBorder(new EmptyBorder(8, 10, 8, 10));
 
        String mascota    = cita.getMascota()     != null
            ? cita.getMascota().getNombre() + " · " + cita.getMascota().getEspecie() : "—";
        String dueno      = cita.getCliente()     != null
            ? cita.getCliente().getNombre() + " ": "—";
        String veterinario = cita.getVeterinario() != null
            ? "Dr. " + cita.getVeterinario().getNombre() + " " + cita.getVeterinario().getApellido() : "—";
        String servicio   = cita.getServicio()    != null
            ? cita.getServicio().getNombreServicio() : "—";
        String motivo     = cita.getObservaciones() != null && !cita.getObservaciones().isBlank()
            ? cita.getObservaciones() : "Sin motivo registrado";
 
        ctx.add(ctxLabel("Mascota",      mascota));
        ctx.add(ctxLabel("Dueño",        dueno));
        ctx.add(ctxLabel("Veterinario",  veterinario));
        ctx.add(ctxLabel("Servicio",     servicio));
 
        // Motivo ocupa las dos columnas
        JPanel motivoPanel = new JPanel(new BorderLayout(4, 0));
        motivoPanel.setOpaque(false);
        JLabel lblKey = new JLabel("Motivo: ");
        lblKey.setFont(new Font("SansSerif", Font.BOLD, 11));
        lblKey.setForeground(NARANJA);
        JLabel lblVal = new JLabel(motivo);
        lblVal.setFont(new Font("SansSerif", Font.ITALIC, 11));
        motivoPanel.add(lblKey, BorderLayout.WEST);
        motivoPanel.add(lblVal, BorderLayout.CENTER);
        ctx.add(motivoPanel);
        ctx.add(new JLabel()); // celda vacía para completar la fila
 
        return ctx;
    }
 
    private JPanel ctxLabel(String key, String value) {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        p.setOpaque(false);
        JLabel k = new JLabel(key + ": ");
        k.setFont(new Font("SansSerif", Font.BOLD, 11));
        k.setForeground(NARANJA);
        JLabel v = new JLabel(value);
        v.setFont(new Font("SansSerif", Font.PLAIN, 11));
        p.add(k); p.add(v);
        return p;
    }
 
    // ─── Pestañas ─────────────────────────────────────────────────────────────
 
    private JTabbedPane buildTabs() {
        JTabbedPane tabs = new JTabbedPane();
        tabs.setFont(new Font("SansSerif", Font.PLAIN, 12));
        tabs.addTab("  Datos Clínicos  ",  buildTabClinico());
        tabs.addTab("  Prescripciones  ",  buildTabPrescripciones());
        return tabs;
    }
 
    // ─── Tab 1: Datos clínicos ────────────────────────────────────────────────
 
    private JPanel buildTabClinico() {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBorder(new EmptyBorder(10, 10, 10, 10));
        p.setOpaque(false);
 
        p.add(wrapArea("Diagnóstico", txtDiagnostico = area(4)));
        p.add(Box.createVerticalStrut(8));
        p.add(wrapArea("Tratamiento", txtTratamiento = area(4)));
        p.add(Box.createVerticalStrut(8));
 
        // Peso | Observaciones en dos columnas
        JPanel fila = new JPanel(new GridLayout(1, 2, 10, 0));
        fila.setOpaque(false);
        fila.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));
 
        txtPeso = new MyTextField();
        txtPeso.setHint("Ej: 4.5");
        fila.add(wrap("Peso actual (kg)", txtPeso));
 
        txtObservaciones = area(2);
        fila.add(wrapArea("Observaciones", txtObservaciones));
        p.add(fila);
 
        return p;
    }
 
    // ─── Tab 2: Prescripciones ────────────────────────────────────────────────
 
    private JPanel buildTabPrescripciones() {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBorder(new EmptyBorder(10, 10, 10, 10));
        p.setOpaque(false);
 
        // ── Medicamentos ──
        p.add(sectionLabel("Medicamentos"));
        p.add(Box.createVerticalStrut(4));
        p.add(buildFilaMedicamento());
        p.add(Box.createVerticalStrut(6));
 
        panelMedicamentos = new JPanel();
        panelMedicamentos.setLayout(new BoxLayout(panelMedicamentos, BoxLayout.Y_AXIS));
        panelMedicamentos.setOpaque(false);
        p.add(scrollAsignados(panelMedicamentos, 110));
        p.add(Box.createVerticalStrut(12));
 
        // ── Instrumentos ──
        p.add(sectionLabel("Instrumentos usados"));
        p.add(Box.createVerticalStrut(4));
        p.add(buildFilaInstrumento());
        p.add(Box.createVerticalStrut(6));
 
        panelInstrumentos = new JPanel();
        panelInstrumentos.setLayout(new BoxLayout(panelInstrumentos, BoxLayout.Y_AXIS));
        panelInstrumentos.setOpaque(false);
        p.add(scrollAsignados(panelInstrumentos, 90));
 
        return p;
    }
 
    /** Fila de agregar medicamento: combo + dosis + frecuencia + duración + botón. */
    private JPanel buildFilaMedicamento() {
        JPanel fila = new JPanel(new BorderLayout(5, 0));
        fila.setOpaque(false);
        fila.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
 
        cmbMedicamentos = new JComboBox<>();
        cmbMedicamentos.setSelectedIndex(-1);
        cmbMedicamentos.setRenderer(nombreRenderer(v ->
            v instanceof Medicamento m ? m.getNombre() : ""));
 
        JPanel campos = new JPanel(new GridLayout(1, 3, 4, 0));
        campos.setOpaque(false);
        campos.setPreferredSize(new Dimension(220, 36));
 
        txtDosis      = miniField("Dosis");
        txtFrecuencia = miniField("Frecuencia");
        txtDuracion   = miniField("Duración");
        campos.add(txtDosis);
        campos.add(txtFrecuencia);
        campos.add(txtDuracion);
 
        btnRecetar = new Button();
        btnRecetar.setText("+ Recetar");
        btnRecetar.setBackground(NARANJA);
        btnRecetar.setForeground(Color.WHITE);
        btnRecetar.setPreferredSize(new Dimension(90, 36));
 
        fila.add(cmbMedicamentos, BorderLayout.CENTER);
        fila.add(campos,          BorderLayout.EAST);
 
        JPanel wrapper = new JPanel(new BorderLayout(5, 0));
        wrapper.setOpaque(false);
        wrapper.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        wrapper.add(fila,       BorderLayout.CENTER);
        wrapper.add(btnRecetar, BorderLayout.EAST);
        return wrapper;
    }
 
    /** Fila de agregar instrumento: combo + botón. */
    private JPanel buildFilaInstrumento() {
        JPanel fila = new JPanel(new BorderLayout(5, 0));
        fila.setOpaque(false);
        fila.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
 
        cmbInstrumentos = new JComboBox<>();
        cmbInstrumentos.setSelectedIndex(-1);
        cmbInstrumentos.setRenderer(nombreRenderer(v ->
            v instanceof InstrumentoMedico i ? i.getNombre() : ""));
 
        btnUsarInstrumento = new Button();
        btnUsarInstrumento.setText("+ Agregar");
        btnUsarInstrumento.setBackground(NARANJA);
        btnUsarInstrumento.setForeground(Color.WHITE);
        btnUsarInstrumento.setPreferredSize(new Dimension(90, 36));
 
        fila.add(cmbInstrumentos,    BorderLayout.CENTER);
        fila.add(btnUsarInstrumento, BorderLayout.EAST);
        return fila;
    }
 
    // ─── Footer ───────────────────────────────────────────────────────────────
 
    private JPanel buildFooter() {
        JPanel footer = new JPanel(new GridLayout(1, 2, 10, 0));
        footer.setOpaque(false);
        footer.setBorder(new EmptyBorder(8, 0, 0, 0));
 
        btnCancelar = new Button();
        btnCancelar.setText("Cancelar");
        btnCancelar.putClientProperty("JButton.buttonType", "borderless");
        btnCancelar.addActionListener(e -> dispose());
 
        btnGuardar = new Button();
        btnGuardar.setText("Guardar y Facturar");
        btnGuardar.setBackground(NARANJA);
        btnGuardar.setForeground(Color.WHITE);
 
        footer.add(btnCancelar);
        footer.add(btnGuardar);
        return footer;
    }
 
    // ─── API pública (llamada desde el controlador) ───────────────────────────
 
    /**
     * Puebla el combo de medicamentos disponibles.
     *
     * @param medicamentos lista de medicamentos activos
     */
    public void cargarMedicamentos(List<Medicamento> medicamentos) {
        cmbMedicamentos.removeAllItems();
        medicamentos.forEach(cmbMedicamentos::addItem);
        cmbMedicamentos.setSelectedIndex(-1);
    }
 
    /**
     * Puebla el combo de instrumentos disponibles.
     *
     * @param instrumentos lista de instrumentos activos
     */
    public void cargarInstrumentos(List<InstrumentoMedico> instrumentos) {
        cmbInstrumentos.removeAllItems();
        instrumentos.forEach(cmbInstrumentos::addItem);
        cmbInstrumentos.setSelectedIndex(-1);
    }
 
    /**
     * Agrega una fila al panel de medicamentos recetados.
     *
     * @param resumen  texto a mostrar (nombre + dosis + frecuencia + duración)
     * @param onRemove acción al pulsar ✕
     */
    public void agregarMedicamentoRecetado(String resumen, Runnable onRemove) {
        panelMedicamentos.add(filaAsignado(resumen, onRemove));
        panelMedicamentos.revalidate();
        panelMedicamentos.repaint();
    }
 
    /**
     * Agrega una fila al panel de instrumentos usados.
     *
     * @param nombre   nombre del instrumento
     * @param onRemove acción al pulsar ✕
     */
    public void agregarInstrumentoUsado(String nombre, Runnable onRemove) {
        panelInstrumentos.add(filaAsignado(nombre, onRemove));
        panelInstrumentos.revalidate();
        panelInstrumentos.repaint();
    }
 
    /** Limpia la lista de medicamentos recetados del panel. */
    public void limpiarMedicamentos() {
        panelMedicamentos.removeAll();
        panelMedicamentos.revalidate();
        panelMedicamentos.repaint();
    }
 
    /** Limpia la lista de instrumentos usados del panel. */
    public void limpiarInstrumentos() {
        panelInstrumentos.removeAll();
        panelInstrumentos.revalidate();
        panelInstrumentos.repaint();
    }
 
    // ─── Helpers UI privados ──────────────────────────────────────────────────
 
    private JPanel filaAsignado(String texto, Runnable onRemove) {
        JPanel row = new JPanel(new BorderLayout(6, 0));
        row.setOpaque(false);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 28));
        row.setBorder(new EmptyBorder(1, 6, 1, 4));
 
        JLabel lbl = new JLabel("• " + texto);
        lbl.setFont(new Font("SansSerif", Font.PLAIN, 11));
 
        JButton btnX = new JButton("✕");
        btnX.setFocusPainted(false); btnX.setBorderPainted(false);
        btnX.setContentAreaFilled(false);
        btnX.setForeground(new Color(200, 60, 60));
        btnX.setFont(new Font("SansSerif", Font.BOLD, 10));
        btnX.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnX.addActionListener(e -> {
            onRemove.run();
            Container parent = row.getParent();
            if (parent != null) { parent.remove(row); parent.revalidate(); parent.repaint(); }
        });
 
        row.add(lbl,  BorderLayout.CENTER);
        row.add(btnX, BorderLayout.EAST);
        return row;
    }
 
    private JScrollPane scrollAsignados(JPanel panel, int height) {
        JScrollPane scroll = new JScrollPane(panel);
        scroll.setPreferredSize(new Dimension(0, height));
        scroll.setMaximumSize(new Dimension(Integer.MAX_VALUE, height));
        scroll.setBorder(BorderFactory.createLineBorder(new Color(230, 140, 30, 70), 1));
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        return scroll;
    }
 
    private JLabel sectionLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("SansSerif", Font.BOLD, 12));
        lbl.setForeground(NARANJA);
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        return lbl;
    }
 
    private JTextArea area(int rows) {
        JTextArea ta = new JTextArea(rows, 0);
        ta.setLineWrap(true);
        ta.setWrapStyleWord(true);
        ta.setFont(new Font("SansSerif", Font.PLAIN, 12));
        ta.setBorder(new EmptyBorder(4, 6, 4, 6));
        return ta;
    }
 
    private JPanel wrapArea(String label, JTextArea area) {
        JPanel p = new JPanel(new BorderLayout(0, 3));
        p.setOpaque(false);
        p.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("SansSerif", Font.PLAIN, 12));
        lbl.setForeground(new Color(120, 120, 120));
        JScrollPane scroll = new JScrollPane(area);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));
        p.add(lbl,    BorderLayout.NORTH);
        p.add(scroll, BorderLayout.CENTER);
        return p;
    }
 
    private JPanel wrap(String label, JComponent comp) {
        JPanel p = new JPanel(new BorderLayout(0, 3));
        p.setOpaque(false);
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("SansSerif", Font.PLAIN, 12));
        lbl.setForeground(new Color(120, 120, 120));
        p.add(lbl,  BorderLayout.NORTH);
        p.add(comp, BorderLayout.CENTER);
        return p;
    }
 
    private MyTextField miniField(String hint) {
        MyTextField f = new MyTextField();
        f.setHint(hint);
        f.setPreferredSize(new Dimension(0, 32));
        return f;
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
 
    public Cita                      getCita()                 { return cita;                 }
    public Button                    getBtnGuardar()           { return btnGuardar;           }
    public Button                    getBtnRecetar()           { return btnRecetar;           }
    public Button                    getBtnUsarInstrumento()   { return btnUsarInstrumento;   }
    public JTextArea                 getTxtDiagnostico()       { return txtDiagnostico;       }
    public JTextArea                 getTxtTratamiento()       { return txtTratamiento;       }
    public MyTextField               getTxtPeso()              { return txtPeso;              }
    public JTextArea                 getTxtObservaciones()     { return txtObservaciones;     }
    public JComboBox<Medicamento>    getCmbMedicamentos()      { return cmbMedicamentos;      }
    public MyTextField               getTxtDosis()             { return txtDosis;             }
    public MyTextField               getTxtFrecuencia()        { return txtFrecuencia;        }
    public MyTextField               getTxtDuracion()          { return txtDuracion;          }
    public JComboBox<InstrumentoMedico> getCmbInstrumentos()   { return cmbInstrumentos;      }
} 

