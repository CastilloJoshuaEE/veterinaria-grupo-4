package com.mycompany.veterinaria.grupo4.view.swing.table;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

/**
 *
 * @author juan
 */
public class TableCellRender implements TableCellRenderer{
    
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {
        CellAction cell = new CellAction();
        cell.setBackground(isSelected ? table.getSelectionBackground() : table.getBackground());
        
        if (value instanceof ModelAction model) {
            cell.setModelAction(model);
        }
        return cell;
    }
}
