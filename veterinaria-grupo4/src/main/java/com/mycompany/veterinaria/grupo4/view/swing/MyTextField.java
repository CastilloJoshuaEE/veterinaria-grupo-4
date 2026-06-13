package com.mycompany.veterinaria.grupo4.view.swing;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Insets;
import java.awt.RenderingHints;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JTextField;
import javax.swing.UIManager;

public class MyTextField extends JTextField {

    private Icon prefixIcon;
    private Icon suffixIcon;
    private String hint = "";

    public String getHint() {
        return hint;
    }

    public void setHint(String hint) {
        this.hint = hint;
    }

    public Icon getPrefixIcon() {
        return prefixIcon;
    }

    public void setPrefixIcon(Icon prefixIcon) {
        this.prefixIcon = prefixIcon;
        initBorder();
    }

    public Icon getSuffixIcon() {
        return suffixIcon;
    }

    public void setSuffixIcon(Icon suffixIcon) {
        this.suffixIcon = suffixIcon;
        initBorder();
    }

    public MyTextField() {
        setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setOpaque(false); //  necesario para que nuestro paintComponent dibuje el fondo
        setFont(new java.awt.Font("sansserif", 0, 13));
        setSelectionColor(new Color(75, 175, 152));

        boolean isDark = UIManager.getBoolean("laf.dark");
        if (isDark) {
            setForeground(UIManager.getColor("TextField.foreground"));
            // fondo oscuro sutil — gris oscuro con transparencia
            setBackground(new Color(255, 255, 255, 18));
        } else {
            setForeground(Color.decode("#7A8C8D"));
            // fondo claro sutil — gris muy bajito
            setBackground(new Color(0, 0, 0, 15));
        }
        
        // Al final del constructor
        addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent e) {
                repaint(); // redibujar con borde naranja
            }
            @Override
            public void focusLost(java.awt.event.FocusEvent e) {
                repaint(); // redibujar sin borde naranja
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create(); 
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        boolean isDark = UIManager.getBoolean("laf.dark");

        // ── Fondo redondeado ────────────────────────────────────────
        if (isDark) {
            g2.setColor(new Color(255, 255, 255, 18)); // blanco muy transparente
        } else {
            g2.setColor(Color.white);       // negro muy transparente → gris plomito
        }
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);

        // ── Borde (naranja con focus, gris sin focus) ────────────────
        if (isFocusOwner()) {
            g2.setColor(new Color(230, 140, 30, 180));  // naranja bajito
            g2.setStroke(new java.awt.BasicStroke(1.8f));
        } else {
            g2.setColor(isDark
                ? new Color(255, 255, 255, 40)
                : new Color(0, 0, 0, 30));
            g2.setStroke(new java.awt.BasicStroke(1.2f));
        }
        g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 10, 10);

        g2.dispose(); // 
        paintIcon(g);
        super.paintComponent(g); // texto encima del fondo
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        if (getText().length() == 0) {
            int h = getHeight();
            ((Graphics2D) g).setRenderingHint(
                RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            Insets ins = getInsets();
            FontMetrics fm = g.getFontMetrics();

            boolean isDark = UIManager.getBoolean("laf.dark");
            g.setColor(isDark
                ? new Color(160, 160, 160)   // gris claro en oscuro
                : new Color(180, 180, 180)); // gris suave en claro
            g.drawString(hint, ins.left, h / 2 + fm.getAscent() / 2 - 2);
        }
    }

    private void paintIcon(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        if (prefixIcon != null) {
            Image prefix = ((ImageIcon) prefixIcon).getImage();
            int y = (getHeight() - prefixIcon.getIconHeight()) / 2;
            g2.drawImage(prefix, 10, y, this);
        }
        if (suffixIcon != null) {
            Image suffix = ((ImageIcon) suffixIcon).getImage();
            int y = (getHeight() - suffixIcon.getIconHeight()) / 2;
            g2.drawImage(suffix, getWidth() - suffixIcon.getIconWidth() - 10, y, this);
        }
    }

    private void initBorder() {
        int left = 15;
        int right = 15;
        if (prefixIcon != null) {
            left = prefixIcon.getIconWidth() + 15;
        }
        if (suffixIcon != null) {
            right = suffixIcon.getIconWidth() + 15;
        }
        setBorder(javax.swing.BorderFactory.createEmptyBorder(10, left, 10, right));
    }
}