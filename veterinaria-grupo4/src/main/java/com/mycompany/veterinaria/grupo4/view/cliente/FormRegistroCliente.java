package com.mycompany.veterinaria.grupo4.view.cliente;

import com.mycompany.veterinaria.grupo4.model.entity.Cliente;
import com.mycompany.veterinaria.grupo4.view.swing.Button;
import com.mycompany.veterinaria.grupo4.view.swing.MyTextField;
import java.awt.BorderLayout;
import java.awt.Color;
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
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

/**
 *
 * @author juan
 */
public class FormRegistroCliente extends JDialog {
    private MyTextField txtCedula;
    private MyTextField txtNombre;
    private MyTextField txtApellido;
    private MyTextField txtTelefono;
    private MyTextField txtEmail;
    private MyTextField txtDireccion;

    // ── Botones ──────────────────────────────────────────────────
    private Button btnAccion;   // "Registrar" o "Actualizar"
    private Button btnCancelar;

    // ── Modo ─────────────────────────────────────────────────────
    private boolean modoEdicion = false;
    private Cliente clienteActual;

    // ── Constructor NUEVO ────────────────────────────────────────
    public FormRegistroCliente(Frame parent) {
        super(parent, true);
        init("Nuevo Cliente", "Registrar");
    }

    // ── Constructor EDITAR ───────────────────────────────────────
    public FormRegistroCliente(Frame parent, Cliente cliente) {
        super(parent, true);
        this.modoEdicion = true;
        this.clienteActual = cliente;
        init("Editar Cliente", "Actualizar");
        rellenarCampos(cliente);
    }

    // ── Construcción del formulario ──────────────────────────────
    private void init(String titulo, String labelBoton) {
        setUndecorated(true);           // sin barra de título del OS
        setBackground(new Color(0, 0, 0, 0)); // fondo transparente para redondear
        setSize(480, 520);
        setLocationRelativeTo(getParent());
        // ── Panel raíz con esquinas redondeadas ──────────────────
        JPanel root = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);
                for (int i = 8; i >= 1; i--) {
                    g2.setColor(new Color(0, 0, 0, i * 6)); // de transparente a más oscuro
                    g2.fill(new RoundRectangle2D.Float(i, i,
                        getWidth() - (i * 2), getHeight() - (i * 2), 20, 20));
                }
                g2.setColor(UIManager.getColor("Panel.background"));
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 20, 20));

                // ── Borde naranja sutil ──────────────────────────────
                g2.setColor(new Color(230, 140, 30, 80)); // naranja bajito
                g2.setStroke(new java.awt.BasicStroke(1.5f));
                g2.draw(new RoundRectangle2D.Float(1, 1, getWidth() - 2, getHeight() - 2, 20, 20));

                g2.dispose();
            }
        };
        root.setOpaque(false);
        root.setBorder(new EmptyBorder(24, 28, 24, 28));
        setContentPane(root);

        // ── Header: título + botón X ─────────────────────────────
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        header.setBorder(new EmptyBorder(0, 0, 20, 0));

        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 18));
        lblTitulo.setForeground(UIManager.getColor("Label.foreground"));

        JButton btnCerrar = new JButton("✕");
        btnCerrar.setFocusPainted(false);
        btnCerrar.setBorderPainted(false);
        btnCerrar.setContentAreaFilled(false);
        btnCerrar.setForeground(new Color(150, 150, 150));
        btnCerrar.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnCerrar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnCerrar.addActionListener(e -> dispose());

        header.add(lblTitulo, BorderLayout.WEST);
        header.add(btnCerrar, BorderLayout.EAST);
        root.add(header, BorderLayout.NORTH);

        // ── Formulario ───────────────────────────────────────────
        JPanel form = new JPanel(new GridLayout(0, 1, 0, 10));
        form.setOpaque(false);

        txtCedula    = campo(form, "Cédula");
        txtNombre    = campo(form, "Nombre");
        txtApellido  = campo(form, "Apellido");
        txtTelefono  = campo(form, "Teléfono");
        txtEmail     = campo(form, "Email");
        txtDireccion = campo(form, "Dirección");

        root.add(form, BorderLayout.CENTER);

        // ── Footer: botones ──────────────────────────────────────
        JPanel footer = new JPanel(new GridLayout(1, 2, 10, 0));
        footer.setOpaque(false);
        footer.setBorder(new EmptyBorder(20, 0, 0, 0));

        btnCancelar = new Button();
        btnCancelar.setText("Cancelar");
        btnCancelar.putClientProperty("JButton.buttonType", "borderless");
        btnCancelar.addActionListener(e -> dispose());

        btnAccion = new Button();
        btnAccion.setText(labelBoton);

        footer.add(btnCancelar);
        footer.add(btnAccion);
        root.add(footer, BorderLayout.SOUTH);

        // Sombra al root
        getRootPane().putClientProperty("JRootPane.shadow", Boolean.TRUE);
        
        // ── WindowFocusListener para cerrar cuando pierde el foco ──
        addWindowFocusListener(new WindowFocusListener() {
            @Override
            public void windowGainedFocus(WindowEvent e) {
                // No es necesario hacer nada aquí
            }
            
            @Override
            public void windowLostFocus(WindowEvent e) {
                // Solo cerrar si la nueva ventana que gana el foco NO es un JOptionPane o un hijo de este diálogo.
                // Esto evita cerrar el diálogo cuando se abre un mensaje de error o confirmación.
                if (e.getOppositeWindow() != null && !(e.getOppositeWindow() instanceof javax.swing.JDialog)) {
                    // Pequeña pausa para asegurar que no se está interactuando con el formulario
                    SwingUtilities.invokeLater(() -> dispose());
                }
            }
        });        
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
    // ── Helper: crea label + campo y lo agrega al panel ─────────
    private MyTextField campo(JPanel panel, String hint) {
        JPanel wrap = new JPanel(new BorderLayout(0, 4));
        wrap.setOpaque(false);

        JLabel lbl = new JLabel(hint);
        lbl.setFont(new Font("SansSerif", Font.PLAIN, 12));
        lbl.setForeground(new Color(120, 120, 120));

        MyTextField field = new MyTextField();
        field.setHint(hint);
        field.setPreferredSize(new Dimension(0, 38));

        wrap.add(lbl, BorderLayout.NORTH);
        wrap.add(field, BorderLayout.CENTER);
        panel.add(wrap);
        return field;
    }

    // ── Rellena campos cuando es modo edición ────────────────────
    private void rellenarCampos(Cliente c) {
        txtCedula.setText(c.getCedula());
        txtNombre.setText(c.getNombre());
        txtApellido.setText(c.getApellido());
        txtTelefono.setText(c.getTelefono());
        txtEmail.setText(c.getCorreoElectronico());
        txtDireccion.setText(c.getDireccion() != null ? c.getDireccion() : "");
    }

    public Button getBtnAccion() {
        return btnAccion;
    }

    public Button getBtnCancelar() {
        return btnCancelar;
    }

    public Cliente getClienteActual() {
        return clienteActual;
    }

    public MyTextField getTxtCedula() {
        return txtCedula;
    }

    public MyTextField getTxtNombre() {
        return txtNombre;
    }

    public MyTextField getTxtApellido() {
        return txtApellido;
    }

    public MyTextField getTxtTelefono() {
        return txtTelefono;
    }

    public MyTextField getTxtEmail() {
        return txtEmail;
    }

    public MyTextField getTxtDireccion() {
        return txtDireccion;
    }

    public boolean isModoEdicion() {
        return modoEdicion;
    }
}