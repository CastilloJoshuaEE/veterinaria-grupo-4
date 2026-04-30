
package com.mycompany.veterinaria.grupo4.view.swing.table;

import com.mycompany.veterinaria.grupo4.model.entity.EstadoCita;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.JLabel;
/**
 *
 * @author Raven
 */
public class TableCitaEstado extends JLabel{
    
    private EstadoCita estadoCita;
    
    public TableCitaEstado() {
        setForeground(Color.WHITE);
    }
    
    public EstadoCita getEstadoCita() {
        return estadoCita;
    }

    public void setEstadoCita(EstadoCita estadoCita) {
        this.estadoCita = estadoCita;
        setText(estadoCita.toString());
        repaint();
    }

    @Override
    protected void paintComponent(Graphics grphcs) {
        if (estadoCita != null) {
            Graphics2D g2 = (Graphics2D) grphcs;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            GradientPaint g;
            g = switch (estadoCita) {
                case PENDIENTE -> new GradientPaint(0, 0, new Color(255, 167, 38), 0, getHeight(), new Color(167, 94, 236));
                case REALIZADA -> new GradientPaint(0, 0, new Color(102, 187, 106), 0, getHeight(), new Color(123, 123, 245));
                case CANCELADA -> new GradientPaint(0, 0, new Color(255, 138, 128), 0, getHeight(), new Color(211, 184, 61));
                default -> new GradientPaint(0, 0, new Color(241, 208, 62), 0, getHeight(), new Color(211, 184, 61));
            };
            g2.setPaint(g);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 1, 1);
        }
        super.paintComponent(grphcs);
    }
}
