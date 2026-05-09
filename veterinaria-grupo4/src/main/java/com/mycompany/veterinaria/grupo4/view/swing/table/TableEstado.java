
package com.mycompany.veterinaria.grupo4.view.swing.table;

import com.mycompany.veterinaria.grupo4.model.entity.Estado;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.JLabel;

/**
 *
 * @author juan
 */
public class TableEstado extends JLabel{
    private Estado estado;
    
    public TableEstado() {
        setForeground(Color.WHITE);
    }
    
    public Estado getEstado() {
        return estado;
    }
    
    public void setEstado(Estado estado) {
        this.estado = estado;
        setText(estado.toString());
        repaint();
    }
    
    @Override
    protected void paintComponent(Graphics grphcs) {
        if (estado != null) {
            Graphics2D g2 = (Graphics2D) grphcs;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            GradientPaint g;
            g = switch (estado) {
                // Verde: activo = positivo
                case ACTIVO -> new GradientPaint(
                    0, 0, new Color(76, 175, 80),      // verde medio
                    getWidth(), 0, new Color(56, 142, 60) // verde oscuro
                );
                // Rojo: inactivo = negativo
                case INACTIVO -> new GradientPaint(
                    0, 0, new Color(229, 115, 115),    // rojo claro
                    getWidth(), 0, new Color(198, 40, 40) // rojo oscuro
                );
                // Naranja: para cualquier otro estado
                default -> new GradientPaint(
                    0, 0, new Color(255, 183, 77),     // naranja claro
                    getWidth(), 0, new Color(230, 140, 30) // naranja oscuro
                );
            };
            g2.setPaint(g);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 1, 1);
        }
        super.paintComponent(grphcs);
    }
}
