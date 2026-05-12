package com.mycompany.veterinaria.grupo4.view.swing.table;

import javax.swing.JLabel;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

/**
 *
 * @author Raven
 */
public class TableHeader extends JLabel{
    public TableHeader(String text) {
        super(text);
        setOpaque(true);
        setBackground(UIManager.getColor("TableHeader.background"));
        setForeground(UIManager.getColor("TableHeader.foreground"));
        setFont(new Font("sansserif", 1, 12));
        setBorder(new EmptyBorder(10, 5, 10, 5));
    }

    @Override
    protected void paintComponent(Graphics grphcs) {
        super.paintComponent(grphcs);
        grphcs.setColor(UIManager.getColor("Table.gridColor"));
        grphcs.drawLine(0, getHeight() - 1, getWidth(), getHeight() - 1);
    }
}
