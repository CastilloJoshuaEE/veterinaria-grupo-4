package com.mycompany.veterinaria.grupo4.view.auth;

import com.mycompany.veterinaria.grupo4.view.swing.Button;
import com.mycompany.veterinaria.grupo4.view.swing.MyPasswordField;
import com.mycompany.veterinaria.grupo4.view.swing.MyTextField;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.awt.geom.RoundRectangle2D;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import net.miginfocom.swing.MigLayout;

public class FormRestablecerContrasena extends JDialog {
    
    private MyTextField txtEmail;
    private MyPasswordField txtNuevaContrasena;
    private MyPasswordField txtConfirmarContrasena;
    private Button btnRestablecer;
    private JButton btnCancelar;
    private boolean confirmado = false;
    private JButton btnToggleNueva;
    private JButton btnToggleConfirmar;
    
    public FormRestablecerContrasena(Frame parent) {
        super(parent, "Restablecer Contraseña", true);
        init();
    }
    
    private void init() {
        setUndecorated(true);
        setBackground(new Color(0, 0, 0, 0));
        setSize(420, 450);
        setLocationRelativeTo(getParent());
        
        addWindowFocusListener(new WindowFocusListener() {
            @Override public void windowGainedFocus(WindowEvent e) {}
            @Override public void windowLostFocus(WindowEvent e) {
                dispose();
            }
        });
        
        JPanel root = buildRoot();
        root.add(buildHeader(), BorderLayout.NORTH);
        root.add(buildCuerpo(), BorderLayout.CENTER);
        root.add(buildFooter(), BorderLayout.SOUTH);
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
                g2.setStroke(new java.awt.BasicStroke(1.5f));
                g2.draw(new RoundRectangle2D.Float(1, 1, getWidth() - 2, getHeight() - 2, 20, 20));
                g2.dispose();
            }
        };
        root.setOpaque(false);
        root.setBorder(new EmptyBorder(20, 28, 20, 28));
        return root;
    }
    
    private JPanel buildHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        header.setBorder(new EmptyBorder(0, 0, 8, 0));
        
        JLabel lbl = new JLabel("Restablecer Contraseña");
        lbl.setFont(new Font("SansSerif", Font.BOLD, 18));
        lbl.setForeground(new Color(230, 140, 30));
        
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
        JPanel panel = new JPanel(new MigLayout("wrap, fillx", "push[center]push", "push[]10[]10[]10[]10[]push"));
        panel.setOpaque(false);
        
        // Icono
        ImageIcon icono = null;
        try {
            java.net.URL url = getClass().getResource("/image/LOGO-ICON.png");
            if (url != null) {
                icono = new ImageIcon(url);
            } else {
                icono = new ImageIcon();
            }
        } catch (Exception e) {
            icono = new ImageIcon();
        }
        
        JLabel iconLabel = new JLabel(icono);
        panel.add(iconLabel, "span, center");
        
        JLabel instruccion = new JLabel("Ingrese su correo y nueva contraseña");
        instruccion.setFont(new Font("SansSerif", Font.PLAIN, 12));
        instruccion.setForeground(new Color(120, 120, 120));
        panel.add(instruccion, "span, center, gapbottom 10");
        
        // Email
        JLabel lblEmail = new JLabel("Correo Electrónico");
        lblEmail.setFont(new Font("SansSerif", Font.PLAIN, 12));
        lblEmail.setForeground(new Color(120, 120, 120));
        panel.add(lblEmail, "span, gaptop 5");
        
        txtEmail = new MyTextField();
        txtEmail.setPrefixIcon(new ImageIcon(getClass().getResource("/icon/mail.png")));
        txtEmail.setHint("ejemplo@correo.com");
        panel.add(txtEmail, "w 80%, h 38");
        
        // Nueva Contraseña con botón toggle
        JPanel nuevaPassPanel = new JPanel(new BorderLayout(5, 0));
        nuevaPassPanel.setOpaque(false);
        nuevaPassPanel.setPreferredSize(new Dimension(0, 38));
        
        txtNuevaContrasena = new MyPasswordField();
        txtNuevaContrasena.setPrefixIcon(new ImageIcon(getClass().getResource("/icon/pass.png")));
        txtNuevaContrasena.setHint("Mínimo 6 caracteres");
        nuevaPassPanel.add(txtNuevaContrasena, BorderLayout.CENTER);
        
        btnToggleNueva = new JButton();
        btnToggleNueva.setIcon(new ImageIcon(getClass().getResource("/icon/eye-off.png")));
        btnToggleNueva.setBorderPainted(false);
        btnToggleNueva.setContentAreaFilled(false);
        btnToggleNueva.setFocusPainted(false);
        btnToggleNueva.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnToggleNueva.setPreferredSize(new Dimension(35, 35));
        // CORREGIDO: Usar getEchoChar en lugar de isPasswordVisible
        btnToggleNueva.addActionListener(e -> {
            if (txtNuevaContrasena.getEchoChar() == '\u2022' || txtNuevaContrasena.getEchoChar() == '*') {
                txtNuevaContrasena.setEchoChar((char) 0);
                btnToggleNueva.setIcon(new ImageIcon(getClass().getResource("/icon/eye.png")));
            } else {
                txtNuevaContrasena.setEchoChar('*');
                btnToggleNueva.setIcon(new ImageIcon(getClass().getResource("/icon/eye-off.png")));
            }
        });
        nuevaPassPanel.add(btnToggleNueva, BorderLayout.EAST);
        panel.add(nuevaPassPanel, "w 80%, h 38");
        
        // Confirmar Contraseña con botón toggle
        JPanel confirmarPassPanel = new JPanel(new BorderLayout(5, 0));
        confirmarPassPanel.setOpaque(false);
        confirmarPassPanel.setPreferredSize(new Dimension(0, 38));
        
        txtConfirmarContrasena = new MyPasswordField();
        txtConfirmarContrasena.setPrefixIcon(new ImageIcon(getClass().getResource("/icon/pass.png")));
        txtConfirmarContrasena.setHint("Repita su contraseña");
        confirmarPassPanel.add(txtConfirmarContrasena, BorderLayout.CENTER);
        
        btnToggleConfirmar = new JButton();
        btnToggleConfirmar.setIcon(new ImageIcon(getClass().getResource("/icon/eye-off.png")));
        btnToggleConfirmar.setBorderPainted(false);
        btnToggleConfirmar.setContentAreaFilled(false);
        btnToggleConfirmar.setFocusPainted(false);
        btnToggleConfirmar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnToggleConfirmar.setPreferredSize(new Dimension(35, 35));
        // CORREGIDO: Usar getEchoChar en lugar de isPasswordVisible
        btnToggleConfirmar.addActionListener(e -> {
            if (txtConfirmarContrasena.getEchoChar() == '\u2022' || txtConfirmarContrasena.getEchoChar() == '*') {
                txtConfirmarContrasena.setEchoChar((char) 0);
                btnToggleConfirmar.setIcon(new ImageIcon(getClass().getResource("/icon/eye.png")));
            } else {
                txtConfirmarContrasena.setEchoChar('*');
                btnToggleConfirmar.setIcon(new ImageIcon(getClass().getResource("/icon/eye-off.png")));
            }
        });
        confirmarPassPanel.add(btnToggleConfirmar, BorderLayout.EAST);
        panel.add(confirmarPassPanel, "w 80%, h 38");
        
        return panel;
    }
    
    private JPanel buildFooter() {
        JPanel footer = new JPanel(new MigLayout("fillx", "push[]10[]push", "[]"));
        footer.setOpaque(false);
        footer.setBorder(new EmptyBorder(10, 0, 0, 0));
        
        btnCancelar = new JButton("Cancelar");
        btnCancelar.setFocusPainted(false);
        btnCancelar.setContentAreaFilled(false);
        btnCancelar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnCancelar.setForeground(new Color(150, 150, 150));
        btnCancelar.addActionListener(e -> dispose());
        footer.add(btnCancelar, "w 40%, h 36");
        
        btnRestablecer = new Button();
        btnRestablecer.setBackground(new Color(230, 140, 30));
        btnRestablecer.setForeground(Color.WHITE);
        btnRestablecer.setText("Restablecer");
        btnRestablecer.addActionListener(e -> {
            if (validarDatos()) {
                confirmado = true;
                dispose();
            }
        });
        footer.add(btnRestablecer, "w 40%, h 36");
        
        return footer;
    }
    
    private boolean validarDatos() {
        String email = txtEmail.getText().trim();
        String nueva = new String(txtNuevaContrasena.getPassword());
        String confirmar = new String(txtConfirmarContrasena.getPassword());
        
        if (email.isEmpty()) {
            javax.swing.JOptionPane.showMessageDialog(this,
                "Por favor, ingrese su correo electrónico.",
                "Campo requerido", javax.swing.JOptionPane.WARNING_MESSAGE);
            return false;
        }
        
        if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            javax.swing.JOptionPane.showMessageDialog(this,
                "Por favor, ingrese un correo electrónico válido.",
                "Formato inválido", javax.swing.JOptionPane.WARNING_MESSAGE);
            return false;
        }
        
        if (nueva.isEmpty()) {
            javax.swing.JOptionPane.showMessageDialog(this,
                "Por favor, ingrese una nueva contraseña.",
                "Campo requerido", javax.swing.JOptionPane.WARNING_MESSAGE);
            return false;
        }
        
        if (nueva.length() < 6) {
            javax.swing.JOptionPane.showMessageDialog(this,
                "La contraseña debe tener al menos 6 caracteres.",
                "Contraseña muy corta", javax.swing.JOptionPane.WARNING_MESSAGE);
            return false;
        }
        
        if (confirmar.isEmpty()) {
            javax.swing.JOptionPane.showMessageDialog(this,
                "Por favor, confirme su nueva contraseña.",
                "Campo requerido", javax.swing.JOptionPane.WARNING_MESSAGE);
            return false;
        }
        
        if (!nueva.equals(confirmar)) {
            javax.swing.JOptionPane.showMessageDialog(this,
                "Las contraseñas no coinciden. Por favor, verifíquelas.",
                "Contraseñas no coinciden", javax.swing.JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        return true;
    }
    
    // ========== GETTERS ==========
    
    public String getEmail() {
        return txtEmail.getText().trim();
    }
    
    public String getNuevaContrasena() {
        return new String(txtNuevaContrasena.getPassword());
    }
    
    public String getConfirmarContrasena() {
        return new String(txtConfirmarContrasena.getPassword());
    }
    
    public boolean isConfirmado() {
        return confirmado;
    }
}