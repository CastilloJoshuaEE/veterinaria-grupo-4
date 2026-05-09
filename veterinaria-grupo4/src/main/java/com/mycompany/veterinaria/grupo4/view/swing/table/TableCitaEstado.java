
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
            Color g;
            g = switch (estadoCita) {
                case PENDIENTE -> new Color(255, 193, 7);
                case REALIZADA -> new Color(76, 175, 80);
                case CANCELADA -> new Color(244, 67, 54);
                default -> new Color(33, 150, 243);
            };
            g2.setPaint(g);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 1, 1);
        }
        super.paintComponent(grphcs);
    }
}
