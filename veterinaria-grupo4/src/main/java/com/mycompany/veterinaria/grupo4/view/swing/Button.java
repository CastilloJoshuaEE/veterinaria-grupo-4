package com.mycompany.veterinaria.grupo4.view.swing;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import javax.swing.JButton;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

public class Button extends JButton {

    public Color getEffectColor() {
        return effectColor;
    }

    public void setEffectColor(Color effectColor) {
        this.effectColor = effectColor;
    }

    private Timer animator;
    private int targetSize;
    private float animatSize;
    private Point pressedPoint;
    private float alpha;
    // Quitamos el color quemado de aquí para asignarlo dinámicamente
    private Color effectColor; 
    private int frameCount = 0;
    private final int totalFrames = 20;

    public Button() {
        setContentAreaFilled(false);
        setBorder(new EmptyBorder(5, 0, 5, 0));
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent me) {
                targetSize = Math.max(getWidth(), getHeight()) * 2;
                animatSize = 0;
                pressedPoint = me.getPoint();
                alpha = 0.5f;
                frameCount = 0;
                if (animator.isRunning()) {
                    animator.stop();
                }
                animator.start();
            }
        });
        animator = new Timer(10, (ActionEvent e) -> {
            frameCount++;
            float fraction = (float) frameCount / totalFrames;

            if (fraction > 0.5f) {
                alpha = 1 - fraction;
            }
            animatSize = fraction * targetSize;
            repaint();

            if (frameCount >= totalFrames) {
                animator.stop();
            }
        });
    }

    // AGREGADO: Este método es vital. FlatLaf lo llama al cambiar de tema.
    @Override
    public void updateUI() {
        super.updateUI();
        // Verificamos si estamos en modo oscuro para ajustar el color de la ola
        boolean isDark = UIManager.getBoolean("laf.dark");
        effectColor = isDark ? new Color(255, 255, 255) : new Color(150, 150, 150);
    }

    @Override
    protected void paintComponent(Graphics grphcs) {
        int width = getWidth();
        int height = getHeight();
        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = img.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Ahora getBackground() nos dará el color oficial de FlatLaf (Oscuro o Claro)
        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, width, height, height, height);
        
        if (pressedPoint != null) {
            g2.setColor(effectColor);
            
            // RED DE SEGURIDAD: Acorralamos 'alpha' para que jamás se salga de 0.0f a 1.0f
            float alphaSeguro = Math.max(0.0f, Math.min(1.0f, alpha));
            
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, alphaSeguro));
            g2.fillOval((int) (pressedPoint.x - animatSize / 2), (int) (pressedPoint.y - animatSize / 2), (int) animatSize, (int) animatSize);
        }
        
        g2.dispose();
        grphcs.drawImage(img, 0, 0, null);
        super.paintComponent(grphcs);
    }
}